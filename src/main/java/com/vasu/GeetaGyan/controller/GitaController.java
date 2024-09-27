package com.vasu.GeetaGyan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vasu.GeetaGyan.model.Chapter;
import com.vasu.GeetaGyan.model.Verse;
import com.vasu.GeetaGyan.model.ChapterComment;
import com.vasu.GeetaGyan.model.HindiChapter;
import com.vasu.GeetaGyan.model.HindiVerse;
import com.vasu.GeetaGyan.repository.ChapterRepository;
import com.vasu.GeetaGyan.repository.HindiChapterRepository;
import com.vasu.GeetaGyan.service.ChapterCommentService;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/gita")
@RequiredArgsConstructor
public class GitaController {

    private final ChapterRepository chapterRepository;
    private final HindiChapterRepository hindiChapterRepository;
    private final ChapterCommentService chapterCommentService;

    // @PostMapping("/cleanup")
    // public void cleanupDuplicates() {
    // gitaScraperService.removeDuplicateChapters();
    // }

    @GetMapping("/chapter/{number}")
    public Chapter getChapter(@PathVariable int number) {
        Chapter chapter = chapterRepository.findByNumber(number)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found"));

        chapter.getVerses().forEach(verse -> {
            if (verse.getTranslation() != null) {
                String commentaryPattern = String.format("%d\\.%d", number, verse.getVerseNumber());
                String trimmedTranslation = verse.getTranslation().replaceAll(commentaryPattern + ".*", "").trim();
                verse.setTranslation(trimmedTranslation);
            }
        });

        return chapter;
    }

    @GetMapping("/chapter/hindi/{number}")
    public HindiChapter getHindiChapter(@PathVariable int number) {
        HindiChapter chapter = hindiChapterRepository.findByNumber(number)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found"));

        return chapter;
    }

    @GetMapping("/chapter/{chapterNumber}/verse/{verseNumber}")
    public Verse getVerse(@PathVariable int chapterNumber, @PathVariable int verseNumber) {
        Chapter chapter = chapterRepository.findByNumber(chapterNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found"));

        Verse verse = chapter.getVerses().stream()
                .filter(v -> v.getVerseNumber() == verseNumber)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Verse not found"));

        String commentaryPattern = String.format("%d\\.%d", chapterNumber, verseNumber);

        if (verse.getTranslation() != null) {
            String trimmedTranslation = verse.getTranslation().replaceAll(commentaryPattern + ".*", "").trim();
            verse.setTranslation(trimmedTranslation);
        }

        return verse;
    }

    @GetMapping("/chapter/{chapterNumber}/hindi/verse/{verseNumber}")
    public HindiVerse getHindiVerse(@PathVariable int chapterNumber, @PathVariable int verseNumber) {
        HindiChapter chapter = hindiChapterRepository.findByNumber(chapterNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found"));

        HindiVerse verse = chapter.getVerses().stream()
                .filter(v -> v.getVerseNumber() == verseNumber)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Verse not found"));
        return verse;
    }

    @PostMapping("/chapter/{chapterNumber}/comment")
    public ChapterComment addChapterComment(@PathVariable int chapterNumber, @RequestBody ChapterComment comment) {
        comment.setChapterNumber(chapterNumber);
        return chapterCommentService.addComment(comment);
    }

    @GetMapping("/chapter/{chapterNumber}/comments")
    public List<ChapterComment> getChapterComments(@PathVariable int chapterNumber) {
        return chapterCommentService.getCommentsByChapter(chapterNumber);
    }
}
