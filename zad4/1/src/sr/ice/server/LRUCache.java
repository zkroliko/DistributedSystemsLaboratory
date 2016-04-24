package sr.ice.server;

import java.util.HashMap;

import Adder.AdderInterface;

public class LRUCache {
	int capacity;
	HashMap<Integer, ServantNode> map = new HashMap<Integer, ServantNode>();
	ServantNode head = null;
	ServantNode end = null;
	
	public LRUCache(int capcity) {
		this.capacity = capacity;
	}
	
	public AdderInterface get() {
		if (head != null) {
			ServantNode res = map.get(end.id);
			System.out.println("get: " + res.id);
			remove(res);
			setHead(res);
			return res.adder;
		}
		return null;
	}
	
	public void setHead(ServantNode node) {
		node.next = head;
		node.prev = null;
		
		if (head != null) {
			head.prev = node;
		}
		
		head = node;
		
		if (end == null) end = head;
	}
	
	public void remove(ServantNode node) {
		if (node.prev != null) {
			node.prev.next = node.next;
		} else {
			head = node.next;
		}
		
		if (node.next != null) {
			node.next.prev = node.prev;
		} else {
			end = node.prev;
		}
	}
	
	public void set(int id, AdderInterface adder) {
		ServantNode created = new ServantNode(id, adder);
		setHead(created);
		map.put(id, created);
	}
}
