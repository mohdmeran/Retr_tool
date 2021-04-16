package com.mycompany.finaltoken;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author black
 */
public class Query extends Document {
    
    public Query(File file) {
        super(file);
    }
    
    @Override
    String toText() {
        
        String text = "";
        
        try {
            Scanner sc = new Scanner(file);
            
            while(true) {
                text += sc.nextLine();
                if(sc.hasNextLine()){
                    text += "\n";
                    continue;
                }
                break;
            }
        } catch (FileNotFoundException ex) {
            
        }
        
        return text;
    }
    
    @Override
    float calculateNormalizedTF(Word word) {
        // Sort countMap base on highest frequency to get the max_freq
        SortedSet<Word> sortedWords = new TreeSet<>(countMap.values()).descendingSet();
        int max_freq = sortedWords.first().count;
        
        return (float) (word.count * 1.0/ max_freq);
    }
}
