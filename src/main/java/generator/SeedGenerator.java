package generator;

import java.util.Random;

public class SeedGenerator implements IGenerator<Long> {
    private final Random random;

    public SeedGenerator(long seed) {
        this.random = new Random(seed);
    }

    public SeedGenerator() {
        this.random = new Random();
    }

    public Random getRandom() {
        return random;
    }

    @Override
    public Long sample() {
        return this.random.nextLong();
    }
}
