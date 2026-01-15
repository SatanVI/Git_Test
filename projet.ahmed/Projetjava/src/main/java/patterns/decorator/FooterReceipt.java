package patterns.decorator;

public class FooterReceipt implements Receipt {
    private final Receipt inner;

    public FooterReceipt(Receipt inner) {
        this.inner = inner;
    }

    @Override
    public String render() {
        return inner.render() + "\n--- Merci pour votre achat ---";
    }
}
