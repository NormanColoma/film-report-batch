package com.films.films.batch;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@AllArgsConstructor
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
    public void afterJob(JobExecution jobExecution) { }
}
