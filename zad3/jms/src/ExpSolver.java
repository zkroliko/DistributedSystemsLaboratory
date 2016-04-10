import org.exolab.jms.client.JmsTopic;
import org.exolab.jms.config.*;

import javax.jms.*;
import javax.jms.TopicConnectionFactory;
import javax.naming.NamingException;
import java.io.IOException;

public class ExpSolver extends Agent {

    // Type
    private Applicable operation;

	// JMS Administrative objects
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

    @Override
    protected void initializeTopics() {
        inputTopic = new JmsTopic("queries" + type);
        outputTopic = new JmsTopic("solutions" + type);
    }

    @Override
    protected void initializeQueues() {

    }

	protected void initializeJmsClientObjects() throws JMSException {
		connection = ((TopicConnectionFactory)connectionFactory).createTopicConnection();
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        publisher = session.createPublisher(outputTopic);
        subscriber = session.createSubscriber(inputTopic);
        System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
    }

    @Override
    protected void initializeAdditionalComponents() throws InvalidOperationException {
        initializeOperation();
    }


    private void initializeOperation() throws InvalidOperationException {
        switch (this.type) {
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
                this.operation = ((first, second) -> first / second);
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
