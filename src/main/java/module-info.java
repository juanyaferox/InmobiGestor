module com.feroxdev.inmobigestor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires spring.context;
    requires spring.tx;
    requires spring.data.jpa;

    opens com.feroxdev.inmobigestor.controller to javafx.fxml;
    exports com.feroxdev.inmobigestor;
    exports com.feroxdev.inmobigestor.controller;
}