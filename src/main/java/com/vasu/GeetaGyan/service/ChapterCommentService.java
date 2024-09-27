package com.vasu.GeetaGyan.service;

import org.springframework.stereotype.Service;
import com.vasu.GeetaGyan.model.ChapterComment;
import com.vasu.GeetaGyan.repository.ChapterCommentRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChapterCommentService {

    private final ChapterCommentRepository chapterCommentRepository;

    public ChapterComment addComment(ChapterComment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return chapterCommentRepository.save(comment);
    }

    public List<ChapterComment> getCommentsByChapter(int chapterNumber) {
        return chapterCommentRepository.findByChapterNumber(chapterNumber);
    }
}