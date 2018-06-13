package com.films.films.batch;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
@Slf4j
public class BatchConfiguration {
    private final FilmReader filmReader;
    private final StepBuilderFactory steps;
    private final FilmWriter filmWriter;

    @Bean
    public Step saveFilms() {
        return steps.get("readFilmsStep")
                .<Film, Film>chunk(1)
                .reader(filmReader.createReader())
                .writer(filmWriter)
                .build();
    }

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, FilmJobListener listener){
        return jobBuilderFactory.get("filmsJob")
                .listener(listener)
                .start(saveFilms())
                .build();
    }
}
