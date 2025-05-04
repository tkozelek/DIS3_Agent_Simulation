package config;

import OSPStat.Stat;

public class StatFormatter {

    public static String statToStringTime(Stat stat, String name) {
        double[] is = stat.sampleSize() > 2 ? stat.confidenceInterval_95() : new double[]{0, 0};
        return String.format("%s: %.2f <%.2f | %.2f> %.2f <%.2f | %.2f>",
                name,
                stat.mean() / 60 / 60,
                is[0] / 60 / 60,
                is[1] / 60 / 60,
                stat.mean(),
                is[0],
                is[1]);
    }

    public static String statToString(Stat stat, String name) {
        double[] is = stat.sampleSize() > 2 ? stat.confidenceInterval_95() : new double[]{0, 0};
        return String.format("%s: %.2f <%.2f | %.2f>",
                name,
                stat.mean(),
                is[0],
                is[1]);
    }

    public static String statToStringPercentual(Stat stat, String name) {
        double[] is = stat.sampleSize() > 2 ? stat.confidenceInterval_95() : new double[]{0, 0};
        return String.format("%s: %.2f%% <%.2f%% | %.2f%%>",
                name,
                stat.mean() * 100,
                is[0] * 100,
                is[1] * 100);
    }
}