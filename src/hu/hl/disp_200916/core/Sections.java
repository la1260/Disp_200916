package hu.hl.disp_200916.core;

import java.util.TreeMap;

import java.util.Map.Entry;

public class Sections extends TreeMap<SectionRecordKey, SectionRecordValue> {
	private static final long serialVersionUID= 1L;
	private final Trains train;
	private final Routes route;
	/**A lista kétféle indexelésének (globális idő vagy globális pozició) kiválasztása.*/
	public static enum Field {T, P}
	private final DispCore dispcorelistener;
	public Sections(DispCore dispcorelistener, Trains train, Routes route) {
		this.dispcorelistener= dispcorelistener;
		this.train= train;
		this.route= route;
	}
	/**Megadja, hogy a train_id-nek van-e rekordja a listában.*/
	private boolean containsTrainId(int train_id) {
		return keySet().stream().anyMatch(sectionrecordkey -> sectionrecordkey.train_id==train_id);
	}
	/**Megadja a Train utolsó, listából kinyerhető időpontját.*/
	private double getMaxT(int train_id) {
		SectionRecordValue sectionrecordvalue= entrySet().stream().filter(e0 -> e0.getKey().train_id==train_id && e0.getKey().field.equals(Field.T)).max((e1, e2) -> Double.compare(e1.getKey().value, e2.getKey().value)).get().getValue();
		return sectionrecordvalue.t+sectionrecordvalue.d;
	}
	/**1. Minden vonatra t0-hoz képest dt-vel későbbi időpontban ellenőrzi a vonat gyorsulását, sebességét. Lassulás, állás esetén foglalni próbál, sikeres foglalás esetén átszámolja a Section-okat.<br/>2. Minden vonatra, az új t0+dt időpontbeli állapot alapján felszabadítja a meghaladott Rail-okat. A kihaladt vonatokat törli a listából, és meghívja a listener "passout" metódusát a kihaladt vonat azonosítójával. t0+dt értékével tér vissza.*/
	public double step(double t0, double dt) {
		train.keySet().forEach(train_id -> {
			if (getA(train_id, t0+dt)<0 || getA(train_id, t0+dt)==0 && getV(train_id, t0+dt)==0) {
				int last_index= route.setUser(train_id, getRouteRecordNo(train_id, t0+dt));
				if (-1<last_index) {
					calc(train_id, t0, last_index);
				}
			}
		});
		int[] kihaladt_train_id= {-1};
		train.keySet().forEach(train_id -> {
			double pg= getPG(train_id, t0)-train.getL(train_id);
			if (0<pg && setFree(train_id, t0+dt, pg, getPG(train_id, t0+dt)-train.getL(train_id))) { //pg<0 - a vonat vége még be sem jött a pályára, tehát nincs mit felszabadítani.
				kihaladt_train_id[0]= train_id;
			}
		});
		if (-1<kihaladt_train_id[0]) {
			train.remove(kihaladt_train_id[0]);
			dispcorelistener.passout(t0+dt, kihaladt_train_id[0]);
		}
		return t0+dt;
	}
	/**A megadott vonat t-beli állapotánál elvágja annak Section-jait; last_route_record_no-ig levő Route rekordokra felveszi a Section-okat.*/
	private void calc(int train_id, double t, int last_route_record_no) {
		put(train_id, t, last_route_record_no, (route.isPassOut(train_id, last_route_record_no)) ? Math.min(train.getVMax(train_id), route.getRailVMax(train_id, last_route_record_no)) : 0);
	}
	/**Ua. mint calc; annak egy elemi lépése, egy Route rekordra, rekurzívan hiwa a megelőző rekordokat, azoknak vin-t átatdva és azoktól vout-jukat visszakapva.*/
	private double put(int train_id, double t, int route_rec_id, double vout) {
		vout= (route.isBeforeReversion(train_id, route_rec_id)) ? 0 : vout;
		double pi= (getRouteRecordNo(train_id, t)==route_rec_id) ? getPI(train_id, t) : route.getPStart(train_id, route_rec_id);
		double li= route.getPEnd(train_id, route_rec_id)-pi;
		SectionRecordValue r0= new SectionRecordValue(train_id, route_rec_id);
		SectionRecordValue r1= new SectionRecordValue(train_id, route_rec_id);
		SectionRecordValue r2= new SectionRecordValue(train_id, route_rec_id);
		r0.a= train.get(train_id).a_p;
		r1.a= 0;
		r2.a= train.get(train_id).a_n;
		r1.v= Math.min(train.getVMax(train_id), route.getRailVMax(train_id, route_rec_id));
		r1.v= Math.min(r1.v, Math.sqrt(vout*vout-2*r2.a*li));
		r0.v= (!containsTrainId(train_id)) ? r1.v : (getRouteRecordNo(train_id, t)==route_rec_id) ? getV(train_id, t) : put(train_id, t, route_rec_id-1, r1.v);
		r1.v= Math.min(r1.v, Math.sqrt(r0.v*r0.v+2*r0.a*li));
		r2.v= r1.v= Math.min(r1.v, Math.sqrt((r0.v*r0.v*r2.a-vout*vout*r0.a+2*li*r0.a*r2.a)/(r2.a-r0.a)));
		vout= Math.min(r1.v, vout);
		r0.d= Math.max(0, (r1.v-r0.v)/r0.a);
		r2.d= Math.max(0, (vout-r1.v)/r2.a);
		r0.l= (r0.v+r1.v)*r0.d/2;
		r2.l= (vout+r1.v)*r2.d/2;
		r1.l= li-r0.l-r2.l;
		r1.d= r1.l/r1.v;
		r0.t= (getRouteRecordNo(train_id, t)==route_rec_id || !containsTrainId(train_id)) ? t : getMaxT(train_id);
		r1.t= r0.t+r0.d;
		r2.t= r1.t+r1.d;
		r0.pg= getPG(train_id, r0.t);
		r1.pg= r0.pg+r0.l;
		r2.pg= r1.pg+r1.l;
		r0.pi= pi;
		r1.pi= r0.pi+r0.l;
		r2.pi= r1.pi+r1.l;
		if (containsTrainId(train_id) && getRouteRecordNo(train_id, t)==route_rec_id) {
			cut(train_id, t);
		}
		if (0<r0.d) {
			put(new SectionRecordKey(train_id, Field.T, r0.t), r0);
			put(new SectionRecordKey(train_id, Field.P, r0.pg), r0);
		}
		if (0<r1.d) {
			put(new SectionRecordKey(train_id, Field.T, r1.t), r1);
			put(new SectionRecordKey(train_id, Field.P, r1.pg), r1);
		}
		if (0<r2.d) {
			put(new SectionRecordKey(train_id, Field.T, r2.t), r2);
			put(new SectionRecordKey(train_id, Field.P, r2.pg), r2);
		}
		return vout;
	}
	/**Elvágja a Train t időpontbeli sectionját, és törli az utána levőket.*/
	private void cut(int train_id, double t) {
		SectionRecordValue record= floorEntry(new SectionRecordKey(train_id, Field.T, t)).getValue();
		if (record.d<t-record.t) {
			SectionRecordValue record2= new SectionRecordValue(train_id, record.route_record_no);
			record2.t= record.t+record.d;
			record2.d= t-record2.t;			
			record2.a= 0;
			record2.v= 0;			
			record2.l= 0;
			record2.pg= getPG(train_id, t);
			record2.pi= getPI(train_id, t);
			put(new SectionRecordKey(train_id, Field.T, record2.t), record2); 
		} else {
			record.d= t-record.t;
			record.l= getPG(train_id, t)-record.pg;
		}
		entrySet().removeIf(e -> e.getKey().train_id==train_id && e.getKey().field.equals(Field.T) && t<e.getKey().value);
		entrySet().removeIf(e -> e.getKey().train_id==train_id && e.getKey().field.equals(Field.P) && record.pg<e.getKey().value);
	}
	/** A léptetés előtti és utáni vonatvég globál pozíciói között levő Rail Record-ok felszabadítása, kihaladáskor t alapján a kihaladás befejeztének megállapítása.*/ 
	public boolean setFree(int train_id, double t, double before_p, double after_p) {
		int first_route_rec_id= floorEntry(new SectionRecordKey(train_id, Field.P, before_p)).getValue().route_record_no;
		int last_route_rec_id= floorEntry(new SectionRecordKey(train_id, Field.P, after_p)).getValue().route_record_no;
		return route.resetUser(train_id, first_route_rec_id, last_route_rec_id, route.isPassOut(train_id, first_route_rec_id) && route.getPEnd(train_id, first_route_rec_id)+train.getL(train_id)<getPI(train_id, t));
	}
	/**A vonat gyorsulása t időpontban. Ha a t túlmutat az utolsó Section vég(idő)pontján vagy a vonat nem szerepel a listában, akkor visszatérési érték 0.*/
	public double getA(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		SectionRecordValue record;
		if (!containsTrainId(train_id) || e==null || (record= e.getValue()).t+record.d<=t) {
			return 0;
		} else {
			return record.a;
		}
	}
	/**A vonat sebessége t időpontban. Ha a t túlmutat az utolsó Section vég(idő)pontján miközben nem kihaladás zajlik, vagy a vonat nem szerepel a listában, akkor visszatérési érték 0.*/
	public double getV(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		SectionRecordValue record;
		if (!containsTrainId(train_id) || e==null || (record= e.getValue()).t+record.d<=t && !route.isPassOut(train_id, record.route_record_no)) {
			return 0;
		} else {
			return record.v+record.a*(t-e.getKey().value);
		}
	}
	/**A vonat poziciója t időpontban, a vonat indulásához képest. A listában nem szereplő vonat esetén a visszatérési érték 0.*/
	public double getPG(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		if (!containsTrainId(train_id) || e==null) {
			return 0;
		} else {
			SectionRecordValue record= e.getValue();
			t-= record.t;
			t= (route.isPassOut(train_id, record.route_record_no) || t<record.d) ? t : record.d; //változó sebességű szakasznál nem címezhet a tartam fölé; kihaladáskor viszont ez meg van engedve.  
			return t*(record.v+t*record.a/2)+record.pg;
		}
	}
	/**A vonat poziciója t időpontban, a Section Rail-jának a vonat mögötti kezdőpontjához képest. A listában nem szereplő vonat esetén a visszatérési érték 0.*/
	public double getPI(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		if (!containsTrainId(train_id) || e==null) {
			return 0;
		} else {
			SectionRecordValue record= e.getValue();
			t-= record.t;
			t= (route.isPassOut(train_id, record.route_record_no) || t<record.d) ? t : record.d; //változó sebességű szakasznál nem címezhet a tartam fölé; kihaladáskor viszont ez meg van engedve.  
			return t*(record.v+t*record.a/2)+record.pi;
		}
	}	
	/**A vonat Routes-beli rekordjának azonosítója t időpontban. Ha a vonat nem szerepel a listában, akkor visszatérési érték 0.*/
	public int getRouteRecordNo(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		if (!containsTrainId(train_id) || e==null) {
			return 0;
		} else {
			return e.getValue().route_record_no;
		}
	}	
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		entrySet().stream().filter(f -> f.getKey().field.equals(Field.T)).forEach(e -> {
			header.setLength(0);
			header.append(e.getKey().toString().split("\r")[0]+"\t"); body.append(e.getKey().toString().split("\r")[1]+"\t");
			header.append(e.getValue().toString().split("\r")[0]+"\t"); body.append(e.getValue().toString().split("\r")[1]+"\t");
			header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
		});
		return (header.toString()+body.toString()).replace('.', ',');
	}
