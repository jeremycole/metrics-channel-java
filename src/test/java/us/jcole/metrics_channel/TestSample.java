package us.jcole.metrics_channel;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSample {
    @Test
    public void testCreation() {
        Sample sample = new Sample(new Date(0));
        sample.add("foo", 1.0, Sample.Type.Absolute);
        assertEquals(sample.getDate(), new Date(0));
    }

    public static void notReallyMain(String[] args) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

            Sample a = new Sample(df.parse("2017-12-05T00:00:00+0000"));
            a.add("foo", 15.0, Sample.Type.Absolute);
            a.add("bar", 23.0, Sample.Type.Counter);

            Sample b = new Sample(df.parse("2017-12-05T00:00:01+0000"));
            b.add("foo", 16.0, Sample.Type.Absolute);
            b.add("bar", 27.0, Sample.Type.Counter);

            Sample c = b.minus(a);

            c.dump(System.out);
        } catch (ParseException pe) {
            System.err.println("ParseException: " + pe);
        }
    }
}
