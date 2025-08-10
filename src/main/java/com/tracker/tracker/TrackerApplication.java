package com.tracker.tracker;

import com.tracker.tracker.dto.BugDTO;
import com.tracker.tracker.dto.mapper.BugMapper;
import com.tracker.tracker.entity.Bug;
import com.tracker.tracker.repository.BugsRepository;
import com.tracker.tracker.service.BugsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class TrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackerApplication.class, args);
    }

    @Profile("!prod")
    @Bean
    public CommandLineRunner initializeData(BugsRepository bugsRepository, BugMapper bugMapper) {
        return args -> {
            log.info("Inserting some development data..");
            Stream.of(
                            BugDTO.builder().description("Test desc 1").assignedToUserId(1).build(),
                            BugDTO.builder().description("Test desc 2").build(),
                            BugDTO.builder().description("Test desc 3").assignedToUserId(2).build()
                    )
                    .map(bugMapper::toBugEntity)
                    .forEach(bugsRepository::save);
        };
    }

}
