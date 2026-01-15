package repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Book;
import domain.User;

public class FileStorage {
    private static final String DATA_DIR = "data";
    private static final String BOOKS_FILE = DATA_DIR + "/books.txt";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String SALES_FILE = DATA_DIR + "/sales.txt";
    private static final String RENTALS_FILE = DATA_DIR + "/rentals.txt";
    private static final String RESERVATIONS_FILE = DATA_DIR + "/reservations.txt";

    private static FileStorage instance;

    private FileStorage() {
        ensureDataDir();
    }

    public static synchronized FileStorage getInstance() {
        if (instance == null) {
            instance = new FileStorage();
        }
        return instance;
    }

    private void ensureDataDir() {
        try {
            Path dir = Paths.get(DATA_DIR);
            if (Files.notExists(dir)) {
                Files.createDirectories(dir);
            }
            Path books = Paths.get(BOOKS_FILE);
            if (Files.notExists(books)) {
                Files.createFile(books);
            }
            Path users = Paths.get(USERS_FILE);
            if (Files.notExists(users)) {
                Files.createFile(users);
            }
                Path sales = Paths.get(SALES_FILE);
                if (Files.notExists(sales)) Files.createFile(sales);
                Path rentals = Paths.get(RENTALS_FILE);
                if (Files.notExists(rentals)) Files.createFile(rentals);
                Path reservations = Paths.get(RESERVATIONS_FILE);
                if (Files.notExists(reservations)) Files.createFile(reservations);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier de données: " + e.getMessage(), e);
        }
    }

