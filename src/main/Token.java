package main;

/**
 * Created by Kris on 3/24/2017.
 */
public class Token {
    private String kind;
    private String details;

    public Token(String k, String d) {
        kind = k;
        details = d;
    }

    public boolean isKind(String s) {
        return kind.equals(s);
    }

    public String getKind() {
        return kind;
    }

    public String getDetails() {
        return details;
    }

    public String toString() {
        return "[" + kind + "," + details + "]";
    }

    public boolean matches(String knd, String dets) {
        return kind.equals(knd) && details.equals(dets);
    }

}