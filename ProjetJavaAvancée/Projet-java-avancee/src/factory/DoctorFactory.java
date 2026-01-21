package factory;

import model.Doctor;

public class DoctorFactory {
    private static int nextId = 1;

    public static Doctor createDoctor(String name, String specialty) {
        return new Doctor(nextId++, name, specialty);
    }
}
