package com.tracker.tracker.dto;

import com.tracker.tracker.enums.BugStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BugDTO {

    @Size(max = 250, message = "Description too long!")
    private String description;
    private BugStatus status = BugStatus.OPEN;
    private Integer assignedToUserId;
}
