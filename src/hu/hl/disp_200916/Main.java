package hu.hl.disp_200916;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Main {
	private Rail rail= new Rail();
	private Train train= new Train();
	private Route route= new Route(rail, train);
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
		rail.put(0, Rail.Type.J, 0, 6, 8, 9, null, 80d, 40d);
		rail.put(1, Rail.Type.J, 0, 7, 11, 10, null, 80d, 40d);
		rail.put(2, Rail.Type.J, 0, 8, 12, 10, null, 80d, 40d);
		rail.put(3, Rail.Type.J, 0, 11, 13, 9, 14, 80d, 40d);
		rail.put(4, Rail.Type.J, 0, 15, 16, null, null, 80d, null);
		rail.put(5, Rail.Type.J, 0, 16, 17, null, null, 80d, null);
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
		rail.put(18, Rail.Type.R, 500, 27, 6, null, null, 100d, null);
		rail.put(19, Rail.Type.R, 500, 28, 7, null, null, 100d, null);
		rail.put(20, Rail.Type.R, 500, 12, 22, null, null, 100d, null);
		rail.put(21, Rail.Type.R, 500, 13, 23, null, null, 100d, null);
		rail.put(22, Rail.Type.R, 500, 20, 29, null, null, 100d, null);
		rail.put(23, Rail.Type.R, 500, 21, 30, null, null, 100d, null);
		rail.put(24, Rail.Type.R, 500, 14, 15, null, null, 100d, null);
		rail.put(25, Rail.Type.R, 500, 17, 26, null, null, 100d, null);
		rail.put(26, Rail.Type.R, 500, 25, 31, null, null, 100d, null);
		rail.put(27, Rail.Type.T, 0, 18, null, null, null, null, null);
		rail.put(28, Rail.Type.T, 0, 19, null, null, null, null, null);
		rail.put(29, Rail.Type.T, 0, 22, null, null, null, null, null);
		rail.put(30, Rail.Type.T, 0, 23, null, null, null, null, null);
		rail.put(31, Rail.Type.T, 0, 26, null, null, null, null, null);
		train.put(0, 27, 100, 90, 80, -160);
		train.put(1, 27, 100, 90, 80, -160);
		train.put(2, 27, 100, 90, 80, -160);
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
		route.put(1, 0, 27);
		route.put(1, 1, 18);
		route.put(1, 2, 6);
		route.put(1, 3, 0);
		route.put(1, 4, 9);
		route.put(1, 5, 3);
		route.put(1, 6, 14);
		route.put(1, 7, 24);
		route.put(1, 8, 24);
		route.put(1, 9, 24);
		route.put(1, 10, 15);
		route.put(1, 11, 4);
		route.put(1, 12, 16);
		route.put(1, 13, 5);
		route.put(1, 14, 17);
		route.put(1, 15, 25);
		route.put(1, 16, 26);
		route.put(1, 17, 26);
		route.put(1, 18, 25);
		route.put(1, 19, 25);
		route.put(1, 20, 26);
		route.put(1, 21, 27);
		route.put(2, 0, 27);
		route.put(2, 1, 18);
		route.put(2, 2, 6);
		route.put(2, 3, 0);
		route.put(2, 4, 9);
		route.put(2, 5, 3);
		route.put(2, 6, 14);
		route.put(2, 7, 24);
		route.put(2, 8, 24);
		route.put(2, 9, 24);
		route.put(2, 10, 15);
		route.put(2, 11, 4);
		route.put(2, 12, 16);
		route.put(2, 13, 5);
		route.put(2, 14, 17);
		route.put(2, 15, 25);
		route.put(2, 16, 26);
		route.put(2, 17, 26);
		route.put(2, 18, 25);
		route.put(2, 19, 25);
		route.put(2, 20, 26);
		route.put(2, 21, 31);
		rail.setUser(24, 56);
		System.out.print("Rail:\r"+rail);
		System.out.print("Train:\r"+train);
		System.out.print("Route:\r"+route);
   	}
}

