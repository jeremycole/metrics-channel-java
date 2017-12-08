package us.jcole.metrics_channel;

import org.junit.Test;
import us.jcole.metrics_channel.collectors.oshi.OshiCpuCollector;
import us.jcole.metrics_channel.collectors.oshi.OshiMemoryCollector;

public class TestOshiCollectors {
    @Test
    public void testOshiCpuCollector() {
        Collector collector = new OshiCpuCollector();

        Sample sample = collector.getSample();
        sample.dump(System.out);
    }

    @Test
    public void testOshiMemoryCollector() {
        Collector collector = new OshiMemoryCollector();

        Sample sample = collector.getSample();
        sample.dump(System.out);
    }
}
