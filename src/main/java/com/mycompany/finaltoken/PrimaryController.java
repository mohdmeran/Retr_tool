package com.mycompany.finaltoken;

import java.io.File;
import java.util.List;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController {
    // Global Variables
    @FXML private VBox container;
    @FXML private ListView<String> lv_files;     
    @FXML private Button b_addFolder;
    @FXML private Button b_addFile;
    @FXML private Button b_searchButton;
    @FXML private Button b_reset;
    @FXML private TextField query_input_field;    
    @FXML private ListView<Hyperlink> lv_rank;
    private HostServices hostServices;

    Engine engine = new Engine();
    
    //  Methods
//    @FXML
//    public void initialize(URL url, ResourceBundle rb) {
//        fileName_rank.setCellValueFactory(new PropertyValueFactory<Document, File>());
//    }
    
    @FXML
    void onClickSearchButton(ActionEvent event) {
        String query = query_input_field.getText();
        
        if(query == null || "".equals(query)) return;
        
        lv_rank.getItems().clear();
        
        List<Document> result = engine.rank_docs_query(query);       
        
        int count = 0;
        for(Document doc : result) {            
            
            String path = doc.file.toString();
            
            final File file = new File(path);

            Hyperlink hp = new Hyperlink("Rank " + (++count) + ": " + path);
            hp.setOnAction(e -> {
               hostServices.showDocument(file.getAbsolutePath());
            });
            lv_rank.getItems().add(hp);
        }
    }

    @FXML
    void onClickAddFile(ActionEvent event) {
        Stage stage = (Stage) container.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile == null) return;
        
        String path = selectedFile.getPath();
        
        if(engine.addByFile(path)){
            lv_files.getItems().add(selectedFile.getPath());
        }
        
        checkResetFolder();
    }

    @FXML
    void onClickAddFolder(ActionEvent event) {
        Stage stage = (Stage) container.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if(selectedDirectory == null) return;
        
        String path = selectedDirectory.getPath();
        
        for(File file : engine.addByDirectory(path)) {
            lv_files.getItems().add(file.toString()); 
        }
        
        checkResetFolder();
    }
    
    @FXML
    void onClickReset(ActionEvent event) {
        engine = new Engine();
        if(engine.documents.size() < 1) {
            lv_files.getItems().clear();
        }
        lv_rank.getItems().clear();
        
        b_reset.setVisible(false);
    }
    
    void checkResetFolder() {
        if(lv_files.getItems().size() > 0) {
            b_reset.setVisible(true);
        }
    }
    
    void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
