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
        final String RESET_CODE = "\u001B[0m";
        final String SPECIAL_STYLE_CODE = "\u001B[35m";
        String temp;
        try {
            System.out.print(logEntryType.STYLE_CODE);
            temp = "<" + logEntryType.name() + ">" + ZonedDateTime.now();
            System.out.println(temp);
            bufferedWriter.write(temp);
            bufferedWriter.newLine();
            for (String message : messages) {
                temp = "\t" + message;
                System.out.println(temp);
                bufferedWriter.write(temp);
                bufferedWriter.newLine();
            }
            System.out.print(RESET_CODE);
            bufferedWriter.flush();
        } catch (IOException ioException) {
            System.out.print(RESET_CODE);
            System.out.print(SPECIAL_STYLE_CODE);
            System.out.println("*********");
            System.out.println(Arrays.toString(messages));
            System.out.println("*********");
            System.out.println("Write log failed.");
            System.out.println(ioException.getClass().getName() + ": " + ioException.getMessage());
            System.out.print(RESET_CODE);

            ioException.printStackTrace(System.err);
            System.exit(-1);
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
         * The green-colored info type indicating
         * the normal actions.
         */
        INFO("\u001B[32m"),
        /**
         * The yellow-colored warning type indicating
         * the improper actions causing not serious consequences.
         */
        WARNING("\u001B[33m"),
        /**
         * The red-colored error type indicating
         * the exceptions that may affect the server's operations.
         */
        ERROR("\u001B[31m");

        public final String STYLE_CODE;

        /**
         * Constructs the types above.
         */
        LogEntryType(final String STYLE_CODE) {
            this.STYLE_CODE = STYLE_CODE;
        }

        /**
         * Overrides {@code toString():String} to provide details about the instances.
         *
         * @return a string representation of the instances.
         */
        @Override
        public final String toString() {
            final String RESET_CODE = "\u001B[0m";
            return new StringJoiner(", ", LogEntryType.class.getSimpleName() + "{", "}")
                    .add("name='" + STYLE_CODE + name() + RESET_CODE + "'")
                    .add("ordinal=" + STYLE_CODE + ordinal() + RESET_CODE)
                    .toString();
        }
    }
}