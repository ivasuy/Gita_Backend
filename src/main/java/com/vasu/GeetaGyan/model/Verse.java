package com.vasu.GeetaGyan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Verse {
    private int verseNumber;
    private String heading;
    private String shlok;
    private String translation;
}
