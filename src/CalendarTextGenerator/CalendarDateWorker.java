package CalendarTextGenerator;

import CalendarTextGenerator.fx.CalendarGenerator;
import CalendarTextGenerator.fx.UtilsAlerts;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Lukas on 10.03.2017
 */
@SuppressWarnings("SpellCheckingInspection")
class CalendarDateWorker {
    static CalendarDateHolidayObject<String, String, LocalDate> calculateHolidays(int year, String federalState, boolean hasAll) {
        CalendarDateHolidayObject<String, String, LocalDate> dateClass = new CalendarDateHolidayObject<>();

        // return empty
        if (Objects.equals(federalState, "NONE"))
            return dateClass;

        // holidays calculated by easter sunday
        int easterSunday = easterSunday(year);
        LocalDate easterSundayDate = LocalDate.of(year, (easterSunday < 31 ? 3 : 4), (easterSunday < 31 ? easterSunday : easterSunday - 31));

        if (hasAll) {
            dateClass.put("Weiberfastnacht", "ALL", easterSundayDate.minusDays(52), false);
            //dateClass.put("Fastnachtssamstag", "ALL", easterSundayDate.minusDays(50), false);
            //dateClass.put("Fastnachtssonntag", "ALL", easterSundayDate.minusDays(49), false);
            dateClass.put("Rosenmontag", "ALL", easterSundayDate.minusDays(48), false);
            dateClass.put("Faschingsdienstag", "ALL", easterSundayDate.minusDays(47), false);
            dateClass.put("Aschermittwoch", "ALL", easterSundayDate.minusDays(46), false);
            dateClass.put("Ostersonntag", "ALL,BB", easterSundayDate, true);
            dateClass.put("Gründonnerstag", "ALL", easterSundayDate.minusDays(3), false);
            dateClass.put("Karfreitag", "ALL", easterSundayDate.minusDays(2), true);
            dateClass.put("Ostermontag", "ALL", easterSundayDate.plusDays(1), true);
            dateClass.put("Christi Himmelfahrt", "ALL", easterSundayDate.plusDays(39), true);
            dateClass.put("Pfingstsonntag", "ALL,BB", easterSundayDate.plusDays(49), true);
            dateClass.put("Pfingstmontag", "ALL", easterSundayDate.plusDays(50), true);
            dateClass.put("Fronleichnam", "ALL", easterSundayDate.plusDays(60), true);

            // calculated days
            dateClass.put("Muttertag", "ALL", getNthOfMonth(DayOfWeek.SUNDAY, 2, 5, year), false);
            dateClass.put("1. Advent", "ALL", lastSundayOfAdvent(year).minusWeeks(3), false);
            dateClass.put("2. Advent", "ALL", lastSundayOfAdvent(year).minusWeeks(2), false);
            dateClass.put("3. Advent", "ALL", lastSundayOfAdvent(year).minusWeeks(1), false);
            dateClass.put("4. Advent", "ALL", lastSundayOfAdvent(year), false);

            // other holidays
            dateClass.put("Neujahr", "ALL", LocalDate.of(year, 1, 1), true);
            dateClass.put("Valentinstag", "ALL", LocalDate.of(year, 2, 14), false);
            dateClass.put("Tag der Arbeit", "ALL", LocalDate.of(year, 5, 1), true);
            dateClass.put("Tag der Deutschen Einheit", "ALL", LocalDate.of(year, 10, 3), true);
            if (year == 2017)
                dateClass.put("Reformationstag", "ALL", LocalDate.of(year, 10, 31), true);

            // other holidays, christmas time
            dateClass.put("Heiligabend", "ALL", LocalDate.of(year, 12, 24), false);
            dateClass.put("1. Weihnachstag", "ALL", LocalDate.of(year, 12, 25), true);
            dateClass.put("2. Weihnachstag", "ALL", LocalDate.of(year, 12, 26), true);
            dateClass.put("Sylvester", "ALL", LocalDate.of(year, 12, 31), false);
        }

        /*
        * MULTIPLE IF STATES 0>
        * part of "other holidays"
        * */
        // part of "other holidays"
        if (federalState.contains("BW") || federalState.contains("BY") || federalState.contains("ST"))
            dateClass.put("Heilige Drei Könige", "BW,BY,ST", LocalDate.of(year, 1, 6), true);
        if (federalState.contains("BW") || federalState.contains("BY"))
            dateClass.put("Mariä Himmelfahrt", "BY,SL", LocalDate.of(year, 8, 15), true);
        if (federalState.contains("BW") || federalState.contains("BY") || federalState.contains("NW") || federalState.contains("RP") || federalState.contains("SL"))
            dateClass.put("Allerheiligen", "BW,BY,NW,RP,SL", LocalDate.of(year, 11, 1), true);
        if (federalState.contains("SN"))
            dateClass.put("Buß- und Bettag", "SN", LocalDate.of(year, 11, 22), true);
        if (year != 2017 && (federalState.contains("BB") || federalState.contains("MV") || federalState.contains("SN") || federalState.contains("ST") || federalState.contains("TH")))
            dateClass.put("Reformationstag", "BB,MV,SN,ST,TH", LocalDate.of(year, 10, 31), true);

        return dateClass;
    }

    private void calculateAllHolidays(int year) {
        calculateHolidays(year, "", true);
    }

