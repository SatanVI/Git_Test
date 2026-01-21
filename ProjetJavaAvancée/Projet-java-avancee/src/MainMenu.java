import service.*;
import java.util.Scanner;

public class MainMenu {
    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private Scanner scanner;

    public MainMenu(PatientService patientService, DoctorService doctorService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Gerer les patients");
            System.out.println("2. Gerer les docteurs");
            System.out.println("3. Gerer les rendez-vous");
            System.out.println("4. Quitter");
            System.out.print("Votre choix: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    managePatients();
                    break;
                case 2:
                    manageDoctors();
                    break;
                case 3:
                    manageAppointments();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
        scanner.close();
    }

    private void managePatients() {
        System.out.println("1. Créer un patient");
        System.out.println("2. Lister les patients");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Pour consommer le saut de ligne

        if (choice == 1) {
            System.out.print("Nom du patient: ");
            String name = scanner.nextLine();
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le saut de ligne
            patientService.createPatient(name, age);
            System.out.println("Patient créé avec succès.");
        } else if (choice == 2) {
            patientService.listPatients().forEach(p -> 
                System.out.println("ID: " + p.getId() + " - " + p.getName() + " (" + p.getAge() + " ans)")
            );
        }
    }

    private void manageDoctors() {
        System.out.println("Fonctionnalite docteurs non implementee.");
    }

    private void manageAppointments() {
        System.out.println("Fonctionnalite rendez-vous non implementee.");
    }
}