    public synchronized Map<String, Book> loadBooks() {
        Map<String, Book> res = new HashMap<>();
        Path p = Paths.get(BOOKS_FILE);
        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
            
                String[] parts = line.split("\\|", -1);
                if (parts.length < 7) continue;
                String id = parts[0];
                String isbn = parts[1];
                String titre = parts[2];
                String auteur = parts[3];
                String categorieId = parts[4].isEmpty() ? null : parts[4];
                double prix = 0.0;
                int stock = 0;
                try { prix = Double.parseDouble(parts[5]); } catch (Exception ignored) {}
                try { stock = Integer.parseInt(parts[6]); } catch (Exception ignored) {}
                Book b = new Book(id, isbn, titre, auteur, categorieId, prix, stock);
                res.put(isbn, b);
            }
        } catch (IOException e) {
        }
        return res;
    }

    public synchronized void saveBooks(Collection<Book> books) {
        Path p = Paths.get(BOOKS_FILE);
        List<String> lines = new ArrayList<>();
        for (Book b : books) {
            String categorie = b.getCategorieId() == null ? "" : b.getCategorieId();
            String line = String.join("|",
                    safe(b.getId()),
                    safe(b.getIsbn()),
                    safe(b.getTitre()),
                    safe(b.getAuteur()),
                    safe(categorie),
                    String.valueOf(b.getPrix()),
                    String.valueOf(b.getStock())
            );
            lines.add(line);
        }
        try {
            Files.write(p, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'enregistrer les livres: " + e.getMessage(), e);
        }
    }

    public synchronized Map<String, User> loadUsers() {
        Map<String, User> res = new HashMap<>();
        Path p = Paths.get(USERS_FILE);
        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 3) continue;
                String id = parts[0];
                String nom = parts[1];
                String type = parts[2];
                if (parts.length >= 6) {
                    String username = parts[3];
                    String password = parts[4];
                    String role = parts[5];
                    User u = new User(id, nom, type, username, password, role);
                    res.put(id, u);
                } else {
                    User u = new User(id, nom, type);
                    if ("admin".equalsIgnoreCase(type)) {
                        u.setRole("ADMIN");
                    } else {
                        u.setRole("USER");
                    }
                    u.setUsername(nom);
                    res.put(id, u);
                }
            }
        } catch (IOException e) {
           
        }
        return res;
    }

   
    public synchronized List<domain.Sale> loadSales() {
        List<domain.Sale> res = new ArrayList<>();
        Path p = Paths.get(SALES_FILE);
        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 6) continue;
                String id = parts[0];
                String userId = parts[1];
                String bookIsbn = parts[2];
                java.time.LocalDate date = null;
                try { date = java.time.LocalDate.parse(parts[3]); } catch (Exception ignored) {}
                String status = parts[4];
                double montant = 0.0;
                try { montant = Double.parseDouble(parts[5]); } catch (Exception ignored) {}
                domain.Sale s = new domain.Sale(id, userId, bookIsbn, date, status, montant);
                res.add(s);
            }
        } catch (IOException e) {}
        return res;
    }

    public synchronized List<domain.Rental> loadRentals() {
        List<domain.Rental> res = new ArrayList<>();
        Path p = Paths.get(RENTALS_FILE);
        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 6) continue;
                String id = parts[0];
                String userId = parts[1];
                String bookIsbn = parts[2];
                java.time.LocalDate date = null;
                try { date = java.time.LocalDate.parse(parts[3]); } catch (Exception ignored) {}
                String status = parts[4];
                java.time.LocalDate due = null;
                try { due = java.time.LocalDate.parse(parts[5]); } catch (Exception ignored) {}
                domain.Rental r = new domain.Rental(id, userId, bookIsbn, date, status, due);
                if (parts.length >= 7 && !parts[6].isEmpty()) {
                    try { r.setReturnDate(java.time.LocalDate.parse(parts[6])); } catch (Exception ignored) {}
                }
                if (parts.length >= 8) {
                    try { r.setPenalite(Double.parseDouble(parts[7])); } catch (Exception ignored) {}
                }
                res.add(r);
            }
        } catch (IOException e) {}
        return res;
    }

    public synchronized List<domain.Reservation> loadReservations() {
        List<domain.Reservation> res = new ArrayList<>();
        Path p = Paths.get(RESERVATIONS_FILE);
        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 6) continue;
                String id = parts[0];
                String userId = parts[1];
                String bookIsbn = parts[2];
                java.time.LocalDate date = null;
                try { date = java.time.LocalDate.parse(parts[3]); } catch (Exception ignored) {}
                String status = parts[4];
                java.time.LocalDate exp = null;
                try { exp = java.time.LocalDate.parse(parts[5]); } catch (Exception ignored) {}
                domain.Reservation r = new domain.Reservation(id, userId, bookIsbn, date, status, exp);
                res.add(r);
            }
        } catch (IOException e) {}
        return res;
    }

    public synchronized void appendSale(domain.Sale s) {
        Path p = Paths.get(SALES_FILE);
        String line = String.join("|",
                safe(s.getId()), safe(s.getUserId()), safe(s.getBookIsbn()),
                safe(s.getDate() == null ? "" : s.getDate().toString()), safe(s.getStatus()), String.valueOf(s.getMontant())
        );
        try { Files.write(p, java.util.List.of(line), StandardCharsets.UTF_8, StandardOpenOption.APPEND); } catch (IOException e) {}
    }

    public synchronized void appendRental(domain.Rental r) {
        Path p = Paths.get(RENTALS_FILE);
        String line = String.join("|",
                safe(r.getId()), safe(r.getUserId()), safe(r.getBookIsbn()),
                safe(r.getDate() == null ? "" : r.getDate().toString()), safe(r.getStatus()),
                safe(r.getDueDate() == null ? "" : r.getDueDate().toString()),
                safe(r.getReturnDate() == null ? "" : r.getReturnDate().toString()), String.valueOf(r.getPenalite())
        );
        try { Files.write(p, java.util.List.of(line), StandardCharsets.UTF_8, StandardOpenOption.APPEND); } catch (IOException e) {}
    }

    public synchronized void appendReservation(domain.Reservation r) {
        Path p = Paths.get(RESERVATIONS_FILE);
        String line = String.join("|",
                safe(r.getId()), safe(r.getUserId()), safe(r.getBookIsbn()),
                safe(r.getDate() == null ? "" : r.getDate().toString()), safe(r.getStatus()),
                safe(r.getExpirationDate() == null ? "" : r.getExpirationDate().toString())
        );
        try { Files.write(p, java.util.List.of(line), StandardCharsets.UTF_8, StandardOpenOption.APPEND); } catch (IOException e) {}
    }

    public synchronized void saveUsers(Collection<User> users) {
        Path p = Paths.get(USERS_FILE);
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            String line = String.join("|",
                    safe(u.getId()),
                    safe(u.getNom()),
                    safe(u.getType()),
                    safe(u.getUsername()),
                    safe(u.getPassword()),
                    safe(u.getRole())
            );
            lines.add(line);
        }
        try {
            Files.write(p, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'enregistrer les utilisateurs: " + e.getMessage(), e);
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.replace("|", " ");
    }
}
