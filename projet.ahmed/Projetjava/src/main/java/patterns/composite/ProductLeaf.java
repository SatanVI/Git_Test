package patterns.composite;

import domain.Book;

public class ProductLeaf implements Component {
    private final Book book;

    public ProductLeaf(Book book) { this.book = book; }

    @Override
    public String getName() { return book.getTitre(); }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Book: " + book.getIsbn() + " - " + book.getTitre());
    }

    public Book getBook() { return book; }
}
