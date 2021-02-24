package CalendarTextGenerator.fx;

// todo: create generic or create abstract list extend for this??
public class CalendarObject {
    private int idNr;
    private String dateBegin;
    private String dateEnd;

    CalendarObject(int idNr, String dateBegin, String dateEnd) {
        this.idNr = idNr;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
    }

    @Override
    public String toString() {
        return "CalendarObject{" +
                "idNr=" + idNr +
                ", dateBegin='" + dateBegin + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                '}';
    }

    int getIdNr() {
        return idNr;
    }

    String getDateBegin() {
        return dateBegin;
    }

    String getDateEnd() {
        return dateEnd;
    }
}
