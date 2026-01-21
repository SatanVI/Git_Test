package ui;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

import domain.Book;
import domain.Rental;
import domain.Reservation;
import domain.Sale;
import domain.User;
import patterns.observer.EventBus;
import patterns.singleton.AppLogger;
import repository.BookRepository;
import repository.FileStorage;
import repository.UserRepository;
import service.BookService;
import service.PaymentService;
import service.RentalService;
import service.ReservationService;
import service.SaleService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepo = new BookRepository();
        UserRepository userRepo = new UserRepository();
        EventBus bus = new EventBus();

        bus.subscribe("SALE_CREATED", msg -> System.out.println("[VENTE] " + msg));
        bus.subscribe("RENTAL_CREATED", msg -> System.out.println("[LOCATION] " + msg));
        bus.subscribe("LATE_RENTAL", msg -> System.out.println("[ALERTE RETARD] " + msg));
        bus.subscribe("RENTAL_RETURNED", msg -> System.out.println("[RETOUR] " + msg));
        bus.subscribe("RESERVATION_CREATED", msg -> System.out.println("[RESERVATION] " + msg));
        bus.subscribe("RESERVATION_READY", msg -> System.out.println("[DISPONIBILITÉ] " + msg));
        bus.subscribe("RESERVATION_EXPIRED", msg -> System.out.println("[EXPIRÉE] " + msg));

        BookService bookService = new BookService(bookRepo);
        UserService userService = new UserService(userRepo);
        SaleService saleService = new SaleService(bookRepo, bus);
        PaymentService paymentService = new PaymentService(bus);
        RentalService rentalService = new RentalService(bookRepo, bus, paymentService);
        ReservationService reservationService = new ReservationService(bookRepo, bus);

        Scanner sc = new Scanner(System.in);
        AppLogger.getInstance().info("Application démarrée");
        System.out.println("=== Librairie Nimba ===");

    

        // Authentication
        User currentUser = null;
        while (currentUser == null) {
            System.out.println("\nConnexion:\n1. Se connecter  2. S'inscrire  0. Quitter");
            System.out.print("Choix: ");
            String a = sc.nextLine();
            if ("0".equals(a)) return;
            if ("1".equals(a)) {
                System.out.print("Username: "); String un = sc.nextLine();
                System.out.print("Password: "); String pw = sc.nextLine();
                var opt = userService.authenticate(un, pw);
                if (opt.isPresent()) {
                    currentUser = opt.get();
                    System.out.println("Connecté en tant que: " + currentUser.getNom() + " (" + currentUser.getRole() + ")");
                } else {
                    System.out.println("Échec connexion.");
                }
            } else if ("2".equals(a)) {
                System.out.print("Nom: "); String nom = sc.nextLine();
                System.out.print("Type (etudiant/enseignant/standard): "); String type = sc.nextLine();
                System.out.print("Choisir un username: "); String username = sc.nextLine();
                System.out.print("Choisir un password: "); String password = sc.nextLine();
                var existing = userService.findByUsername(username);
                if (existing.isPresent()) {
                    System.out.println("Username déjà pris.");
                } else {
                    User u = new User(UUID.randomUUID().toString(), nom, type, username, password, "USER");
                    userService.addUser(u);
                    System.out.println("Compte créé. Veuillez vous connecter.");
                    System.out.println("Votre ID: " + u.getId());
                    AppLogger.getInstance().info("Inscription utilisateur: " + username + " id=" + u.getId());
                }
            }
        }

        while (true) {
            boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
            if (isAdmin) {
                System.out.println("\nMenu admin :");
                System.out.println("1. Livres");
                System.out.println("2. Utilisateurs");
                System.out.println("3. Vente");
                System.out.println("4. Location");
                System.out.println("5. Réservation");
                System.out.println("6. Dashboard");
                System.out.println("0. Quitter");
                System.out.print("Choix : ");
                String choix = sc.nextLine();
                if ("0".equals(choix)) { System.out.println(" Au revoir !"); return; }
                switch (choix) {
                    case "1" -> {
                        System.out.println("a. Ajouter  b. Lister  c. Rechercher  d. Supprimer");
                        String c1 = sc.nextLine();
                        if ("a".equals(c1)) {
                            System.out.print("ISBN : "); String isbn = sc.nextLine();
                            System.out.print("Titre : "); String titre = sc.nextLine();
                            System.out.print("Auteur : "); String auteur = sc.nextLine();
                            System.out.print("Catégorie : "); String cat = sc.nextLine();
                            System.out.print("Prix : "); double prix = Double.parseDouble(sc.nextLine());
                            System.out.print("Stock : "); int stock = Integer.parseInt(sc.nextLine());
                            Book book = new Book(UUID.randomUUID().toString(), isbn, titre, auteur, cat, prix, stock);
                            bookService.addBook(book);
                            System.out.println(" Livre ajouté !");
                            AppLogger.getInstance().info("Livre ajouté: " + isbn + " - " + titre);
                        } else if ("b".equals(c1)) {
                            bookService.listAll().forEach(System.out::println);
                        } else if ("c".equals(c1)) {
                            System.out.print("ISBN : "); String isbn = sc.nextLine();
                            bookService.findByIsbn(isbn).ifPresentOrElse(System.out::println, () -> System.out.println("❌ Introuvable."));
                        } else if ("d".equals(c1)) {
                            System.out.print("ISBN : "); String isbn = sc.nextLine();
                            bookService.deleteBook(isbn);
                            System.out.println(" Livre supprimé.");
                        }
                    }

                    case "2" -> {
                        System.out.println("a. Ajouter  b. Lister  c. Supprimer  d. Mon profil");
                        String c2 = sc.nextLine();
                        if ("a".equals(c2)) {
                            System.out.print("Nom : "); String nom = sc.nextLine();
                            System.out.print("Type (etudiant/enseignant/standard) : "); String type = sc.nextLine();
                            System.out.print("Username : "); String username = sc.nextLine();
                            System.out.print("Password : "); String password = sc.nextLine();
                            User user = new User(UUID.randomUUID().toString(), nom, type, username, password, "USER");
                            userService.addUser(user);
                            System.out.println("Utilisateur ajouté !");
                            System.out.println("ID utilisateur créé: " + user.getId());
                            AppLogger.getInstance().info("Utilisateur ajouté: " + username + " (" + nom + ") id=" + user.getId());
                        } else if ("b".equals(c2)) {
                            userService.listAll().forEach(u -> System.out.println(u.getId() + " | " + u.getUsername() + " | " + u.getNom() + " | " + u.getRole()));
                        } else if ("c".equals(c2)) {
                            System.out.print("ID utilisateur : "); String id = sc.nextLine();
                            userService.deleteUser(id);
                            System.out.println(" Utilisateur supprimé.");
                        } else if ("d".equals(c2)) {
                            System.out.println("Profil: " + currentUser);
                        }
                    }

                    case "3" -> {
                        System.out.print("Nom du livre : "); String name = sc.nextLine();
                        var matches = bookService.findByTitle(name);
                        if (matches.isEmpty()) { System.out.println("❌ Aucun livre trouvé pour ce nom."); break; }
                        Book choisi;
                        if (matches.size() == 1) {
                            choisi = matches.get(0);
                        } else {
                            System.out.println("Plusieurs livres trouvés :");
                            for (int i=0;i<matches.size();i++) System.out.println((i+1)+". " + matches.get(i));
                            System.out.print("Choisir le numéro (ou 0 pour annuler) : ");
                            int idx = Integer.parseInt(sc.nextLine());
                            if (idx <= 0 || idx > matches.size()) { System.out.println("Annulé."); break; }
                            choisi = matches.get(idx-1);
                        }

                        System.out.print("ID utilisateur : "); String userId = sc.nextLine();
                        var optUser = userService.findById(userId);
                        if (optUser.isEmpty()) { System.out.println("❌ Utilisateur introuvable."); break; }

                        System.out.print("Paiement (carte/especes/paypal) : "); String pay = sc.nextLine();
                        try {
                            Sale sale = saleService.createSale(choisi, optUser.get(), pay);
                            System.out.println("Vente effectuée : " + sale);
                            AppLogger.getInstance().info("Vente effectuée: " + sale);
                        } catch (IllegalStateException ise) {
                            System.out.println("❌ Erreur : " + ise.getMessage());
                            AppLogger.getInstance().error("Erreur vente: " + ise.getMessage());
                        } catch (Exception ex) {
                            System.out.println("❌ Une erreur est survenue lors de la vente.");
                            AppLogger.getInstance().error("Erreur vente inattendue: " + ex.getMessage());
                        }
                    }

                    case "4" -> {
                        System.out.print("Nom du livre : "); String name = sc.nextLine();
                        var matches = bookService.findByTitle(name);
                        if (matches.isEmpty()) { System.out.println("❌ Aucun livre trouvé pour ce nom."); break; }
                        Book choisi;
                        if (matches.size() == 1) {
                            choisi = matches.get(0);
                        } else {
                            System.out.println("Plusieurs livres trouvés :");
                            for (int i=0;i<matches.size();i++) System.out.println((i+1)+". " + matches.get(i));
                            System.out.print("Choisir le numéro (ou 0 pour annuler) : ");
                            int idx = Integer.parseInt(sc.nextLine());
                            if (idx <= 0 || idx > matches.size()) { System.out.println("Annulé."); break; }
                            choisi = matches.get(idx-1);
                        }

                        System.out.print("ID utilisateur : "); String userId = sc.nextLine();
                        var optUser = userService.findById(userId);
                        if (optUser.isEmpty()) { System.out.println("❌ Utilisateur introuvable."); break; }

                        System.out.print("Durée (jours) : "); int days = Integer.parseInt(sc.nextLine());
                        Rental rental = null;
                        try {
                            rental = rentalService.createRental(choisi, optUser.get(), days);
                            System.out.println(" Location créée : " + rental);
                            AppLogger.getInstance().info("Location créée: " + rental);
                        } catch (IllegalStateException ise) {
                            System.out.println("❌ Erreur : " + ise.getMessage());
                            AppLogger.getInstance().error("Erreur location: " + ise.getMessage());
                        } catch (Exception ex) {
                            System.out.println("❌ Une erreur est survenue lors de la location.");
                            AppLogger.getInstance().error("Erreur location inattendue: " + ex.getMessage());
                        }

                        System.out.print("Retouner le livre ? (y/n) : "); String ret = sc.nextLine();
                        if ("y".equalsIgnoreCase(ret)) {
                            if (rental == null) {
                                System.out.println("❌ Aucun enregistrement de location disponible pour le retour.");
                            } else {
                                LocalDate returnDate = null;
                                while (returnDate == null) {
                                    System.out.print("Date retour (yyyy-MM-dd) : "); String dateStr = sc.nextLine();
                                    try {
                                        returnDate = LocalDate.parse(dateStr);
                                    } catch (java.time.format.DateTimeParseException dtpe) {
                                        System.out.println("❌ Format de date invalide. Utilisez yyyy-MM-dd. Réessayez.");
                                    }
                                }
                                System.out.print("Moyen de paiement pour pénalité (carte/especes/paypal) : "); String payType = sc.nextLine();
                                String paymentMsg = rentalService.returnBook(rental, optUser.get(), returnDate, payType);
                                System.out.println(" Retour traité : " + rental);
                                if (paymentMsg != null) System.out.println(paymentMsg);
                                AppLogger.getInstance().info("Retour traité pour location: " + rental);
                            }
                        }
                    }

                    case "5" -> {
                        System.out.print("Nom du livre : "); String name = sc.nextLine();
                        var matches = bookService.findByTitle(name);
                        if (matches.isEmpty()) { System.out.println("❌ Aucun livre trouvé pour ce nom."); break; }
                        Book choisi;
                        if (matches.size() == 1) {
                            choisi = matches.get(0);
                        } else {
                            System.out.println("Plusieurs livres trouvés :");
                            for (int i=0;i<matches.size();i++) System.out.println((i+1)+". " + matches.get(i));
                            System.out.print("Choisir le numéro (ou 0 pour annuler) : ");
                            int idx = Integer.parseInt(sc.nextLine());
                            if (idx <= 0 || idx > matches.size()) { System.out.println("Annulé."); break; }
                            choisi = matches.get(idx-1);
                        }

                        System.out.print("ID utilisateur : "); String userId = sc.nextLine();
                        var optUser = userService.findById(userId);
                        if (optUser.isEmpty()) { System.out.println("❌ Utilisateur introuvable."); break; }

                        System.out.print("Validité (jours) : "); int validDays = Integer.parseInt(sc.nextLine());
                        try {
                            Reservation reservation = reservationService.createReservation(choisi, optUser.get(), validDays);
                            System.out.println(" Réservation créée : " + reservation);
                        } catch (Exception ex) {
                            System.out.println("❌ Une erreur est survenue lors de la réservation.");
                            AppLogger.getInstance().error("Erreur réservation: " + ex.getMessage());
                        }

                        System.out.print("Notifier disponibilité ? (y/n) : "); String notif = sc.nextLine();
                        if ("y".equalsIgnoreCase(notif)) {
                            reservationService.notifyAvailability(choisi);
                        }

                        System.out.print("Vérifier expirations ? (y/n) : "); String exp = sc.nextLine();
                        if ("y".equalsIgnoreCase(exp)) {
                            reservationService.expireReservations();
                        }
                    }

                    case "6" -> {
                        System.out.println("--- Dashboard admin ---");
                        System.out.println("a. Ventes  b. Locations  c. Réservations");
                        String dash = sc.nextLine();
                        if ("a".equalsIgnoreCase(dash)) {
                            var sales = FileStorage.getInstance().loadSales();
                            if (sales.isEmpty()) System.out.println("Aucune vente enregistrée.");
                            else sales.forEach(System.out::println);
                        } else if ("b".equalsIgnoreCase(dash)) {
                            var rentals = FileStorage.getInstance().loadRentals();
                            if (rentals.isEmpty()) System.out.println("Aucune location enregistrée.");
                            else rentals.forEach(System.out::println);
                        } else if ("c".equalsIgnoreCase(dash)) {
                            var res = FileStorage.getInstance().loadReservations();
                            if (res.isEmpty()) System.out.println("Aucune réservation enregistrée.");
                            else res.forEach(System.out::println);
                        } else {
                            System.out.println("Choix dashboard invalide.");
                        }
                    }

                    default -> System.out.println("❌ Choix invalide.");
                }
            } else {
            
                System.out.println("\nMenu utilisateur :");
                System.out.println("1. Livres");
                System.out.println("2. Achat");
                System.out.println("3. Location");
                System.out.println("4. Réservation");
                System.out.println("0. Quitter");
                System.out.print("Choix : ");
                String choix = sc.nextLine();
                if ("0".equals(choix)) { System.out.println(" Au revoir !"); return; }
                switch (choix) {
                    case "1" -> {
                        System.out.println("a. Lister  b. Rechercher");
                        String c1 = sc.nextLine();
                        if ("a".equals(c1)) {
                            bookService.listAll().forEach(System.out::println);
                        } else if ("b".equals(c1)) {
                            System.out.print("Nom du livre : "); String name = sc.nextLine();
                            var matches = bookService.findByTitle(name);
                            if (matches.isEmpty()) {
                                System.out.println("❌ Introuvable.");
                            } else {
                                matches.forEach(System.out::println);
                            }
                        }
                    }

                    case "2" -> {
                        System.out.print("Nom du livre : "); String name = sc.nextLine();
                        var matches = bookService.findByTitle(name);
                        if (matches.isEmpty()) { System.out.println("❌ Aucun livre trouvé pour ce nom."); break; }
                        Book choisi;
                        if (matches.size() == 1) {
                            choisi = matches.get(0);
                        } else {
                            System.out.println("Plusieurs livres trouvés :");
                            for (int i=0;i<matches.size();i++) System.out.println((i+1)+". " + matches.get(i));
                            System.out.print("Choisir le numéro (ou 0 pour annuler) : ");
                            int idx = Integer.parseInt(sc.nextLine());
                            if (idx <= 0 || idx > matches.size()) { System.out.println("Annulé."); break; }
                            choisi = matches.get(idx-1);
                        }
                        System.out.println("Détails : " + choisi);
                        System.out.print("Voulez-vous acheter ce livre ? (y/n) : "); String confirm = sc.nextLine();
                        if (!"y".equalsIgnoreCase(confirm)) { System.out.println("Achat annulé."); break; }
                        System.out.print("Moyen de paiement (carte/especes/paypal) : "); String pay = sc.nextLine();
                        try {
                            Sale sale = saleService.createSale(choisi, currentUser, pay);
                            System.out.println(" Achat effectué : " + sale);
                            AppLogger.getInstance().info("Achat par " + currentUser.getUsername() + ": " + sale);
                        } catch (IllegalStateException ise) {
                            System.out.println("❌ Erreur : " + ise.getMessage());
                            AppLogger.getInstance().error("Erreur achat: " + ise.getMessage());
                        } catch (Exception ex) {
                            System.out.println("❌ Une erreur est survenue lors de l'achat.");
                            AppLogger.getInstance().error("Erreur achat inattendue: " + ex.getMessage());
                        }
                    }

                    case "3" -> {
                        System.out.print("Nom du livre : "); String name = sc.nextLine();
                        var matches = bookService.findByTitle(name);
                        if (matches.isEmpty()) { System.out.println("❌ Aucun livre trouvé pour ce nom."); break; }
                        Book choisi;
                        if (matches.size() == 1) {
                            choisi = matches.get(0);
                        } else {
                            System.out.println("Plusieurs livres trouvés :");
                            for (int i=0;i<matches.size();i++) System.out.println((i+1)+". " + matches.get(i));
                            System.out.print("Choisir le numéro (ou 0 pour annuler) : ");
                            int idx = Integer.parseInt(sc.nextLine());
                            if (idx <= 0 || idx > matches.size()) { System.out.println("Annulé."); break; }
                            choisi = matches.get(idx-1);
                        }
                        System.out.println("Détails : " + choisi);
                        System.out.print("Voulez-vous louer ce livre ? (y/n) : "); String confirm = sc.nextLine();
                        if (!"y".equalsIgnoreCase(confirm)) { System.out.println("Location annulée."); break; }
                        System.out.print("Durée (jours) : "); int days = Integer.parseInt(sc.nextLine());
                        try {
                            Rental rental = rentalService.createRental(choisi, currentUser, days);
                            System.out.println(" Location créée : " + rental);
                            AppLogger.getInstance().info("Location par " + currentUser.getUsername() + ": " + rental);
                        } catch (IllegalStateException ise) {
                            System.out.println("❌ Erreur : " + ise.getMessage());
                            AppLogger.getInstance().error("Erreur location: " + ise.getMessage());
                        } catch (Exception ex) {
                            System.out.println("❌ Une erreur est survenue lors de la location.");
                            AppLogger.getInstance().error("Erreur location inattendue: " + ex.getMessage());
                        }
                    }

                    case "4" -> {
                        System.out.print("Nom du livre : "); String name = sc.nextLine();
                        var matches = bookService.findByTitle(name);
                        if (matches.isEmpty()) { System.out.println("❌ Aucun livre trouvé pour ce nom."); break; }
                        Book choisi;
                        if (matches.size() == 1) {
                            choisi = matches.get(0);
                        } else {
                            System.out.println("Plusieurs livres trouvés :");
                            for (int i=0;i<matches.size();i++) System.out.println((i+1)+". " + matches.get(i));
                            System.out.print("Choisir le numéro (ou 0 pour annuler) : ");
                            int idx = Integer.parseInt(sc.nextLine());
                            if (idx <= 0 || idx > matches.size()) { System.out.println("Annulé."); break; }
                            choisi = matches.get(idx-1);
                        }
                        System.out.println("Détails : " + choisi);
                        System.out.print("Voulez-vous réserver ce livre ? (y/n) : "); String confirm = sc.nextLine();
                        if (!"y".equalsIgnoreCase(confirm)) { System.out.println("Réservation annulée."); break; }
                        System.out.print("Validité (jours) : "); int validDays = Integer.parseInt(sc.nextLine());
                        try {
                            Reservation reservation = reservationService.createReservation(choisi, currentUser, validDays);
                            System.out.println(" Réservation créée : " + reservation);
                            AppLogger.getInstance().info("Réservation par " + currentUser.getUsername() + ": " + reservation);
                        } catch (Exception ex) {
                            System.out.println("❌ Une erreur est survenue lors de la réservation.");
                            AppLogger.getInstance().error("Erreur réservation: " + ex.getMessage());
                        }
                    }

                    default -> System.out.println("❌ Choix invalide.");
                }
            }
        }
    }
}