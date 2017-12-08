package us.jcole.metrics_channel;

import org.junit.Test;
import us.jcole.metrics_channel.collectors.oshi.OshiCpuCollector;

public class TestOshiCollectors {
    @Test
    public void testOshiCpuCollector() {
        Collector cpu = new OshiCpuCollector();

        Sample sample = cpu.getSample();
        sample.dump(System.out);
    }
}
