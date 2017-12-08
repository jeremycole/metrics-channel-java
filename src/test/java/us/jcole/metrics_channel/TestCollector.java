package us.jcole.metrics_channel;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class TestCollector {
    private static class RandomCollector extends Collector {
        int mCount;
        Random mRandom;

        RandomCollector(int count) {
            mCount = count;
            mRandom = new Random();
        }

        public Iterator<Sample.Data> iterator() {
            return new Iterator<Sample.Data>() {
                int iteratorCount = mCount;

                public void remove() {

                }

                public boolean hasNext() {
                    return iteratorCount > 0;
                }

                public Sample.Data next() {
                    iteratorCount--;
                    return new Sample.Data(String.format("%d", iteratorCount), mRandom.nextDouble(), Sample.Type.Absolute);
                }
            };
        }
    }

    public static void main(String[] args) {
        for (Sample.Data data : new RandomCollector(10)) {
            System.out.printf("%s: %f (%s)\n", data.getName(), data.getValue(), data.getType());
        }

        PeriodicMetric pm = new PeriodicMetric(60);
        pm.collect(new Date(), new RandomCollector(10));
        pm.collect(new Date(), new RandomCollector(10));

        pm.dump(System.out);
    }
}
