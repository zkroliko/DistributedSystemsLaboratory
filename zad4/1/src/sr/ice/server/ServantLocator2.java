package sr.ice.server;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import Demo.*;



public class ServantLocator2 implements Ice.ServantLocator
{
	private String id = null;
	private Object servant = null;
	
	public ServantLocator2(String id)
	{
		this.id = id;
		System.out.println("## ServantLocator1(" + id + ") ##");
	}

	public ServantLocator2(String id, Ice.Object servant)
	{
		this.id = id;
		this.servant = servant;
		System.out.println("## ServantLocator1(" + id + ", obj) ##");
	}

	public Object locate(Current curr, LocalObjectHolder cookie) throws UserException 
	{
		System.out.println("## ServantLocator1 #" +id + " .locate() ##");

		//TODO
		
		return servant;
	}

	public void finished(Ice.Current curr, Ice.Object servant, java.lang.Object cookie) throws UserException 
	{
		System.out.println("## ServantLocator1 #" +id + " .finished() ##");
	}

	public void deactivate(String category)
	{
		System.out.println("## ServantLocator1 #" +id + " .deactivate() ##");
	}
}
