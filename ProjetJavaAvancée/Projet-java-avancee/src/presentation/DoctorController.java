package presentation;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import service.DoctorService;
import model.Doctor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DoctorController implements HttpHandler {
    private DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String name = body.split("\"name\":\"")[1].split("\"")[0];
            String specialty = body.split("\"specialty\":\"")[1].split("\"")[0];

            Doctor doctor = doctorService.createDoctor(name, specialty);

            String response = "{ \"id\": " + doctor.getId() +
                    ", \"name\": \"" + doctor.getName() +
                    "\", \"specialty\": \"" + doctor.getSpecialty() + "\" }";
            exchange.sendResponseHeaders(201, response.length());
            exchange.getResponseBody().write(response.getBytes());
        }
        else if ("GET".equals(exchange.getRequestMethod())) {
            List<Doctor> doctors = doctorService.listDoctors();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < doctors.size(); i++) {
                Doctor d = doctors.get(i);
                sb.append("{ \"id\": ").append(d.getId())
                        .append(", \"name\": \"").append(d.getName())
                        .append("\", \"specialty\": \"").append(d.getSpecialty()).append("\" }");
                if (i < doctors.size() - 1) sb.append(",");
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
