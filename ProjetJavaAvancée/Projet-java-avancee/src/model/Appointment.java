package model;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime date;
    private boolean started;
    private boolean finished;

    public Appointment(int id, Patient patient, Doctor doctor, LocalDateTime date) {
        this.id = id; this.patient = patient; this.doctor = doctor; this.date = date;
        this.started = false; this.finished = false;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDateTime getDate() { return date; }
    public boolean isStarted() { return started; }
    public void setStarted(boolean started) { this.started = started; }
    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }
}