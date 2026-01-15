package patterns.decorator;

public class TaxReceipt implements Receipt {
    private final Receipt inner;
    private final double taxRate;

    public TaxReceipt(Receipt inner, double taxRate) {
        this.inner = inner;
        this.taxRate = taxRate;
    }

    @Override
    public String render() {
        String base = inner.render();
        try {
            String[] parts = base.split(":");
            double price = Double.NaN;
            if (parts.length > 1) {
                String last = parts[parts.length-1].trim();
             
                last = last.replaceAll("[^0-9.,-]","").replace(',', '.');
                price = Double.parseDouble(last);
            }
            if (!Double.isNaN(price)) {
                double tax = price * taxRate;
                return base + " | Taxe: " + String.format("%.2f", tax) + "DHS";
            }
        } catch (Exception ignored) {}
        return base + " | Taxe: calcul indisponible";
    }
}
