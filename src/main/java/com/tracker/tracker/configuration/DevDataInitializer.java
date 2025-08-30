package com.tracker.tracker.configuration;

import com.tracker.tracker.dto.BugDTO;
import com.tracker.tracker.repository.BugsRepository;
import com.tracker.tracker.service.BugsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.stream.Stream;

@Configuration
@Profile("dev")
@Slf4j
public class DevDataInitializer {

    @Bean
    public CommandLineRunner initializeData(BugsRepository bugsRepository, BugsService bugsService) {
        return args -> {
            log.info("Inserting some development data..");
            Stream.of(
                            BugDTO.builder().description("Test desc 1").assignedToUserId(1).build(),
                            BugDTO.builder().description("Test desc 2").build(),
                            BugDTO.builder().description("Test desc 3").assignedToUserId(2).build()
                    )
                    .map(bugsService::getBugEntity)
                    .forEach(bugsRepository::save);
        };
    }
}
