package com.films.films.batch;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@ConditionalOnProperty(name="type", havingValue="pdf")
public class PdfFileReport implements FilmReportGenerator {
    @Override
    public void generateReport(Path filePath, List<Film> films) {
        System.out.println("PDF!!!!!!!!!!!!!!!!");
    }
}
