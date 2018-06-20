package com.films.films.batch;

import com.films.films.batch.configuration.ExecutionConfiguration;
import com.films.films.batch.services.FilmReporter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class FilmWriter<Film> implements ItemWriter<Film> {
    private final ExecutionConfiguration executionConfiguration;
    private final FilmReporter filmReporter;

    @Override
    public void write(List<? extends Film> list) {
        Path path = Paths.get(executionConfiguration.getFilePath());
        filmReporter.generateReport(path, (List<com.films.films.batch.models.Film>) list);
    }
}
