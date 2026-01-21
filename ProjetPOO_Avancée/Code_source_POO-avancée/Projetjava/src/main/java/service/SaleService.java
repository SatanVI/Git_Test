package service;

import java.time.LocalDate;
import java.util.UUID;

import domain.Book;
import domain.Sale;
import domain.User;
import patterns.decorator.BaseReceipt;
import patterns.decorator.FooterReceipt;
import patterns.decorator.Receipt;
import patterns.decorator.TaxReceipt;
import patterns.factory.Payment;
import patterns.factory.PaymentFactory;
import patterns.observer.EventBus;
import patterns.strategy.PricingStrategy;
import patterns.strategy.StandardPricing;
import patterns.strategy.StudentPricing;
import patterns.strategy.TeacherPricing;
import repository.BookRepository;
import repository.FileStorage;

public class SaleService {
    private final BookRepository bookRepo;
    private final EventBus bus;

    public SaleService(BookRepository bookRepo, EventBus bus) {
        this.bookRepo = bookRepo;
        this.bus = bus;
    }

    public Sale createSale(Book book, User user, String paymentType) {
        PricingStrategy strategy = switch (user.getType()) {
            case "etudiant" -> new StudentPricing();
            case "enseignant" -> new TeacherPricing();
            default -> new StandardPricing();
        };
        double finalPrice = strategy.apply(book.getPrix());

        Payment payment = PaymentFactory.create(paymentType);
        String paymentMsg = payment.pay(finalPrice);

        Receipt receipt = new BaseReceipt("Livre: " + book.getTitre() + " | Prix final: " + finalPrice);
        receipt = new TaxReceipt(receipt, 0.18);
        receipt = new FooterReceipt(receipt);

        if (book.getStock() <= 0) {
            throw new IllegalStateException("Stock insuffisant pour le livre: " + book.getTitre());
        }
        book.setStock(book.getStock() - 1);
        bookRepo.save(book);

        Sale sale = new Sale(UUID.randomUUID().toString(), user.getId(), book.getIsbn(),
                LocalDate.now(), "VALIDEE", finalPrice);

        bus.publish("SALE_CREATED", "Nouvelle vente: " + sale);

        System.out.println(paymentMsg);
        System.out.println(receipt.render());

        try {
            FileStorage.getInstance().appendSale(sale);
        } catch (Exception ignored) {}

        return sale;
    }
}