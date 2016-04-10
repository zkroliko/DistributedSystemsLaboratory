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
        this.agentName = "Agent " + new Random().nextInt();
        this.type = type;
        initializeJndiContext(providerUrl);
        initializeObjects();
    }

    protected abstract void initializeObjects() throws NamingException, JMSException, InvalidOperationException;

    protected void initializeJndiContext(String providerUrl) throws NamingException {
        // JNDI Context
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
        props.put(Context.PROVIDER_URL, providerUrl);
        jndiContext = new InitialContext(props);
        System.out.println("JNDI context initialized!");
    }

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