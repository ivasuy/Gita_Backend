package com.vasu.GeetaGyan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HindiVerse {
    private int verseNumber;
    private String shlok;
    private String translation;
}
