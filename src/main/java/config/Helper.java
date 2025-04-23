package config;

public class Helper {
    // 100_000 / pracovny den
    public static int getDays(double time) {
        return (int) (time / (60 * 60 * 8));
    }

    // 100_000 % 8 hodin / hodina
    public static int getHours(double time, int offset) {
        return (int) ((time % (60 * 60 * 8)) / (60 * 60)) + offset;
    }

    // 100_000 % hodina / minuta
    public static int getMinutes(double time) {
        return (int) ((time % (60 * 60)) / 60);
    }

    public static int getSeconds(double time) {
        return (int) (time % 60);
    }

    public static String timeToDateString(double time, int offset) {
        if (time == 0.0) {
            return "-";
        }
        return String.format("%d, %02d:%02d:%02d", getDays(time),getHours(time, offset), getMinutes(time), getSeconds(time));
    }
}
