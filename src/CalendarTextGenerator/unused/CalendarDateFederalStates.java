package CalendarTextGenerator.unused;

// MayBe for later use
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public enum CalendarDateFederalStates {
    NONE(-1, ""), ALL(0, "ALL"),
    BW(1, "Baden-Württemberg"),
    BY(2, "Bayern"),
    BE(3, "Berlin"),
    BB(4, "Brandenburg"),
    HB(5, "Bremen"),
    HH(6, "Hamburg"),
    HE(7, "Hessen"),
    MV(8, "Mecklenburg-Vorpommern"),
    NI(9, "Niedersachsen"),
    NW(10, "Nordrhein-Westfahlen"),
    RP(11, "Rheinland-Pfalz"),
    SL(12, "Saarland"),
    SN(13, "Sachsen"),
    ST(14, "Sachsen-Anhalt"),
    SH(15, "Schleswig-Holstein"),
    TH(16, "Thüringen");

    int code;
    String name;

    CalendarDateFederalStates(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public CalendarDateFederalStates getState() {
        return this;
    }

    public int getStateNum() {
        return this.code;
    }
}
