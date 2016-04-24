// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.client;

import java.util.logging.Logger;

import Adder.AdderInterfacePrx;
import Adder.AdderInterfacePrxHelper;
import Airfield.AirportInfoPrx;
import Airfield.AirportInfoPrxHelper;
import Demo.*;
import Ice.AsyncResult;

public class Client 
{
	private static final Logger logger = Logger.getLogger(Client.class.getName());
	
	public static void main(String[] args) 
	{
		int status = 0;
		Ice.Communicator communicator = null;

		try {
			// 1. Inicjalizacja ICE
			communicator = Ice.Util.initialize(args);

			// 2. Uzyskanie referencji obiektu na podstawie linii w pliku konfiguracyjnym

			Ice.ObjectPrx airportProxy = communicator.stringToProxy("airportInfo/airport:tcp -h localhost -p 10000:udp -h localhost -p 10000:ssl -h localhost -p 10001");			
			Ice.ObjectPrx addProxy = communicator.stringToProxy("adder/add:tcp -h localhost -p 10000:udp -h localhost -p 10000:ssl -h localhost -p 10001");
			Ice.ObjectPrx addPoolProxy = communicator.stringToProxy("adderpool/addpool:tcp -h localhost -p 10000:udp -h localhost -p 10000:ssl -h localhost -p 10001");
			Ice.ObjectPrx addDeafultProxy = communicator.stringToProxy("adderDefault/addDefault:tcp -h localhost -p 10000:udp -h localhost -p 10000:ssl -h localhost -p 10001");

			// 3. Rzutowanie, zawê¿anie
			
			// Airport info
			
			AirportInfoPrx airport = AirportInfoPrxHelper.checkedCast(airportProxy);
			if (airport == null) throw new Error("Invalid airport info proxy");			

			AdderInterfacePrx add = AdderInterfacePrxHelper.checkedCast(addProxy);
			if (add == null) throw new Error("Invalid add proxy");			

			AdderInterfacePrx addpool = AdderInterfacePrxHelper.checkedCast(addPoolProxy);
			if (addpool == null) throw new Error("Invalid addPool proxy");			

			AdderInterfacePrx addDefault = AdderInterfacePrxHelper.checkedCast(addDeafultProxy);
			if (addDefault == null) throw new Error("Invalid addDefault proxy");		

			// 4. Wywolanie zdalnych operacji

			String line = null;
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
						
			AsyncResult ar = null;
						
			do
			{
				try
				{
					System.out.print("==> ");
					System.out.flush();
					line = in.readLine();

					if (line == null)
					{
						break;
					}
					else if (line.startsWith("getLoad"))
					{
						String [] split = line.split(" ");
						if (split.length > 1) {
							int load = airport.getLoad(split[1]);
							logger.info("Aiport load is " + load);
						} else {
							logger.warning("Incorrect input");
						}
					}
					else if (line.startsWith("addLoad"))
					{
						String [] split = line.split(" ");
						if (split.length > 2) {
							airport.addLoad(split[1], Integer.parseInt(split[2]));
							logger.info("New data added.");
						} else {
							logger.warning("Incorrect input");
						}
					}
					else if (line.startsWith("add")) {
						String[] arguments = line.split(" ");
						if (arguments.length > 2) {
							int a = Integer.parseInt(arguments[1]);
							int b = Integer.parseInt(arguments[2]);
							int result = add.add(a, b);
							logger.info("Result of addition " + result);
						} else {
							logger.warning("Incorrect input");
						}
					}
					else if (line.startsWith("pooladd")) {
						String[] arguments = line.split(" ");
						if (arguments.length > 2) {
							int a = Integer.parseInt(arguments[1]);
							int b = Integer.parseInt(arguments[2]);
							int result = addpool.add(a, b);
							logger.info("Result of addition with servant pool " + result);
						} else {
							logger.warning("Incorrect input");
						}
					}
					else if (line.startsWith("defaultadd")) {
						String[] arguments = line.split(" ");
						if (arguments.length > 2) {
							int a = Integer.parseInt(arguments[1]);
							int b = Integer.parseInt(arguments[2]);
							int result = addDefault.add(a, b);
							logger.info("Result of addition with default servant: " + result);
						} else {
							logger.warning("Incorrect input");
						}
					}
					else if (line.equals("x"))
					{
						// Nothing to do
					}
				}
				catch (java.io.IOException ex)
				{
					System.err.println(ex);
				}
			}
			while (!line.equals("x"));


		} catch (Ice.LocalException e) {
			e.printStackTrace();
			status = 1;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			status = 1;
		}
		if (communicator != null) {
			// Clean up
			//
			try {
				communicator.destroy();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				status = 1;
			}
		}
		System.exit(status);
	}

}