import org.apache.activemq.ActiveMQConnectionFactory;
import task.KMeansParams;

import javax.jms.*;

public class MessageProducer implements Runnable {

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

            String msg = "Hello World";
            KMeansParams params = new KMeansParams(2, 5, "s1");
            
            // insert message
            TextMessage message = session.createTextMessage(msg);
            System.out.println("MessageProducer Sent: " + msg);
            producer.send(message);

            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Oh no, something went wrong.");
        }
    }

}