/*
	public String toString2() {
		StringBuilder body= new StringBuilder();
		entrySet().stream().filter(e0 -> e0.getKey().field.equals(Field.T)).forEach(e1 -> {
			body.append(e1.getKey().value+"\t\t\t".substring(2-e1.getKey().train_id)+e1.getValue().v);
			body.append('\r');
			if (e1.getKey().train_id==0 && higherEntry(e1.getKey()).getKey().field.equals(Field.P)) { //&& 305.7<e1.getKey().value) {
				body.append(getMaxT(0)+"\t"+getV(0, getMaxT(0)));
				body.append('\r');
			}
			if (e1.getKey().train_id==1 && higherEntry(e1.getKey()).getKey().field.equals(Field.P)) {
				body.append(getMaxT(1)+"\t\t"+getV(1, getMaxT(1)));
				body.append('\r');
			}
			if (e1.getKey().train_id==2 && higherEntry(e1.getKey()).getKey().field.equals(Field.P)) {
				body.append(getMaxT(2)+"\t\t\t"+getV(2, getMaxT(2)));
				body.append('\r');
			}
		});
		return body.toString().replace('.', ',');
	}
*/
}

class SectionRecordKey implements Comparable<SectionRecordKey> {
	public final int train_id;
	public final Sections.Field field; 
	public final double value;
	public SectionRecordKey(int train_id, Sections.Field field, double value) {
		this.train_id= train_id;
		this.field= field;
		this.value= value;
	}
	public int compareTo(SectionRecordKey sectionrecordkey) {
		int compare0= Integer.compare(train_id, sectionrecordkey.train_id)*4;
		int compare1= field.compareTo(sectionrecordkey.field)*2;
		int compare2= Double.compare(value, sectionrecordkey.value);
		return compare0+compare1+compare2;
	}
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		header.setLength(0);
		header.append("tid\t"); body.append(train_id+"\t");
		header.append("fld\t"); body.append(field+"\t");
		header.append("vle\t"); body.append(value+"\t");
		header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
		return header.toString()+body.toString();
	}
}

class SectionRecordValue {
	public final int train_id;
	public final int route_record_no;
	public double t;
	public double d;
	/** A Section Record pozíciója a vonat indulásához képest */
	public double pg;
	/** A Section Record pozíciója az Item-jének a kezdetéhez képest */
	public double pi;
	public double l;
	public double a; 
	public double v;
	public SectionRecordValue(int train_id, int route_record_no) {
		this.train_id= train_id;
		this.route_record_no= route_record_no;
	}
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		header.setLength(0);
//		header.append("tid\t"); body.append(train_id+"\t");
		header.append("rri\t"); body.append(route_record_no+"\t");
//		header.append("t\t"); body.append(t+"\t");
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