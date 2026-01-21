package presentation;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import service.AppointmentService;
import service.PatientService;
import service.DoctorService;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentController implements HttpHandler {
    private AppointmentService appointmentService;
    private PatientService patientService;
    private DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService, PatientService patientService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            int patientId = Integer.parseInt(body.split("\"patientId\":")[1].split(",")[0].trim());
            int doctorId = Integer.parseInt(body.split("\"doctorId\":")[1].split(",")[0].trim());
            String dateStr = body.split("\"date\":\"")[1].split("\"")[0];
            LocalDateTime date = LocalDateTime.parse(dateStr);

            Patient patient = patientService.listPatients().stream().filter(p -> p.getId() == patientId).findFirst().orElse(null);
            Doctor doctor = doctorService.listDoctors().stream().filter(d -> d.getId() == doctorId).findFirst().orElse(null);

            if (patient == null || doctor == null) {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
                return;
            }

            Appointment appt = appointmentService.createAppointment(patient, doctor, date);

            String response = "{ \"id\": " + appt.getId() +
                    ", \"patientId\": " + patient.getId() +
                    ", \"doctorId\": " + doctor.getId() +
                    ", \"date\": \"" + appt.getDate() + "\" }";
            exchange.sendResponseHeaders(201, response.length());
            exchange.getResponseBody().write(response.getBytes());
        }
        else if ("GET".equals(exchange.getRequestMethod())) {
            List<Appointment> appts = appointmentService.listAppointments();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < appts.size(); i++) {
                Appointment a = appts.get(i);
                sb.append("{ \"id\": ").append(a.getId())
                        .append(", \"patientId\": ").append(a.getPatient().getId())
                        .append(", \"doctorId\": ").append(a.getDoctor().getId())
                        .append(", \"date\": \"").append(a.getDate()).append("\" }");
                if (i < appts.size() - 1) sb.append(",");
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
