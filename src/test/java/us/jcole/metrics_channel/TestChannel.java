package us.jcole.metrics_channel;

import java.util.Date;

public class TestChannel {
    static class SampleProducerThread implements Runnable {
        @Override
        public void run() {
            System.out.println("SampleProducerThread...");

            Channel channel = new Channel("vm://localhost");

            Sample sample = new Sample(new Date());
            sample.add("test", 1.0, Sample.Type.Absolute);
            channel.sendSample(1000, sample);

            channel.close();
        }
    }

    static class SampleConsumerThread implements Runnable {
        @Override
        public void run() {
            System.out.println("SampleConsumerThread...");

            Channel channel = new Channel("vm://localhost");

            Sample sample = channel.receiveSample(1000);
            sample.dump(System.out);

            channel.close();
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting SampleConsumerThread...");
        Thread consumerThread = new Thread(new SampleConsumerThread());
        consumerThread.start();

        System.out.println("Starting SampleProducerThread...");
        Thread producerThread = new Thread(new SampleProducerThread());
        producerThread.start();

        try {
            System.out.println("Waiting for threads to end...");
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
