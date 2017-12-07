package us.jcole.metrics_channel;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import us.jcole.metrics_channel.proto.MetricsChannel;

import java.util.Date;

public class TestSampleMessage {
    @Test
    public void testRoundTrip() {
        Sample originalSample = new Sample(new Date());

        originalSample.add("foo", 15.0, Sample.Type.Absolute);
        originalSample.add("bar", 23.0, Sample.Type.Counter);

        MetricsChannel.SampleMessage sampleMessage = originalSample.toMessage();
        Sample fromSampleMessage = Sample.fromMessage(sampleMessage);

        assertEquals(originalSample, fromSampleMessage);
    }
}
