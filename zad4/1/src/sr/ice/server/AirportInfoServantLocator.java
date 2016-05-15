package sr.ice.server;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import sr.ice.impl.AirportInfoI;

import java.util.logging.Logger;

public class AirportInfoServantLocator implements Ice.ServantLocator 
{
	private static final Logger logger = Logger.getLogger(AirportInfoServantLocator.class.getName());

	private String id = null;
	private Object servant = null;
	
	public AirportInfoServantLocator(String id)
	{
		this.id = id;
		logger.info("## AirportInfoServantLocator(" + id + ") ##");
	}

	public AirportInfoServantLocator(String id, Ice.Object servant)
	{
		this.id = id;
		this.servant = servant;
		logger.info("## AirportInfoServantLocator(" + id + ", obj) ##");
	}

	public Object locate(Current curr, LocalObjectHolder cookie) throws UserException 
	{
	    // Check if we have instantiated a servant already.
	    //
	 	 
        // Instantiate a servant
    	
        servant = new AirportInfoI();
 
        // Add the servant to the ASM.
        //
        curr.adapter.add(servant, curr.id);

	    
	    logger.info("## AirportInfoServantLocator #" +id + " .locate() ##");
		
		return servant;
	}

	public void finished(Ice.Current curr, Ice.Object servant, java.lang.Object cookie) throws UserException 
	{
		logger.info("## AirportInfoServantLocator #" +id + " .finished() ##");
	}

	public void deactivate(String category)
	{
		logger.info("## ServantLocator #" +id + " .deactivate() ##");
	}
}
