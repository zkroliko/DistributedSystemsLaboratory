// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.server;


import Ice.Identity;
import sr.ice.impl.CalcI;
import sr.ice.impl.UserManagementI;

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

			// METODA 2 (niepolecana): Konfiguracja adaptera Adapter1 jest w kodzie Ÿród³owym
			Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter1", "tcp -h localhost -p 10000:udp -h localhost -p 10000");

			// 3. Stworzenie serwanta/serwantów
			
			CalcI calcServant1 = new CalcI();
			
		    UserManagementI umServant1 = new UserManagementI(adapter);		    

			Ice.ServantLocator windLocator= new ServantLocator1("locator1");
			adapter.addServantLocator(windLocator, "runway");
					    
			// 4. Dodanie wpisów do ASM
			adapter.add(calcServant1, new Identity("calc11", "calc"));

//	        adapter.add(windLocator.locate(), new Identity("um11", "users"));
	        

	        // 5. Aktywacja adaptera i przejœcie w pêtlê przetwarzania ¿¹dañ
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
