package hu.hl.disp_200916.core;

import java.util.ArrayList;
import java.util.TreeMap;

/**Track nem lehet rövidebb 10m-nél.*/
public class Rails extends TreeMap<Integer, Rail> { 
	private static final long serialVersionUID= 1L;
	public enum Type{T, R, L, J};	
	private final DispCore dispcorelistener;
	public Rails(DispCore dispcorelistener) {
		this.dispcorelistener= dispcorelistener;
	}
	public void put(int rail_id, Type type, double l, int next_a_rail_id, int next_b_rail_id, int next_c_rail_id, int next_d_rail_id, double v_max_0, double v_max_1, int x, int y, int width, int height) {
		super.put(rail_id, new Rail(rail_id, type, l, next_a_rail_id, next_b_rail_id, next_c_rail_id, next_d_rail_id, v_max_0, v_max_1, x, y, width, height));
		dispcorelistener.update(rail_id);
	}
	//Statikus függvények (visszatérési értéküket külső tényező nem befolyásolja).
	public Type getType(int rail_id) {
		return get(rail_id).type;
	}
	public double getL(int rail_id) {
		return get(rail_id).l;
	}
	public int getX(int rail_id) {
		return get(rail_id).x;
	}
	public int getY(int rail_id) {
		return get(rail_id).y;
	}
	public int getWidth(int rail_id) {
		return get(rail_id).width;
	}
	public int getHeight(int rail_id) {
		return get(rail_id).height;
	}
	/**Megadja, hogy a megadott Rail-on 0 (egyenes) vagy 1 (kitérő) irányú-e az áthaladás - az előző és a következő Rail-ok alapján.*/
	public int getJunctionDir(int rail_id, int prev_rail_id, int next_rail_id) {
		return ((get(rail_id).next.indexOf(prev_rail_id) | get(rail_id).next.indexOf(next_rail_id)) & 2)>>1;  
	}
	/**Visszatérési értéke a megadott Rail-on a max. áthaladási sebesség (J esetén az áthaladási iránytól függő, T esetén a csatlakozó Rail-é).*/
	public double getVMax(int rail_id, int junction_dir) {
		Rail record= get((getType(rail_id).equals(Type.T)) ? get(rail_id).next.get(0) : rail_id);
		Double result= record.v_max.get(junction_dir);
		return (result==-1) ?  Double.POSITIVE_INFINITY : result;
	}
	//Dinamikus függvények
	/**A megadott Rail User-jének értéke. J esetén a csomóponthoz csatlakozó összes Rail-t, L esetén a Rail-hoz harmadikként megadott (a Link-et keresztező másik Link) Rail-t vizsgálja.*/
	public int getUser(int rail_id) {
		Rail rail= get(rail_id);		
		Integer result= rail.user;
		switch (getType(rail_id)) {
		case J:
			result= (result==-1 && containsKey(rail.next.get(0))) ? get(rail.next.get(0)).user : result;
			result= (result==-1 && containsKey(rail.next.get(1))) ? get(rail.next.get(1)).user : result;
			result= (result==-1 && containsKey(rail.next.get(3))) ? get(rail.next.get(3)).user : result;
		case L:			
			result= (result==-1 && containsKey(rail.next.get(2))) ? get(rail.next.get(2)).user : result;
		default:
			return result;
		}		
	}
	/**A megadott Rail User-ének beállítása. T és J tipusok esetén nem kerül beállításra; mert ezeket (0 méretük miatt) a felszabadítás nem szabadítaná fel.*/
	public void setUser(int rail_id, int train_id) {
		if (getType(rail_id).equals(Type.L) || getType(rail_id).equals(Type.R)) {
			get(rail_id).user= train_id;
		}
		dispcorelistener.update(rail_id);
	}
	public int getColor(int rail_id) {
		return get(rail_id).color;
	}
	public void setColor(int rail_id, int color) {
		get(rail_id).color= color;
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
	public Rail(int id, Rails.Type type, double l, int next_a_rail_id, int next_b_rail_id, int next_c_rail_id, int next_d_rail_id, double v_max_0, double v_max_1, int x, int y, int width, int height) {
		this.id= id;
		this.type= type;
		this.l= l;
		this.next.add(next_a_rail_id);
		this.next.add(next_b_rail_id);
		this.next.add(next_c_rail_id);
		this.next.add(next_d_rail_id);
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