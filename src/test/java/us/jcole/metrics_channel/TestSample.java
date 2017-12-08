package us.jcole.metrics_channel;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSample {
    @Test
    public void testCreation() {
        Sample sample = new Sample(new Date(0));
        sample.add("foo", 1.0, Sample.Type.Absolute);
        assertEquals(sample.getDate(), new Date(0));
        Sample.Data data = sample.get("foo");
        assertEquals(data.getName(), "foo");
        assertTrue(Double.compare(data.getValue(), 1.0) == 0);
        assertEquals(data.getType(), Sample.Type.Absolute);
    }

    @Test
    public void testMinus() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

            Sample a = new Sample(df.parse("2017-12-05T00:00:00+0000"));
            a.add("foo", 15.0, Sample.Type.Absolute);
            a.add("bar", 23.0, Sample.Type.Counter);

            Sample b = new Sample(df.parse("2017-12-05T00:00:01+0000"));
            b.add("foo", 16.0, Sample.Type.Absolute);
            b.add("bar", 27.0, Sample.Type.Counter);

            Sample c = b.minus(a);

            assertTrue(c.size() == 4);

            assertTrue(c.exists(".time.elapsed"));
            assertTrue(Double.compare(c.get(".time.elapsed").getValue(), 1000.0) == 0);
            assertEquals(c.get(".time.elapsed").getType(), Sample.Type.Absolute);

            assertTrue(c.exists("foo"));
            assertTrue(Double.compare(c.get("foo").getValue(), 16.0) == 0);
            assertEquals(c.get("foo").getType(), Sample.Type.Absolute);

            assertTrue(c.exists("bar"));
            assertTrue(Double.compare(c.get("bar").getValue(), 27.0) == 0);
            assertEquals(c.get("bar").getType(), Sample.Type.Counter);

            assertTrue(c.exists("bar.rate"));
            assertTrue(Double.compare(c.get("bar.rate").getValue(), 0.004) == 0);
            assertEquals(c.get("bar.rate").getType(), Sample.Type.Rate);
        } catch (ParseException pe) {
            System.err.println("ParseException: " + pe);
            assertTrue(false);
        }
    }
}
