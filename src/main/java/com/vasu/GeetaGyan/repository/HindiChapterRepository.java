package com.vasu.GeetaGyan.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vasu.GeetaGyan.model.HindiChapter;

public interface HindiChapterRepository extends MongoRepository<HindiChapter, String> {
    Optional<HindiChapter> findByNumber(int number);
}
