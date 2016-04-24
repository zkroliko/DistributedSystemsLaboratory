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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Airfield.AirportInfo;
import Ice.Current;

public class AirportInfoI extends AirportInfo
{
	private static final Logger logger = Logger.getLogger(AirportInfoI.class.getName());

	private static final long serialVersionUID = -2448962912780867770L;
    //private WorkQueue _workQueue;
	private final String csvFile = "runways.csv";
	
	private String code;
	
	private int length;
	    
    public AirportInfoI()
    {
        String line = "";
        try {
        	BufferedReader br = new BufferedReader(new FileReader(csvFile));
        	if ((line = br.readLine()) != null) {
        		String[] lineStr = line.split(" ");
        		if (lineStr.length > 1) {
            		code = lineStr[0];
            		length = Integer.parseInt(lineStr[1]);
        		}
        	}
        } catch (IOException e) {
        	// TODO
        }
    }

	@Override
	public String getCode(Current __current) {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public int getLoad(Current __current) {
		// TODO Auto-generated method stub
		return length;
	}

	
}

