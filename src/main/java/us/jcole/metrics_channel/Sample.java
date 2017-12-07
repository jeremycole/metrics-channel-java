package us.jcole.metrics_channel;

import us.jcole.metrics_channel.proto.MetricsChannel;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sample {
    public enum Type {
        Rate,
        Counter,
        Absolute,
    }

    public static class Data {
        String mName;
        double mValue;
        Type mType;

        public Data(String name, Double value, Type type) {
            mName = name;
            mValue = value;
            mType = type;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public double getValue() {
            return mValue;
        }

        public void setValue(double value) {
            mValue = value;
        }

        public Type getType() {
            return mType;
        }

        public void setType(Type type) {
            mType = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data data = (Data) o;
            return Double.compare(data.mValue, mValue) == 0 &&
                    Objects.equals(mName, data.mName) &&
                    mType == data.mType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(mName, mValue, mType);
        }
    }

    private Date mDate;
    private Map<String, Data> mData;

    public Sample(Date date) {
        mDate = date;
        mData = new HashMap<String, Data>();
    }

    public Date getDate() {
        return mDate;
    }

    public Map<String, Data> getData() {
        return mData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sample sample = (Sample) o;
        return Objects.equals(mDate, sample.mDate) &&
                Objects.equals(mData, sample.mData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mDate, mData);
    }

    void dump(PrintStream ps) {
        ps.println(mDate.toString() + ":");
        for (String key : mData.keySet()) {
            Sample.Data data = mData.get(key);
            ps.printf("  %-20s = %f (%s)\n", key, data.getValue(), data.getType());
        }
    }

    void add(Data data) {
        mData.put(data.getName(), data);
    }

    void add(String name, Double value, Type type) {
        mData.put(name, new Data(name, value, type));
    }

    Data first() {
        mData.values().
    }

    static Sample minus(Sample a, Sample b) {
        Sample result = new Sample(b.getDate());

        double elapsed_time = a.getDate().getTime() - b.getDate().getTime();
        result.add(".time.elapsed", elapsed_time, Type.Absolute);

        for (String key : a.getData().keySet()) {
            if (!b.getData().containsKey(key)) {
                result.add(key, null, null);
            }

            Data this_data = a.getData().get(key);
            Data other_data = b.getData().get(key);

            if (this_data.getType() != other_data.getType()) {
                result.add(key, null, null);
            }

            switch (this_data.getType()) {
                case Counter:
                    double rated_value = (this_data.getValue() - other_data.getValue()) / elapsed_time;
                    result.add(key + ".rate", rated_value, Type.Rate);
                    break;
            }
            result.add(key, this_data.getValue(), this_data.getType());
        }

        return result;
    }

    Sample minus(Sample other) {
        return Sample.minus(this, other);
    }

    private static MetricsChannel.MetricMessage.Type convertTypeToMessageType(Type type) {
        switch (type) {
            case Rate:
                return MetricsChannel.MetricMessage.Type.RATE;
            case Counter:
                return MetricsChannel.MetricMessage.Type.COUNTER;
            case Absolute:
                return MetricsChannel.MetricMessage.Type.ABSOLUTE;
        }
        return null;
    }

    private static Type convertMessageTypeToType(MetricsChannel.MetricMessage.Type metricMessageType) {
        switch (metricMessageType) {
            case RATE:
                return Type.Rate;
            case COUNTER:
                return Type.Counter;
            case ABSOLUTE:
                return Type.Absolute;
        }
        return null;
    }

    public MetricsChannel.SampleMessage toMessage() {
        MetricsChannel.SampleMessage.Builder sampleMessageBuilder = MetricsChannel.SampleMessage.newBuilder();
        sampleMessageBuilder.setDate(mDate.getTime());

        for (Data data : mData.values()) {
            sampleMessageBuilder.addMetrics(MetricsChannel.MetricMessage.newBuilder()
                    .setName(data.getName())
                    .setValue(data.getValue())
                    .setType(convertTypeToMessageType(data.getType()))
                    .build());
        }

        return sampleMessageBuilder.build();
    }

    public static Sample fromMessage(MetricsChannel.SampleMessage sampleMessage) {
        Sample sample = new Sample(new Date(sampleMessage.getDate()));

        for (MetricsChannel.MetricMessage metricMessage : sampleMessage.getMetricsList()) {
            sample.add(metricMessage.getName(), metricMessage.getValue(), convertMessageTypeToType(metricMessage.getType()));
        }

        return sample;
    }
}
