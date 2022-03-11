package hu.hl.disp_200916.core;

import java.util.TreeMap;

public class Trains extends TreeMap<Integer, Train> {
	private static final long serialVersionUID= 1L;
	public void put(int train_id, int target_rail_id, double l, double v_max, double a_p, double a_n) {
		super.put(train_id, new Train(train_id, target_rail_id, l, v_max, a_p, a_n));
	}
	public int getTargetItemId(int train_id) {
		return get(train_id).target_rail_id;
	}
	public double getL(int train_id) {
		return get(train_id).l;
	}
	public double getVMax(int train_id) {
		return get(train_id).v_max;
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

class Train {
	public final int id;
	public final int target_rail_id;	
	public final double l;
	public final double v_max;
	public final double a_p;
	public final double a_n;
	public Train(int id, int target_rail_id, double l, double v_max, double a_p, double a_n) {
		this.id= id;
		this.target_rail_id= target_rail_id;
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
		header.append("target_item_id\t"); body.append(target_rail_id+"\t");
		header.append("l\t"); body.append(l+"\t");
		header.append("v_max\t"); body.append(v_max+"\t");
		header.append("a_p\t"); body.append(a_p+"\t");
		header.append("a_n\t"); body.append(a_n+"\t");
		header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
		return header.toString()+body.toString();
	}
}