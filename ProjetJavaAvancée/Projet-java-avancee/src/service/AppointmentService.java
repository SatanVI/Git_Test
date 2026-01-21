package service;

import java.time.LocalDateTime;
import java.util.List;
import repository.AppointmentRepository;
import model.Appointment;
import model.Patient;
import model.Doctor;
import observer.Observer;

public class AppointmentService {
    private AppointmentRepository repository;
    private List<Observer> observers;

    public AppointmentService(AppointmentRepository repository, List<Observer> observers) {
        this.repository = repository;
        this.observers = observers;
    }



    public Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime date) {
        Appointment appt = new Appointment(0, patient, doctor, date);
        Appointment saved = repository.save(appt);
        notifyObservers("Rendez-vous créé: " + patient.getName() + " avec " + doctor.getName());
        return saved;
    }

    public void startAppointment(int id) {
        Appointment appt = repository.findById(id);
        if (appt != null) {
            appt.setStarted(true);
            notifyObservers("Rendez-vous démarré: " + id);
        }
    }

    public void finishAppointment(int id) {
        Appointment appt = repository.findById(id);
        if (appt != null) {
            appt.setFinished(true);
            notifyObservers("Rendez-vous terminé: " + id);
        }
    }

    public List<Appointment> listAppointments() {
        return repository.findAll();
    }

    private void notifyObservers(String event) {
        for (Observer o : observers) {
            o.update(event);
        }
    }
}