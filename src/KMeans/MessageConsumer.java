package KMeans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import task.KMeansReturn;

import javax.jms.*;
import java.io.IOException;

public class MessageConsumer implements Runnable {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void run() {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            //Create Connection
            Connection connection = factory.createConnection();

            // Start the connection
            connection.start();

            // Create Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Create queue
            Destination queue = session.createQueue("KMEANS_QUEUE");

            javax.jms.MessageConsumer consumer = session.createConsumer(queue, "JMSCorrelationID='blaat'");

            Message message = consumer.receive();



            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();

                KMeansReturn result = parseReturnMessage(text);
                System.out.println("KMeans.MessageConsumer Received: ");
                System.out.println("K = " + result.getAmountOfClusters());
                System.out.println("Result = " + result.getResult());
                System.out.println("Threads = " + result.getNumThreads());
                System.out.println("Runtime = " + result.getRunTime());
            } else {
                throw new IllegalArgumentException("Unexpected message " + message);
            }

            session.close();
            connection.close();
        } catch (Exception ex) {
            System.out.println("Exception Occured");
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
}