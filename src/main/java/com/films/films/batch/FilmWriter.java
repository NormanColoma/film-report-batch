package com.films.films.batch;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class FilmWriter<Film> implements ItemWriter<Film> {
    private final ExecutionConfiguration executionConfiguration;
    @Override
    public void write(List<? extends Film> list) throws Exception {
        Path path = Paths.get(executionConfiguration.getFilePath());
        for(Film film : list) {
            byte[] strToBytes = (film.toString() + System.lineSeparator()).getBytes();
            log.info("Film to be written: {}", film);
            Files.write(path, strToBytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }
}
