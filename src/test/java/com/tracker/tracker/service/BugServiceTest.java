package com.tracker.tracker.service;

import com.tracker.tracker.dto.BugResponseDTO;
import com.tracker.tracker.dto.mapper.BugMapper;
import com.tracker.tracker.entity.Bug;
import com.tracker.tracker.entity.User;
import com.tracker.tracker.enums.BugStatus;
import com.tracker.tracker.repository.BugsRepository;
import com.tracker.tracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BugServiceTest {

    @Autowired
    private BugsService bugsService;

    @Autowired
    private BugMapper bugMapper;

    @MockitoBean
    private BugsRepository bugsRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void getAllBugs_ShouldReturnListOfAllBugs() {

        List<BugResponseDTO> expected = List.of(
                BugResponseDTO.builder()
                        .id(1L)
                        .description("Test bug")
                        .reportedByUser("user")
                        .build()
        );

        when(bugsRepository.findAll()).thenReturn(List.of(
                Bug.builder()
                        .id(1L)
                        .description("Test bug")
                        .reportedBy(User.builder()
                                .id(1)
                                .login("user")
                                .build())
                        .build()
        ));

        assertEquals(expected, bugsService.getAllBugs());
    }

    @Test
    public void getAllBugs_shouldReturnEmptyListWhenNothingHasBeenFound() {

        when(bugsRepository.findAll()).thenReturn(List.of());

        assertTrue(bugsService.getAllBugs().isEmpty());
    }

    @Test
    public void onlyBugsWithGivenIds_willBeFetched() {

        List<BugResponseDTO> expected = List.of(
                BugResponseDTO.builder().id(1L).description("Test bug nr 1").reportedByUser("user").build(),
                BugResponseDTO.builder().id(2L).description("Test bug nr 2").reportedByUser("user").build()
        );

        when(bugsRepository.findAllById(Set.of(1, 2))).thenReturn(List.of(
                Bug.builder()
                        .id(1L)
                        .description("Test bug nr 1")
                        .reportedBy(User.builder().id(1).login("user").build()).build(),
                Bug.builder()
                        .id(2L)
                        .description("Test bug nr 2")
                        .reportedBy(User.builder().id(1).login("user").build()).build())
        );

        assertEquals(expected, bugsService.getBugsWithGivenIds(Set.of(1, 2)));
        verify(bugsRepository).findAllById(Set.of(1, 2));
    }

    @Test
    public void bugsWithDoneStatus_willNotBeFetched_whenUsingGivenMethod() {
        Bug doneBug = Bug.builder()
                .id(1L)
                .description("test desc")
                .status(BugStatus.DONE)
                .build();

        when(bugsRepository.findAll()).thenReturn(List.of(doneBug));

        assertTrue(bugsService.getAllNotDoneBugs().isEmpty());
        verify(bugsRepository).findAll();
    }

    @Test
    public void emptyListWillBeReturned_whenSetOfIdsWillBeEmpty() {

        assertEquals(List.of(), bugsService.getBugsWithGivenIds(Set.of()));
        verify(bugsRepository, never()).findAllById(anySet());
    }

}
