package com.vasu.GeetaGyan.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;

// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.query.Criteria;
// import org.springframework.data.mongodb.core.query.Query;
// import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

// import com.vasu.GeetaGyan.model.Chapter;
import com.vasu.GeetaGyan.model.HindiChapter;
import com.vasu.GeetaGyan.model.HindiVerse;
// import com.vasu.GeetaGyan.model.Verse;
// import com.vasu.GeetaGyan.repository.ChapterRepository;
import com.vasu.GeetaGyan.repository.HindiChapterRepository;

// import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Service
@RequiredArgsConstructor
public class GitaScraperService {

    // private final ChapterRepository chapterRepository;
    private final HindiChapterRepository chapterRepository;
    // private final MongoTemplate mongoTemplate;

    // public void removeDuplicateChapters() {
    // List<Integer> chapterNumbers = IntStream.rangeClosed(3,
    // 18).boxed().collect(Collectors.toList());

    // for (Integer chapterNumber : chapterNumbers) {
    // Query query = new Query(Criteria.where("number").is(chapterNumber));
    // query.with(Sort.by(Sort.Direction.ASC, "_id"));

    // List<Chapter> chapters = mongoTemplate.find(query, Chapter.class);

    // if (chapters.size() > 1) {
    // List<String> idsToRemove = chapters.stream()
    // .skip(1)
    // .map(Chapter::getId)
    // .collect(Collectors.toList());

    // Query removeQuery = new Query(Criteria.where("_id").in(idsToRemove));
    // mongoTemplate.remove(removeQuery, Chapter.class);

    // System.out.println("Removed " + idsToRemove.size() + " duplicate(s) for
    // chapter " + chapterNumber);
    // }
    // }
    // }

    // @PostConstruct
    // public void init() {
    // try {
    // scrapeAndSaveAllChapters();
    // System.out.println("Data Scraping and Saving Completed.");
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    public void scrapeAndSaveAllChapters() throws IOException {
        for (int i = 1; i <= 18; i++) {
            HindiChapter chapter = scrapeChapter(i);
            chapterRepository.save(chapter);
        }
    }

    public HindiChapter scrapeChapter(int chapterNumber) throws IOException {
        String url = "https://www.holy-bhagavad-gita.org/chapter/" + chapterNumber + "/hi";
        Document doc = Jsoup.connect(url).get();
        String heading = doc.select("h1.headingBg[itemprop=name]").text();
        String chapterTitle = heading.contains(":") ? heading.split(":")[1].trim() : heading;

        String summary = "";
        Elements summaryElements = doc.select("div.chapterIntro p");
        if (!summaryElements.isEmpty()) {
            summary = summaryElements.first().text();
        }

        HindiChapter chapter = new HindiChapter();
        chapter.setNumber(chapterNumber);
        chapter.setTitle(chapterTitle);
        chapter.setSummary(summary);

        List<HindiVerse> verses = new ArrayList<>();
        int verseNumber = 1;
        while (true) {
            try {
                HindiVerse verse = scrapeVerse(chapterNumber, verseNumber);
                if (verse.getShlok().isEmpty()) {
                    break;
                }
                verses.add(verse);
                verseNumber++;
            } catch (IOException e) {
                break;
            }
        }
        chapter.setVerses(verses);

        return chapter;
    }

    public HindiVerse scrapeVerse(int chapterNumber, int verseNumber) throws IOException {
        String url = "https://www.holy-bhagavad-gita.org/chapter/" + chapterNumber + "/verse/" + verseNumber + "/hi";
        Document doc = Jsoup.connect(url).get();
        String shlok = doc.select("div#originalVerse p").text();

        Elements translationElements = doc.select("div#translation p");
        String translation = "";
        if (!translationElements.isEmpty()) {
            Element translationElement = translationElements.first();
            translationElement.select("span.verseShort").remove();
            translation = translationElement.text().trim();
        }

        HindiVerse verse = new HindiVerse();
        verse.setVerseNumber(verseNumber);
        verse.setShlok(shlok);
        verse.setTranslation(translation);

        return verse;
    }

}
