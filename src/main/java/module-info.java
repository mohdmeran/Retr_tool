module com.mycompany.finaltoken {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires org.apache.pdfbox;
    requires org.jsoup;
    requires poi.ooxml;
//    requires poi;

    opens com.mycompany.finaltoken to javafx.fxml;
    exports com.mycompany.finaltoken; 
}
