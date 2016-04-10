import org.exolab.jms.client.JmsTopic;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class ExpConsumer extends Agent {

	// JMS Administrative objects
	private Topic inputTopic;

	// JMS Client objects
	private TopicConnection connection;
	private TopicSession session;
	private TopicSubscriber subscriber;

	public ExpConsumer() throws NamingException, JMSException, InvalidOperationException {
		super();
	}

	public ExpConsumer(String type) throws NamingException, JMSException, InvalidOperationException {
		super(type);
	}

	public ExpConsumer(String type, String providerUrl) throws NamingException, JMSException, InvalidOperationException {
		super(type,providerUrl);
	}

    @Override
    protected void initializeTopics() {
        inputTopic = new JmsTopic("solutions" + type);
    }

    @Override
    protected void initializeQueues() {

    }

	protected void initializeJmsClientObjects() throws JMSException {
		connection = ((TopicConnectionFactory)connectionFactory).createTopicConnection();
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		subscriber = session.createSubscriber(inputTopic);
		System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
	}

    @Override
    protected void initializeAdditionalComponents() throws InvalidOperationException {

    }

    public void start() throws JMSException, IOException {

        subscriber.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("Message received");
                if (message instanceof ObjectMessage) {
                    try {
                        Expression exp = (Expression) ((ObjectMessage)message).getObject();
                        if (exp.isSolved()) {
                            System.out.println("Received solved expression: " + exp);;
                        } else {
                            System.err.println("Received expression but it's not solved");
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

		connection.start();
		String msg = String.format("Connection started - reading messages of type %s possible!", type);
		System.out.println(msg);

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

    protected void closeConnection() {
        // close the connection
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException exception) {
                exception.printStackTrace();
            }
        }
    }

}
