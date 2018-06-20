package com.films.films.batch.services;

import com.films.films.batch.models.Film;

import java.nio.file.Path;
import java.util.List;

public interface FilmReporter {
    void generateReport(Path filePath, List<Film> films);
}
