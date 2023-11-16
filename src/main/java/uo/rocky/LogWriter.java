package uo.rocky;

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

/**
 * Handles the writing of log entries to a log file.
 *
 * <p> All fields and methods are static.
 *
 * @author Rocky Haotian Du
 */
public final class LogWriter {

    private static final OpenOption[] OPEN_OPTIONS = new StandardOpenOption[]{APPEND, CREATE};

    private static BufferedWriter bufferedWriter = null;

    /**
     * Returns the BufferedWriter instance for writing log entries.
     *
     * @return The BufferedWriter instance for writing log entries.
     */
    public static synchronized BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    /**
     * Sets up the BufferedWriter for writing logs to the specified file.
     *
     * @param logFile The path to the log file.
     * @throws IOException If an I/O error occurs while setting up the writer.
     */
    public static synchronized void setBufferedWriter(Path logFile) throws IOException {
        bufferedWriter = Files.newBufferedWriter(logFile, UTF_8, OPEN_OPTIONS);
    }

    /**
     * Appends a log entry of a specified type with provided content.
     *
     * @param logEntryType The type of log entry (INFO, WARNING, ERROR).
     * @param content      The content to be logged, represented as one or more strings.
     */
    public static synchronized void appendEntry(LogEntryType logEntryType, String... content) {
        String temp;
        try {
            System.out.println(temp = "<" + logEntryType.name() + "> " + ZonedDateTime.now());
            bufferedWriter.write(temp);
            bufferedWriter.newLine();
            for (String line : content) {
                System.out.println(temp = "\t" + line);
                bufferedWriter.write(temp);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException ioException) {
            System.out.println("*********");
            System.out.println(Arrays.toString(content));
            System.out.println("*********");
            System.out.println("Write log failed.");
            System.out.println(ioException.getClass().getName() + ": " + ioException.getMessage());
//            throw new RuntimeException(ioException);
        }
    }

    /**
     * Closes the BufferedWriter.
     *
     * @throws IOException If an I/O error occurs while closing the writer.
     */
    public static synchronized void close() throws IOException {
        bufferedWriter.close();
    }

    /**
     * Enumerates the types of log entries.
     */
    public enum LogEntryType {

        INFO,
        WARNING,
        ERROR;

        LogEntryType() {
        }

        /**
         * Overrides toString() to provide details about the instances.
         *
         * @return A string representation of the type.
         */
        @Override
        public final String toString() {
            return new StringJoiner(", ", LogEntryType.class.getSimpleName() + "{", "}")
                    .add("name='" + name() + "'")
                    .add("ordinal=" + ordinal())
                    .toString();
        }
    }
}