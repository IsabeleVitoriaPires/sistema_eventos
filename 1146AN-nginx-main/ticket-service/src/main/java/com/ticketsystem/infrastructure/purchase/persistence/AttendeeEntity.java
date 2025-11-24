package com.ticketsystem.infrastructure.purchase.persistence;

import com.ticketsystem.domain.ticket.Attendee;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * JPA Embeddable for Attendee information
 * Stored within TicketEntity
 */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeEntity {

    @Column(name = "attendee_full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "attendee_cpf", nullable = false, length = 14)
    private String cpf;

    @Column(name = "attendee_email", nullable = false, length = 200)
    private String email;

    @Column(name = "attendee_birth_date", nullable = false)
    private LocalDate birthDate;

    public Attendee toDomain() {
        return Attendee.builder()
                .fullName(this.fullName)
                .cpf(this.cpf)
                .email(this.email)
                .birthDate(this.birthDate)
                .build();
    }

    public static AttendeeEntity fromDomain(Attendee attendee) {
        if (attendee == null) return null;
        return AttendeeEntity.builder()
                .fullName(attendee.getFullName())
                .cpf(attendee.getCpf())
                .email(attendee.getEmail())
                .birthDate(attendee.getBirthDate())
                .build();
    }
}
