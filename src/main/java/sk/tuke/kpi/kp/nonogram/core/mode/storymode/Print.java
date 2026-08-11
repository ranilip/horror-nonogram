package sk.tuke.kpi.kp.nonogram.core.mode.storymode;

public class Print {

    public static void character(String emotion, String text) {
        String face = switch (emotion) {
            case "MeAlarmed" -> " (  O﹏o)!   |";
            case "MeAngry" -> " (  ಠ.ಠ)   |";
            case "MeHorrified" -> " ( ・◇・)?  |";
            case "CompanionWhispering" -> " ( @´ω｀@)  |";
            case "CompanionSmiling" -> " (  @°▽°@)  |";
            default -> " ( ・∀・)   |";
        };

        separatorText();
        System.out.println(face + "    " + text);
        separatorText();
        System.out.println("\n");
    }

    public static void showIntro() {
        separatorStartEnd();
        System.out.println("""
                 ______                          _   ______       ___     _                           ______   \s
                (  /          o           /      /  (  / /       ( /     //              /       /)  (  /      \s
                  /--    ,_  ,  (   __ __/ _    /     / /_  _     / (   // __,  _ _   __/    __ //    -/--__ _,\s
                (/____/_/|_)_(_/_)_(_)(_/_(/_  /    _/ / /_(/_  _/_/_)_(/_(_/(_/ / /_(_/_   (_)//_   _/  (_)(_)_
                        /|                                                                    /)             /|\s
                       (/                                                                    (/             (/ \s
                """);
        separatorStartEnd();
        System.out.print(" ".repeat(37 )+"Press Enter to continue...\n");
        separatorStartEnd();
    }

    public static void showEnding() {
        separatorStartEnd();
        System.out.println("""
                 ______          \s
                (  /             /
                  /--   _ _   __/\s
                (/____// / /_(_/_\s
                """);
        separatorStartEnd();
    }

    public static void separator() {
        System.out.println("\n\n"+"=".repeat(100 )+"\n".repeat(2));
    }

    public static void separatorText() {
        System.out.println("-".repeat(100 ));
    }

    public static void separatorStartEnd() {
        System.out.println("\n"+"=".repeat(100 )+"\n");
    }

}
