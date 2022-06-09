import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ReadDataUtil {

    public static boolean readConfiguration(NamedBody b, String path, String day) throws IOException {
        return readConfiguration(b, path, day, 0);
    }

    // Reads the position and velocity vector on the specified 'day' from the file with the
    // specified 'path', and sets position and current velocity of 'b' accordingly. If
    // successful the method returns 'true'. If the specified 'day' was not found in the file,
    // 'b' is unchanged and the method returns 'false'.
    // The file format is validated before reading the state.
    // Lines before the line "$$SOE" and after the line "$$EOE" the are ignored. Each line of the
    // file between the line "$$SOE" and the line "$$EOE" is required to have the following format:
    // JDTDB, TIME, X, Y, Z, VX, VY, VZ
    // where JDTDB is interpretable as a 'double' value, TIME is a string and X, Y, Z, VX, VY and
    // VZ are interpretable as 'double' values. JDTDB can be ignored. The character ',' must only
    // be used as field separator. If the file is not found, an exception of the class
    // 'StateFileNotFoundException' is thrown. If it does not comply with the format described
    // above, the method throws an exception of the class 'StateFileFormatException'. Both
    // exceptions are subtypes of 'IOException'.
    // Precondition: b != null, path != null, day != null and has the format YYYY-MM-DD.
    public static boolean readConfiguration(NamedBody b, String path, String day, int offset)
            throws IOException {
        StringBuilder s = new StringBuilder();
        if (day.lastIndexOf('-') - day.indexOf('-') == 3)
            day = replaceMonthWithName(day);
        boolean found = false, read = false, readStart = false, readEnd = false;
        try {
            BufferedReader in = null;
            try {
                Reader r = new FileReader(path);
                in = new BufferedReader(r);
                String line;
                int off = 0;
                for (int i = 1; ( line = in. readLine ()) != null; i++) {
                    if (line.equals("$$SOE")) {
                        read = true;
                        readStart = true;
                        continue;
                    } else if (line.equals("$$EOE")) {
                        read = false;
                        readEnd = true;
                        continue;
                    }
                    if (read) {
                        try {
                            if (line.indexOf('-') == -1 && !readEnd) break;
                            double jdtdb = Double.parseDouble(line.substring(0, line.indexOf(',')).trim());
                            line = nextValue(line);
                            String time = line.substring(0, line.indexOf(',')).trim();
                            line = nextValue(line);
                            double x = Double.parseDouble(line.substring(0, line.indexOf(',')).trim());
                            line = nextValue(line);
                            double y = Double.parseDouble(line.substring(0, line.indexOf(',')).trim());
                            line = nextValue(line);
                            double z = Double.parseDouble(line.substring(0, line.indexOf(',')).trim());
                            line = nextValue(line);
                            double vx = Double.parseDouble(line.substring(0, line.indexOf(',')).trim());
                            line = nextValue(line);
                            double vy = Double.parseDouble(line.substring(0, line.indexOf(',')).trim());
                            line = nextValue(line);
                            double vz = Double.parseDouble(line.substring(0, line.indexOf(',')).trim());
                            if (time.contains(day)) {
                                found = true;
                            }
                            if (found && off++ == offset) {
                                b.setState(new Vector3(x, y, z).times(1000), new Vector3(vx, vy, vz).times(1000));
                            }
                        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                            throw new StateFileFormatException(e.getMessage(), e.getCause());
                        }
                    }
                }
            } finally {
                if (in != null) { in.close (); }
            }
            if (!(readStart && readEnd))
                throw new StateFileFormatException("Illegal file format: No start/end-line!", new Throwable());
        } catch ( FileNotFoundException e) {
            throw new StateFileNotFoundException(e.getMessage(), e.getCause());
        }
        return found;
    }

    private static String nextValue(String line) {
        return line.substring(line.indexOf(',') + 1);
    }

    private static String replaceMonthWithName(String day) throws NumberFormatException {
        String y = day.substring(0, day.indexOf('-')+1);
        String m = day.substring(day.indexOf('-')+1, day.lastIndexOf('-'));
        m = new DateFormatSymbols().getShortMonths()[Integer.parseInt(m)-1];
        String d = day.substring(day.lastIndexOf('-'));
        return y + m + d;
    }
}

