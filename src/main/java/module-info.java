module com.ajaxict.edhub {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ajaxict.edhub to javafx.fxml;
    exports com.ajaxict.edhub;
}