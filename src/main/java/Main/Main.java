/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author black
 */
public class Main {
    public static void main(String args[]) throws IOException {
        
        Engine e = new Engine("C:\\Users\\black\\Desktop\\Test");
        
        e.getAllFilePaths();
        e.initAllDocument();
        e.calculateTermFrequencies();
        List<Document> docs = e.rank_docs_query("big cat afraid");
        Collections.sort(docs, new Sortbyrank().reversed());
        
        for(Document doc : docs) {
            System.out.println("File: " + doc.file.getName() + " Sim: " + doc.rank);
        }
    }
}

class Sortbyrank implements Comparator<Document>
{
    public int compare(Document a, Document b)
    {
        return Float.compare(a.rank, b.rank);
    }
}
