import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageConsumer implements Runnable {

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
            Destination queue = session.createQueue("KMEANS_QUEU");

            javax.jms.MessageConsumer consumer = session.createConsumer(queue);

            Message message = consumer.receive();

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("MessageConsumer Received: " + text);
            }

            session.close();
            connection.close();
        }
        catch (Exception ex) {
            System.out.println("Exception Occured");
        }
    }
}