package observer;

public class AuditObserver implements Observer {
    @Override
    public void update(String event) {
        System.out.println("[AUDIT] " + event);
    }
}
