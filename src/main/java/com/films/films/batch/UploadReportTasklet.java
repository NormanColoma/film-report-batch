package com.films.films.batch;

import com.films.films.batch.configuration.ExecutionConfiguration;
import com.films.films.batch.configuration.GoogleStorageConfiguration;
import com.films.films.batch.services.emitter.FilmReporterEmitter;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@AllArgsConstructor
@Slf4j
public class UploadReportTasklet implements Tasklet {
    private final ExecutionConfiguration executionConfiguration;
    private final GoogleStorageConfiguration googleStorageConfiguration;
    private final FilmReporterEmitter filmReporterProducer;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        try {
            String fileName = executionConfiguration.getFilePath();

            Credentials credentials = getGoogleCredentials(googleStorageConfiguration.getCredentials());
            Storage storage = getStorageService(googleStorageConfiguration.getProjectId(), credentials);

            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            storage.create(BlobInfo.newBuilder(googleStorageConfiguration.getBucket(), fileName).build(), bytes);

            log.info("file updated successfully");

            filmReporterProducer.emit(fileName);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return null;
    }

    private Storage getStorageService(String projectId, Credentials credentials) {
        return StorageOptions.newBuilder()
                        .setCredentials(credentials)
                        .setProjectId(projectId)
                        .build().getService();
    }

    private Credentials getGoogleCredentials(String credentialsPath) throws IOException {
        InputStream inputStream = Files.newInputStream(Paths.get(credentialsPath));
        return GoogleCredentials.fromStream(inputStream);
    }
}
