module com.ajaxict.edhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.ajaxict.edhub to javafx.fxml;
    exports com.ajaxict.edhub;
}