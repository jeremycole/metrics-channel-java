package us.jcole.metrics_channel;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Date;

public class TestChannel {
    static class Result {
        Sample sample;
    }

    static abstract class SampleProducerConsumerThread  {
        Result mResult;
        public SampleProducerConsumerThread(Result result) {
            mResult = result;
        }

    }

    static class SampleProducerThread extends SampleProducerConsumerThread implements Runnable {
        public SampleProducerThread(Result result) {
            super(result);
        }

        @Override
        public void run() {
            System.out.println("SampleProducerThread...");

            Channel channel = new Channel("vm://localhost");

            Sample sample = new Sample(new Date());
            sample.add("test", 1.0, Sample.Type.Absolute);
            channel.sendSample(1000, sample);

            mResult.sample = sample;

            channel.close();
        }
    }

    static class SampleConsumerThread extends SampleProducerConsumerThread implements Runnable {
        public SampleConsumerThread(Result result) {
            super(result);
        }

        @Override
        public void run() {
            System.out.println("SampleConsumerThread...");

            Channel channel = new Channel("vm://localhost");

            Sample sample = channel.receiveSample(1000);

            mResult.sample = sample;

            channel.close();
        }
    }

    @Test
    public void testProducerConsumer() {
        System.out.println("Starting SampleConsumerThread...");
        Result consumerResult = new Result();
        Thread consumerThread = new Thread(new SampleConsumerThread(consumerResult));
        consumerThread.start();

        System.out.println("Starting SampleProducerThread...");
        Result producerResult = new Result();
        Thread producerThread = new Thread(new SampleProducerThread(producerResult));
        producerThread.start();

        try {
            System.out.println("Waiting for threads to end...");
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(producerResult.sample, consumerResult.sample);
    }
}
