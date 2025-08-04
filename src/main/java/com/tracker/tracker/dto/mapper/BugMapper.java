package com.tracker.tracker.dto.mapper;

import com.tracker.tracker.dto.BugDTO;
import com.tracker.tracker.dto.BugResponseDTO;
import com.tracker.tracker.entity.Bug;
import com.tracker.tracker.entity.User;
import com.tracker.tracker.enums.BugStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BugMapper {

    public Bug toBugEntity(BugDTO bugDTO) {

        Bug bug = new Bug();
        bug.setDescription(bugDTO.getDescription());
        bug.setStatus(bugDTO.getStatus() != null ? bugDTO.getStatus() : BugStatus.OPEN);
        return bug;
    }

    public BugResponseDTO toBugResponse(Bug bug) {

        BugResponseDTO bugResponseDTO = new BugResponseDTO();
        bugResponseDTO.setDescription(bug.getDescription());
        bugResponseDTO.setStatus(bug.getStatus());
        bugResponseDTO.setId(bug.getId());
        bugResponseDTO.setCreatedAt(bug.getCreatedAt());
        bugResponseDTO.setReportedByUser(bug.getReportedBy().getLogin());
        bugResponseDTO.setAssignedToUser(
                Optional.ofNullable(bug.getAssignedTo())
                        .map(User::getLogin)
                        .orElse(null)
        );
        return bugResponseDTO;
    }
}
