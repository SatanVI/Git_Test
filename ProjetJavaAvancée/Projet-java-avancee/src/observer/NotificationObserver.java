package observer;

public class NotificationObserver implements Observer {
    @Override
    public void update(String event) {
        System.out.println("[NOTIFICATION] " + event);
    }
}
