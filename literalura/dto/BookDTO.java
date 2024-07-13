package com.eduarda.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(
        @JsonAlias("title") String title,
        List<AuthorDTO> authors,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("download_count") int downloadCount
) {
}
