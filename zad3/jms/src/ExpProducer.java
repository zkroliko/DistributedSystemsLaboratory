import org.exolab.jms.client.JmsTopic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ExpProducer {

	private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";
	private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
	private static final String DEFAULT_TYPE = "+";

    // Type
    private String type;

	// Application JNDI context
	private Context jndiContext;
	
	// JMS Administrative objects
	private TopicConnectionFactory connectionFactory;
    private Topic outputTopic;
	
	// JMS Client objects
	private TopicConnection connection;
	private TopicSession session;
	private TopicPublisher publisher;
	
	// Business Logic
	private String clientName;
	
	public ExpProducer() throws NamingException, JMSException {
		this("Client " + new Random().nextInt());
	}
	
	public ExpProducer(String clientName) throws NamingException, JMSException {
		this(clientName, DEFAULT_JMS_PROVIDER_URL, DEFAULT_TYPE);
	}
	
	public ExpProducer(String clientName, String providerUrl, String type) throws NamingException, JMSException {
		this.clientName = clientName;
		initializeJndiContext(providerUrl);
		initializeAdministrativeObjects(type);
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

	private void initializeAdministrativeObjects(String type) throws NamingException {
		// ConnectionFactory
		connectionFactory = (TopicConnectionFactory) jndiContext.lookup("ConnectionFactory");
		// Destination
        //outputTopic = (Topic) jndiContext.lookup("production" + type);
        outputTopic = new JmsTopic("queries" + type);
		System.out.println("JMS administrative objects (ConnectionFactory, Destinations) initialized!");
	}
	
	private void initializeJmsClientObjects() throws JMSException {
		connection = connectionFactory.createTopicConnection();
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		publisher = session.createPublisher(outputTopic);
		System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
	}

	public void start() throws JMSException, IOException {

		connection.start();
		System.out.println("Connection started - sending messages possible!");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Provide message to be sent or type !stop to stop the program");
		while (true) {
			System.out.print("Type message: ");
			String userInput = br.readLine();
			if ("!stop".equalsIgnoreCase(userInput)) {
				break;
			}
            sendMessage(userInput);
		}
	}

    private void sendMessage(String userInput) throws JMSException {
        Pattern p = Pattern.compile("(.+)([\\+|\\-|\\*|\\/])(.+)");
        Matcher m = p.matcher(userInput);
        if (m.matches()) {
            String[] tokens = {m.group(1),m.group(2),m.group(3)};
            System.out.println(String.format("Parsed expression: %s %s %s",tokens[0], tokens[2], tokens[1]));
            try {
                double first = Double.parseDouble(tokens[0]);
                double second = Double.parseDouble(tokens[2]);
                String op = tokens[1];
                Expression expr = new Expression(first,second,op);
                ObjectMessage msg = session.createObjectMessage();
                msg.setObject(expr);
                publisher.publish(msg);
                System.out.println("Query published");
            } catch (NumberFormatException e) {
                System.err.println("The operands must be real numbers");
            }
        } else {
            System.err.println("Malformed expression on input");
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
