import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

    public ExpSolver() throws NamingException, JMSException, InvalidOperationException {
        this.type = DEFAULT_TYPE;
    }

    public ExpSolver(String type) throws NamingException, JMSException, InvalidOperationException {
        this(type, DEFAULT_JMS_PROVIDER_URL);
    }

    public ExpSolver(String type, String providerUrl) throws NamingException, JMSException, InvalidOperationException {
        this.clientName = "Client " + new Random().nextInt();
        this.type = type;
        initializeJndiContext(providerUrl);
        initializeAdministrativeObjects(type);
        initializeJmsClientObjects();
        initializeOperation(type);
    }

    protected void initializeJndiContext(String providerUrl) throws NamingException {
        // JNDI Context
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
        props.put(Context.PROVIDER_URL, providerUrl);
        jndiContext = new InitialContext(props);
        System.out.println("JNDI context initialized!");
    }
}
