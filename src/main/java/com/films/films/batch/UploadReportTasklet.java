package com.films.films.batch;

import com.films.films.batch.configuration.ExecutionConfiguration;
import com.films.films.batch.configuration.GoogleStorageConfiguration;
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
    private final GoogleStorageConfiguration googleStorageConfiguration;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        try {
            String fileName = executionConfiguration.getFilePath();
            String credentialsPath = googleStorageConfiguration.getCredentials();
            String projectId = googleStorageConfiguration.getProjectId();
            String bucket = googleStorageConfiguration.getBucket();
            Path filePath = Paths.get(fileName);

            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(credentialsPath);
            Credentials credentials = GoogleCredentials.fromStream(inputStream);


            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build().getService();

            byte[] bytes = Files.readAllBytes(filePath);
            storage.create(BlobInfo.newBuilder(bucket, fileName).build(), bytes);

            log.info("file updated successfully");
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return null;
    }
}
