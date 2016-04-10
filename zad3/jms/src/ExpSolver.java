import org.exolab.jms.client.JmsTopic;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class ExpSolver extends Agent {


    // Type
    private Applicable operation;

	// JMS Administrative objects
	private TopicConnectionFactory connectionFactory;
	private Topic inputTopic;
    private Topic outputTopic;

	// JMS Client objects
	private TopicConnection connection;
	private TopicSession session;
	private TopicPublisher publisher;
    private TopicSubscriber subscriber;

	public ExpSolver() throws NamingException, JMSException, InvalidOperationException {
		super();
	}

	public ExpSolver(String type) throws NamingException, JMSException, InvalidOperationException {
		super(type);
	}

	public ExpSolver(String type, String providerUrl) throws NamingException, JMSException, InvalidOperationException {
		super(type,providerUrl);
	}

    protected void initializeObjects() throws NamingException, JMSException, InvalidOperationException {
        initializeAdministrativeObjects(type);
        initializeJmsClientObjects();
        initializeOperation(type);
    }

	private void initializeAdministrativeObjects(String type) throws NamingException {
		// ConnectionFactory
		connectionFactory = (TopicConnectionFactory) jndiContext.lookup("ConnectionFactory");
		// Destination
//      inputTopic = (Topic) jndiContext.lookup("production" + type);
//      outputTopic = (Topic) jndiContext.lookup("distribution" + type);
		inputTopic = new JmsTopic("queries" + type);
        outputTopic = new JmsTopic("solutions" + type);
		System.out.println("JMS administrative objects (ConnectionFactory, Destinations) initialized!");
	}
	
	private void initializeJmsClientObjects() throws JMSException {
		connection = connectionFactory.createTopicConnection();
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        publisher = session.createPublisher(outputTopic);
        subscriber = session.createSubscriber(inputTopic);
        System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
    }

    private void initializeOperation(String type) throws InvalidOperationException {
        switch (type) {
            case "+":
                this.operation = ((first, second) -> first + second);
                break;
            case "-":
                this.operation = ((first, second) -> first - second);
                break;
            case "*":
                this.operation = ((first, second) -> first * second);
                break;
            case "/":
                this.operation = ((first, second) -> first + Double.max(java.lang.Math.ulp(second),second));
                break;
            default:
                throw new InvalidOperationException("Operator " + type + " not supported");
        }
    }

    public void start() throws JMSException, IOException {

        subscriber.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("Message received");
                if (message instanceof ObjectMessage) {
                    try {
                        Expression exp = (Expression) ((ObjectMessage)message).getObject();
                        System.out.println("Received expression: " + exp);
                        // Solving
                        solveExpression(exp);
                        System.out.println("Expression solved: " + exp);
                        // Publishing
                        ObjectMessage msg = session.createObjectMessage();
                        msg.setObject(exp);
                        publisher.publish(msg);
                        System.out.println("Expression published!");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    } catch (InvalidOperationException e) {
                        System.err.println(e.getMessage() + " : Ignoring!");
                    }
                }
            }
        });

        connection.start();
        String msg = String.format("Connection started - solving messages of type %s possible!", type);
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

    public void solveExpression(Expression exp) throws InvalidOperationException {
        if (exp.getOperation().equals(type))
            exp.solve(this.operation);
        else {
            String msg = String.format("Required type is %s and %s was provided.", exp.getOperation(), type);
            throw new InvalidOperationException(msg);
        }
    }

}
