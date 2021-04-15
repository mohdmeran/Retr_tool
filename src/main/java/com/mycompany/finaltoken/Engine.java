package com.mycompany.finaltoken;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author black
 */
public class Engine {
    List<Document> documents = new ArrayList<>();
    Map<String, Word> countMap = new HashMap<>();
    
    public Engine() {}
    
    List<File> addByDirectory(String directory_path) {
        final List<File> temp = new ArrayList<>();
        
        try {
            Stream<Path> path_files = Files.list(Paths.get(directory_path)).filter((path) -> !Files.isDirectory(path));
            path_files.forEach(path -> {
                final File file = path.toFile();
                
                if(addFile(file)){
                    temp.add(file);
                }
            });
        } catch (IOException ex) {
            return null;
        }
        
        return temp;
    }
    
    Boolean addByFile(String file_path) {
        return addFile(new File(file_path)); 
    }
    
    private Boolean addFile(File file) {
        String ext = FilenameUtils.getExtension(file.getPath());
        Boolean res = true;
        Document doc;
        
        switch(ext) {
//            case "pptx":
//                doc = new PPT(file);
//                break;
            case "html":
                doc = new HTML(file);
                break;
            case "pdf":
                doc = new PDF(file);
                break;
            default:
                System.out.println("unsupported extension for filename: " + file.getName());
                doc = null;
                res = false;
        }
        
        if(doc != null) {
            documents.add(doc);
            calculateTermFrequencies(doc);
        }
        
        return res;
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
        
        List<Document> result = binaryFilters(documents, (Query) queryObj);
        
        Collections.sort(result, new Sortbyrank().reversed());
        
        return result;
    }
    
    private Map<String, Word> calculateTermFrequencies(Document document){
        Map<String, Word> temp = document.calculateTermFrequencies();
        for(Word wordObj : temp.values()) {
            Word engine_level_wordObj = countMap.get(wordObj.word);
            if(engine_level_wordObj != null) {
                engine_level_wordObj.count += wordObj.count;
                continue;
            }

            Word tempWord = new Word();
            tempWord.word = wordObj.word;
            tempWord.count = wordObj.count;

            countMap.put(wordObj.word, tempWord);
        }
        
        return countMap;
    }
    
    private List<Document> binaryFilters(List<Document> docs, Query q) {
        List<Document> temp = new ArrayList<>();
        
        for(Document doc : docs) {
            boolean contain_query = false;
            for(Word word : q.countMap.values()) {
                if(doc.countMap.get(word.word) != null) {
                    contain_query = true;
                    break;
                }
            }
            
            if(contain_query) temp.add(doc);
        }
        
        return temp;
    }
    
    class Sortbyrank implements Comparator<Document> {
        public int compare(Document a, Document b)
        {
            return Float.compare(a.rank, b.rank);
        }
    }
}
