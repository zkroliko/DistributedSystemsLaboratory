package sr.ice.server;

import Adder.AdderInterface;

public class ServantNode {
	int id;
	AdderInterface adder;
	ServantNode prev;
	ServantNode next;
	
	public ServantNode(int id, AdderInterface adder) {
		this.id = id;
		this.adder = adder;
	}
}
