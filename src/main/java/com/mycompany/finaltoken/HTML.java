package com.mycompany.finaltoken;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;

/**
 *
 * @author black
 */
public class HTML extends Document {

    public HTML(File file) {
        super(file);
    }

    @Override
    String toText() {
        try {
            org.jsoup.nodes.Document doc = Jsoup.parse(file, "UTF-8", "");
            return doc.body().text();
        } catch (IOException ex) {
            return "";
        }
    }
    
}
