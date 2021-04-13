/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
    
    float getidf (String word) {
        return countMap.get(word) != null ? countMap.get(word).getIDF(documents.size()) : 0;
    }
    
    List<Document> rank_docs_query(String query){
        try {
            FileWriter queryWriter = new FileWriter("query.txt");
            queryWriter.write(query);
            queryWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File file = new File("query.txt");
        Document queryObj = new Query(file);
        queryObj.calculateTermFrequencies();
        
        float query_vector_length = queryObj.getVectorLength(this);
        Map<String, Word> query_word_map = queryObj.countMap;
        
        for(Document doc : documents) {
            Map<String, Word> doc_word_map = doc.countMap;

            float total_wij_wiq = 0;
            float document_query_vector = doc.getVectorLength(this) * query_vector_length;
            
            for(String query_word : query_word_map.keySet()){
                
                Word doc_word = doc_word_map.get(query_word);
                float wij = doc_word != null ? doc_word.normalized_tf * this.getidf(doc_word.word) : 0;
                float wiq = query_word_map.get(query_word).normalized_tf * this.getidf(query_word);
                
                total_wij_wiq += wij * wiq;
            }
            
            doc.rank = total_wij_wiq == 0 ? 0 : total_wij_wiq / document_query_vector;
        }
        
        return documents;
    }
    
    Map<String, Word> calculateTermFrequencies(){
        documents.forEach(document -> {
            Map<String, Word> temp = document.calculateTermFrequencies();
            for(Word wordObj : temp.values()) {
                Word engine_level_wordObj = countMap.get(wordObj.word);
                if(engine_level_wordObj != null) {
                    engine_level_wordObj.count += wordObj.count;
                    continue;
                }
                
                Word tempWord = new Word();
                tempWord.word = wordObj.word;
                tempWord.count = 1;
                
                countMap.put(wordObj.word, tempWord);
            }
        });
        
        return countMap;
    }
}
