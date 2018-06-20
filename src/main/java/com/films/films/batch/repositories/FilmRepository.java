package com.films.films.batch.repositories;

import com.films.films.batch.models.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends MongoRepository<Film, String> { }
