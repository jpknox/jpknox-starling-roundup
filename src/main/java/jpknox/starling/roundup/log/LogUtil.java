package jpknox.starling.roundup.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

/**
 * I would normally use a logging framework but just for fun, I wrote this!
 */
public final class LogUtil {

    private LogUtil() {
    }

    private enum LogLevel {
        INFO,
        WARN
    }

    public static void info(final String message, Object... args) {
        log(LogLevel.INFO, message, args);
    }

    public static void warn(final String message, Throwable t) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        log(LogLevel.WARN, String.format("%s\n%s", message, sw));
    }

    private static void log(final LogLevel level, final String message, Object... args) {
        final String givenMessage = String.format(message, args);
        final String finalMessage = "%s %s %s:%d - %s";
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        //noinspection RedundantStringFormatCall - easier to read
        System.out.println(
                String.format(
                        finalMessage,
                        LocalDateTime.now(),
                        level.name(),
                        stackTraceElement.getClassName(),
                        stackTraceElement.getLineNumber(),
                        givenMessage));
    }

}
