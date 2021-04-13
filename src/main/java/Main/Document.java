/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author black
 */
abstract class Document implements Comparable<Document>{
    File file;
    Map<String, Word> countMap = new HashMap<String, Word>();
    float rank;
    
    public Document(File file) {
        this.file = file;
    }
    
    abstract String toText();
    
    Map<String, Word> calculateTermFrequencies(){
        try {
            String text = toText();

            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] words = line.split("[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+");
                for (String word : words) {
                    if ("".equals(word)) {
                        continue;
                    }

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
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return countMap;
    }
    
    float getVectorLength(Engine engine) {
        float total_weight_vector = 0;
        
        for(Word word : countMap.values()) {
            float normalized_tf = (float) (word.count * 1.0);
            float idf = engine.getidf(word.getWord());
            
            float weight_vector = normalized_tf * idf;
            
            total_weight_vector += weight_vector * weight_vector;
            
            word.normalized_tf = normalized_tf;
        }

        return (float) Math.sqrt(total_weight_vector);
    }
    
    float getRank() {
        return this.rank;
    }
    
    public int compareTo(Document b) { return Comparator.comparing(Document::getRank)
          .compare(this, b); }
}
