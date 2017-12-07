package us.jcole.metrics_channel;

import java.io.PrintStream;
import java.util.*;

public class PeriodicMetric {
    private int mSampleSize;
    private List<Sample> mSamples;

    public PeriodicMetric(int sampleSize) {
        mSampleSize = sampleSize;
        mSamples = new LinkedList<Sample>();
    }

    public void addSample(Sample sample) {
        if (mSamples.size() == mSampleSize)
            mSamples.remove(0);
        mSamples.add(sample);
    }

    public void collect(Date date, Collector collector) {
        Sample sample = new Sample(date);

        for (Sample.Data data : collector) {
            sample.add(data);
        }

        addSample(sample);
    }

    public int pruneOlderThan(Date date) {
        int removedSamples = 0;

        for (Sample sample : mSamples) {
            if (sample.getDate().before(date)) {
                mSamples.remove(sample);
                removedSamples++;
            }
        }
        return removedSamples;
    }

    void dump(PrintStream ps) {
        ps.printf("Number of samples: %d of %d:\n",
                mSamples.size(), mSampleSize);
        for (Sample sample : mSamples) {
            sample.dump(ps);
        }
    }
}
