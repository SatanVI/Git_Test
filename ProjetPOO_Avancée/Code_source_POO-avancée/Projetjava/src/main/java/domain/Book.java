package domain;

public class Book {
    private final String id;
    private final String isbn;
    private String titre;
    private String auteur;
    private String categorieId;
    private double prix;
    private int stock;

    public Book(String id, String isbn, String titre, String auteur, String categorieId, double prix, int stock) {
        this.id = id;
        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.categorieId = categorieId;
        this.prix = prix;
        this.stock = stock;
    }

    public String getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public String getCategorieId() { return categorieId; }
    public double getPrix() { return prix; }
    public int getStock() { return stock; }

    public void setTitre(String titre) { this.titre = titre; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public void setCategorieId(String categorieId) { this.categorieId = categorieId; }
    public void setPrix(double prix) { this.prix = prix; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) | %.2fDHS | Stock: %d", isbn, titre, auteur, prix, stock);
    }
}