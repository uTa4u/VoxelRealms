package su.uTa4u.VoxelRealms.logger;

import su.uTa4u.VoxelRealms.Main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public final class Logger {
    private static final DateTimeFormatter MESSAGE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
    private static final DateTimeFormatter FILENAME_FORMATTER = DateTimeFormatter.ofPattern("HH-mm-ss_dd-MM-yyyy");
    private static final PrintWriter WRITER;

    static {
        File logsDir = new File("logs/");
        if (!logsDir.isDirectory() && !logsDir.mkdir()) {
            throw new RuntimeException("Failed to create the `logs` directory for Logger");
        }
        File latestLog = new File(logsDir,"latest.log");
        try {
            if (latestLog.exists()) {
                String datetime = ZonedDateTime.ofInstant(
                        Instant.ofEpochMilli(latestLog.lastModified()),
                        Clock.systemDefaultZone().getZone()
                ).format(FILENAME_FORMATTER);
                File prevLog = new File(logsDir, datetime + ".log");
                Files.move(latestLog.toPath(), prevLog.toPath(), REPLACE_EXISTING);
            }
            WRITER = new PrintWriter(latestLog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
        final String logMsg = String.format(
                "[%s] [%s] [%s]: %s",
                ZonedDateTime.now().format(MESSAGE_FORMATTER),
                logLevel,
                Thread.currentThread().getStackTrace()[3],
                msg
        );
        System.out.println((supportAnsi ? ansiColor : "") + logMsg + (supportAnsi ? "\u001B[0m" : ""));
        WRITER.println(logMsg);
    }
}
