/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.Comparator;

/**
 *
 * @author black
 */
public class Word implements Comparable<Word> {
    String word;
    int count = 0;
    float normalized_tf;

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
    
    public float getIDF(int total_documents) {
        // log (N/tf)
//        System.out.println("total doc: " + total_documents);
//        System.out.println(word + " count: " + count);       
//        System.out.println("idf" + (float) Math.log10(total_documents * 1.0 / count));
        return (float) Math.log10(total_documents * 1.0 / count);
    }

    @Override
    public int compareTo(Word b) { return Comparator.comparing(Word::getCount)
          .thenComparing(Word::getWord)
          .compare(this, b); }
}
