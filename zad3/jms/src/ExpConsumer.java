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
	private TopicConnectionFactory connectionFactory;
	private Topic inputTopic;

	// JMS Client objects
	private TopicConnection connection;
	private TopicSession session;
	private TopicPublisher publisher;
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

    protected void initializeObjects() throws NamingException, JMSException {
        initializeAdministrativeObjects(type);
        initializeJmsClientObjects();
    }

	private void initializeAdministrativeObjects(String type) throws NamingException {
		// ConnectionFactory
		connectionFactory = (TopicConnectionFactory) jndiContext.lookup("ConnectionFactory");
		// Destination
//      inputTopic = (Topic) jndiContext.lookup("production" + type);
		this.type = type;
		inputTopic = new JmsTopic("solutions" + type);
		System.out.println("JMS administrative objects (ConnectionFactory, Destinations) initialized!");
	}

	private void initializeJmsClientObjects() throws JMSException {
		connection = connectionFactory.createTopicConnection();
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		subscriber = session.createSubscriber(inputTopic);
		System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
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
