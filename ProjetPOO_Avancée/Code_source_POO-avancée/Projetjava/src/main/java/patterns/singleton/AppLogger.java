package patterns.singleton;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;


public class AppLogger {
    private static final AppLogger INSTANCE = new AppLogger();
    private final Path logPath;

    private AppLogger() {
        try {
            Path dir = Paths.get("data");
            if (Files.notExists(dir)) Files.createDirectories(dir);
            logPath = dir.resolve("app.log");
            if (Files.notExists(logPath)) Files.createFile(logPath);
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'initialiser le logger: " + e.getMessage(), e);
        }
    }

    public static AppLogger getInstance() { return INSTANCE; }

    public synchronized void info(String msg) { log("INFO", msg); }
    public synchronized void error(String msg) { log("ERROR", msg); }

    private void log(String level, String msg) {
        String line = String.format("%s [%s] %s", LocalDateTime.now(), level, msg);
        System.out.println(line);
        try {
            Files.write(logPath, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Impossible d'écrire le log: " + e.getMessage());
        }
    }
}
