package com.tracker.tracker.controller;

import com.tracker.tracker.dto.BugDTO;
import com.tracker.tracker.dto.BugResponseDTO;
import com.tracker.tracker.enums.BugStatus;
import com.tracker.tracker.service.BugsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/bugs")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BugsController {

    private final BugsService bugsService;

    @GetMapping
    public List<BugResponseDTO> getAllBugs() {
        log.info("handling a request for all bugs..");
        return bugsService.getAllBugs();
    }

    @GetMapping("/active")
    public List<BugResponseDTO> getAllNotDoneBugs() {
        return bugsService.getAllNotDoneBugs();
    }

    @GetMapping("/ids")
    public List<BugResponseDTO> getBugsWithGivenIds(@RequestParam(required = false) Set<Integer> id) {
        return bugsService.getBugsWithGivenIds(id);
    }

    @GetMapping("/{bugId}")
    public BugResponseDTO getBugWithGivenId(@PathVariable @Positive Integer bugId) {
        return bugsService.getBugWithGivenId(bugId);
    }

    @GetMapping("/my-bugs")
    public List<BugResponseDTO> bugsAssignedToMe() {
        return bugsService.getBugsAssignedToMe();
    }

    @PostMapping()
    public ResponseEntity<BugResponseDTO> addBug(@Valid @RequestBody BugDTO bug) {
        return ResponseEntity.status(201).body(bugsService.addBug(bug));
    }

    @PatchMapping("/{bugId}/status")
    public ResponseEntity<BugResponseDTO> updateBugStatus(@Positive @PathVariable Integer bugId, @RequestParam BugStatus newStatus) {
        return ResponseEntity.status(200).body(bugsService.updateBugStatus(bugId, newStatus));
    }

    @PatchMapping("/{bugId}/assignee")
    public ResponseEntity<BugResponseDTO> assignToUser(@Positive @PathVariable Integer bugId, @RequestBody @Positive Integer userId) {
        return ResponseEntity.status(201).body(bugsService.assignBug(bugId, userId));
    }

    @PutMapping("/{bugId}")
    public ResponseEntity<BugResponseDTO> updateBug(@PathVariable @Positive Integer bugId, @Valid @RequestBody BugDTO bugDTO) {
        return ResponseEntity.ok().body(bugsService.updateBug(bugId, bugDTO));
    }

    @DeleteMapping("/{bugId}")
    public ResponseEntity<Void> deleteBug(@Positive @PathVariable Integer bugId) {
        bugsService.deleteBug(bugId);
        return ResponseEntity.noContent().build();
    }

}
