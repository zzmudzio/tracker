package com.tracker.tracker.dto;

import com.tracker.tracker.enums.BugStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BugDTO {

    @NotBlank(message = "Description must not be blank!")
    @Size(max = 250, message = "Description too long!")
    private String description;

    @Builder.Default
    @NotNull(message = "Status can not be explicitly set to null!")
    private BugStatus status = BugStatus.OPEN;

    private Integer assignedToUserId;
}
