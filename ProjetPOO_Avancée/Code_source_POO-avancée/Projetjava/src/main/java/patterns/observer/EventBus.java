package patterns.observer;

import java.util.*;
import java.util.function.Consumer;

public class EventBus {
    private final Map<String, List<Consumer<String>>> subscribers = new HashMap<>();

    public void subscribe(String eventType, Consumer<String> handler) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    public void publish(String eventType, String message) {
        subscribers.getOrDefault(eventType, List.of()).forEach(h -> h.accept(message));
    }
}