package presentation;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import service.AppointmentService;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AppointmentStartController implements HttpHandler {
    private AppointmentService appointmentService;

    public AppointmentStartController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Lecture du corps de la requête
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            // Exemple attendu: {"id":1}
            int id = Integer.parseInt(body.split("\"id\":")[1].split("}")[0].trim());

            // Appel du service
            appointmentService.startAppointment(id);

            // Réponse JSON
            String response = "{ \"status\": \"started\", \"appointmentId\": " + id + " }";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(400, -1); // Mauvaise méthode
        }
        exchange.close();
    }
}