module com.mycompany.chatapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    opens com.mycompany.chatapplication to javafx.fxml;
    exports com.mycompany.chatapplication;
}
