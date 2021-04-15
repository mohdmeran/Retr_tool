package com.mycompany.finaltoken;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//import org.apache.poi.xslf.usermodel.XMLSlideShow;
//import org.apache.poi.xslf.usermodel.XSLFShape;
//import org.apache.poi.xslf.usermodel.XSLFSlide;
//import org.apache.poi.xslf.usermodel.XSLFTextShape;
/**
 *
 * @author black
 */
public class PPT extends Document {
    public PPT(File file) {
        super(file);
    }

    @Override
    String toText() {
//        try {            
//            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
//            String text = "";
//            // get slides
//            for (XSLFSlide slide : ppt.getSlides()) {
//                for (XSLFShape sh : slide.getShapes()) {
//                    String name = sh.getShapeName();
//                    if(sh instanceof XSLFTextShape) {
//                        text += ((XSLFTextShape) sh).getText() + "\n";
//                    }
//                }
//            }
//            
//            return text;
//        } catch (FileNotFoundException ex) {
//            
//        } catch (IOException ex) {
//            
//        }
        
        return "";
    }
}
