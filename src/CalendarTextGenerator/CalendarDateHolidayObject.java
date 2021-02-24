package CalendarTextGenerator;

import java.util.ArrayList;

/**
 * Capture dates in multiple ArrayLists.
 * DateName, FederalState, ActualDate, isNationalHoliday?
 *
 * @author Lukas 10.03.2017
 */
public final class CalendarDateHolidayObject<L, K, R> implements CalendarDateBaseObject<L,K,R> {
    private ArrayList<L> name = new ArrayList<>();
    private ArrayList<K> states = new ArrayList<>();
    private ArrayList<R> date = new ArrayList<>();
    private ArrayList<Boolean> isNational = new ArrayList<>();

    /**
     * Save a date in the list.
     * Have a look at {@link java.time.LocalDate LocalDate}.
     *
     * @param name       name of the date
     * @param states     states the date is for
     * @param date       the actual date
     * @param isNational <em>boolean</em> for if the date is national
     */
    void put(L name, K states, R date, boolean isNational) {
        this.name.add(name);
        this.states.add(states);
        this.date.add(date);
        this.isNational.add(isNational);
    }

    @Override
    public void put(L id, K value1, R value2) {
        put(id, value1, value2, false);
    }

    @Override
    public int size() {
        return name.size();
    }

    /**
     * Retrieving the set date name by the date chosen
     *
     * @param date the date searching the name for
     * @return the name if found, <em>null</em> if no name is set
     */
    @Override
    public L returnNameByDate(R date) {
        for (int i = 0; i < this.date.size(); i++)
            if (this.date.get(i).equals(date))
                return name.get(i);
        return null;
    }

    /**
     * Check if a date is national holiday
     *
     * @param date returning the bool for if the date is national
     * @return <em>boolean</em> for is the date is national
     */
    boolean returnIsNationalByDate(R date) {
        for (int i = 0; i < this.date.size(); i++)
            if (this.date.get(i).equals(date))
                return isNational.get(i);
        return false;
    }

    @Override
    public String toString() {
        StringBuilder oneBuilder = new StringBuilder("CalendarDateHolidayObject{");

        for (int i = 0; i < name.size(); i++)
            oneBuilder.append("date{" + "name=")
                    .append(name.get(i)).append(", states=")
                    .append(states.get(i)).append(", date=")
                    .append(date.get(i)).append(", isNational=")
                    .append(isNational.get(i)).append("}");
        String one = oneBuilder.toString();
        one += "}";

        return one;
    }
}
