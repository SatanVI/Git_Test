package service;

import java.util.List;
import repository.PatientRepository;
import model.Patient;
import observer.Observer;   // <-- corriger ici

public class PatientService {
    private PatientRepository repository;
    private List<Observer> observers;

    public PatientService(PatientRepository repository, List<Observer> observers) {
        this.repository = repository;
        this.observers = observers;
    }



    public Patient createPatient(String name, int age) {
        Patient p = new Patient(0, name, age);
        Patient saved = repository.save(p);
        notifyObservers("Patient créé: " + saved.getName());
        return saved;
    }

    public List<Patient> listPatients() {
        return repository.findAll();
    }

    private void notifyObservers(String event) {
        for (Observer o : observers) {
            o.update(event);
        }
    }
}