package repository;

import java.util.*;
import model.Appointment;

public class AppointmentRepository {
    private List<Appointment> appointments = new ArrayList<>();
    private int nextId = 1;

    public Appointment save(Appointment appointment) {
        appointment.setId(nextId++);
        appointments.add(appointment);
        return appointment;
    }
    public List<Appointment> findAll() { return appointments; }
    public Appointment findById(int id) {
        return appointments.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }
}
