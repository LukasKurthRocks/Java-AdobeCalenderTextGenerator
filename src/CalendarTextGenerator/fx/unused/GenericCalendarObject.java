package CalendarTextGenerator.fx.unused;

@SuppressWarnings("unused")
public class GenericCalendarObject<L, K, R> {
    private L identification;
    private K begin;
    private R end;

    public GenericCalendarObject(L identification, K begin, R end) {
        this.identification = identification;
        this.begin = begin;
        this.end = end;
    }

    public L getIdentification() {
        return identification;
    }

    public K getBegin() {
        return begin;
    }

    public R getEnd() {
        return end;
    }
}
