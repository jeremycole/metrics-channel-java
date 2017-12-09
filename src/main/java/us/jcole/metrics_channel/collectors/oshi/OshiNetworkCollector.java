package us.jcole.metrics_channel.collectors.oshi;

import oshi.hardware.NetworkIF;
import us.jcole.metrics_channel.Sample;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OshiNetworkCollector extends OshiCollector {
    private final String prefix = "network/";

    @Override
    public Iterator<Sample.Data> iterator() {
        List<Sample.Data> dataList = new LinkedList<>();

        for (NetworkIF networkIF : mHardwareAbstractionLayer.getNetworkIFs()) {
            String ifPrefix = prefix + networkIF.getName() + "/";
            dataList.add(new Sample.Data(ifPrefix + "rx_bytes", (double) networkIF.getBytesRecv(), Sample.Type.Counter));
            dataList.add(new Sample.Data(ifPrefix + "tx_bytes", (double) networkIF.getBytesSent(), Sample.Type.Counter));
            dataList.add(new Sample.Data(ifPrefix + "rx_packets", (double) networkIF.getPacketsRecv(), Sample.Type.Counter));
            dataList.add(new Sample.Data(ifPrefix + "tx_packets", (double) networkIF.getPacketsSent(), Sample.Type.Counter));
            dataList.add(new Sample.Data(ifPrefix + "rx_errors", (double) networkIF.getInErrors(), Sample.Type.Counter));
            dataList.add(new Sample.Data(ifPrefix + "tx_errors", (double) networkIF.getOutErrors(), Sample.Type.Counter));
        }

        return dataList.iterator();
    }
}
