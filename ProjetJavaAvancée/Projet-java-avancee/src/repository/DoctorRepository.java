package repository;

import java.util.*;
import model.Doctor;

public class DoctorRepository {
    private List<Doctor> doctors = new ArrayList<>();
    public Doctor save(Doctor doctor) { doctors.add(doctor); return doctor; }
    public List<Doctor> findAll() { return doctors; }
}
