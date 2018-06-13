package com.films.films.batch;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
public class FilmReader {
    private final FilmRepository filmRepository;


    public ItemReader<Film> createReader() {
        HashMap<String, Sort.Direction> sort = new HashMap<>();
        sort.put("name", Sort.Direction.ASC);
        RepositoryItemReader<Film> reader = new RepositoryItemReader<>();
        reader.setRepository(filmRepository);
        reader.setMethodName("findAll");
        reader.setSort(sort);
        return reader;
    }
}
