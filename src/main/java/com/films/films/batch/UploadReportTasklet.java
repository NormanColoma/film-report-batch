package com.films.films.batch;

import com.films.films.batch.configuration.ExecutionConfiguration;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@AllArgsConstructor
@Slf4j
public class UploadReportTasklet implements Tasklet {
    private final ExecutionConfiguration executionConfiguration;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        try {
            String fileName = executionConfiguration.getFilePath();
            Path filePath = Paths.get(fileName);

            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("credentials.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .setProjectId("films-batch")
                    .build().getService();

            byte[] bytes = Files.readAllBytes(filePath);
            storage.create(BlobInfo.newBuilder("film-reports", fileName).build(), bytes);

            log.info("file updated successfully");
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return null;
    }
}
