package tn.enicarthage.campushub.request;

import tn.enicarthage.campushub.model.enums.RequestStatus;
import tn.enicarthage.campushub.model.enums.RequestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    @Enumerated(EnumType.STRING)
    private RequestType type; // ROOM_BOOKING, MATERIAL, CERTIFICATE, OTHER

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // PENDING, APPROVED, REJECTED

    private String adminNote; // feedback from admin when reviewed

    private LocalDateTime submittedAt;
}

//This is where students can submit requests to the administration
// (room bookings, material requests, certificates, etc.) and track their status.