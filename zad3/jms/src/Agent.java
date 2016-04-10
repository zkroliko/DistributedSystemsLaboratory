import org.exolab.jms.client.JmsTopic;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public abstract class Agent {

    protected static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";
    protected static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
    protected static final String DEFAULT_TYPE = "+";

    // Type
    protected String type;

    // JMS Administrative objects
    protected ConnectionFactory connectionFactory;

    // Application JNDI context
    protected Context jndiContext;

    // Business Logic
    protected String agentName;

    public Agent() throws NamingException, JMSException, InvalidOperationException {
        this(DEFAULT_TYPE);
    }

    public Agent(String type) throws NamingException, JMSException, InvalidOperationException {
        this(type, DEFAULT_JMS_PROVIDER_URL);
    }

    public Agent(String type, String providerUrl) throws NamingException, JMSException, InvalidOperationException {
        this.agentName = "Agent" + new Random().nextInt();
        this.type = type;
        initializeJndiContext(providerUrl);
        initializeObjects();
    }

    protected void initializeJndiContext(String providerUrl) throws NamingException {
        // JNDI Context
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
        props.put(Context.PROVIDER_URL, providerUrl);
        jndiContext = new InitialContext(props);
        System.out.println("JNDI context initialized!");
    }

    protected void initializeObjects() throws NamingException, JMSException, InvalidOperationException {
        initializeAdministrativeObjects();
        initializeJmsClientObjects();
        initializeAdditionalComponents();
    }

    private void initializeAdministrativeObjects() throws NamingException {
        connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");

        // Destinations
        initializeTopics();
        initializeQueues();
        System.out.println("JMS administrative objects (ConnectionFactory, Destinations) initialized!");
    }

    protected abstract void initializeTopics();

    protected abstract void initializeQueues();

    protected abstract void initializeJmsClientObjects() throws JMSException;

    protected abstract void initializeAdditionalComponents() throws InvalidOperationException;

    // Business logic

    public abstract void start() throws JMSException, IOException;

    public void stop() {
        // close the context
        if (jndiContext != null) {
            try {
                jndiContext.close();
            } catch (NamingException exception) {
                exception.printStackTrace();
            }
        }
        closeConnection();
    }

    protected abstract void closeConnection();
}