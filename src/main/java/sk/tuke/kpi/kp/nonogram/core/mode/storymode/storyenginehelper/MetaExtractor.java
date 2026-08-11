package sk.tuke.kpi.kp.nonogram.core.mode.storymode.storyenginehelper;

public class MetaExtractor {

    public static String getBetween(String text, String start, String end) {
        int s = text.indexOf(start);
        int e = text.indexOf(end);
        if (s == -1 || e == -1) return null;
        return text.substring(s + start.length(), e).trim();
    }

    public static String getAfter(String text, String start) {
        int s = text.indexOf(start);
        if (s == -1) return null;
        return text.substring(s + start.length()).replaceAll("\\D", "").trim();
    }

    public static Integer parseIntOrNull(String s) {
        try {
            return (s == null || s.equals("null")) ? null : Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

