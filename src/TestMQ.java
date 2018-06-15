public class TestMQ {
    public static void main(String[] args) {
        MessageProducer producer = new MessageProducer();
        MessageConsumer consumer = new MessageConsumer();

        Thread producerThread = new Thread(producer);
        producerThread.start();

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();


//        System.out.println("Press a key to terminate");
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("End");

    }
}
