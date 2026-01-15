package domain;

public class User {
    private final String id;
    private String nom;
    private String type; 
    private String username;
    private String password;
    private String role; 

    public User(String id, String nom, String type) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.username = nom;
        this.password = "";
        this.role = "USER";
    }

    public User(String id, String nom, String type, String username, String password, String role) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.username = username;
        this.password = password;
        this.role = role == null ? "USER" : role;
    }

    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getType() { return type; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setNom(String nom) { this.nom = nom; }
    public void setType(String type) { this.type = type; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return String.format("%s (%s)", nom, type);
    }
}