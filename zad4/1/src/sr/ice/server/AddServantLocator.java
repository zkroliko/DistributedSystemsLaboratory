package sr.ice.server;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import sr.ice.impl.AdderI;

import java.util.logging.Level;
import java.util.logging.Logger;

import Adder.AdderInterface;
import Demo.*;


public class AddServantLocator implements Ice.ServantLocator
{
	private static final Logger logger = Logger.getLogger(AddServantLocator.class.getName());
	
	private String id = null;
	
	public AddServantLocator(String id)
	{
		this.id = id;
	}

	public Object locate(Current curr, LocalObjectHolder cookie) throws UserException 
	{		
		// instantiate new Adder servant
		AdderInterface adder = new AdderI();
		logger.info("New adder created " + adder.ice_id());
		
		return adder;
	}

	public void finished(Ice.Current curr, Ice.Object servant, java.lang.Object cookie) throws UserException 
	{
		logger.entering(this.getClass().getName(), "finished");
		
		// do nothing
	}

	public void deactivate(String category)
	{
		logger.info("## ServantLocator #" +id + " .deactivate() ##");
	}
}