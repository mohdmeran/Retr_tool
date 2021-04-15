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
    float getVectorLength(Engine engine) {
        // Sort countMap base on highest frequency to get the max_freq
        SortedSet<Word> sortedWords = new TreeSet<>(countMap.values()).descendingSet();
        int max_freq = sortedWords.first().count;
        
        float total_weight_vector = 0;
        
        for(Word word : countMap.values()) {
            float normalized_tf = (float) (word.count * 1.0/ max_freq);
            float idf = engine.getidf(word.getWord());
            
            float weight_vector = normalized_tf * idf;
            
            total_weight_vector += weight_vector * weight_vector;
            word.normalized_tf = normalized_tf;
        }
        
        return (float) Math.sqrt(total_weight_vector);
    }
    
}
