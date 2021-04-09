/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
        try {            
            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
            String text = "";
            // get slides
            for (XSLFSlide slide : ppt.getSlides()) {
                for (XSLFShape sh : slide.getShapes()) {
                    String name = sh.getShapeName();
                    if(sh instanceof XSLFTextShape) {
                        text += ((XSLFTextShape) sh).getText() + "\n";
                    }
                }
            }
            
            return text;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PPT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PPT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
}
