// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package sr.ice.impl;

import java.util.logging.Logger;
import Adder._AdderInterfaceDisp;
import Ice.Current;

public class AdderSlowI extends _AdderInterfaceDisp
{
	private static final Logger logger = Logger.getLogger(AdderSlowI.class.getName());
	private static final long serialVersionUID = -2448962912780867770L;
	private int id = 0;

    public AdderSlowI()
    {
        // stateless constructor
    }
    
    public AdderSlowI(int id) {
    	this.id = id;
    }


	@Override
	public int add(int a, int b, Current __current) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return a+b;
	}


	@Override
	public int getId(Current __current) {
		return this.id;
	}

}
