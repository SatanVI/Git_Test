// src/Main.java
import com.sun.net.httpserver.HttpServer;
import observer.AuditObserver;
import observer.LogObserver;
import observer.NotificationObserver;
import observer.Observer;
import presentation.*;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import service.AppointmentService;
import service.DoctorService;
import service.PatientService;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // === Initialisation des Observers ===
        List<Observer> observers = Arrays.asList(
                new NotificationObserver(),
                new LogObserver(),
                new AuditObserver()
        );

        // === Initialisation des Repositories ===
        PatientRepository patientRepo = new PatientRepository();
        DoctorRepository doctorRepo = new DoctorRepository();
        AppointmentRepository appointmentRepo = new AppointmentRepository();

        // === Initialisation des Services ===
        PatientService patientService = new PatientService(patientRepo, observers);
        DoctorService doctorService = new DoctorService(doctorRepo, observers);
        AppointmentService appointmentService = new AppointmentService(appointmentRepo, observers);

        // === Choix du mode ===
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Choisissez le mode de fonctionnement ===");
        System.out.println("1. Menu console interactif");
        System.out.println("2. Serveur HTTP (port 8080)");
        System.out.print("Votre choix: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            // Mode console
            MainMenu menu = new MainMenu(patientService, doctorService, appointmentService);
            menu.start();
        } else if (choice == 2) {
            // Mode serveur HTTP
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/health", exchange -> {
                String response = "{ \"status\": \"ok\" }";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            });

            server.createContext("/patients", new PatientController(patientService));
            server.createContext("/doctors", new DoctorController(doctorService));
            server.createContext("/appointments", new AppointmentController(appointmentService, patientService, doctorService));
            server.createContext("/appointments/start", new AppointmentStartController(appointmentService));
            server.createContext("/appointments/finish", new AppointmentFinishController(appointmentService));

            server.start();
            System.out.println("Succes: Serveur demarre sur le port 8080");
        } else {
            System.out.println("Erreur: Choix invalide. Programme termine.");
        }

        sc.close();
    }
}