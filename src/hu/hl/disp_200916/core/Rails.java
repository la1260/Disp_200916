package hu.hl.disp_200916.core;

import java.util.ArrayList;
import java.util.TreeMap;

public class Rails extends TreeMap<Integer, Rail> { //Track nem lehet rövidebb 10m-nél.
	private static final long serialVersionUID= 1L;
	public enum Type{T, R, L, J};	
	private final DispCore dispcorelistener;
	public Rails(DispCore dispcorelistener) {
		this.dispcorelistener= dispcorelistener;
	}
	public void put(int id, Type type, double l, int next_a_item_id, int next_b_item_id, int next_c_item_id, int next_d_item_id, double v_max_0, double v_max_1, int x, int y, int width, int height) {
		super.put(id, new Rail(id, type, l, next_a_item_id, next_b_item_id, next_c_item_id, next_d_item_id, v_max_0, v_max_1, x, y, width, height));
		dispcorelistener.update(id);
	}
	public Type getType(int id) {
		return get(id).type;
	}
	public double getL(int id) {
		return get(id).l;
	}
	public int getX(int id) {
		return get(id).x;
	}
	public int getY(int id) {
		return get(id).y;
	}
	public int getWidth(int id) {
		return get(id).width;
	}
	public int getHeight(int id) {
		return get(id).height;
	}
	public int getJunctionDir(int id, int prev_id, int next_id) {
		return ((get(id).next.indexOf(prev_id) | get(id).next.indexOf(next_id)) & 2)>>1;  
	}
	public double getVMax(int id, int junctiondir) {
		Rail record= get((getType(id).equals(Type.T)) ? get(id).next.get(0) : id);
		Double result= record.v_max.get(junctiondir);
		return (result==-1) ?  Double.POSITIVE_INFINITY : result;
	}
	public int getUser(int id) {
		Rail record= get(id);		
		Integer result= record.user;
		switch (getType(id)) {
		case J:
			result= (result==-1 && containsKey(record.next.get(0))) ? get(record.next.get(0)).user : result;
			result= (result==-1 && containsKey(record.next.get(1))) ? get(record.next.get(1)).user : result;
			result= (result==-1 && containsKey(record.next.get(3))) ? get(record.next.get(3)).user : result;
		case L:			
			result= (result==-1 && containsKey(record.next.get(2))) ? get(record.next.get(2)).user : result;
		default:
			return result;
		}		
	}
	public void setUser(int id, int user) {
		if (get(id).type.equals(Type.L) || get(id).type.equals(Type.R)) {
			get(id).user= user;
		}
		dispcorelistener.update(id);
	}
	public int getColor(int id) {
		return get(id).color;
	}
	public void setColor(int id, int color) {
		get(id).color= color;
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

class Rail {
	public final int id;
	public final Rails.Type type;
	public final double l;
	public final ArrayList<Integer> next= new ArrayList<Integer>();
	public final ArrayList<Double> v_max= new ArrayList<Double>();
	public int user= -1;
	public int color;
	public final int x;
	public final int y;
	public final int width;
	public final int height;		
	public Rail(int id, Rails.Type type, double l, int next_a_item_id, int next_b_item_id, int next_c_item_id, int next_d_item_id, double v_max_0, double v_max_1, int x, int y, int width, int height) {
		this.id= id;
		this.type= type;
		this.l= l;
		this.next.add(next_a_item_id);
		this.next.add(next_b_item_id);
		this.next.add(next_c_item_id);
		this.next.add(next_d_item_id);
		this.v_max.add(v_max_0);
		this.v_max.add(v_max_1);
		this.x= x;
		this.y= y;
		this.width= width;
		this.height= height;
	}
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		header.setLength(0);
		header.append("id\t"); body.append(id+"\t");
		header.append("type\t"); body.append(type+"\t");
		header.append("l\t"); body.append(l+"\t");
		header.append("next_a_item_id\t"); body.append(next.get(0)+"\t");
		header.append("next_b_item_id\t"); body.append(next.get(1)+"\t");
		header.append("next_c_item_id\t"); body.append(next.get(2)+"\t");
		header.append("next_d_item_id\t"); body.append(next.get(3)+"\t");
		header.append("v_max_0\t"); body.append(v_max.get(0)+"\t");
		header.append("v_max_1\t"); body.append(v_max.get(1)+"\t");
		header.append("user\t"); body.append(user+"\t");
		header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
		return header.toString()+body.toString();
	}
}