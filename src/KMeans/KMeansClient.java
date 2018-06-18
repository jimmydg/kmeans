package KMeans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import task.KMeansParams;
import task.KMeansReturn;

import javax.jms.*;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import java.io.IOException;
import java.util.Random;

public class KMeansClient {
    private static final ObjectMapper MAPPER = new ObjectMapper();

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

            // Create a MessageProducer
            MessageProducer producer = session.createProducer(in);

            // Creating the parameter object
            KMeansParams params = new KMeansParams(2, 5, "s1");

            // Creating a message
            TextMessage message = createTextMessage(session, params);

            // Tell the producer to send the message
            producer.send(message);

            // Wait for the returning message
            MessageConsumer consumer = session.createConsumer(out);
            Message returnMessage = consumer.receive();

            if (returnMessage instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) returnMessage;
                String text = textMessage.getText();

                KMeansReturn result = parseReturnMessage(text);
                System.out.println(result);
                System.out.println("--------- RESULTS: ---------");
                System.out.println("K = " + result.getAmountOfClusters());
                System.out.println("Result = " + result.getResult());
                System.out.println("Threads = " + result.getNumThreads());
                System.out.println("Runtime (ms) = " + result.getRunTime());

            } else {
                throw new IllegalArgumentException("Unexpected message " + message);
            }

            // Clean up
            session.close();
            connection.close();
        } catch (JMSException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Transforming the JSON body to object
     *
     * @param text JSON
     * @return GregorySeriesReturn object
     * @throws IOException Exception
     */
    private static KMeansReturn parseReturnMessage(String text) throws IOException {
        return MAPPER.readValue(text, KMeansReturn.class);
    }

    private static TextMessage createTextMessage(Session session, KMeansParams parms) throws JMSException, JsonProcessingException {
        String jsonString = MAPPER.writeValueAsString(parms);
        return session.createTextMessage(jsonString);
    }
}
