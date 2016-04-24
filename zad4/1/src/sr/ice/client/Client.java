// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.client;

import Airfield.AirportInfoPrx;
import Airfield.AirportInfoPrxHelper;
import Demo.*;
import Ice.AsyncResult;

public class Client 
{
	public static void main(String[] args) 
	{
		int status = 0;
		Ice.Communicator communicator = null;

		try {
			// 1. Inicjalizacja ICE
			communicator = Ice.Util.initialize(args);

			// 2. Uzyskanie referencji obiektu na podstawie linii w pliku konfiguracyjnym
			// Ice.ObjectPrx base = communicator.propertyToProxy("Calc1.Proxy");
			// 2. To samo co powy¿ej, ale mniej ³adnie
			Ice.ObjectPrx base1 = communicator.stringToProxy("calc/calc11:tcp -h localhost -p 10000:udp -h localhost -p 10000:ssl -h localhost -p 10001");
			Ice.ObjectPrx runwayBase = communicator.stringToProxy("runway/runway:tcp -h localhost -p 10000:udp -h localhost -p 10000:ssl -h localhost -p 10001");			
			
			// 3. Rzutowanie, zawê¿anie
			CalcPrx calc1 = CalcPrxHelper.checkedCast(base1);
			if (calc1 == null) throw new Error("Invalid proxy");
			
			// Runway
			
			AirportInfoPrx airport = AirportInfoPrxHelper.checkedCast(runwayBase);
			if (airport == null) throw new Error("Invalid airport info proxy");


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
					if (line.equals("add1"))
					{
						float r = calc1.add1(7, 8);
						System.out.println("RESULT (syn) = " + r);
					}
					if (line.equals("subtract"))
					{
						float r = calc1.subtract(7, 8);
						System.out.println("RESULT (syn) = " + r);
					}
					if (line.equals("aiportCode"))
					{
						String code = airport.getCode();
						System.out.println("Aiport code is " + code);
					}
					if (line.equals("getLoad"))
					{
						int load = airport.getLoad();
						System.out.println("Aiport load is " + load);
					}
					if (line.contains("setLoad"))
					{
						String [] split = line.split(" ");
						if (split.length > 1) {
							airport.setLoad(Integer.parseInt(split[1]));
						} else {
							System.err.println("Incorrect input");
						}
					}
					if(line.equals("add2 1"))
					{
						
					}
					if(line.equals("add2 2"))
					{
						
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