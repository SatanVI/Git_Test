package presentation;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import service.AppointmentService;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AppointmentFinishContoller implements HttpHandler {
    private AppointmentService appointmentService;

    public AppointmentFinishContoller(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            // Exemple attendu: {"id":1}
            int id = Integer.parseInt(body.split("\"id\":")[1].split("}")[0].trim());

            appointmentService.finishAppointment(id);

            String response = "{ \"status\": \"finished\", \"appointmentId\": " + id + " }";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
        } else {
            exchange.sendResponseHeaders(400, -1);
        }
        exchange.close();
    }
}
