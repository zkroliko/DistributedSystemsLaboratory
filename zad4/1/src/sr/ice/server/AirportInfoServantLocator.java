package sr.ice.server;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import sr.ice.impl.AirportInfoI;
import Airfield.AirportInfo;
import Demo.*;



public class AirportInfoServantLocator implements Ice.ServantLocator
{
	private String id = null;
	private Object servant = null;
	
	public AirportInfoServantLocator(String id)
	{
		this.id = id;
		System.out.println("## AirportInfoServantLocator(" + id + ") ##");
	}

	public AirportInfoServantLocator(String id, Ice.Object servant)
	{
		this.id = id;
		this.servant = servant;
		System.out.println("## AirportInfoServantLocator(" + id + ", obj) ##");
	}

	public Object locate(Current curr, LocalObjectHolder cookie) throws UserException 
	{
	    // Check if we have instantiated a servant already.
	    //
	    Ice.Object servant = curr.adapter.find(curr.id);
	 
	    if (servant == null) { // We don't have a servant already
	 
	        // Instantiate a servant
	    	
	        servant = new AirportInfoI();
	 
	        // Add the servant to the ASM.
	        //
	        curr.adapter.add(servant, curr.id);
	    }
	    
		System.out.println("## AirportInfoServantLocator #" +id + " .locate() ##");
		
		return servant;
	}

	public void finished(Ice.Current curr, Ice.Object servant, java.lang.Object cookie) throws UserException 
	{
		System.out.println("## AirportInfoServantLocator #" +id + " .finished() ##");
	}

	public void deactivate(String category)
	{
		System.out.println("## ServantLocator1 #" +id + " .deactivate() ##");
	}
}
