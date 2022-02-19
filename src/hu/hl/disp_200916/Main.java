package hu.hl.disp_200916;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.NavigableMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {
	private Rail rail= new Rail();
	private Train train= new Train();
	private Route route= new Route(rail, train);
	private Section section= new Section(train, route); 
	public static long nextms(long currentms, long refms, int periodmonth) { 
		GregorianCalendar gc= new GregorianCalendar();
		gc.setTimeInMillis(refms);
		int rmm= gc.get(Calendar.MONTH)%periodmonth;
		int rdm= gc.get(Calendar.DATE);
		gc.setTimeInMillis(currentms);
		gc.add(Calendar.DATE, 1);
		while (gc.get(Calendar.MONTH)%periodmonth!=rmm || gc.get(Calendar.DATE)!=rdm) {
			gc.add(Calendar.DATE, 1);
		}
		return gc.getTimeInMillis();
	}
	public static void main(String[] args) {
		new Main();
/*		TreeMap<Long, String> tm= new TreeMap<Long, String>();
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTimeInMillis(), 2), "v��zsz��mla");
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.OCTOBER, 15).getTimeInMillis(), 1), "villany��ra, g��zsz��mla");
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.NOVEMBER, 4).getTimeInMillis(), 1), "g��z��ra, villanysz��mla");
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.NOVEMBER, 1).getTimeInMillis(), 1), "flipsz��mla");
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.NOVEMBER, 7).getTimeInMillis(), 3), "szem��td��j, biztos��t��s");
		System.out.println(
			tm
//			String.format("%ty-%1$tm-%1$td %1$tH:%1$tM:%1$tS", nextms)+"\t"
		); */
	}
	public Main() {
		rail.put(0, Rail.Type.J, 0, 6, 8, 9, null, 72/3.6, 36/3.6);
		rail.put(1, Rail.Type.J, 0, 7, 11, 10, null, 72/3.6, 36/3.6);
		rail.put(2, Rail.Type.J, 0, 8, 12, 10, null, 72/3.6, 36/3.6);
		rail.put(3, Rail.Type.J, 0, 11, 13, 9, 14, 72/3.6, 36/3.6);
		rail.put(4, Rail.Type.J, 0, 15, 16, null, null, 72/3.6, null);
		rail.put(5, Rail.Type.J, 0, 16, 17, null, null, 72/3.6, null);
		rail.put(6, Rail.Type.L, 25, 18, 0, null, null, null, null);
		rail.put(7, Rail.Type.L, 25, 19, 1, null, null, null, null);
		rail.put(8, Rail.Type.L, 25, 0, 2, null, null, null, null);
		rail.put(9, Rail.Type.L, 25, 0, 3, 10, null, null, null);
		rail.put(10, Rail.Type.L, 25, 1, 2, 9, null, null, null);
		rail.put(11, Rail.Type.L, 25, 1, 3, null, null, null, null);
		rail.put(12, Rail.Type.L, 25, 2, 20, null, null, null, null);
		rail.put(13, Rail.Type.L, 25, 3, 21, null, null, null, null);
		rail.put(14, Rail.Type.L, 25, 3, 24, null, null, null, null);
		rail.put(15, Rail.Type.L, 25, 24, 4, null, null, null, null);
		rail.put(16, Rail.Type.L, 25, 4, 5, null, null, null, null);
		rail.put(17, Rail.Type.L, 25, 5, 25, null, null, null, null);
		rail.put(18, Rail.Type.R, 500, 27, 6, null, null, 90/3.6, null);
		rail.put(19, Rail.Type.R, 500, 28, 7, null, null, 90/3.6, null);
		rail.put(20, Rail.Type.R, 500, 12, 22, null, null, 90/3.6, null);
		rail.put(21, Rail.Type.R, 500, 13, 23, null, null, 90/3.6, null);
		rail.put(22, Rail.Type.R, 500, 20, 29, null, null, 90/3.6, null);
		rail.put(23, Rail.Type.R, 500, 21, 30, null, null, 90/3.6, null);
		rail.put(24, Rail.Type.R, 500, 14, 15, null, null, 80/3.6, null);
		rail.put(25, Rail.Type.R, 500, 17, 26, null, null, 90/3.6, null);
		rail.put(26, Rail.Type.R, 500, 25, 31, null, null, 90/3.6, null);
		rail.put(27, Rail.Type.T, 0, 18, null, null, null, null, null);
		rail.put(28, Rail.Type.T, 0, 19, null, null, null, null, null);
		rail.put(29, Rail.Type.T, 0, 22, null, null, null, null, null);
		rail.put(30, Rail.Type.T, 0, 23, null, null, null, null, null);
		rail.put(31, Rail.Type.T, 0, 26, null, null, null, null, null);
		train.put(0, 27, 100, 90/3.6, 2.5, -5);
		train.put(1, 27, 100, 90/3.6, 2.5, -5);
		train.put(2, 27, 100, 90/3.6, 2.5, -5);
		route.put(0, 0, 27);
        route.put(0, 1, 18);
		route.put(0, 2, 6);
        route.put(0, 3, 0);
		route.put(0, 4, 9);
		route.put(0, 5, 3);
		route.put(0, 6, 14);
		route.put(0, 7, 24);
		route.put(0, 8, 24);
		route.put(0, 9, 24);
		route.put(0, 10, 15);
		route.put(0, 11, 4);
		route.put(0, 12, 16);
		route.put(0, 13, 5);
		route.put(0, 14, 17);
		route.put(0, 15, 25);
		route.put(0, 16, 26);
		route.put(0, 17, 26);
		route.put(0, 18, 25);
		route.put(0, 19, 25);
		route.put(0, 20, 26);
//		route.put(1, 0, 27);
//		route.put(1, 1, 18);
//		route.put(1, 2, 6);
//		route.put(1, 3, 0);
//		route.put(1, 4, 9);
//		route.put(1, 5, 3);
//		route.put(1, 6, 14);
//		route.put(1, 7, 24);
//		route.put(1, 8, 24);
//		route.put(1, 9, 24);
//		route.put(1, 10, 15);
//		route.put(1, 11, 4);
//		route.put(1, 12, 16);
//		route.put(1, 13, 5);
//		route.put(1, 14, 17);
//		route.put(1, 15, 25);
//		route.put(1, 16, 26);
//		route.put(1, 17, 26);
//		route.put(1, 18, 25);
//		route.put(1, 19, 25);
//		route.put(1, 20, 26);
//		route.put(1, 21, 27);
//		route.put(2, 0, 27);
//		route.put(2, 1, 18);
//		route.put(2, 2, 6);
//		route.put(2, 3, 0);
//		route.put(2, 4, 9);
//		route.put(2, 5, 3);
//		route.put(2, 6, 14);
//		route.put(2, 7, 24);
//		route.put(2, 8, 24);
//		route.put(2, 9, 24);
//		route.put(2, 10, 15);
//		route.put(2, 11, 4);
//		route.put(2, 12, 16);
//		route.put(2, 13, 5);
//		route.put(2, 14, 17);
//		route.put(2, 15, 25);
//		route.put(2, 16, 26);
//		route.put(2, 17, 26);
//		route.put(2, 18, 25);
//		route.put(2, 19, 25);
//		route.put(2, 20, 26);
//		route.put(2, 21, 31);

//		System.out.print("Rail:\r"+rail);
//		System.out.print("Train:\r"+train);
		
		System.out.print("Route:\r"+route);
//		
//		route.setUser(0, 0);
//		System.out.print("Route:\r"+route);
//		
//		route.setUser(0, 1);
//		System.out.print("Route:\r"+route);
//		
//		route.setUser(0, 9);
//		System.out.print("Route:\r"+route);
//		
//		route.setUser(0, 15);
//		System.out.print("Route:\r"+route);
		
//		section.put(0, 1, 0);

		double t= 5;
		while (t<200) {
			if ((
				section.getA(0, t+0.1)<0 ||
				section.getA(0, t+0.1)==0 && section.getV(0, t+0.1)==0
				) &&
				route.setUser(0, (section.getRouteRecID(0, t+0.1)==0) ? -1 : section.getRouteRecID(0, t+0.1))
				) {
				section.calc(0, t);
//				System.out.print("Section:\r"+section);
			}
			t+=0.1;
		}
		
/*		t= 147;
		System.out.print("t:"+t+"\r");
		System.out.print("a:"+section.getA(0, t)+"\r");
		System.out.print("v:"+section.getV(0, t)+"\r");
		System.out.print("p global:"+section.getP(0, t, Section.Ref.G)+"\r");
		System.out.print("p item:"+section.getP(0, t, Section.Ref.I)+"\r");
		
/*		double pg= 3000;
		System.out.print("pg:"+pg+"\r");
		System.out.print("section:"+section.get(Section.Index.P).get(0).floorEntry(pg).getValue()+"\r");
*/		
	}
}

