package hu.hl.disp_200916.core;

import java.util.TreeMap;
import java.util.Map.Entry;

public class Section extends TreeMap<Section.Index, TreeMap<Integer, TreeMap<Double, Section.Record>>> {
	private static final long serialVersionUID= 1L;
	/** Globális idő, vagy globális pozició alapján törtnéjen a lekérdezés */
	public enum Index {P, T}
	private final Trains train;
	private final Route route;
	private final DispCore dispcorelistener;
	public class Record {
		public final int train_id;
		public final int route_rec_id;
		public double t;
		public double d;
		/** A Section Record pozíciója a vonat indulásához képest */
		public double pg;
		/** A Section Record pozíciója az Item-jének a kezdetéhez képest */
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
	public Section(DispCore dispcorelistener, Trains train, Route route) {
		this.dispcorelistener= dispcorelistener;
		this.train= train;
		this.route= route;
		put(Index.P, new TreeMap<Integer, TreeMap<Double, Section.Record>>());
		put(Index.T, new TreeMap<Integer, TreeMap<Double, Section.Record>>());
	}
	/** A t-vel megadott pozíciótól a last_index-ig levő Rail Record-oknak megfelelően felveszi a Section Record-okat; a t-t követő korábbi Section Record-ok törlésre kerülnek.  
	 * @param train_id
	 * @param t
	 * @param last_index Route Record id
	 */
	public double step(double t0, double dt) {
		train.keySet().forEach(train_id -> {
			if (getA(train_id, t0+dt)<0 || getA(train_id, t0+dt)==0 && getV(train_id, t0+dt)==0) {
				int last_index= route.setUser(train_id, getRouteRecID(train_id, t0+dt));
				if (-1<last_index) {
					calc(train_id, t0, last_index);
				}
			}
		});
		int[] kihaladt_train_id= {-1};
		train.keySet().forEach(train_id -> {
			double pg= getP(train_id, t0, Section.Ref.G)-train.getL(train_id);
			if (0<pg && felszab(train_id, t0+dt, pg, getP(train_id, t0+dt, Section.Ref.G)-train.getL(train_id))) { //pg<0 - a vonat vége még be sem jött a pályára, tehát nincs mit felszabadítani.
				kihaladt_train_id[0]= train_id;
			}
		});
		if (-1<kihaladt_train_id[0]) {
			train.remove(kihaladt_train_id[0]);
			dispcorelistener.kihaladt(kihaladt_train_id[0]);
		}
		return t0+dt;
	}
	private void calc(int train_id, double t, int last_index) {
		if (!get(Index.T).containsKey(train_id)) {
			get(Index.T).put(train_id, new TreeMap<Double, Record>());
			get(Index.P).put(train_id, new TreeMap<Double, Record>());
		}
		put(train_id, t, last_index, (route.isKihaladas(train_id, last_index)) ? Math.min(train.getVMax(train_id), route.getItemVMax(train_id, last_index)) : 0);
	}
	private double put(int train_id, double t, int route_rec_id, double vout) {
		vout= (route.isBeforeReversion(train_id, route_rec_id)) ? 0 : vout;
		double pi= (getRouteRecID(train_id, t)==route_rec_id) ? getP(train_id, t, Ref.I) : route.getPStart(train_id, route_rec_id);
		double li= route.getPEnd(train_id, route_rec_id)-pi;
		Record r0= new Record(train_id, route_rec_id);
		Record r1= new Record(train_id, route_rec_id);
		Record r2= new Record(train_id, route_rec_id);
		r0.a= train.get(train_id).a_p;
		r1.a= 0;
		r2.a= train.get(train_id).a_n;
		r1.v= Math.min(train.getVMax(train_id), route.getItemVMax(train_id, route_rec_id));
		r1.v= Math.min(r1.v, Math.sqrt(vout*vout-2*r2.a*li));
		r0.v= (get(Index.T).get(train_id).isEmpty()) ? r1.v : (getRouteRecID(train_id, t)==route_rec_id) ? getV(train_id, t) : put(train_id, t, route_rec_id-1, r1.v);
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
	/** A léptetés előtti és utáni vonatvég pozíciók között levő Rail Record-ok felszabadítása. 
	 * @param train_id
	 * @param t Kihaladáskor ez alapján határozható meg, hogy az utolsó Rail Record-ot elhagyta-e a vonat vége.
	 * @param before_p A vonat elejének pozíciója léptetés előtt, innentől indul a felszabadítás.
	 * @param after_p A vonat elejének pozíciója léptetés után (a felszabadítás ennek a Rail Record-ját csak akkor érinti, ha a vonat teljesen kihaladt).
	 * @return True= a vontat kihaladt a pályáról.
	 */
	public boolean felszab(int train_id, double t, double before_p, double after_p) {
		int first_route_rec_id= get(Index.P).get(train_id).floorEntry(before_p).getValue().route_rec_id;
		int last_route_rec_id= get(Index.P).get(train_id).floorEntry(after_p).getValue().route_rec_id;
		return route.resetUser(train_id, first_route_rec_id, last_route_rec_id, route.isKihaladas(train_id, first_route_rec_id) && route.getPEnd(train_id, first_route_rec_id)+train.getL(train_id)<getP(train_id, t, Ref.I));
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
	 * @param ref Mihez képest van megadva az eredmény: <b>G:</b> Global (a vonat indulásához képest) <b>I:</b> Item (a Section Rail Record-jának kezdetéhez képest).
	 * @return A vonat poziciója a megadott időpontban.
	 */
	public double getP(int train_id, double t, Ref ref) {
		if (!get(Index.T).containsKey(train_id) || get(Index.T).get(train_id).isEmpty()) {
			return 0;
		} else {
			Record record= get(Index.T).get(train_id).floorEntry(t).getValue();
			t-= record.t;
			t= (route.isKihaladas(train_id, record.route_rec_id) || t<record.d) ? t : record.d; //változó sebességű szakasznál nem címezhet a tartam fölé; kihaladáskor viszont ez meg van engedve.  
			double result= t*(record.v+t*record.a/2);
			if (ref.equals(Ref.G)) result+= record.pg;
			if (ref.equals(Ref.I)) result+= record.pi;
			return result;
		}
	}
	private int getRouteRecID(int train_id, double t) {
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