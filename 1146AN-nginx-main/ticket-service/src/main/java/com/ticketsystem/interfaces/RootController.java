package com.ticketsystem.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class RootController {
    @GetMapping("/api/tickets/_health")
    public Map<String, String> health() {
        return Map.of("service","ticket-service","status","OK");
    }
}