class Rail extends TreeMap<Integer, Rail.Record> { //Track nem lehet rövidebb 10m-nél.
	private static final long serialVersionUID= 1L;
	public enum Type{T, R, L, J};	
	public class Record {
		private final int id;
		private final Type type;
		private final double l;
		public final int next_a_item_id;
		public final Integer next_b_item_id;
		public final Integer next_c_item_id;
		public final Integer next_d_item_id;
		private final Double v_max_0;
		private final Double v_max_1;
		private Integer user;
		public Record(int id, Type type, double l, int next_a_item_id, Integer next_b_item_id, Integer next_c_item_id, Integer next_d_item_id, Double v_max_0, Double v_max_1) {
			this.id= id;
			this.type= type;
			this.l= l;
			this.next_a_item_id= next_a_item_id;
			this.next_b_item_id= next_b_item_id;
			this.next_c_item_id= next_c_item_id;
			this.next_d_item_id= next_d_item_id;
			this.v_max_0= v_max_0;
			this.v_max_1= v_max_1;
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
	public void put(int id, Type type, double l, int next_a_item_id, Integer next_b_item_id, Integer next_c_item_id, Integer next_d_item_id, Double v_max_0, Double v_max_1) {
		super.put(id, new Record(id, type, l, next_a_item_id, next_b_item_id, next_c_item_id, next_d_item_id, v_max_0, v_max_1));
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
		Record item= get(id);
		Double result= (index==0) ? item.v_max_0 : item.v_max_1;
		return (result==null) ?  Double.POSITIVE_INFINITY : result;
	}
	public Integer getUser(int id) {
		Record item= get(id);		
		Integer result= item.user;
		switch (getType(id)) {
		case J:
			result= (result==null) ? get(item.next_a_item_id).user : result;
			result= (result==null && item.next_b_item_id!=null) ? get(item.next_b_item_id).user : result;
			result= (result==null && item.next_d_item_id!=null) ? get(item.next_d_item_id).user : result;
		case L:			
			result= (result==null && item.next_c_item_id!=null) ? get(item.next_c_item_id).user : result;
		default:
			return result;
		}
	}
	public void setUser(int id, int user) {
		get(id).user= user; 
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

class Train extends TreeMap<Integer, Train.Record> {
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

class Route extends TreeMap<Integer, TreeMap<Integer, Route.Record>> {
	private static final long serialVersionUID= 1L;
	public final Rail rail;
	private final Train train;
	public class Record {
		public final int train_id;
		public final int route_rec_id;
		public final int rail_id;
		public Record(int train_id, int train_no, int rail_id) {
			super();
			this.train_id= train_id;
			this.route_rec_id= train_no;
			this.rail_id= rail_id;
		}
		public String toString() {
			StringBuilder header= new StringBuilder();
			StringBuilder body= new StringBuilder();
			header.setLength(0);
			header.append("tid\t"); body.append(train_id+"\t");
			header.append("rri\t"); body.append(route_rec_id+"\t");
			header.append("rid\t"); body.append(rail_id+"\t");
			header.append("rtp\t"); body.append(rail.getType(rail_id)+"\t");
			header.append("usr\t"); body.append(rail.getUser(rail_id)+"\t");
			header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
			return header.toString()+body.toString();
		}	
	}
	public Route(Rail rail, Train train) {
		this.rail= rail;
		this.train= train;
	}
	private boolean isFirst(int train_id, int no) {
		return get(train_id).lowerKey(no)==null;
	}
	private boolean isPrevLast(int train_id, int no) {
		return !isLast(train_id, no) && get(train_id).higherKey(no+1)==null;
	}
	private boolean isLast(int train_id, int no) {
		return get(train_id).higherKey(no)==null;
	}
	public boolean isBeforeReversion(int train_id, int no) {
		return get(train_id).get(no+1)!=null && get(train_id).get(no).rail_id==get(train_id).get(no+1).rail_id;
	}
	public boolean isAfterReversion(int train_id, int no) {
		return get(train_id).get(no-1)!=null && get(train_id).get(no).rail_id==get(train_id).get(no-1).rail_id;
	}
	private boolean isOriginalDirection(int train_id, int no) {
		return ((get(train_id).entrySet().stream().filter(e -> e.getValue().route_rec_id<=no && isAfterReversion(train_id, e.getValue().route_rec_id)).count() & 1)==0) ? true : false;
	}
	private boolean isLastTerminal(int train_id) {
		return rail.getType(get(train_id).lastEntry().getValue().rail_id).equals(Rail.Type.T);
	}
	private boolean isLastTarget(int train_id) {
		return get(train_id).lastEntry().getValue().rail_id==train.getTargetItemId(train_id);
	}
	public Integer getNearestTrackNo(int train_id, int no) { //no-tól a legközelebbi track indexe (min. no+1)
		Optional<java.util.Map.Entry<Integer, Record>> o_nearest_track_rail_no= get(train_id).tailMap(no, false).entrySet().stream().filter(e -> rail.getType(e.getValue().rail_id).equals(Rail.Type.R)).findFirst();
		return (o_nearest_track_rail_no.isPresent()) ? o_nearest_track_rail_no.get().getKey() : null; //ha nincs no+1-en vagy utána track, akkor null
	}
	private Integer getUser(int train_id, int no) { //no+1-től a legközelebbi trackig levő itemek júzerei közül az első nem null értékű visszaadása
		Integer nearest_track_no= getNearestTrackNo(train_id, no);
		if (nearest_track_no!=null) {
			Optional<java.util.Map.Entry<Integer, Record>> o_user= get(train_id).subMap(no, false, nearest_track_no, true).entrySet().stream().filter(e -> rail.getUser(e.getValue().rail_id)!=null).findFirst();
			if (o_user.isPresent()) { //van-e no+1-en vagy utána item nem null júzerrel 
				return rail.getUser(o_user.get().getValue().rail_id);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public boolean setUser(int train_id, int no) { //no+1-től a legközelebbi trackig levő itemek júzerének train_id megadása, ha lehetséges
		Set<Entry<Integer, Record>> foglalando= get(train_id).subMap(no, false, getNearestTrackNo(train_id, no), true).entrySet();
		if (foglalando.stream().allMatch(e -> rail.getUser(e.getValue().rail_id)==null)) {
			foglalando.stream().forEach(e -> rail.setUser(e.getValue().rail_id, train_id));
			return true;
		} else {
			return false;
		}
	}
	public int getStopLevelOnItem(int train_id, int no) {  
		//az utolsó előttin vagyunk, az utolsó terminal és nem target 
		//az utolsón vagyunk, és az utolsó nem terminal  
		if (isPrevLast(train_id, no) && isLastTerminal(train_id) && !isLastTarget(train_id) || isLast(train_id, no) && !isLastTerminal(train_id)) {
			return 4; //a megadott itemről továbbhaladás csak útvonalmódosítás után lehetséges
		} else if (isBeforeReversion(train_id, no)) {
			return 3; //a megadott itemről továbbhaladás lefékezés és megfordulás után
		} else if (getUser(train_id, no)==null) {
			return 1; //a megadott itemről továbbhaladás csak az útvonal további részének foglalása után lehetséges (senki se használja)
		} else if (getUser(train_id, no)==train_id) {
			return 0; //a megadott itemről továbbhaladáshoz minden feltétel adott (nekem van lefoglalva)
		} else {
			return 2; //a megadott itemről továbbhaladás csak az útvonal további részének felszabadulása után lehetséges (valaki más használja)
		}
	}
	private double getPStart(int train_id, int no) {
		return (isAfterReversion(train_id, no)) ? 10+train.getL(train_id) : 0;
	}
	public double getPEnd(int train_id, int no) {
		return Math.max(rail.getL(get(train_id).get(no).rail_id)-((getStopLevelOnItem(train_id, no)==0) ? 0 : 10), 0);
	}
	public double getItemVMax(int train_id, int no) {
		Rail.Type type= rail.getType(get(train_id).get(no).rail_id);
		switch (type) {
		case T:
			if (isFirst(train_id, no) && !isLast(train_id, no)) {
				return rail.getVMax(get(train_id).get(no+1).rail_id, 0);
			} else if (!isFirst(train_id, no) && isLast(train_id, no)) {
				return rail.getVMax(get(train_id).get(no-1).rail_id, 0);
			}
		case R:
			return rail.getVMax(get(train_id).get(no).rail_id, 0);
		default:
			int d= 1;
			double result= rail.getVMax(get(train_id).get(no).rail_id, 0);
			boolean bl= !rail.getType(get(train_id).get(no-1).rail_id).equals(Rail.Type.R);
			boolean bf= !rail.getType(get(train_id).get(no+1).rail_id).equals(Rail.Type.R);
			while (bl || bf) {
				if (bl) {
					result= Math.min(result, rail.getVMax(get(train_id).get(no-d).rail_id, rail.getJunctionDir(get(train_id).get(no-d).rail_id, get(train_id).get(no-d-1).rail_id, get(train_id).get(no-d+1).rail_id))); 
					bl= !rail.getType(get(train_id).get(no-d-1).rail_id).equals(Rail.Type.R);
				}
				if (bf) {
					result= Math.min(result, rail.getVMax(get(train_id).get(no+d).rail_id, rail.getJunctionDir(get(train_id).get(no+d).rail_id, get(train_id).get(no+d-1).rail_id, get(train_id).get(no+d+1).rail_id)));
					bf= !rail.getType(get(train_id).get(no+d+1).rail_id).equals(Rail.Type.R);
				}
				d++;
			}
			return result;
		}
	}
	public void put(int train_id, int no, int rail_id) {
		if (!containsKey(train_id)) {
			put(train_id, new TreeMap<Integer, Route.Record>());
		}
		get(train_id).put(no, new Record(train_id, no, rail_id));
	}
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		entrySet().forEach(e_0 -> {
			e_0.getValue().entrySet().forEach(e_1 -> {
				header.setLength(0);
				header.append(e_1.getValue().toString().split("\r")[0]+"\t"); body.append(e_1.getValue().toString().split("\r")[1]+"\t");
				header.append("frs\t"); body.append(isFirst(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("pls\t"); body.append(isPrevLast(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("lst\t"); body.append(isLast(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("bfr\t"); body.append(isBeforeReversion(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("afr\t"); body.append(isAfterReversion(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("ord\t"); body.append(isOriginalDirection(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("ltm\t"); body.append(isLastTerminal(e_1.getValue().train_id)+"\t");
				header.append("ltg\t"); body.append(isLastTarget(e_1.getValue().train_id)+"\t");
				header.append("ntr\t"); body.append(getNearestTrackNo(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("sli\t"); body.append(getStopLevelOnItem(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("pst\t"); body.append(getPStart(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("pen\t"); body.append(getPEnd(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("ivm\t"); body.append(getItemVMax(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
			});
		});
		return header.toString()+body.toString();
	}	
}

class Section extends TreeMap<Section.Index, TreeMap<Integer, TreeMap<Double, Section.Record>>> {
	private static final long serialVersionUID= 1L;
	public enum Index {P, T}
	private final Train train;
	private final Route route;
	public class Record {
		public final int train_id;
		public final int route_rec_id;
		public double t;
		public double d;
		/** A Section pozíciója a vonat indulásához képest */
		public double pg;
		/**A Section pozíciója az Item-jének kezdetéhez képest */
		public double pi;
		public double l;
		public double a; 
		public double v;
		public Record(int train_id, int route_rec_id) {
			this.train_id= train_id;
			this.route_rec_id= route_rec_id;
		}
		public String toString() {
			StringBuilder header= new StringBuilder();
			StringBuilder body= new StringBuilder();
			header.setLength(0);
			header.append("tid\t"); body.append(train_id+"\t");
			header.append("rri\t"); body.append(route_rec_id+"\t");
			header.append("t\t"); body.append(t+"\t");
			header.append("d\t"); body.append(d+"\t");
			header.append("pg\t"); body.append(pg+"\t");
			header.append("pi\t"); body.append(pi+"\t");
			header.append("l\t"); body.append(l+"\t");
			header.append("a\t"); body.append(a+"\t");
			header.append("v\t"); body.append(v+"\t");
			header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
			return header.toString()+body.toString();
		}
	}
	public Section(Train train, Route route) {
		this.train= train;
		this.route= route;
		put(Index.P, new TreeMap<Integer, TreeMap<Double, Section.Record>>());
		put(Index.T, new TreeMap<Integer, TreeMap<Double, Section.Record>>());
	}
	/**
	 * A t-vel megadott pozíciótól a train_id számára lefoglalt item-ekre felveszi a section-okat. 
	 * @param train_id
	 * @param t
	 */
	public void calc(int train_id, double t) {
		if (!get(Index.T).containsKey(train_id)) {
			get(Index.T).put(train_id, new TreeMap<Double, Record>());
			get(Index.P).put(train_id, new TreeMap<Double, Record>());
		}
		int last_index=	route
		.get(train_id)
		.entrySet()
		.stream()
		.filter(e -> getRouteRecID(train_id, t)<=e.getKey() && route.rail.getUser(e.getValue().rail_id)!=null && route.rail.getUser(e.getValue().rail_id)==train_id)
		.max((e1, e2) -> Integer.compare(e1.getKey(), e2.getKey()))
		.get()
		.getKey();
		put(train_id, t, last_index, 0);
	}
	private double put(int train_id, double t, int route_rec_id, double vout) {
		vout= (route.isBeforeReversion(train_id, route_rec_id)) ? 0 : vout;
		double pi= (getRouteRecID(train_id, t)==route_rec_id) ? getP(train_id, t, Ref.I) : (route.isAfterReversion(train_id, route_rec_id)) ? 10+train.getL(train_id) : 0;
		double li= route.getPEnd(train_id, route_rec_id)-pi;
		Record r0= new Record(train_id, route_rec_id);
		Record r1= new Record(train_id, route_rec_id);
		Record r2= new Record(train_id, route_rec_id);
		r0.a= train.get(train_id).a_p;
		r1.a= 0;
		r2.a= train.get(train_id).a_n;
		r1.v= Math.min(train.getVMax(train_id), route.getItemVMax(train_id, route_rec_id));
		r1.v= Math.min(r1.v, Math.sqrt(vout*vout-2*r2.a*li));
		r0.v= (getRouteRecID(train_id, t)==route_rec_id) ? getV(train_id, t) : put(train_id, t, route_rec_id-1, r1.v);
		r1.v= Math.min(r1.v, Math.sqrt(r0.v*r0.v+2*r0.a*li));
		r2.v= r1.v= Math.min(r1.v, Math.sqrt((r0.v*r0.v*r2.a-vout*vout*r0.a+2*li*r0.a*r2.a)/(r2.a-r0.a)));
		vout= Math.min(r1.v, vout);
		r0.d= Math.max(0, (r1.v-r0.v)/r0.a);
		r2.d= Math.max(0, (vout-r1.v)/r2.a);
		r0.l= (r0.v+r1.v)*r0.d/2;
		r2.l= (vout+r1.v)*r2.d/2;
		r1.l= li-r0.l-r2.l;
		r1.d= r1.l/r1.v;
		r0.t= (getRouteRecID(train_id, t)==route_rec_id || get(Index.T).get(train_id).isEmpty()) ? t : get(Index.T).get(train_id).lastKey()+get(Index.T).get(train_id).lastEntry().getValue().d;
		r1.t= r0.t+r0.d;
		r2.t= r1.t+r1.d;
		r0.pg= getP(train_id, r0.t, Ref.G);
		r1.pg= r0.pg+r0.l;
		r2.pg= r1.pg+r1.l;
		r0.pi= pi;
		r1.pi= r0.pi+r0.l;
		r2.pi= r1.pi+r1.l;
		if (!get(Index.T).get(train_id).isEmpty() && getRouteRecID(train_id, t)==route_rec_id) {
			cut(train_id, t);
		}
		if (0<r0.d) {
			get(Index.T).get(train_id).put(r0.t, r0);
			get(Index.P).get(train_id).put(r0.pg, r0);
		}
		if (0<r1.d) {
			get(Index.T).get(train_id).put(r1.t, r1);
			get(Index.P).get(train_id).put(r1.pg, r1);
		}
		if (0<r2.d) {
			get(Index.T).get(train_id).put(r2.t, r2);
			get(Index.P).get(train_id).put(r2.pg, r2);
		}
		return vout;
	}
	private void cut(int train_id, double t) {
		Record record= get(Index.T).get(train_id).floorEntry(t).getValue();
		if (record.d<t-record.t) {
			Record record2= new Record(train_id, record.route_rec_id);
			record2.t= record.t+record.d;
			record2.d= t-record2.t;			
			record2.a= 0;
			record2.v= 0;			
			record2.l= 0;
			record2.pg= getP(train_id, t, Ref.G);
			record2.pi= getP(train_id, t, Ref.I);
			get(Index.T).get(train_id).put(record2.t, record2);
		} else {
			record.d= t-record.t;
			record.l= getP(train_id, t, Ref.G)-record.pg;
		}
		get(Index.T).get(train_id).entrySet().removeIf(e -> t<e.getKey());
		get(Index.P).get(train_id).entrySet().removeIf(e -> record.pg<e.getKey());
	}
	public double getA(int train_id, double t) {
		if (!get(Index.T).containsKey(train_id) || get(Index.T).get(train_id).isEmpty() || get(Index.T).get(train_id).floorKey(t)+get(Index.T).get(train_id).floorEntry(t).getValue().d<=t) {
			return 0;
		} else {
			Entry<Double, Record> e= get(Index.T).get(train_id).floorEntry(t);
			Record record= e.getValue();
			return record.a;
		}
	}
	public double getV(int train_id, double t) {
		if (!get(Index.T).containsKey(train_id) || get(Index.T).get(train_id).isEmpty() || get(Index.T).get(train_id).floorKey(t)+get(Index.T).get(train_id).floorEntry(t).getValue().d<=t) {
			return 0;
		} else {
			Entry<Double, Record> e= get(Index.T).get(train_id).floorEntry(t);
			Record record= e.getValue();
			return record.v+record.a*(t-e.getKey());
		}
	}
	public enum Ref{G, I}
	/**
	 * @param train_id A vonat azonosítója.
	 * @param t A vonat indulása óta eltelt idő.
	 * @param ref Mihez képest van megadva az eredmény: <b>G:</b> Global (a vonat indulásához képest) <b>I:</b> Item (a Section Item-jének kezdetéhez képest).
	 * @return A vonat poziciója a megadott időpontban.
	 */
	public double getP(int train_id, double t, Ref ref) {
		if (!get(Index.T).containsKey(train_id) || get(Index.T).get(train_id).isEmpty()) {
			return 0;
		} else {
			t= Math.min(t, get(Index.T).get(train_id).floorKey(t)+get(Index.T).get(train_id).floorEntry(t).getValue().d);
			Entry<Double, Record> e= get(Index.T).get(train_id).floorEntry(t);
			t-=e.getKey();
			Record record= e.getValue();
			double result= t*(record.v+t/2*record.a);
			if (ref.equals(Ref.G)) result+= record.pg;
			if (ref.equals(Ref.I)) result+= record.pi;
			return result;
		}
	}
	public int getRouteRecID(int train_id, double t) {
		if (!get(Index.T).containsKey(train_id) || get(Index.T).get(train_id).isEmpty()) {
			return 0;
		} else {
			return get(Index.T).get(train_id).floorEntry(t).getValue().route_rec_id;
		}
	}	
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		get(Index.T).entrySet().forEach(e_0 -> {
			e_0.getValue().entrySet().forEach(e_1 -> {
				header.setLength(0);
				header.append(e_1.getValue().toString().split("\r")[0]+"\t"); body.append(e_1.getValue().toString().split("\r")[1]+"\t");
				header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
			});
		});
		return (header.toString()+body.toString()).replace('.', ',') ;
	}
}

/* step (double dt):
 * Kikeressük az aktuális idő + dt szerinti gyorsulást;
 * Ha ez a gyorulás negatív
 *   akkor kikeressük azt a Section-t, ahol már nem negatív, és az ott lévő sebességet meghatározzuk,
 *   egyébként az aktuális idő + dt szerinti sebességet meghatározzuk;
 * Ha ez a sebesség 0 és foglalással feloldható (foglalással nem feloldhatónak számít a visszafordulás miatti megállás is!) 
 *   akkor foglalás, aktuális időnél calc (section elvágása, újraszámolás);
 * Aktuális idő + dt szerinti új aktuális idő, pozició, sebesség, gyorsulás meghatározása, régi pozíció-vonathossztól új pozíció-vonathosszig felszabadítás.
 */