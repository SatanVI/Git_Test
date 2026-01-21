package service;


import java.util.List;
import repository.DoctorRepository;
import model.Doctor;
import factory.DoctorFactory;
import observer.Observer;

public class DoctorService {
    private DoctorRepository repository;
    private List<Observer> observers;

    public DoctorService(DoctorRepository repository, List<Observer> observers) {
        this.repository = repository;
        this.observers = observers;
    }


    public Doctor createDoctor(String name, String specialty) {
        Doctor doctor = DoctorFactory.createDoctor(name, specialty);
        repository.save(doctor);
        notifyObservers("Médecin créé: " + doctor.getName() + " (" + doctor.getSpecialty() + ")");
        return doctor;
    }

    public List<Doctor> listDoctors() {
        return repository.findAll();
    }

    private void notifyObservers(String event) {
        for (Observer o : observers) {
            o.update(event);
        }
    }
}