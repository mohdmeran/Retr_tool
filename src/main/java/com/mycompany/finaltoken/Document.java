package com.mycompany.finaltoken;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
/**
 *
 * @author black
 */
abstract class Document implements Comparable<Document>{
    File file;
    Map<String, Word> countMap = new HashMap<>();
    float rank;
    
    public Document(File file) {
        this.file = file;
    }
    
    // Abstract function of each different type of document to fetch text
    abstract String toText();
    
    // Calculate Term Frequencies Document level
    Map<String, Word> calculateTermFrequencies(){
        try {
            File f_stopwords = new File("english_stopwords.txt");
            List<String> stop_words = Files.readAllLines(f_stopwords.toPath(), StandardCharsets.UTF_8);
            
            String text = toText();

            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] words = line.split("[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+");
                for (String word : words) {
                    
                    word = word.toLowerCase();
                    
                    // skip if empty string or stop word
                    if ("".equals(word) || stop_words.contains(word)) {
                        continue;
                    }
                    
                    // add word to mapping
                    Word wordObj = countMap.get(word);
                    if (wordObj == null) {
                        wordObj = new Word();
                        wordObj.word = word;
                        wordObj.count = 0;
                        countMap.put(word, wordObj);
                    }

                    wordObj.count++;
                }
            }

            reader.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        return countMap;
    }
    
    // Calculate document vector length
    float getVectorLength(Engine engine) {
        float total_weight_vector = 0;
        
        for(Word word : countMap.values()) {
            float normalized_tf = calculateNormalizedTF(word);
            float idf = engine.getidf(word.getWord());
            
            float weight_vector = normalized_tf * idf;
            
            total_weight_vector += weight_vector * weight_vector;
            
            word.normalized_tf = normalized_tf;
        }

        return (float) Math.sqrt(total_weight_vector);
    }
    
    // return document rank value
    float getRank() {
        return this.rank;
    }
    
    float calculateNormalizedTF(Word word) {
        return (float) (word.count * 1.0);
    }
    
    // Compare document by rank
    @Override
    public int compareTo(Document b) { return Comparator.comparing(Document::getRank)
          .compare(this, b); }
}
