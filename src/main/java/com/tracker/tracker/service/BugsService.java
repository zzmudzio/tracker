package com.tracker.tracker.service;

import com.tracker.tracker.dto.BugDTO;
import com.tracker.tracker.dto.BugResponseDTO;
import com.tracker.tracker.dto.mapper.BugMapper;
import com.tracker.tracker.entity.Bug;
import com.tracker.tracker.entity.User;
import com.tracker.tracker.enums.BugStatus;
import com.tracker.tracker.exceptions.ResourceNotFoundException;
import com.tracker.tracker.repository.BugsRepository;
import com.tracker.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BugsService {

    private final BugsRepository bugsRepository;
    private final BugMapper bugMapper;
    private final UserRepository userRepository;

    public List<BugResponseDTO> getAllBugs() {
        return
                bugsRepository
                        .findAll()
                        .stream()
                        .map(bugMapper::toBugResponse)
                        .toList();
    }

    public List<BugResponseDTO> getBugsWithGivenIds(Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return bugsRepository
                .findAllById(ids)
                .stream()
                .map(bugMapper::toBugResponse)
                .toList();
    }

    public BugResponseDTO addBug(BugDTO bugDTO) {
        Bug bug = bugMapper.toBugEntity(bugDTO);
        bug.setCreatedAt(LocalDateTime.now());
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (bugDTO.getAssignedToUserId() != null) {
            User assignedUser = userRepository
                    .findById(bugDTO.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with given id not found!"));
            bug.setAssignedTo(assignedUser);
        }
        User loggedUser = userRepository.findUserByLogin(userName).orElseThrow(() -> new ResourceNotFoundException("User with given id is not logged in!"));
        bug.setReportedBy(loggedUser);
        Bug savedBug = bugsRepository.save(bug);
        return bugMapper.toBugResponse(savedBug);
    }

    public BugResponseDTO getBugWithGivenId(Integer id) {
        return bugsRepository
                .findById(id)
                .map(bugMapper::toBugResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Bug with given id not found!"));
    }

    public BugResponseDTO updateBugStatus(Integer bugId, BugStatus newStatus) {
        Bug bug = bugsRepository
                .findById(bugId)
                .orElseThrow(() -> new ResourceNotFoundException("Bug with given id not found!"));
        bug.setStatus(newStatus);
        Bug updatedBug = bugsRepository.save(bug);
        return bugMapper.toBugResponse(updatedBug);
    }

    public BugResponseDTO updateBug(Integer bugId, BugDTO bugDTO) {
        Bug bug = bugsRepository.findById(bugId).orElseThrow(() -> new ResourceNotFoundException("Bug with given id not found!"));
        bug.setStatus(bugDTO.getStatus());
        bug.setDescription(bugDTO.getDescription());
        if (bugDTO.getAssignedToUserId() != null) {
            User user = userRepository.findById(bugDTO.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with given id not found!"));
            bug.setAssignedTo(user);
        } else {
            bug.setAssignedTo(null);
        }
        Bug newBug = bugsRepository.save(bug);
        return bugMapper.toBugResponse(newBug);
    }

    public void deleteBug(Integer bugId) {
        if (!bugsRepository.existsById(bugId)) {
            throw new ResourceNotFoundException("Bug with given id not found!");
        }
        bugsRepository.deleteById(bugId);
    }

    public List<BugResponseDTO> getBugsAssignedToMe() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userRepository
                .findUserByLogin(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User with given login not found!"));
        return loggedUser
                .getAssignedBugs()
                .stream()
                .map(bugMapper::toBugResponse)
                .toList();
    }

}
