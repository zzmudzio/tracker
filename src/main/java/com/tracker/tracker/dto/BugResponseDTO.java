package com.tracker.tracker.dto;

import com.tracker.tracker.enums.BugStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BugResponseDTO {

    private Long id;
    private String description;
    private LocalDateTime createdAt;
    private BugStatus status;
    private String reportedByUser;
    private String assignedToUser;
}
