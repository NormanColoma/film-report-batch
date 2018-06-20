package com.films.films.batch.services;

import com.films.films.batch.models.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name="type", havingValue="txt")
public class FlatFileFilmReport implements FilmReporter {
    @Override
    public void generateReport(Path filePath, List<Film> films) {
        for(Film film : films) {
            byte[] strToBytes = (film.toString() + System.lineSeparator()).getBytes();
            log.info("Film to be written: {}", film);
            try {
                Files.write(filePath, strToBytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
