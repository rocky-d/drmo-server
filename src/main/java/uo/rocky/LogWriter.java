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
 * <p>
 * All fields and methods are static.
 *
 * @author Rocky Haotian Du
 */
public final class LogWriter {

    private static final OpenOption[] OPEN_OPTIONS = new StandardOpenOption[]{APPEND, CREATE};

    private static BufferedWriter bufferedWriter = null;

    /**
     * Returns {@link #bufferedWriter}.
     *
     * @return {@link #bufferedWriter}.
     */
    public static synchronized BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    /**
     * Sets up {@link #bufferedWriter} by a new {@link BufferedWriter} object
     * for writing logs to a log file.
     *
     * @param logFile the path to the log file.
     * @throws IOException if an I/O error occurs while setting up {@link #bufferedWriter}.
     */
    public static synchronized void setBufferedWriter(Path logFile) throws IOException {
        bufferedWriter = Files.newBufferedWriter(logFile, UTF_8, OPEN_OPTIONS);
    }

    /**
     * Appends a log entry of a {@link LogEntryType} instance with provided messages.
     *
     * @param logEntryType the type of log entry (INFO, WARNING, ERROR).
     * @param messages     the messages to be logged, represented as one or more strings.
     */
    public static synchronized void appendEntry(LogEntryType logEntryType, String... messages) {
        String temp;
        try {
            System.out.println(temp = "<" + logEntryType.name() + "> " + ZonedDateTime.now());
            bufferedWriter.write(temp);
            bufferedWriter.newLine();
            for (String line : messages) {
                System.out.println(temp = "         " + line);
                bufferedWriter.write(temp);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException ioException) {
            System.out.println("*********");
            System.out.println(Arrays.toString(messages));
            System.out.println("*********");
            System.out.println("Write log failed.");
            System.out.println(ioException.getClass().getName() + ": " + ioException.getMessage());
//            throw new RuntimeException(ioException);
        }
    }

    /**
     * Closes {@link #bufferedWriter}.
     *
     * @throws IOException if an I/O error occurs while closing {@link #bufferedWriter}.
     */
    public static synchronized void close() throws IOException {
        bufferedWriter.close();
    }

    /**
     * Enumerates and represents the types of log entries.
     */
    public enum LogEntryType {

        /**
         * The info type indicating the normal actions.
         */
        INFO(),
        /**
         * The warning type indicating the improper actions causing not serious consequences.
         */
        WARNING(),
        /**
         * The error type indicating the exceptions that may affect the server's operations.
         */
        ERROR();

        /**
         * Constructs the types above.
         */
        LogEntryType() {
        }

        /**
         * Overrides {@code toString()} to provide details about the instances.
         *
         * @return a string representation of the instances.
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