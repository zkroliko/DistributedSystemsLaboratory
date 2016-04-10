import org.exolab.jms.client.JmsQueue;
import org.exolab.jms.client.JmsTopic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.*;
import javax.naming.NamingException;

public class ExpProducer extends Agent {

	// JMS Administrative objects
    private Topic solverDiscovery;
    private Queue foundSolver;
    private Queue expressionSending;


	// JMS Client objects
	private TopicConnection topicConnection;
	private TopicSession discoverySession;
	private TopicPublisher publisher;

    private QueueConnection queueConnection;
    private QueueSession queueSession;
    private QueueSender sender;
    private QueueReceiver receiver;

    // Waiting for solver

	public ExpProducer() throws NamingException, JMSException, InvalidOperationException {
		super();
	}

	public ExpProducer(String type) throws NamingException, JMSException, InvalidOperationException {
		super(type);
	}

	public ExpProducer(String type, String providerUrl) throws NamingException, JMSException, InvalidOperationException {
		super(type,providerUrl);
	}


    @Override
    protected void initializeTopics() {
        solverDiscovery = new JmsTopic("findingSolvers" + type);
    }

    @Override
    protected void initializeQueues() {
        foundSolver = new JmsQueue(agentName+type);
    }

	protected void initializeJmsClientObjects() throws JMSException {
		topicConnection = ((TopicConnectionFactory)connectionFactory).createTopicConnection();
		discoverySession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        publisher = discoverySession.createPublisher(solverDiscovery);

        queueConnection = ((QueueConnectionFactory)connectionFactory).createQueueConnection();
        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        receiver = queueSession.createReceiver(foundSolver);

		System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
	}

    @Override
    protected void initializeAdditionalComponents() throws InvalidOperationException {

    }

    public void start() throws JMSException, IOException {

		topicConnection.start();
		System.out.println("Connection started - sending messages possible!");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Provide message to be sent or type !stop to stop the program");
		while (true) {
			System.out.print("Type message: ");
			String userInput = br.readLine();
			if ("!stop".equalsIgnoreCase(userInput)) {
				break;
			}
            try {
                Expression exp = createExpression(userInput);
                publishExpression(exp);
            } catch (InvalidInputException | InvalidOperationException e) {
                System.err.println(e.getMessage());
            }
        }
	}

    private Expression createExpression(String userInput) throws JMSException, InvalidInputException {
        Pattern p = Pattern.compile("(.+)([\\+|\\-|\\*|\\/])(.+)");
        Matcher m = p.matcher(userInput);
        if (m.matches()) {
            String[] tokens = {m.group(1),m.group(2),m.group(3)};
            System.out.println(String.format("Parsed expression: %s %s %s",tokens[0], tokens[2], tokens[1]));
            try {
                double first = Double.parseDouble(tokens[0]);
                double second = Double.parseDouble(tokens[2]);
                String op = tokens[1];
                return new Expression(first,second,op);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("The operands must be real numbers");
            }
        } else {
            throw new InvalidInputException("Malformed expression on input");
        }
    }

    private void publishExpression(Expression exp) throws JMSException, InvalidOperationException {
        // First we must find a solver
        notifySolvers();
        // Now a solver has been found
        if (exp.getOperation().equals(type)) {
            System.out.println("Sending expression");
            ObjectMessage msg = discoverySession.createObjectMessage();
            msg.setObject(exp);
            sender.send(msg);
            System.out.println("Expression sent to solver");
        } else {
            String msg = "Invalid operand in expression. Agent configured to operand " + type;
            throw new InvalidOperationException(msg);
        }
    }

    private void notifySolvers() throws JMSException {

        TextMessage msg = discoverySession.createTextMessage(foundSolver.getQueueName());

        boolean waitingForSolver = true;
        while (waitingForSolver) {
            publisher.publish(msg);
            System.out.println("Asked for a solver with: " + msg.getText());

            System.out.println("Waiting for a solver on queue: " + receiver.getQueue().getQueueName());
            System.out.println(receiver.getQueue());
            Message input = receiver.receive(1000);
            if (input instanceof TextMessage) {
                System.out.println("Found solver for " + type);
                System.out.println("Solver is: " + ((TextMessage) input).getText());

                expressionSending = new JmsQueue(((TextMessage) input).getText());
                sender = queueSession.createSender(expressionSending);
                waitingForSolver = false;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void closeConnection() {
        // close the topicConnection
        if (topicConnection != null) {
            try {
                topicConnection.close();
            } catch (JMSException exception) {
                exception.printStackTrace();
            }
        }
    }


}
