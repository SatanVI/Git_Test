package patterns.adapter;

public class ExternalPaypalClient {
    private final String merchantId;

    public ExternalPaypalClient(String merchantId) {
        this.merchantId = merchantId;
    }

    public boolean sendPayment(double amount) {
        System.out.println("[ExternalPaypalClient] Sending payment for merchant=" + merchantId + " amount=" + amount);
        return true;
    }
}