class Rail extends TreeMap<Integer, Rail.Record> {
	private static final long serialVersionUID = 1L;
	public enum Type{T, R, L, J};	
	public class Record {
		private final int id;
		private final Type type;
		private final double l;
		private final int next_a_item_id;
		private final Integer next_b_item_id;
		private final Integer next_c_item_id;
		private final Integer next_d_item_id;
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
	public double getVMax(int id, int index) {
		Record item= get(id);
		Double result= (index==0) ? item.v_max_0 : item.v_max_1;
		return (result==null) ?  Double.POSITIVE_INFINITY : result;
	}
	public Integer getUser(int id) {
		return get(id).user; 
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
	private static final long serialVersionUID = 1L;
	public class Record {
		private final int id;
		private final int target_item_id;	
		private final double l;
		private final double v_max;
		private final double a_p;
		private final double a_n;
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
	private static final long serialVersionUID = 1L;
	private final Rail rail;
	private final Train train;
	public class Record {
		private final int train_id;
		private final int train_no;
		private final int rail_id;
		private double v_in;
		private double v_max;
		public Record(int train_id, int train_no, int rail_id) {
			super();
			this.train_id= train_id;
			this.train_no= train_no;
			this.rail_id= rail_id;
		}
		public String toString() {
			StringBuilder header= new StringBuilder();
			StringBuilder body= new StringBuilder();
			header.setLength(0);
			header.append("tid\t"); body.append(train_id+"\t");
			header.append("tno\t"); body.append(train_no+"\t");
			header.append("rid\t"); body.append(rail_id+"\t");
//			header.append("v_in\t"); body.append(v_in+"\t");
//			header.append("v_max\t"); body.append(v_max+"\t");
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
	private boolean isBeforeReversion(int train_id, int no) {
		return get(train_id).get(no+1)!=null && get(train_id).get(no).rail_id==get(train_id).get(no+1).rail_id;
	}
	private boolean isAfterReversion(int train_id, int no) {
		return get(train_id).get(no-1)!=null && get(train_id).get(no).rail_id==get(train_id).get(no-1).rail_id;
	}
	private boolean isOriginalDirection(int train_id, int no) {
		return ((get(train_id).entrySet().stream().filter(e -> e.getValue().train_no<=no && isAfterReversion(train_id, e.getValue().train_no)).count() & 1)==0) ? true : false;
	}
	private boolean isLastTerminal(int train_id) {
		return rail.getType(get(train_id).lastEntry().getValue().rail_id).equals(Rail.Type.T);
	}
	private boolean isLastTarget(int train_id) {
		return get(train_id).lastEntry().getValue().rail_id==train.getTargetItemId(train_id);
	}
	private Integer getNearestTrackNo(int train_id, int no) { //no+1-től a legközelebbi track indexe
		Optional<java.util.Map.Entry<Integer, Record>> o_nearest_track_rail_no= get(train_id).tailMap(no, false).entrySet().stream().filter(e -> rail.getType(e.getValue().rail_id).equals(Rail.Type.R)).findFirst();
		if (o_nearest_track_rail_no.isPresent()) { //van-e no+1-en vagy utána track
			return o_nearest_track_rail_no.get().getKey();
		} else {
			return null; 
		}
	}
	private Integer getUser(int train_id, int no) { //no+1-től a legközelebbi trackig levő itemek júzerének kikeresése
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
	private int getStopLevelOnItem(int train_id, int no) {  
		TreeMap<Integer, Route.Record> route= get(train_id);
		//az utolsó előttin vagyunk, az utolsó terminal és nem target 
		//az utolsón vagyunk, és az utolsó nem terminal  
		if (isPrevLast(train_id, no) && isLastTerminal(train_id) && !isLastTarget(train_id) || isLast(train_id, no) && !isLastTerminal(train_id)) {
			return 4; //a megadott itemről továbbhaladás csak útvonalmódosítás után lehtséges
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
	private double getPEnd(int train_id, int no) {
		return rail.getL(get(train_id).get(no).rail_id)-((getStopLevelOnItem(train_id, no)==0) ? 0 : 10);
	}
	private double getItemVMax(int train_id, int no) {
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
					result= Math.min(result, rail.getVMax(get(train_id).get(no-d).rail_id, 0));
					bl= !rail.getType(get(train_id).get(no-d-1).rail_id).equals(Rail.Type.R);
				}
				if (bf) {
					result= Math.min(result, rail.getVMax(get(train_id).get(no+d).rail_id, 0));
					bf= !rail.getType(get(train_id).get(no+d+1).rail_id).equals(Rail.Type.R);
				}
				d++;
			}
			return result;
		}
/*		System.out.println(train_id+"\t"+no+"\t"+end_no);
		return Math.min(result, train.getVMax(train_id));
		boolean top_level= nos==null;
		if (top_level) {
			nos= new HashSet<Integer>();
		}
		if (!nos.contains(no)) {
			nos.add(no);
			v_maxs.add(
				rail.getVMax(get(train_id).get(no-1).rail_id, get(train_id).get(no).rail_id, get(train_id).get(no+1).rail_id));
			Rail.Type item_type= rail.getType(get(train_id).get(no).rail_id);
			if (item_type.equals(Rail.Type.J) || item_type.equals(Rail.Type.L)) {
				Rail.Type prev_item_type= rail.getType(get(train_id).get(no-1).rail_id);
				if (prev_item_type.equals(Rail.Type.J) || prev_item_type.equals(Rail.Type.L)) {
					getItemVMax(train_id, no-1, nos);
				}
				Rail.Type next_item_type= rail.getType(get(train_id).get(no+1).rail_id);
				if (next_item_type.equals(Rail.Type.J) || next_item_type.equals(Rail.Type.L)) {
					getItemVMax(train_id, no+1, nos);
				}
			}
		}
		if (top_level) {
			System.out.println(no+"\t"+nos);
			no= nos.stream().min((n0, n1) -> {
				return Double.compare(
					rail.getVMax(get(train_id).get(n0-1).rail_id, get(train_id).get(n0).rail_id, get(train_id).get(n0+1).rail_id),
					rail.getVMax(get(train_id).get(n1-1).rail_id, get(train_id).get(n1).rail_id, get(train_id).get(n1+1).rail_id)
				);
			}).get();
			System.out.println(no);
		return 1;//Math.min(train.getVMax(train_id), 1); //rail.getVMax(get(train_id).get(no-1).rail_id, get(train_id).get(no).rail_id, get(train_id).get(no+1).rail_id));
		} else {
			return Double.NaN;
		} */
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
				header.append("frs\t"); body.append(isFirst(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("pls\t"); body.append(isPrevLast(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("lst\t"); body.append(isLast(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("bfr\t"); body.append(isBeforeReversion(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("afr\t"); body.append(isAfterReversion(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("ord\t"); body.append(isOriginalDirection(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("ltm\t"); body.append(isLastTerminal(e_1.getValue().train_id)+"\t");
				header.append("ltg\t"); body.append(isLastTarget(e_1.getValue().train_id)+"\t");
				header.append("sli\t"); body.append(getStopLevelOnItem(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("pst\t"); body.append(getPStart(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("pen\t"); body.append(getPEnd(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.append("ivm\t"); body.append(getItemVMax(e_1.getValue().train_id, e_1.getValue().train_no)+"\t");
				header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
			});
		});
		return header.toString()+body.toString();
	}	
}