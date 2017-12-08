package us.jcole.metrics_channel;

import java.util.Date;

public abstract class Collector implements Iterable<Sample.Data> {
    public Sample getSample() {
        Sample sample = new Sample(new Date());
        for (Sample.Data data : this) {
            sample.add(data);
        }
        return sample;
    }
}
