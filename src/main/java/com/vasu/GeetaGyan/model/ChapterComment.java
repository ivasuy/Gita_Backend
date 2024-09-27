package com.vasu.GeetaGyan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Document(collection = "chapterComments")
@Data
public class ChapterComment {
    @Id
    private String id;

    private int chapterNumber;
    private String userName;
    private String comment;
    private LocalDateTime createdAt;
}