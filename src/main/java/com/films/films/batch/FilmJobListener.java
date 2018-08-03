package com.films.films.batch;

import com.films.films.batch.configuration.ExecutionConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@AllArgsConstructor
@Slf4j
public class FilmJobListener extends JobExecutionListenerSupport {
    private final ExecutionConfiguration executionConfiguration;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
        Path path = Paths.get(executionConfiguration.getFilePath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Batch executed successfully. Exiting...");
        System.exit(0);
    }
}
