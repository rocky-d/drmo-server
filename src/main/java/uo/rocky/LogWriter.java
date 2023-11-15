package uo.rocky;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;

public final class LogWriter {
    private static BufferedWriter bufferedWriter = null;

    public static BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public static void setBufferedWriter(Path logFile) throws IOException {
        bufferedWriter = Files.newBufferedWriter(logFile, UTF_8, APPEND);
    }

    public static synchronized void write(String content) throws IOException {
        bufferedWriter.write(content);
    }

    public static synchronized void flush() throws IOException {
        bufferedWriter.flush();
    }

    public static synchronized void close() throws IOException {
        bufferedWriter.close();
    }
}