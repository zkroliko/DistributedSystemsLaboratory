package sr.ice.server;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import sr.ice.impl.AdderI;

import java.util.logging.Level;
import java.util.logging.Logger;

import Adder.AdderInterface;


public class AddPoolServantLocator implements Ice.ServantLocator
{
	private static final Logger logger = Logger.getLogger(AddPoolServantLocator.class.getName());
	private String id = null;
	private LRUCache cache = new LRUCache(100);
	
	public AddPoolServantLocator(String id, int n)
	{
		this.id = id;
		// Creating servant instances
		for (int i = 0; i < n; i++) {
			cache.set(i, new AdderI());
		}
	}

	public Object locate(Current curr, LocalObjectHolder cookie) throws UserException 
	{
		AdderInterface o = cache.get();
		logger.fine("Getting servant from cache " + (o.getId()));
		
		return o;
	}

	public void finished(Ice.Current curr, Ice.Object servant, java.lang.Object cookie) throws UserException 
	{
		logger.entering(this.getClass().getName(), "finished");
	}

	public void deactivate(String category)
	{
		logger.info("## ServantLocator #" +id + " .deactivate() ##");

	}
}