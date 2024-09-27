package com.vasu.GeetaGyan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.vasu.GeetaGyan.model.ChapterComment;
import java.util.List;

public interface ChapterCommentRepository extends MongoRepository<ChapterComment, String> {
    List<ChapterComment> findByChapterNumber(int chapterNumber);
}