package us.jcole.metrics_channel;

import java.util.Date;

public class TestPeriodicMetric {
    public static void main(String[] args) {
        PeriodicMetric pm = new PeriodicMetric(60);

        Sample sample = new Sample(new Date());
        sample.add("one", 1.0, Sample.Type.Absolute);
        sample.add("two", 2.0, Sample.Type.Counter);
        Sample.Data three = new Sample.Data("three", 3.0, Sample.Type.Rate);
        sample.add(three);
        pm.addSample(sample);

        pm.dump(System.out);
    }
}
