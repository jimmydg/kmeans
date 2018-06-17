package KMeans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import task.KMeansParams;
import task.KMeansReturn;

import javax.jms.*;

public class KMeansProcessor {

    public static void main(String[] args) {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination in = session.createQueue("KMEANS_IN");
            Destination out = session.createQueue("KMEANS_OUT");


            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(in);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(out);

            ObjectMapper mapper = new ObjectMapper();

            // Wait for a message
            Message message = consumer.receive();

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();

                KMeansParams params = mapper.readValue(text, KMeansParams.class);

                KMeans kMeans = new KMeans();
                KMeansReturn result = kMeans.execute(params);

                TextMessage returnMessage = session.createTextMessage(mapper.writeValueAsString(result));
                returnMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                producer.send(returnMessage);
                System.out.println("message sent: " + returnMessage);

            } else {
                throw new IllegalArgumentException("Unexpected message " + message);
            }

            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
