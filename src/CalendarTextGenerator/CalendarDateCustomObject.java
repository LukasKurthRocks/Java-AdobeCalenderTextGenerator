package CalendarTextGenerator;

import java.util.ArrayList;

public final class CalendarDateCustomObject<L, K, R> implements CalendarDateBaseObject<L, K, R> {
    private ArrayList<L> id = new ArrayList<>();
    private ArrayList<K> value1 = new ArrayList<>();
    private ArrayList<R> value2 = new ArrayList<>();

    @Override
    public void put(L id, K value1, R value2) {
        for (L l : this.id) {
            if (l == id) {
                System.err.println("id " + id + " already exists");
                return;
            }
        }

        this.id.add(id);
        this.value1.add(value1);
        this.value2.add(value2);
    }

    // todo: returning a NEW object? this could be better, right?
    public CalendarDateCustomObject<L, K, R> get(L getById) {
        for (int i = 0; i < id.size(); i++) {
             L thisID = id.get(i);
             if (thisID == getById) {
                 CalendarDateCustomObject<L, K, R> newDate = new CalendarDateCustomObject<>();
                 newDate.put(id.get(i), value1.get(i), value2.get(i));
                 return newDate;
             }
        }

        return null;
    }

    public ArrayList<L> getId() {
        return id;
    }

    public ArrayList<K> getValue1() {
        return value1;
    }

    public ArrayList<R> getValue2() {
        return value2;
    }

    public int size() {
        return id.size();
    }

    @Override
    public L returnNameByDate(R value2) {
        return null;
    }
}
