package observer;

public class LogObserver implements Observer {
    @Override
    public void update(String event) {
        System.out.println("[LOG] " + event);
    }
}
