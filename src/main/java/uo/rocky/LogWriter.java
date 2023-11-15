package uo.rocky;

import uo.rocky.entity.Coordinate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.StringJoiner;

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

    public static synchronized void appendEntry(LogEntryType logEntryType, String... content) {
        try {
            bufferedWriter.write("<" + logEntryType.name() + "> " + ZonedDateTime.now());
            bufferedWriter.newLine();
            for (String line : content) {
                bufferedWriter.write("\t" + line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException ioException) {
            System.out.println("*********");
            System.out.println(Arrays.toString(content));
            System.out.println("*********");
            System.out.println("Write log failed.");
            System.out.println(ioException.getClass().getName() + ": " + ioException.getMessage());
            throw new RuntimeException(ioException);
        }
    }

    public static synchronized void close() throws IOException {
        bufferedWriter.close();
    }

    public enum LogEntryType {
        INFO,
        WARNING,
        ERROR;

        LogEntryType() {
        }

        @Override
        public final String toString() {
            return new StringJoiner(", ", Coordinate.Dangertype.class.getSimpleName() + "{", "}")
                    .add("name='" + name() + "'")
                    .add("ordinal=" + ordinal())
                    .toString();
        }
    }
}