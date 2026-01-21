package repository;


import java.util.*;
import model.Patient;

public class PatientRepository {
    private List<Patient> patients = new ArrayList<>();
    private int nextId = 1;

    public Patient save(Patient patient) {
        patient.setId(nextId++);
        patients.add(patient);
        return patient;
    }
    public List<Patient> findAll() { return patients; }
    public boolean delete(int id) { return patients.removeIf(p -> p.getId() == id); }
}
