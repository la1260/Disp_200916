package hu.hl.disp_200916.core;

import java.util.TreeMap;
import java.util.Vector;

/**Track nem lehet rövidebb 10m-nél.*/
public class Rails extends TreeMap<Integer, Rail> { 
	private static final long serialVersionUID= 1L;
	public enum Type{T, R, L, J};
	/** F: nem használt (fekete)<br/>R: fenntartott (zöld)<br/>M: mozgó vonat által elfoglalt (piros)<br/>S: álló vonat által elfoglalt (sárga)<br/>P: útvonal tervezés alatt (kék)*/
	public enum Status{F, R, M, S, P};
	private final DispCoreListener dispcorelistener;
	public Rails(DispCoreListener dispcorelistener) {
		this.dispcorelistener= dispcorelistener;
	}
	/** Rail léterhozása és hozzáadása a listához
	 * @param rail_id Azonosító
	 * @param type Típus
	 * @param l Rail Hossz
	 * @param next_a_rail_id Egyenes irányban, egyik vég felől csatlakozó Rail (ha nincs, -1)
	 * @param next_b_rail_id Egyenes irányban, másik vég felől csatlakozó Rail (ha nincs, -1)
	 * @param next_c_rail_id Kitérő irányban, egyik vég felől csatlakozó Rail (ha nincs, -1)
	 * @param next_d_rail_id Kitérő irányban, másik vég felől csatlakozó Rail (ha nincs, -1)
	 * @param v_max_0 Egyenes irányban max sebesség
	 * @param v_max_1 Kitérő irányban max sebesség
	 * @param x Szimbólum kezdőpontjának x koordinátája
	 * @param y Szimbólum kezdőpontjának y koordinátája
	 * @param width Szimbólum végpontjának távolsága x koordinátától
	 * @param height Szimbólum végpontjának távolsága y koordinátától
	 * @param name Beleírt scöveg
	 */
	public void put(int rail_id, Type type, double l, int next_a_rail_id, int next_b_rail_id, int next_c_rail_id, int next_d_rail_id, double v_max_0, double v_max_1, int x, int y, int width, int height, String name) {
		super.put(rail_id, new Rail(rail_id, type, l, next_a_rail_id, next_b_rail_id, next_c_rail_id, next_d_rail_id, v_max_0, v_max_1, x, y, width, height, name));
		setStatus(rail_id, Status.F);
	}
	//Statikus függvények (ugyanazokra a bemeneti értékekre mindig ugyanazt a visszatérési értéket fogja adni).
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
	public String getName(int rail_id) {
		return get(rail_id).name;
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
	/**A megadott Rail-hoz in_rail_id irányából történő behaladás esetén a kimeneti Rail_id-k visszaadása.<br/>in_rail_id= -1 esetén az első bekötetlen bemenet felől (Terminal esetén így kérdezhető le a kimenet).<br/>Nem csatlakozó bemenet esetén üres lista visszaadása.*/
	public Vector<Integer> getOutRailIds(int rail_id, int in_rail_id) {
		Vector<Integer> result= new Vector<Integer>(get(rail_id).next);
		if (getType(rail_id).equals(Type.L)) result.setSize(2);
		int index= result.indexOf(in_rail_id);
		if (-1<index && getType(rail_id).equals(Type.J)) result.removeElementAt(index^2);
		result.removeIf(i -> i==-1 || i==in_rail_id || index==-1);
		return result;
	}
	//Dinamikus függvények
	/**A megadott Rail User-jének azonosítója. J esetén a csomóponthoz csatlakozó összes Rail-t, L esetén a Rail-hoz harmadikként megadott (a Link-et keresztező másik Link) Rail-t vizsgálja. Ha nincs user, -1.*/
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
	/**A megadott Rail User-ének beállítása.*/
	public void setUser(int rail_id, int train_id) {
		get(rail_id).user= train_id;
		dispcorelistener.railUpdate(rail_id);
	}
	/**A megadott Rail státuszának (színének) lekérdezése.*/
	public Status getStatus(int rail_id) {
		return get(rail_id).status;
	}
	/**A megadott Rail státuszának (színének) beállítása.*/
	public void setStatus(int rail_id, Status status) {
		get(rail_id).status= status;
		dispcorelistener.railUpdate(rail_id);
	}
	/**Kiírandó scöveg. Track esetén ha létező az user, akkor az; ha nem, akkor a vágány név mezője. Nem Track esetén üres.*/
	public String getText(int rail_id) {
		int train_id= getUser(rail_id);
		return (getType(rail_id).equals(Type.R)) ? (train_id==-1) ? getName(rail_id) : String.valueOf(train_id) : "";		
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
	public final Vector<Integer> next= new Vector<Integer>();
	public final Vector<Double> v_max= new Vector<Double>();
	public int user= -1;
	public Rails.Status status;
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	public final String name;
	public Rail(int id, Rails.Type type, double l, int next_a_rail_id, int next_b_rail_id, int next_c_rail_id, int next_d_rail_id, double v_max_0, double v_max_1, int x, int y, int width, int height, String name) {
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
		this.name= name;
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
		header.append("stat\t"); body.append(status+"\t");
		header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
		return header.toString()+body.toString();
	}
}