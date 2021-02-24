package CalendarTextGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * @author Lukas on 10.03.2017
 */
@SuppressWarnings("Duplicates")
public class AdobeCalendarGenerator {
    private static String getCalendarWithAdobeStyleTags(int thatYear, String mainAdobeStyleTag, String federalStateToSearch, boolean showNational, boolean fill31spaces,
                                                        char nationalHolidayChar, char nonNationalHolidayChar, char companyOtherChar, char specialHolidayChar, char birthdayChar, String csvCharSet) {
        // dec before and jan after
        int lastYear = thatYear - 1;
        int nextYear = thatYear + 1;

        // calc the holidays by easter sunday
        CalendarDateHolidayObject<String, String, LocalDate> thisHolidays = CalendarDateWorker.calculateHolidays(thatYear, federalStateToSearch, showNational);
        CalendarDateHolidayObject<String, String, LocalDate> lastHolidays = CalendarDateWorker.calculateHolidays(lastYear, federalStateToSearch, showNational);
        CalendarDateHolidayObject<String, String, LocalDate> nextHolidays = CalendarDateWorker.calculateHolidays(nextYear, federalStateToSearch, showNational);
        ArrayList<LocalDate> fridays = CalendarDateWorker.getSecondFridays();
        ArrayList<LocalDate> customs = CalendarDateWorker.calculateCustomHolidays();

        // Get Birthdays (name, dd.mm.yyyy)
        HashMap<String, LocalDate> birthdays = CalendarDateWorker.calculateBirthdays(csvCharSet);

        // define the day name format (Mo, Fr)
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE", Locale.GERMANY);

        // start
        StringBuilder masterLine = new StringBuilder();

        // dec before
        if (mainAdobeStyleTag.contains("14")) {
            for (int i = 1; i <= YearMonth.of(lastYear, 12).lengthOfMonth(); i++) {
                try {
                    String lastDayName = dateFormat.format(
                            new SimpleDateFormat("yyy-M-d").parse(
                                    String.format("%d-%d-%d", lastYear, 12, i)));

                    LocalDate lastDate = LocalDate.of(lastYear, 12, i);
                    String lastHolidayName = lastHolidays.returnNameByDate(lastDate);
                    boolean lastNationalHoliday = lastHolidays.returnIsNationalByDate(lastDate);

                    // write the lines ...
                    masterLine.append(mainAdobeStyleTag);

                    // get person with actual birthday
                    String birthdayPersonName = null;
                    int twoDigitBirthdayYear = 0;

                    for (Map.Entry<String, LocalDate> e : birthdays.entrySet())
                        if (e.getValue().withYear(lastDate.getYear()).isEqual(lastDate)) {
                            birthdayPersonName = e.getKey();
                            twoDigitBirthdayYear = e.getValue().getYear() % 100;
                        }

                    // friday from company, then holiday
                    // if holiday name is set, char will be displayed
                    masterLine.append(customs.contains(lastDate) ? specialHolidayChar :
                            (fridays.contains(lastDate) ? companyOtherChar :
                                    (lastHolidayName != null ? (lastNationalHoliday ? nationalHolidayChar : nonNationalHolidayChar) :
                                            (birthdayPersonName != null ? birthdayChar : ""))));

                    // add day-number and day-name
                    masterLine.append("\t").append(i).append("\t").append(lastDayName);

                    // holidayName + char for not national, week of date number if monday
                    masterLine.append("\t").append(lastHolidayName != null ? lastHolidayName + (!lastNationalHoliday ? "\t" + nonNationalHolidayChar : "\t") + "\n" :
                            (birthdayPersonName != null ? birthdayPersonName + " " + twoDigitBirthdayYear + "\t" + birthdayChar + "\n" :
                                    (lastDayName.equals("Mo") ? getWeekOfDate(lastYear, 12, i) + "\n" : "\t\n"))).append("");


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        // every month of chosen year
        for (int i = 1; i <= 12; i++) {
            // every day
            for (int x = 1; x <= YearMonth.of(thatYear, i).lengthOfMonth(); x++) {
                try {
                    // get day name
                    String dayName = dateFormat.format(
                            new SimpleDateFormat("yyyy-M-d").parse(
                                    String.format("%d-%d-%d", thatYear, i, x)));

                    LocalDate thisDate = LocalDate.of(thatYear, i, x);
                    String holidayName = thisHolidays.returnNameByDate(thisDate);
                    boolean nationalHoliday = thisHolidays.returnIsNationalByDate(thisDate);

                    // write the lines ...
                    masterLine.append(mainAdobeStyleTag);

                    // get person with actual birthday
                    String birthdayPersonName = null;
                    int twoDigitBirthdayYear = 0;

                    for (Map.Entry<String, LocalDate> e : birthdays.entrySet())
                        if (e.getValue().withYear(thisDate.getYear()).isEqual(thisDate)) {
                            birthdayPersonName = e.getKey();
                            twoDigitBirthdayYear = e.getValue().getYear() % 100;
                        }

                    // friday and special from company, then holiday
                    // if holiday name is set, char will be displayed
                    masterLine.append(customs.contains(thisDate) ? specialHolidayChar : (fridays.contains(thisDate) ? companyOtherChar :
                            (holidayName != null ? (nationalHoliday ? nationalHolidayChar : nonNationalHolidayChar) : (birthdayPersonName != null ? birthdayChar : ""))));

                    // add day-number and day-name
                    masterLine.append("\t").append(x).append("\t").append(dayName);

                    // holidayName + char for not national, week of date (1-52) number if monday
                    masterLine.append("\t").append(holidayName != null ? holidayName + (!nationalHoliday ? "\t" + nonNationalHolidayChar : "\t") + "\n" : (birthdayPersonName != null ? birthdayPersonName + " " + twoDigitBirthdayYear + "\t" + birthdayChar + "\n" : (dayName.equals("Mo") ?
                            getWeekOfDate(thatYear, i, x) + "\n" : "\t\n"))).append("");
                } catch (ParseException e) {
                    // app does not throw exception
                    System.err.println(e.getMessage());
                }
            }

            // add more style tags to fill the column (empty space)
            if (fill31spaces) {
                int difference = 31 - YearMonth.of(thatYear, i).lengthOfMonth();

                while (0 < difference--)
                    masterLine.append(mainAdobeStyleTag);
            }
        }

        // jan after
        if (mainAdobeStyleTag.contains("14")) {
            for (int i = 1; i <= YearMonth.of(nextYear, 1).lengthOfMonth(); i++) {
                try {
                    String nextDayName = dateFormat.format(
                            new SimpleDateFormat("yyy-M-d").parse(
                                    String.format("%d-%d-%d", nextYear, 1, i)));

                    LocalDate nextDate = LocalDate.of(nextYear, 1, i);
                    String nextHolidayName = nextHolidays.returnNameByDate(nextDate);
                    boolean nextNationalHoliday = nextHolidays.returnIsNationalByDate(nextDate);

                    // write the lines ...
                    masterLine.append(mainAdobeStyleTag);

                    // get person with actual birthday
                    String birthdayPersonName = null;
                    int twoDigitBirthdayYear = 0;

                    for (Map.Entry<String, LocalDate> e : birthdays.entrySet())
                        if (e.getValue().withYear(nextDate.getYear()).isEqual(nextDate)) {
                            birthdayPersonName = e.getKey();
                            twoDigitBirthdayYear = e.getValue().getYear() % 100;
                        }

                    // friday from company, then holiday
                    // if holiday name is set, char will be displayed
                    masterLine.append(customs.contains(nextDate) ? specialHolidayChar : (fridays.contains(nextDate) ? companyOtherChar :
                            (nextHolidayName != null ? (nextNationalHoliday ? nationalHolidayChar : nonNationalHolidayChar) : (birthdayPersonName != null ? birthdayChar : ""))));

                    // add day-number and day-name
                    masterLine.append("\t").append(i).append("\t").append(nextDayName);

                    // holidayName + char for not national, week of date number if monday
                    masterLine.append("\t").append(nextHolidayName != null ? nextHolidayName + (!nextNationalHoliday ? "\t" + nonNationalHolidayChar : "\t") + "\n" : (birthdayPersonName != null ? birthdayPersonName + " " + twoDigitBirthdayYear + "\t" + birthdayChar + "\n" : (nextDayName.equals("Mo") ?
                            getWeekOfDate(nextYear, 1, i) + "\n" : "\t\n"))).append("");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return masterLine.toString();
    }

    // Getting week number ...
    private static int getWeekOfDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        TemporalField woy = WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear();
        return localDate.get(woy);
    }

    private static String convertToAdobeTaggedText(String text, String format) {
        // replace tab with adobe tag
        text = text.replace("\t", "<0x0009>");

        // replace newLine with adobe tag
        text = text.replace("\n", "<0x000D>");

        // replace spaces with adobe tag
        text = text.replace(format, "<-_->");
        text = text.replace(" ", "<0x00A0>");
        text = text.replace("<-_->", format);

        return text;
    }

    /**
     * @param format                     name set in inDesign calendar
     * @param searchForThisState         mainly for this state (germany)
     * @param alsoSearchNationalHolidays also national holidays
     * @param fill31spaces               fill the remaining days (to 31) with spaces
     * @param specialHolidayChar         character for formatting special company things
     * @param nationalHolidayChar        character for formatting national holidays
     * @param nonNationalHolidayChar     character for formatting non-national holidays
     * @param companyOtherChar           character for formatting company holidays
     * @param year                       the year for the calendar     @return complete adobe (mainly inDesign) styled text
     * @see #convertToAdobeTaggedText(String, String) convertToAdobeTaggedText (tags + stuff)
     */
    public static String getCalendarAsAdobeTaggedText(String format, String searchForThisState, boolean alsoSearchNationalHolidays, boolean fill31spaces,
                                                      char nationalHolidayChar, char nonNationalHolidayChar, char companyOtherChar, char specialHolidayChar, char birthdayChar, String csvCharSet,  int year) {

        String mainStyleTag = "<pstyle:" + format + ">";

        // Reformat data into adobe (creative apps) style tags
        String allText = getCalendarWithAdobeStyleTags(year, mainStyleTag, searchForThisState,
                alsoSearchNationalHolidays, fill31spaces, nationalHolidayChar, nonNationalHolidayChar, companyOtherChar, specialHolidayChar, birthdayChar, csvCharSet);

        return "<ANSI-WIN>" + System.getProperty("line.separator") + convertToAdobeTaggedText(allText, format);
    }
}
