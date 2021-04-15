module com.mycompany.finaltoken {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
//    requires poi.ooxml;
    requires org.apache.pdfbox;
    requires org.jsoup;

    opens com.mycompany.finaltoken to javafx.fxml;
    exports com.mycompany.finaltoken;
    
}
