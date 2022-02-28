package hu.hl.disp_200916.core;

import java.util.TreeMap;

public class Train extends TreeMap<Integer, Train.Record> {
	private static final long serialVersionUID= 1L;
	public class Record {
		private final int id;
		private final int target_item_id;	
		private final double l;
		public final double v_max;
		public final double a_p;
		public final double a_n;
		public Record(int id, int target_item_id, double l, double v_max, double a_p, double a_n) {
			this.id= id;
			this.target_item_id= target_item_id;
			this.l= l;
			this.v_max= v_max;
			this.a_p= a_p;
			this.a_n= a_n;
		}
		public String toString() {
			StringBuilder header= new StringBuilder();
			StringBuilder body= new StringBuilder();
			header.setLength(0);
			header.append("id\t"); body.append(id+"\t");
			header.append("target_item_id\t"); body.append(target_item_id+"\t");
			header.append("l\t"); body.append(l+"\t");
			header.append("v_max\t"); body.append(v_max+"\t");
			header.append("a_p\t"); body.append(a_p+"\t");
			header.append("a_n\t"); body.append(a_n+"\t");
			header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
			return header.toString()+body.toString();
		}
	}
	public void put(int id, int target_item_id, double l, double v_max, double a_p, double a_n) {
		super.put(id, new Record(id, target_item_id, l, v_max, a_p, a_n));
	}
	public int getTargetItemId(int id) {
		return get(id).target_item_id;
	}
	public double getL(int id) {
		return get(id).l;
	}
	public double getVMax(int id) {
		return get(id).v_max;
	}	
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		entrySet().forEach(e -> {
			header.setLength(0);
			header.append(e.getValue().toString().split("\r")[0]+"\r"); body.append(e.getValue().toString().split("\r")[1]+"\r");
		});
		return header.toString()+body.toString();
	}
}