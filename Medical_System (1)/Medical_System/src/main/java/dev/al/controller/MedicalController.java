package dev.al.controller;
import dev.al.constants.Roles;
import dev.al.model.*;
import dev.al.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

@Controller
public class MedicalController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final UserService userService;

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public MedicalController(AppointmentService appointmentService,
                             DoctorService doctorService,
                             PatientService patientService,
                             PrescriptionService prescriptionService,
                             UserService userService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
        this.userService = userService;
    }
//Faqja e login
    public void start() {
        System.out.println("Mireseerdhet!");

        User user = null;
        while (user == null) {
            System.out.print("Emri i llogarise: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            Optional<User> opt = userService.login(username, password);
            if (opt.isPresent()) {
                user = opt.get();
                System.out.println("Mireseerdhe: " + user.getRole());
            } else {
                System.out.println("Ndodhi nje gabim. Provojeni perseri.");
            }
        }

        switch (user.getRole()) {
            case Roles.DOCTOR:
                runDoctorMenu(user);
                break;
            case Roles.PATIENT:
                runPatientMenu(user);
                break;
            default:
                System.out.println("Ky rol nuk eshte i vlefshem. Provojeni perseri.");
        }
    }
//Faqja e rolit DOCTOR
    private void runDoctorMenu(User user) {
        Doctor doctor = doctorService.findDoctorById(user.getLinkedId())
                .orElseThrow(() -> new RuntimeException("Doktori nuk u gjet."));

        while (true) {
            System.out.println("\nMenu e doktorit:");
            System.out.println("1. Vizitat e mia");
            System.out.println("2. Pacientet e mi");
            System.out.println("3. Kerko nje pacient");
            System.out.println("4. Orari i doktoreve");
            System.out.println("5. Shto nje pacient");
            System.out.println("6. Anulo nje pacient");
            System.out.println("7. Dil nga llogaria");
            System.out.print("Zgjidhni nje opsion: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<Appointment> apps = appointmentService.getAppointmentsForDoctor(doctor.getId());
                    apps.forEach(System.out::println);
                    break;

                case "2":
                    Set<Patient> patients = doctorService.getPatients(doctor.getId());
                    patients.forEach(System.out::println);
                    break;
                case "3":
                    System.out.print("Shkruani ID-në e pacientit për kërkim: ");
                    try {
                        long patientId = Long.parseLong(scanner.nextLine());
                        Optional<Patient> patientOpt = patientService.findPatientById(patientId);
                        if (patientOpt.isPresent()) {
                            System.out.println("Pacienti u gjet: " + patientOpt.get());
                        } else {
                            System.out.println("Pacienti nuk u gjet me ID-në e dhënë.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ju lutem shkruani një numër valid për ID-në e pacientit.");
                    }
                    break;

                case "4":
                    doctorService.findAvailableDoctors()
                            .forEach(System.out::println);
                    break;

                case "5":
                    System.out.print("Patient ID to add: ");
                    long ptoAdd = Long.parseLong(scanner.nextLine());
                    doctorService.addPatient(doctor.getId(), ptoAdd);
                    System.out.println("Patient added.");
                    break;

                case "6":
                    System.out.print("Patient ID to remove: ");
                    long ptoRemove = Long.parseLong(scanner.nextLine());
                    doctorService.removePatient(doctor.getId(), ptoRemove);
                    System.out.println("Patient removed.");
                    break;

                case "7":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
//Faqja e rolit PATIENT
    private void runPatientMenu(User user) {
        Patient patient = patientService.findPatientById(user.getLinkedId())
                .orElseThrow(() -> new RuntimeException("Pacienti nuk u gjet"));

        while (true) {
            System.out.println("\nMenu e pacientit:");
            System.out.println("1. Vizitat e mia");
            System.out.println("2. Caktoni nje vizite");
            System.out.println("3. Anuloni nje vizite");
            System.out.println("4. Receta ime");
            System.out.println("5. Dil nga llogaria");
            System.out.print("Zgjidhni nje opsion: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    appointmentService.getAppointmentsForPatient(patient.getId())
                            .forEach(System.out::println);
                    break;

                case "2":
                    System.out.print("ID e doktorit: ");
                    long did = Long.parseLong(scanner.nextLine());
                    System.out.print("Arsyeja: ");
                    String reason = scanner.nextLine();
                    Appointment ap = new Appointment();
                    ap.setDoctor(doctorService.findDoctorById(did).orElseThrow());
                    ap.setPatient(patient);
                    ap.setAppointmentDate(LocalDateTime.now());
                    ap.setAppointmentTime(LocalDateTime.now().plusDays(1));
                    ap.setReason(reason);
                    appointmentService.scheduleAppointment(ap);
                    System.out.println("Vizita u caktua.");
                    break;

                case "3":
                    System.out.print("ID e vizites: ");
                    long aid = Long.parseLong(scanner.nextLine());
                    if (appointmentService.delete(aid)) {
                        System.out.println("Vizita u anulua.");
                    } else {
                        System.out.println("Vizita nuk u anulua dot. Provojeni perseri.");
                    }
                    break;

                case "4":
                    for (Appointment app : appointmentService.getAppointmentsForPatient(patient.getId())) {
                        prescriptionService.getPrescriptionsByAppointment(app.getId())
                                .forEach(System.out::println);
                    }
                    break;

                case "5":
                    return;
                default:
                    System.out.println("Zgjedhje e pavlefshme.");
            }
        }
    }
}