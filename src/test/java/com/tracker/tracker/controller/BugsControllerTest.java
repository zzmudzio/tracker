package com.tracker.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.tracker.configuration.TestSecurityConfig;
import com.tracker.tracker.dto.BugDTO;
import com.tracker.tracker.dto.BugResponseDTO;
import com.tracker.tracker.enums.BugStatus;
import com.tracker.tracker.exceptions.ResourceNotFoundException;
import com.tracker.tracker.service.BugsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BugsController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class BugsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BugsService bugsService;

    private static final List<BugResponseDTO> BUG_RESPONSE = List.of(
            BugResponseDTO.builder().id(1L).description("test bug desc 1").build(),
            BugResponseDTO.builder().id(2L).description("test bug desc 2").build()
    );

    @Test
    public void shouldReturnBugsListAndStatus200_whenRequested() throws Exception {
        String expected = objectMapper.writeValueAsString(BUG_RESPONSE);

        when(bugsService.getAllBugs()).thenReturn(BUG_RESPONSE);

        mockMvc.perform(get("/api/v1/bugs"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    public void shouldReturnBugsWithGivenIds_whenRequested() throws Exception {
        String expected = objectMapper.writeValueAsString(BUG_RESPONSE);

        when(bugsService.getBugsWithGivenIds(Set.of(1, 2))).thenReturn(BUG_RESPONSE);

        mockMvc.perform(get("/api/v1/bugs/ids")
                        .param("id", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
        verify(bugsService).getBugsWithGivenIds(Set.of(1, 2));

    }

    @Test
    public void shouldReturnBlankResponse_whenEmptySetIsPassed() throws Exception {

        when(bugsService.getBugsWithGivenIds(null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/bugs/ids"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    public void shouldReturnBugWithGivenId_whenPassed() throws Exception {
        String expected = objectMapper.writeValueAsString(BUG_RESPONSE.get(0));

        when(bugsService.getBugWithGivenId(1)).thenReturn(BUG_RESPONSE.get(0));

        mockMvc.perform(get("/api/v1/bugs/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

    }

    @Test
    public void shouldThrowAnException_bugWithGivenIdIsNotFound() throws Exception {

        when(bugsService.getBugWithGivenId(111)).thenThrow(new ResourceNotFoundException("Bug with given id not found!"));

        mockMvc.perform(get("/api/v1/bugs/111"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bug with given id not found!"));
    }

    @Test
    public void shouldReturnBugs_whichAreAssignedToMe() throws Exception {
        String expected = objectMapper.writeValueAsString(BUG_RESPONSE);

        when(bugsService.getBugsAssignedToMe()).thenReturn(BUG_RESPONSE);

        mockMvc.perform(get("/api/v1/bugs/my-bugs"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
        verify(bugsService).getBugsAssignedToMe();

    }

    @Test
    public void shouldAddAndReturnABug_whenProperDataIsSent() throws Exception {
        BugDTO bugToAdd = BugDTO.builder()
                .description("Test description")
                .status(BugStatus.OPEN)
                .assignedToUserId(1)
                .build();

        String contentToAdd = objectMapper.writeValueAsString(bugToAdd);

        when(bugsService.addBug(any(BugDTO.class))).thenReturn(
                BugResponseDTO.builder()
                        .description(bugToAdd.getDescription())
                        .id(1L)
                        .createdAt(LocalDateTime.now())
                        .assignedToUser(String.valueOf(bugToAdd.getAssignedToUserId()))
                        .reportedByUser("1")
                        .status(bugToAdd.getStatus())
                        .build()
        );

        mockMvc.perform(post("/api/v1/bugs")
                        .content(contentToAdd)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(bugsService).addBug(any(BugDTO.class));

    }

    @Test
    public void shouldUpdateBugStatusAndReturnUpdatedObject_whenRequested() throws Exception {
        BugResponseDTO expected = BugResponseDTO.builder()
                .status(BugStatus.IN_PROGRESS)
                .description("This is a test description")
                .build();
        String expectedResponse = objectMapper.writeValueAsString(expected);

        when(bugsService.updateBugStatus(1, BugStatus.IN_PROGRESS)).thenReturn(expected);

        mockMvc.perform(patch("/api/v1/bugs/1/status")
                        .param("newStatus", "IN_PROGRESS"))
                .andExpect(status().is(200))
                .andExpect(content().json(expectedResponse));

        verify(bugsService).updateBugStatus(1, BugStatus.IN_PROGRESS);

    }

    @Test
    public void shouldThrowAnExceptionWhenUpdatingStatus_whenGivenObjectHasNotBeenFound() throws Exception {

        when(bugsService.updateBugStatus(1, BugStatus.IN_PROGRESS)).thenThrow(new ResourceNotFoundException("Bug with given id not found!"));

        mockMvc.perform(patch("/api/v1/bugs/1/status")
                        .param("newStatus", "IN_PROGRESS"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bug with given id not found!"));

        verify(bugsService).updateBugStatus(1, BugStatus.IN_PROGRESS);

    }

    @Test
    public void shouldUpdateBugAndReturnUpdatedObject_whenRequested() throws Exception {
        BugDTO newBugContent = BugDTO.builder()
                .description("Test description")
                .status(BugStatus.OPEN)
                .assignedToUserId(1)
                .build();
        String contentToUpdate = objectMapper.writeValueAsString(newBugContent);

        when(bugsService.updateBug(1, newBugContent)).thenReturn(BugResponseDTO.builder()
                .description(newBugContent.getDescription())
                .id(1L)
                .assignedToUser(String.valueOf(newBugContent.getAssignedToUserId()))
                .status(newBugContent.getStatus())
                .build());

        mockMvc.perform(put("/api/v1/bugs/1")
                        .content(contentToUpdate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bugsService).updateBug(eq(1), any(BugDTO.class));
    }

    @Test
    public void shouldDeleteBugWithGivenId_whenRequested() throws Exception {

        mockMvc.perform(delete("/api/v1/bugs/1"))
                .andExpect(status().isNoContent());

        verify(bugsService).deleteBug(eq(1));
    }


}
