/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author black
 */
public class Engine {
    String directory_path;
    Stream<Path> path_files;
    List<Document> documents = new ArrayList<>();
    Map<String, Word> countMap = new HashMap<String, Word>();
    
    public Engine(String directory_path) {
        this.directory_path = directory_path;
    }
    
    //get all file path from root directory (does not walk trought all subDirectory)
    boolean getAllFilePaths() {
        try {
            path_files = Files.list(Paths.get(directory_path)).filter((path) -> !Files.isDirectory(path));
        } catch (IOException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }    
        return true;
    }
    
    boolean initAllDocument() {
        path_files.forEach(path -> {
            String ext = FilenameUtils.getExtension(path.toString());
            switch(ext) {
                case "pptx":
                    documents.add(new PPT(path.toFile()));
                    break;
                case "html":
                    documents.add(new HTML(path.toFile()));
                    break;
                case "pdf":
                    documents.add(new PDF(path.toFile()));
                    break;
                default:
                    System.out.println("unsupported extension for filename: " + path.getFileName());
            }
        });
        
        return true;
    }
    
    boolean getTopTermFrequencies(int max_top){
        documents.forEach(document -> {
            System.out.println(document.file.getName());
            try {
                String text = document.toText();
                
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
        });
        
        SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values()).descendingSet();
 
        int count = 1;
        for (Word word : sortedWords) {
            // if(count > max_top) break;
            System.out.println(word.word + ": " + word.count);
            
            count++;
        }
        
        return true;
    }
    
    
    
    public static class Word implements Comparable<Word> {
        String word;
        int count;

        @Override
        public int hashCode() { return word.hashCode(); }

        @Override
        public boolean equals(Object obj) { return word.equals(((Word)obj).word); }
        
        int getCount() {
            return this.count;
        }
        
        String getWord() {
            return this.word;
        }
        
        @Override
        public int compareTo(Word b) { return Comparator.comparing(Word::getCount)
              .thenComparing(Word::getWord)
              .compare(this, b); }
    }
}
