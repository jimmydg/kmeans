package KMeans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import task.KMeansParams;

import javax.jms.*;
import java.util.UUID;

public class MessageProducer implements Runnable {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void run() {
        try {
            // Create connection factory
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            //Create connection.
            Connection connection = factory.createConnection();

            // Start the connection
            connection.start();

            // Create a session which is non transactional
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create Destination queue
            Destination queue = session.createQueue("KMEANS_QUEUE");

            // Create a producer
            javax.jms.MessageProducer producer = session.createProducer(queue);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            KMeansParams params = new KMeansParams(2, 5, "s1");

            // insert message
            TextMessage message = createTextMessage(session, params);
            System.out.println("KMeans.MessageProducer Sent: " + message);
            producer.send(message);

            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Oh no, something went wrong.");
        }
    }

    public static TextMessage createTextMessage(Session session,KMeansParams parms) throws JMSException, JsonProcessingException {
        String jsonString = MAPPER.writeValueAsString(parms);
        TextMessage msg = session.createTextMessage(jsonString);
//        msg.setJMSCorrelationID(UUID.randomUUID().toString());
        msg.setJMSCorrelationID("blaat");
        return msg;
    }

}

