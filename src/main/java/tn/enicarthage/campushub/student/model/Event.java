package tn.enicarthage.campushub.student.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "StudentEvent")
@Table(name = "student_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private Long organizerId;

    @Transient
    private Integer registeredCount;
}