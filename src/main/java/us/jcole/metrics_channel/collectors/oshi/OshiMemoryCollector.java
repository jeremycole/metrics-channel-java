package us.jcole.metrics_channel.collectors.oshi;

import oshi.hardware.GlobalMemory;
import us.jcole.metrics_channel.Sample;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OshiMemoryCollector extends OshiCollector {
    private final String prefix = "memory/";

    @Override
    public Iterator<Sample.Data> iterator() {
        GlobalMemory globalMemory = mHardwareAbstractionLayer.getMemory();
        List<Sample.Data> dataList = new LinkedList<>();

        dataList.add(new Sample.Data(prefix + "total", (double) globalMemory.getTotal(), Sample.Type.Absolute));
        dataList.add(new Sample.Data(prefix + "free", (double) globalMemory.getAvailable(), Sample.Type.Absolute));
        dataList.add(new Sample.Data(prefix + "swap_total", (double) globalMemory.getSwapTotal(), Sample.Type.Absolute));
        dataList.add(new Sample.Data(prefix + "swap_used", (double) globalMemory.getSwapUsed(), Sample.Type.Absolute));

        return dataList.iterator();
    }
}
