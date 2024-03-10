package com.example.clientehttp.Controllers;

import com.example.clientehttp.HttpStatusMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.scene.image.Image;

import java.io.*;
import java.net.*;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javafx.stage.FileChooser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import java.nio.file.Files;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class HelloController {
    @FXML
    private RadioButton rbPretty;

    @FXML
    private Label lbTipoContenido;

    @FXML
    private Label lbRespuestaHTTP;

    @FXML
    private ComboBox<String> cbMetodo;

    @FXML
    private RadioButton rbRaw;

    @FXML
    private TextField tfURL;

    private ToggleGroup toggleGroup;

    private HttpClient httpClient;

    private String responseContent = "";

    private String contentType = "";

    private String URLConsulta = "";

    @FXML
    private TextArea taHeaders;

    @FXML
    private Tab tabBody;

    @FXML
    private WebView pWebView;

    @FXML
    private TextArea pTextArea;

    @FXML
    private ImageView ivBody;

    @FXML
    void btnGuardarRespuesta(ActionEvent event) {
        if (contentType != null) {
            if (contentType.startsWith("text/html")) {
                guardarHTML();
            } else if (contentType.startsWith("image/")) {
                guardarImagen();
            } else if (contentType.startsWith("application/pdf")) {
                guardarPDF();
            } else if (contentType.startsWith("application/json")) {
                guardarJSON();
            }
        }
    }

    private void guardarHTML() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos HTML (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                Document doc = Jsoup.parse(responseContent);
                doc.outputSettings().escapeMode(EscapeMode.xhtml);
                doc.outputSettings().prettyPrint(true);

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(doc.outerHtml());
                writer.close();
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al intentar guardar el archivo. Por favor, inténtalo de nuevo.");
            }
        }
    }

    private void guardarImagen() {
        try {
            // Crea un objeto URL con la dirección de la imagen
            URL imageUrl = new URL(URLConsulta);

            // Abre una conexión a la URL y obtiene el flujo de entrada para leer la imagen
            InputStream inputStream = imageUrl.openStream();

            // Configura el diálogo para que el usuario pueda elegir la ubicación y el nombre del archivo
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = null;

            // Determina la extensión del archivo basándose en el tipo de contenido
            if (contentType.startsWith("image/png")) {
                extFilter = new FileChooser.ExtensionFilter("Imagen PNG (*.png)", "*.png");
            } else if (contentType.startsWith("image/jpeg")) {
                extFilter = new FileChooser.ExtensionFilter("Imagen JPEG (*.jpg)", "*.jpg");
            } else if (contentType.startsWith("image/gif")) {
                extFilter = new FileChooser.ExtensionFilter("Imagen GIF (*.gif)", "*.gif");
            }

            if (extFilter != null) {
                fileChooser.getExtensionFilters().add(extFilter);
            } else {
                // Si el tipo de contenido no es reconocido, usar una extensión genérica
                extFilter = new FileChooser.ExtensionFilter("Imagen (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);
            }

            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                // Lee los datos de la imagen del flujo de entrada y escribe en el archivo seleccionado
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarPDF() {
        try {
            // Crea un objeto URL con la dirección del PDF
            URL url = new URL(URLConsulta);

            // Abre una conexión a la URL y obtiene el flujo de entrada para leer el PDF
            InputStream inputStream = url.openStream();

            // Configura el diálogo para que el usuario pueda elegir la ubicación y el nombre del archivo
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                // Lee los datos del PDF del flujo de entrada y escribe en el archivo seleccionado
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarJSON() {
        try {
            // Crea un objeto URL con la dirección del JSON
            URL url = new URL(URLConsulta);

            // Abre una conexión a la URL y obtiene el flujo de entrada para leer el JSON
            InputStream inputStream = url.openStream();

            // Configura el diálogo para que el usuario pueda elegir la ubicación y el nombre del archivo
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos JSON (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                // Lee los datos del JSON del flujo de entrada y escribe en el archivo seleccionado
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iniciarVentana() {
        iniciarComboBox();
        crearGrupoRadioButton();
        httpClient = HttpClient.newHttpClient();
        cbMetodo.getSelectionModel().select(0);
        rbRaw.setSelected(true);

    }

    private void iniciarComboBox() {
        ObservableList<String> metodos = FXCollections.observableArrayList("GET", "HEAD", "OPTIONS");
        cbMetodo.setItems(metodos);
    }

    private void crearGrupoRadioButton() {
        toggleGroup = new ToggleGroup();
        rbPretty.setToggleGroup(toggleGroup);
        rbRaw.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });
    }

    @FXML
    void btnConsultar(ActionEvent event) {
        lbRespuestaHTTP.setTextFill(Color.BLACK);
        lbRespuestaHTTP.setText("Procesando Petición");
        new Thread(() -> consultaHTTP()).start();
    }

    private void consultaHTTP() {
        String url = tfURL.getText().trim();
        URLConsulta = url;
        if (!url.isEmpty()) {
            String metodo = cbMetodo.getValue();

            if (metodo != null && !metodo.isEmpty()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .method(metodo, HttpRequest.BodyPublishers.noBody())
                        .build();

                Task<HttpResponse<String>> task = new Task<>() {
                    @Override
                    protected HttpResponse<String> call() throws Exception {
                        return httpClient.send(request, BodyHandlers.ofString());
                    }
                };

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        Platform.runLater(() -> mostrarRespuestaHTTP(task));
                    }
                });

                task.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        System.out.println("Error al realizar la consulta HTTP.");
                    }
                });

                new Thread(task).start();
            } else {
                System.out.println("Error. Seleccione un método HTTP.");
            }
        } else {
            System.out.println("Error. Ingrese una URL válida.");
        }
    }


    private void mostrarRespuestaHTTP (Task<HttpResponse<String>> task) {
        HttpResponse<String> response = task.getValue();
        int statusCode = response.statusCode();
        String responseBody = response.body();
        String statusMessage = statusCode + " - " + HttpStatusMessage.getMessage(statusCode); // Mensaje de estado genérico
        lbRespuestaHTTP.setText(statusMessage);
        lbTipoContenido.setText(response.headers().firstValue("Content-Type").orElse("Desconocido"));

        Color color;
        if (statusCode >= 100 && statusCode < 200) {
            color = Color.BLUE;
        } else if (statusCode >= 200 && statusCode < 300) {
            color = Color.GREEN;
        } else if (statusCode >= 300 && statusCode < 400) {
            color = Color.YELLOW;
        } else if (statusCode >= 400 && statusCode < 500) {
            color = Color.ORANGE;
        } else {
            color = Color.RED;
        }
        lbRespuestaHTTP.setTextFill(color);


        StringBuilder headersText = new StringBuilder();
        for (String key : response.headers().map().keySet()) {
            List<String> values = response.headers().allValues(key);
            for (String value : values) {
                headersText.append(key).append(": ").append(value).append("\n");
            }
        }

        taHeaders.setText(headersText.toString());

        responseContent = responseBody;
        contentType = response.headers().firstValue("Content-Type").orElse("Desconocido");

        if (rbRaw.isSelected()) {
            tabBody.setContent(pTextArea);
            pTextArea.setText(responseBody);

        } else if (rbPretty.isSelected()) {
            if (contentType.startsWith("text/html")) {
                tabBody.setContent(pWebView);
                pWebView.getEngine().loadContent(responseBody);
            } else if (contentType.startsWith("image/")) {
                tabBody.setContent(ivBody);
                try {
                    URL imageUrl = new URL(URLConsulta);
                    Image image = new Image(imageUrl.toExternalForm());
                    ivBody.setImage(image);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            } else if (contentType.startsWith("application/pdf")) {
                tabBody.setContent(pTextArea);
                pTextArea.setText("No hay visualizador disponible para este tipo de contenido pero lo puedes descargar y visualizar en el equipo.");
            }
        }
        System.out.println("Consulta completada. La consulta HTTP se ha completado correctamente.");
    }

}