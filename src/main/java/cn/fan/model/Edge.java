package cn.fan.model;

public class Edge {
	private String head;
	private String tail;

	public Edge(String head, String tail) {
		super();
		this.head = head;
		this.tail = tail;
	}

	public String getHead() {
		return head;
	}

	public String getTail() {
		return tail;
	}

	@Override
	public String toString() {
		return "Edge [head=" + head + ", tail=" + tail + "]";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof Edge) {
			Edge edge = (Edge) obj;
			if (edge.getHead().equals(head) && edge.getTail().equals(tail)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return head.hashCode() + tail.hashCode();
	}

}
