package us.jcole.metrics_channel.collectors.oshi;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import us.jcole.metrics_channel.Collector;
import us.jcole.metrics_channel.Sample;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OshiCpuCollector extends Collector {
    private final SystemInfo mSystemInfo;
    private final HardwareAbstractionLayer mHardwareAbstractionLayer;
    private final CentralProcessor mCentralProcessor;

    public OshiCpuCollector() {
        mSystemInfo = new SystemInfo();
        mHardwareAbstractionLayer = mSystemInfo.getHardware();
        mCentralProcessor = mHardwareAbstractionLayer.getProcessor();
    }

    @Override
    public Iterator<Sample.Data> iterator() {
        List<Sample.Data> dataList = new LinkedList<>();

        long[] ticks = mCentralProcessor.getSystemCpuLoadTicks();
        long total_ticks = 0;
        for (CentralProcessor.TickType tickType : CentralProcessor.TickType.values()) {
            String name = tickType.toString().toLowerCase() + "_ticks";
            double value = (double) ticks[tickType.getIndex()];
            dataList.add(new Sample.Data(name, value, Sample.Type.Counter));
            total_ticks += value;
        }
        dataList.add(new Sample.Data("total_ticks", (double) total_ticks, Sample.Type.Counter));

        return dataList.iterator();
    }
}
