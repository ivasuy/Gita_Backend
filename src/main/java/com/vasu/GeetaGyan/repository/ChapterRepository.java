package com.vasu.GeetaGyan.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.vasu.GeetaGyan.model.Chapter;

public interface ChapterRepository extends MongoRepository<Chapter, String> {
    Optional<Chapter> findByNumber(int number);
}
