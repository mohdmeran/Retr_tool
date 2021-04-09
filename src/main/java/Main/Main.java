/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author black
 */
public class Main {
    public static void main(String args[]) throws IOException {
        
        Engine e = new Engine("C:\\Users\\black\\Desktop\\Test");
        
        e.getAllFilePaths();
        e.initAllDocument();
        e.getTopTermFrequencies(100);
    }
}
