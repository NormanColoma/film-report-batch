package com.films.films.batch.services;

import com.films.films.batch.models.Film;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@ConditionalOnProperty(name="type", havingValue="pdf")
public class PdfFileReport implements FilmReporter {
    @Override
    public void generateReport(Path filePath, List<Film> films) {
        System.out.println("PDF!!!!!!!!!!!!!!!!");
    }
}
