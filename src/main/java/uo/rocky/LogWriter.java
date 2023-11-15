package uo.rocky;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public final class LogWriter {
    private static final OpenOption[] OPEN_OPTIONS = new StandardOpenOption[]{APPEND, CREATE};

    private static BufferedWriter bufferedWriter = null;

    public static synchronized BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public static synchronized void setBufferedWriter(Path logFile) throws IOException {
        bufferedWriter = Files.newBufferedWriter(logFile, UTF_8, OPEN_OPTIONS);
    }

    public static synchronized void appendEntry(String entry) throws IOException {
        bufferedWriter.write(entry);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public static synchronized void close() throws IOException {
        bufferedWriter.close();
    }
}