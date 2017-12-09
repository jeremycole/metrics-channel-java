package us.jcole.metrics_channel.collectors.oshi;

import oshi.hardware.HWDiskStore;
import us.jcole.metrics_channel.Sample;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OshiDiskCollector extends OshiCollector {
    private final String prefix = "disk/";

    @Override
    public Iterator<Sample.Data> iterator() {
        List<Sample.Data> dataList = new LinkedList<>();

        for (HWDiskStore hwDiskStore : mHardwareAbstractionLayer.getDiskStores()) {
            String diskPrefix = prefix + hwDiskStore.getName() + "/";
            dataList.add(new Sample.Data(diskPrefix + "reads", (double) hwDiskStore.getReads(), Sample.Type.Counter));
            dataList.add(new Sample.Data(diskPrefix + "read_bytes", (double) hwDiskStore.getReadBytes(), Sample.Type.Counter));
            dataList.add(new Sample.Data(diskPrefix + "writes", (double) hwDiskStore.getWrites(), Sample.Type.Counter));
            dataList.add(new Sample.Data(diskPrefix + "write_bytes", (double) hwDiskStore.getWriteBytes(), Sample.Type.Counter));
        }

        return dataList.iterator();
    }
}
