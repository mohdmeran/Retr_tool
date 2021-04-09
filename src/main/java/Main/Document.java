/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;

/**
 *
 * @author black
 */
abstract class Document {
    File file;
    
    public Document(File file) {
        this.file = file;
    }
    
    abstract String toText();
}
