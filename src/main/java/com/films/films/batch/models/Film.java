package com.films.films.batch.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private String id;
    private String name;
    private Integer impressions;
    private Integer downloads;

    public String toString() {
        return String.format("Film id: %s, name: %s, impressions: %s, downloads: %s", id, name, impressions, downloads);
    }
}
