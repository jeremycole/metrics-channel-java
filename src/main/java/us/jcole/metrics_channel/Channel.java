package us.jcole.metrics_channel;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.activemq.ActiveMQConnectionFactory;
import us.jcole.metrics_channel.proto.MetricsChannel;

import javax.jms.*;
import java.util.Locale;

public class Channel {
    String mBrokerUri;
    Connection mConnection;
    Session mSession;

    public Channel(String brokerUri) {
        mBrokerUri = brokerUri;

        connect();
    }

    void connect() {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(mBrokerUri);

            mConnection = connectionFactory.createConnection();
            mConnection.start();

            mSession = mConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException jmse) {
            System.out.println("Caught JMSException: " + jmse.getMessage());
            jmse.printStackTrace();
            throw new RuntimeException("Can't continue!");
        }
    }

    void close() {
        try {
            mSession.close();
            mConnection.close();
        } catch (JMSException jmse) {
            System.out.println("Exception at close! " + jmse.getMessage());
        }
    }

    static String periodTopicName(int period) {
        return String.format(Locale.US, "period/%d", period);
    }

    void sendSample(int period, Sample sample) {
        try {
            Destination destination = mSession.createTopic(periodTopicName(period));

            MessageProducer messageProducer = mSession.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            BytesMessage message = mSession.createBytesMessage();
            message.writeBytes(sample.toMessage().toByteArray());

            messageProducer.send(message);
        } catch (JMSException jmse) {
            System.out.println("Exception sending metrics! " + jmse.getMessage());
        }
    }

    Sample receiveSample(int period) {
        try {
            Destination destination = mSession.createTopic(periodTopicName(period));
            MessageConsumer messageConsumer = mSession.createConsumer(destination);

            Message message = messageConsumer.receive();
            if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] bytes = new byte[(int)bytesMessage.getBodyLength()];
                bytesMessage.readBytes(bytes);
                Sample sample = Sample.fromMessage(MetricsChannel.SampleMessage.parseFrom(bytes));
                return sample;
            }
        } catch (JMSException jmse) {
            System.out.println("Exception receiving metrics! " + jmse.getMessage());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}
