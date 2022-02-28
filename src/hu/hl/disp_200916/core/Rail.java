package hu.hl.disp_200916.core;

import java.util.TreeMap;

public class Rail extends TreeMap<Integer, Record> { //Track nem lehet rövidebb 10m-nél.
	private static final long serialVersionUID= 1L;
	public enum Type{T, R, L, J};	
	private final DispCore dispcorelistener;
	public Rail(DispCore dispcorelistener) {
		this.dispcorelistener= dispcorelistener;
	}
	public void put(int id, Type type, double l, int next_a_item_id, Integer next_b_item_id, Integer next_c_item_id, Integer next_d_item_id, Double v_max_0, Double v_max_1, int x, int y, int width, int height) {
		super.put(id, new Record(id, type, l, next_a_item_id, next_b_item_id, next_c_item_id, next_d_item_id, v_max_0, v_max_1, x, y, width, height));
		dispcorelistener.update(id);
	}
	public Type getType(int id) {
		return get(id).type;
	}
	public double getL(int id) {
		return get(id).l;
	}
	public int getJunctionDir(int id, int prev_id, int next_id) {
		return (get(id).next_a_item_id==prev_id && get(id).next_b_item_id==next_id || get(id).next_a_item_id==next_id && get(id).next_b_item_id==prev_id) ? 0 : 1;  
	}
	public double getVMax(int id, int index) {
		if (getType(id).equals(Type.T)) id= get(id).next_a_item_id;
		Record item= get(id);
		Double result= (index==0) ? item.v_max_0 : item.v_max_1;
		return (result==null) ?  Double.POSITIVE_INFINITY : result;
	}
	public int getUser(int id) {
		Record item= get(id);		
		Integer result= item.user;
		switch (getType(id)) {
		case J:
			result= (result==-1) ? get(item.next_a_item_id).user : result;
			result= (result==-1 && item.next_b_item_id!=null) ? get(item.next_b_item_id).user : result;
			result= (result==-1 && item.next_d_item_id!=null) ? get(item.next_d_item_id).user : result;
		case L:			
			result= (result==-1 && item.next_c_item_id!=null) ? get(item.next_c_item_id).user : result;
		default:
			return result;
		}
	}
	public void setUser(int id, int user) {
		if (get(id).type.equals(Type.L) || get(id).type.equals(Type.R)) {
			get(id).setUser(user);
		}
		dispcorelistener.update(id);
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

class Record {
	public final int id;
	public final Rail.Type type;
	public final double l;
	public final int next_a_item_id;
	public final Integer next_b_item_id;
	public final Integer next_c_item_id;
	public final Integer next_d_item_id;
	public final Double v_max_0;
	public final Double v_max_1;
	public int user= -1;
	public int color;
	public final int x;
	public final int y;
	public final int width;
	public final int height;		
	public Record(int id, Rail.Type type, double l, int next_a_item_id, Integer next_b_item_id, Integer next_c_item_id, Integer next_d_item_id, Double v_max_0, Double v_max_1, int x, int y, int width, int height) {
		this.id= id;
		this.type= type;
		this.l= l;
		this.next_a_item_id= next_a_item_id;
		this.next_b_item_id= next_b_item_id;
		this.next_c_item_id= next_c_item_id;
		this.next_d_item_id= next_d_item_id;
		this.v_max_0= v_max_0;
		this.v_max_1= v_max_1;
		this.x= x;
		this.y= y;
		this.width= width;
		this.height= height;
	}
	public int getColor() {
		return color;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user= user;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		header.setLength(0);
		header.append("id\t"); body.append(id+"\t");
		header.append("type\t"); body.append(type+"\t");
		header.append("l\t"); body.append(l+"\t");
		header.append("next_a_item_id\t"); body.append(next_a_item_id+"\t");
		header.append("next_b_item_id\t"); body.append(next_b_item_id+"\t");
		header.append("next_c_item_id\t"); body.append(next_c_item_id+"\t");
		header.append("next_d_item_id\t"); body.append(next_d_item_id+"\t");
		header.append("v_max_0\t"); body.append(v_max_0+"\t");
		header.append("v_max_1\t"); body.append(v_max_1+"\t");
		header.append("user\t"); body.append(user+"\t");
		header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
		return header.toString()+body.toString();
	}
}