package projetjava.service;


import projetjava.model.Patient;
import projetjava.model.Medecin;
import projetjava.model.RendezVous;
import projetjava.repositories.IRepository;
import projetjava.util.MedicalFactory;
import projetjava.observer.NotificationManager;

import java.util.List;

public class MedicalService {
    private IRepository<Patient> patientRepo;
    private IRepository<Medecin> medecinRepo;
    private IRepository<RendezVous> rdvRepo;
    private NotificationManager notificationManager;

    public MedicalService(IRepository<Patient> patientRepo,
                          IRepository<Medecin> medecinRepo,
                          IRepository<RendezVous> rdvRepo,
                          NotificationManager notifManager) {
        this.patientRepo = patientRepo;
        this.medecinRepo = medecinRepo;
        this.rdvRepo = rdvRepo;
        this.notificationManager = notifManager;
    }

    public Patient registerPatient(String nom, String email) {

        Patient p = MedicalFactory.createPatient(nom, email);

        patientRepo.save(p);

        notificationManager.notifyAll("Nouveau patient créé : " + nom);

        return p;
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public void deletePatient(Long id) {
        patientRepo.delete(id);
        notificationManager.notifyAll("Patient supprimé : ID " + id);
    }


    public Medecin registerMedecin(String nom, String specialite) {
        Medecin m = MedicalFactory.createMedecin(nom, specialite);
        medecinRepo.save(m);
        notificationManager.notifyAll("Nouveau médecin créé : " + nom);
        return m;
    }

    public List<Medecin> getAllMedecins() {
        return medecinRepo.findAll();
    }

    public void deleteMedecin(Long id) {
        medecinRepo.delete(id);
        notificationManager.notifyAll("Médecin supprimé : ID " + id);
    }



    public RendezVous createRendezVous(Long patientId, Long medecinId, String date) {
        if (patientRepo.findById(patientId) == null) {
            throw new IllegalArgumentException("Patient introuvable (ID " + patientId + ")");
        }
        if (medecinRepo.findById(medecinId) == null) {
            throw new IllegalArgumentException("Medecin introuvable (ID " + medecinId + ")");
        }

        RendezVous rdv = MedicalFactory.createRendezVous(patientId, medecinId, date);
        rdvRepo.save(rdv);
        notificationManager.notifyAll("Nouveau RDV planifié le " + date);
        return rdv;
    }

    public boolean startRendezVous(Long id) {
        RendezVous rdv = rdvRepo.findById(id);

        if (rdv != null && "PLANIFIE".equals(rdv.getStatut())) {
            rdv.setStatut("EN_COURS");
            notificationManager.notifyAll("Rendez-vous n°" + id + " a démarré.");
            return true;
        }
        return false;
    }

    public boolean finishRendezVous(Long id) {
        RendezVous rdv = rdvRepo.findById(id);

        if (rdv != null && "EN_COURS".equals(rdv.getStatut())) {
            rdv.setStatut("TERMINE");
            notificationManager.notifyAll("Rendez-vous n°" + id + " est terminé.");
            return true;
        }
        return false;
    }

    public List<RendezVous> getAllRendezVous() {
        return rdvRepo.findAll();
    }
}