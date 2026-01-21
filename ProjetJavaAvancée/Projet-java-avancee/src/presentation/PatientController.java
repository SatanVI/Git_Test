package presentation;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import service.PatientService;
import model.Patient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PatientController implements HttpHandler {
    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String name = body.split("\"name\":\"")[1].split("\"")[0];
            int age = Integer.parseInt(body.split("\"age\":")[1].split("}")[0].trim());

            Patient patient = patientService.createPatient(name, age);

            String response = "{ \"id\": " + patient.getId() +
                    ", \"name\": \"" + patient.getName() +
                    "\", \"age\": " + patient.getAge() + " }";
            exchange.sendResponseHeaders(201, response.length());
            exchange.getResponseBody().write(response.getBytes());
        }
        else if ("GET".equals(exchange.getRequestMethod())) {
            List<Patient> patients = patientService.listPatients();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < patients.size(); i++) {
                Patient p = patients.get(i);
                sb.append("{ \"id\": ").append(p.getId())
                        .append(", \"name\": \"").append(p.getName())
                        .append("\", \"age\": ").append(p.getAge()).append(" }");
                if (i < patients.size() - 1) sb.append(",");
            }
            sb.append("]");
            String response = sb.toString();
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
        } else {
            exchange.sendResponseHeaders(400, -1);
        }
        exchange.close();
    }
}