    // calc, interesting ...
    private static int easterSunday(int year) {
        int a, b, c, d, e, k, m, n, p, q;

        a = year % 19;
        b = year % 4;
        c = year % 7;
        k = year / 100;
        p = (8 * k + 13) / 25;
        q = k / 4;
        m = (15 + k - p - q) % 30;
        n = (4 + k - q) % 7;
        d = (19 * a + m) % 30;
        e = (2 * b + 4 * c + 6 * d + n) % 7;

        if (d + e == 35)
            return 50;
        else if (d == 28 && e == 6 && (11 * m + 11) % 30 < 19)
            return 49;
        return 22 + d + e;
    }

    static ArrayList<LocalDate> getSecondFridays() {
        ArrayList<LocalDate> fridays = new ArrayList<>();
        boolean reachedFirstFriday = false;

        // loading year from global properties
        int thisYear;
        boolean showFridays = true;
        try {
            Properties localProp = new Properties();
            localProp.loadFromXML(new FileInputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"));
            thisYear = Integer.parseInt(localProp.getProperty("main.year"));
            showFridays = Boolean.parseBoolean((localProp.getProperty("main.show_second_fridays")));
        } catch (IOException e) {
            System.err.println("Exception: " + e.getMessage() + "! Using thisYear value.");
            thisYear = Year.now().getValue();
        }

        // no second fridays
        if (!showFridays) {
            fridays.add(LocalDate.MIN);
            return fridays;
        }

        LocalDate sDate = LocalDate.of(thisYear, 1, 1);
        LocalDate eDate = LocalDate.of(thisYear, 12, 31);

        while (sDate.isBefore(eDate)) {
            if (sDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
                fridays.add(sDate);
                reachedFirstFriday = true;
            }

            if (reachedFirstFriday)
                sDate = sDate.plusWeeks(1);
            else
                sDate = sDate.plusDays(1);
        }

        // only second fridays (downwards, because counting AL sucks)
        for (int i = fridays.size() - 1; i > 0; i -= 2)
            fridays.remove(i);

        return fridays;
    }

    /**
     * Calculate the nth day of a month.
     * e.g.: searching mother's day (2nd SUNDAY of may)
     *
     * @param dayOfWeek the day to search for (e.g. FayOfWeek.SUNDAY)
     * @param nth       the xth dayOfWeek to search for (e.g. 2)
     * @param month     the month searching in for the xth dayOfWeek
     * @param year      the year searching in
     * @return the calculated day
     */
    @SuppressWarnings("SameParameterValue")
    private static LocalDate getNthOfMonth(DayOfWeek dayOfWeek, int nth, int month, int year) {
        LocalDate sDate = LocalDate.of(year, month, 1);
        LocalDate eDate = LocalDate.of(year, month, 31);

        int second = 0;

        int iterations = 0;

        while (sDate.isBefore(eDate)) {
            // find sunday and increment till nth is found
            if (sDate.getDayOfWeek() == dayOfWeek)
                if (++second == nth)
                    return sDate;

            if (second != 0)
                sDate = sDate.plusWeeks(1);
            else
                sDate = sDate.plusDays(1);

            // error break
            if (++iterations > 31)
                throw new RuntimeException("month > 31?");
        }

        // nothing found
        return null;
    }

    private static LocalDate lastSundayOfAdvent(int year) {
        LocalDate eDate = LocalDate.of(year, 12, 25);
        LocalDate sDate = eDate.minusWeeks(1);

        while (sDate.isBefore(eDate)) {
            if (sDate.getDayOfWeek() == DayOfWeek.SUNDAY)
                return sDate;

            sDate = sDate.plusDays(1);
        }

        return sDate;
    }

    static ArrayList<LocalDate> calculateCustomHolidays() {
        ArrayList<LocalDate> customs = new ArrayList<>();
        Properties props = new Properties();
        try {
            props.loadFromXML(new FileInputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read date properties and save in values
        Enumeration<?> e = props.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = props.getProperty(key);

            if (key.contains("custom.date_")) {
                String[] values = value.split(",");

                LocalDate begin = LocalDate.parse(values[0]);

                // either here or in while loop (doesn't matter)
                LocalDate end = LocalDate.parse(values[1]).plusDays(1);

                while (begin.isBefore(end)) {
                    customs.add(begin);

                    begin = begin.plusDays(1);
                }
            }
        }

        return customs;
    }

    static HashMap<String, LocalDate> calculateBirthdays(String csvCharSet) {
        HashMap<String, LocalDate> birthdays = new HashMap<>();

        BufferedReader br = null;
        String line;

        // use semikolon as separator
        String csvSplitBy = ";";

        try {
            if(!new File(System.getProperty("user.dir") + File.separator + "Birthdays.csv").exists())
                new FileOutputStream("Birthdays.csv");
            // read csv file with sepcific charset CSV
            br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("Birthdays.csv"), csvCharSet));
            while ((line = br.readLine()) != null) {
                String[] person = line.split(csvSplitBy);

                if (person[0].length() > 25)
                    UtilsAlerts.showWarning("Name > 25", "" + person[0] + " is too long for calendar.");

                birthdays.put(person[0], LocalDate.parse(person[1], DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            }
        } catch (IOException e) {
            UtilsAlerts.showExceptionDialog(e, e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    UtilsAlerts.showExceptionDialog(e, e.getMessage());
                }
            }
        }

        return birthdays;
    }
}
