import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class NoteboardWriter {

	private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";
	private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
	private static final String DEFAULT_OUTGOING_MESSAGES_QUEUE_NAME = "queue1";

	// Application JNDI context
	private Context jndiContext;
	
	// JMS Administrative objects
	private QueueConnectionFactory queueConnectionFactory;
	private Queue outgoingMessagesQueue;
	
	// JMS Client objects
	private QueueConnection connection;
	private QueueSession session;
	private QueueSender sender;
	
	// Business Logic
	private String clientName;
	
	public NoteboardWriter() throws NamingException, JMSException {
		this("Client " + new Random().nextInt());
	}
	
	public NoteboardWriter(String clientName) throws NamingException, JMSException {
		this(clientName, DEFAULT_JMS_PROVIDER_URL, DEFAULT_OUTGOING_MESSAGES_QUEUE_NAME);
	}
	
	public NoteboardWriter(String clientName, String providerUrl, String outgoingMessagesQueueName) throws NamingException, JMSException {
		this.clientName = clientName;
		initializeJndiContext(providerUrl);
		initializeAdministrativeObjects(outgoingMessagesQueueName);
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

	private void initializeAdministrativeObjects(String outgoingMessagesQueueName) throws NamingException {
		// ConnectionFactory
		queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
		// Destination
		outgoingMessagesQueue = (Queue) jndiContext.lookup(outgoingMessagesQueueName);
		System.out.println("JMS administrative objects (ConnectionFactory, Destinations) initialized!");
	}
	
	private void initializeJmsClientObjects() throws JMSException {
		connection = queueConnectionFactory.createQueueConnection();
		session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		sender = session.createSender(outgoingMessagesQueue);
		System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
	}

	public void start() throws JMSException, IOException {
		connection.start();
		System.out.println("Connection started - sendind messages possible!");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Provide message to be sent or type !stop to stop the program");
		while (true) {
			System.out.print("Type message: ");
			String userInput = br.readLine();
			if ("!stop".equalsIgnoreCase(userInput)) {
				break;
			}
			
			// PUT YOUR CODE HERE
		}
	}
	
	public void stop() {
		// close the context
        if (jndiContext != null) {
            try {
            	jndiContext.close();
            } catch (NamingException exception) {
                exception.printStackTrace();
            }
        }

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
