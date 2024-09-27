package com.vasu.GeetaGyan.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "hindi_chapters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HindiChapter {
    @Id
    private String id;
    private int number;
    private String title;
    private String summary;
    private List<HindiVerse> verses;
}
