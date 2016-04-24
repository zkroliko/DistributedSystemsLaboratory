// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.logging.Logger;

import Airfield.AirportInfo;
import Ice.Current;

public class AirportInfoI extends AirportInfo
{
	private static final Logger logger = Logger.getLogger(AirportInfoI.class.getName());

	private static final long serialVersionUID = -2448962912780867770L;
    //private WorkQueue _workQueue;
	private final String csvFile = "airportInfo.csv";
	
	private String code;
	
	private int load;
	
    private Map<String, Integer> loads = new TreeMap<>();
	    
    public AirportInfoI()
    {
        String line = "";
        try {
        	BufferedReader br = new BufferedReader(new FileReader(csvFile));
        	while ((line = br.readLine()) != null) {
        		String[] lineStr = line.split(" ");
        		if (lineStr.length > 1) {
            		String code = lineStr[0];
            		int load = Integer.parseInt(lineStr[1]);
            		loads.put(code, load);
        		}
        	}
        	br.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

	@Override
	public int getLoad(String code, Current __current) {
		if (loads.containsKey(code)) {
			System.out.println(loads.get(code));
			return loads.get(code);
		} else {
			throw new NoSuchElementException("Missing value for code" + code);
		}
		
	}

	@Override
	public void addLoad(String code, int load, Current __current) {
		loads.put(code, load);
		save();		
	}
	
	private void save() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(csvFile, false));
			for (Entry<String, Integer> entry : loads.entrySet())
			{
				writer.write(String.format("%s %s\n", entry.getKey(), entry.getValue()));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

