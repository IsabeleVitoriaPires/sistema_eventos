package com.example.authservice.application.auth;

import com.example.authservice.application.port.MailSender;
import com.example.authservice.domain.auth.MagicLink;
import com.example.authservice.domain.auth.MagicLinkRepository;
import com.example.authservice.domain.auth.vo.ExpiresAt;
import com.example.authservice.domain.auth.vo.HashedToken;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.infrastructure.config.AppProperties;
import com.example.authservice.support.Digests;
import com.example.authservice.support.RandomTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestMagicLinkHandler {
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private final MagicLinkRepository magicLinkRepository;
    private final MailSender mailSender;

    public record Result(boolean accepted) {}

    public Result handle(String emailRaw) {
        Email email = Email.of(emailRaw);

        Optional<User> userOpt = userRepository.findByEmail(emailRaw);
        if (userOpt.isEmpty()) {
            return new Result(true);
        }

        User user = userOpt.get();

        String token = RandomTokenGenerator.urlSafeToken(32);
        String hash = Digests.sha256Hex(token);

        Instant now = Instant.now();
        long ttl = appProperties.getMagicLink().getTtlSeconds();
        Instant exp = now.plusSeconds(ttl);

        MagicLink link = MagicLink.issueForLogin(
            user.getId(),
            HashedToken.of(hash),
            ExpiresAt.of(exp)
        );
        magicLinkRepository.save(link);

        String base = appProperties.getMagicLink().getVerifyUrlBase();
        String url = base + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        mailSender.sendMagicLink(
            email.getValue(),
            url,
            exp
        );

        return new Result(true);
    }
}
