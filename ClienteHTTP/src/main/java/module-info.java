module com.example.clientehttp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires javafx.web;
    requires java.logging;
    requires org.jsoup;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens com.example.clientehttp.Controllers to javafx.fxml;

    // Exporta el paquete principal com.example.clientehttp al módulo javafx.graphics
    exports com.example.clientehttp;
    exports com.example.clientehttp.Controllers;
}
