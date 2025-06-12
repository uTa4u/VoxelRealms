package su.uTa4u.VoxelRealms.logger;

import su.uTa4u.VoxelRealms.Main;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private Logger() {
    }

    public static Logger create() {
        return new Logger();
    }

    public void debug(String msg) {
        this.log(msg, "DEBUG", "\u001B[32m");
    }

    public void info(String msg) {
        this.log(msg, "INFO", "");
    }

    public void warn(String msg) {
        this.log(msg, "WARN", "\u001B[33m");
    }

    public void error(String msg) {
        this.log(msg, "ERROR", "\u001B[31m");
    }

    private void log(String msg, String logLevel, String ansiColor) {
        final boolean supportAnsi = System.getenv().get("TERM") != null || Main.IS_IN_IDE;
        System.out.printf(
                "%s[%s] [%s] [%s]: %s%s%n",
                supportAnsi ? ansiColor : "",
                ZonedDateTime.now().format(FORMATTER),
                logLevel,
                Thread.currentThread().getStackTrace()[3],
                msg,
                supportAnsi ? "\u001B[0m" : ""
        );
    }
}
