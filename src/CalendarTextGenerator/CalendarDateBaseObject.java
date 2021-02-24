package CalendarTextGenerator;

public interface CalendarDateBaseObject<L, K, R>  {
    /**
     * Save a date in the list.
     * Have a look at {@link java.time.LocalDate LocalDate}.
     *
     * @param id           if of the base object
     * @param value1       states the date is for
     * @param value2       the actual date
     */
    void put(L id, K value1, R value2);

    // void get();

    int size();

    /**
     * Retrieving the set date name by the date chosen
     *
     * @param value2 the value searching the id for
     * @return the name if found, <em>null</em> if no name is set
     */
    L returnNameByDate(R value2);

    @Override
    public String toString();
}
