import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Noteboard {

	private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";
	private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
	private static final String DEFAULT_INCOMING_MESSAGES_QUEUE_NAME = "queue1";

	// Application JNDI context
	private Context jndiContext;
	
	// JMS Administrative objects references
	private QueueConnectionFactory queueConnectionFactory;
	private Queue incomingMessagesQueue;
	
	// JMS Client objects
	private QueueConnection connection;
	private QueueSession session;
	private QueueReceiver receiver;
	
	// Business logic objects
	private StringBuffer textBuffer = new StringBuffer();

	/************** Initialization BEGIN ******************************/
	public Noteboard() throws NamingException, JMSException {
		this(DEFAULT_JMS_PROVIDER_URL, DEFAULT_INCOMING_MESSAGES_QUEUE_NAME);
	}
	
	public Noteboard(String providerUrl, String incomingMessagesQueueName) throws NamingException, JMSException {
		initializeJndiContext(providerUrl);
		initializeAdministrativeObjects(incomingMessagesQueueName);
		initializeJmsClientObjects();
	}

	private void initializeJndiContext(String providerUrl) throws NamingException {
		// JNDI Context
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
		props.put(Context.PROVIDER_URL, providerUrl);
		jndiContext = new InitialContext(props);
		System.out.println("JNDI context initialized!");
	}

	private void initializeAdministrativeObjects(String incomingMessagesQueueName) throws NamingException {
		// ConnectionFactory
		queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
		// Destination
		incomingMessagesQueue = (Queue) jndiContext.lookup(incomingMessagesQueueName);
		System.out.println("JMS administrative objects (ConnectionFactory, Destinations) initialized!");
	}
	
	private void initializeJmsClientObjects() throws JMSException {
		connection = queueConnectionFactory.createQueueConnection();
		session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); // false - non-transactional, AUTO_ACKNOWLEDGE - messages acknowledged after receive() method returns
		receiver = session.createReceiver(incomingMessagesQueue);
		System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
	}
	/************** Initialization END ******************************/
	
	
	
	
	/************** Business logic BEGIN ****************************/
	public void start() throws JMSException, IOException {
		connection.start();
		System.out.println("Connection started - receiving messages possible!");

		// Receive messages synchronously 
		while (true) {
			System.out.println("Waiting for a message to arrive...");
			// PUT YOUR CODE HERE
			
		}
	}
	
	public void stop() {
        // close the connection
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException exception) {
                exception.printStackTrace();
            }
        }
        
		// close the context
        if (jndiContext != null) {
            try {
            	jndiContext.close();
            } catch (NamingException exception) {
                exception.printStackTrace();
            }
        }
	}
	/************** Business logic END ****************************/
}
