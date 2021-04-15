package com.mycompany.finaltoken;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author black
 */
public class PDF extends Document {
    
    public PDF(File file) {
        super(file);
    }
    
    @Override
    String toText() {
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            
            String text = pdfStripper.getText(document);
            document.close();
            return text;
        } catch (IOException ex) {
            return "";
        }
    }
    
}
