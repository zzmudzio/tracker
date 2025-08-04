package com.tracker.tracker.entity;

import com.tracker.tracker.enums.BugStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "bugs")
@Getter
@Setter
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 250, message = "description too long!")
    private String description;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private BugStatus status;

    @ManyToOne
    @JoinColumn(name = "reported_by_id")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;
}
