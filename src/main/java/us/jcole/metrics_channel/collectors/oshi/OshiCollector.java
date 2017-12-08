package us.jcole.metrics_channel.collectors.oshi;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import us.jcole.metrics_channel.Collector;

public abstract class OshiCollector extends Collector {
    protected final SystemInfo mSystemInfo;
    protected final HardwareAbstractionLayer mHardwareAbstractionLayer;

    public OshiCollector() {
        mSystemInfo = new SystemInfo();
        mHardwareAbstractionLayer = mSystemInfo.getHardware();
    }
}
