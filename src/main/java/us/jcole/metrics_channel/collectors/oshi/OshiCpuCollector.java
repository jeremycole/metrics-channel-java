package us.jcole.metrics_channel.collectors.oshi;

import oshi.hardware.CentralProcessor;
import us.jcole.metrics_channel.Sample;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OshiCpuCollector extends OshiCollector {
    private final String prefix = "cpu/";

    @Override
    public Iterator<Sample.Data> iterator() {
        List<Sample.Data> dataList = new LinkedList<>();

        long[] ticks = mHardwareAbstractionLayer.getProcessor().getSystemCpuLoadTicks();
        long total_ticks = 0;
        for (CentralProcessor.TickType tickType : CentralProcessor.TickType.values()) {
            String name = prefix + tickType.toString().toLowerCase() + "_ticks";
            double value = (double) ticks[tickType.getIndex()];
            dataList.add(new Sample.Data(name, value, Sample.Type.Counter));
            total_ticks += value;
        }
        dataList.add(new Sample.Data(prefix + "total_ticks", (double) total_ticks, Sample.Type.Counter));

        return dataList.iterator();
    }
}
