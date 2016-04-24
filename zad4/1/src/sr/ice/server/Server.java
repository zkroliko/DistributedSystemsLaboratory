// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.server;


import Adder.AdderInterface;
import Ice.Identity;
import sr.ice.impl.AdderI;

public class Server
{
	public void t1(String[] args)
	{
		int status = 0;
		Ice.Communicator communicator = null;

		try
		{
			// 1. Inicjalizacja ICE
			communicator = Ice.Util.initialize(args);

			// 2. Konfiguracja adaptera

			// METODA 2 (niepolecana): Konfiguracja adaptera Adapter1 jest w kodzie �r�d�owym
			Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter1", "tcp -h localhost -p 10000:udp -h localhost -p 10000");

			// 3. Stworzenie serwant�w				    

			Ice.ServantLocator windLocator= new AirportInfoServantLocator("airport locator");
			adapter.addServantLocator(windLocator, "airportInfo");
			
			Ice.ServantLocator addLocator = new AddServantLocator("add locator");
			adapter.addServantLocator(addLocator, "adder");
			
			Ice.ServantLocator addPoolLocator = new AddPoolServantLocator("add pool locator", 100);
			adapter.addServantLocator(addPoolLocator, "adderpool");
			
			AdderInterface adderDefault = new AdderI(250);
			adapter.addDefaultServant(adderDefault, "adderDefault");			
					    

	        // 5. Aktywacja adaptera i przej�cie w p�tl� przetwarzania ��da�
			adapter.activate();
			System.out.println("Entering event processing loop...");
			communicator.waitForShutdown();
		}
		catch (Exception e)
		{
			System.err.println(e);
			status = 1;
		}
		if (communicator != null)
		{
			// Clean up
			try
			{
				communicator.destroy();
			}
			catch (Exception e)
			{
				System.err.println(e);
				status = 1;
			}
		}
		System.exit(status);
	}


	public static void main(String[] args)
	{
		Server app = new Server();
		app.t1(args);
	}
}
