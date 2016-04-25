// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.impl;

import java.io.IOException;
import java.util.logging.Logger;
import Adder._AdderInterfaceDisp;
import Ice.Current;
import Pi.PiCalculator;
import Pi._PiCalculatorDisp;
import sr.ice.pi.PiDigitsGenerator;

public class PiI extends _PiCalculatorDisp
{
	private static final Logger logger = Logger.getLogger(PiI.class.getName());
	private static final long serialVersionUID = -2448962912780867770L;
	private int id = 0;
	
	private PiDigitsGenerator generator;

    public PiI()
    {
        generator = new PiDigitsGenerator();
    }
    
    public PiI(int id) {
    	this();
    	this.id = id;
    }

	public byte getDigit(int number, Current __current) {
		try {
			return (byte) generator.getDecimalDigit(number);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
