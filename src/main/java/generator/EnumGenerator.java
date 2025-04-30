package generator;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class EnumGenerator implements IGenerator<Enum<?>> {

    private final TreeMap<Double, Enum<?>> probabilityMap = new TreeMap<>();
    private final Random random;
    private double totalWeight = 0.0;

    public <T extends Enum<T>> EnumGenerator(Map<T, Double> probabilities, SeedGenerator seedGenerator) {
        this.random = new Random(seedGenerator.sample());

        for (Map.Entry<T, Double> entry : probabilities.entrySet()) {
            totalWeight += entry.getValue();
            probabilityMap.put(totalWeight, entry.getKey());
        }
    }

    @Override
    public Enum<?> sample() {
        double value = random.nextDouble() * totalWeight;
        return probabilityMap.higherEntry(value).getValue();
    }
}