package hu.hl.disp_200916.core;

import java.util.TreeMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

public class Sections extends TreeMap<SectionRecordKey, SectionRecordValue> {
	private static final long serialVersionUID= 1L;
//	private final Rails rails;
	private final Trains trains;
	private final Routes routes;
	private final static Random random= new Random();
	/**A lista kétféle indexelésének (globális idő vagy globális pozició) kiválasztása.*/
	public static enum Field {T, P}
	private final DispCoreListener dispcorelistener;
	public Sections(Rails rails, Trains trains, Routes routes, DispCoreListener dispcorelistener) {
//		this.rails= rails;
		this.trains= trains;
		this.routes= routes;
		this.dispcorelistener= dispcorelistener;
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
	/**1. Ellenőrzi, hogy az előírt mennyiségű vonat aktív-e. Ha nem, és van inaktív vonat, egyet véletlenszerűen kiválaszt azok közül, induló útvonallal látja el, aktíwá teszi, és ugrunk vissza az 1 . ponthoz.<br/>2. Minden vonatra t0-hoz képest dt-vel későbbi időpontban ellenőrzi a vonat gyorsulását, sebességét. Lassulás, állás esetén foglalni próbál, sikeres foglalás esetén átszámolja a Section-okat.<br/>3. Minden vonatra, az új t0+dt időpontbeli állapot alapján beállítja a vonat elejének pozíciója alapján a Rail-ok színét, a vonat végének pozíciója alapján felszabadítja a meghaladott Rail-okat. A kihaladt vonatok Active értékét false-ra állítja, Routes-ból és Sections-ból törli rekordjaikat, t0+dt értékével tér vissza.*/
	public double step(double t0, double dt) throws Exception {
		List<Integer> inactive_train_ids;
		while (trains.keySet().stream().filter(train_id -> trains.isActive(train_id)).count()<3 && !(inactive_train_ids= trains.keySet().stream().filter(train_id -> !trains.isActive(train_id)).collect(Collectors.toList())).isEmpty()) {
			int train_id= inactive_train_ids.get(random.nextInt(inactive_train_ids.size()));
			routes.addRouteRecords(train_id, -1, false);
			routes.setActivity(train_id, 0, routes.getRecordCount(train_id), true);
			trains.setActivity(train_id, true);
			
			switch (train_id) {
			case 0:
//				routes.addRouteRecords(train_id, 1, false);
//				routes.addRouteRecords(train_id, 2, false);
//				routes.addRouteRecords(train_id, 26, false);
				break;
			case 1:
				routes.addRouteRecords(train_id, 28, false);
				break;
			case 2:
//				routes.addRouteRecords(train_id, 19, false);
				break;
			}
			routes.setActivity(train_id, 0, routes.getRecordCount(train_id), true);
			trains.setActivity(train_id, true);
//			System.out.println(train_id+": "+routes.getTrainRouteRecords(train_id));
		}
		trains.keySet().stream().filter(train_id -> trains.isActive(train_id) && (getA(train_id, t0+dt)<0 || getA(train_id, t0+dt)==0 && getV(train_id, t0+dt)==0)).forEach(train_id -> {
			int first_route_record_no= getRouteRecordNoT(train_id, t0+dt)+1;
			int last_route_record_no= routes.getNearestTrackRouteRecordNo(train_id, first_route_record_no);
			if (-1<last_route_record_no && routes.isUser(train_id, first_route_record_no, last_route_record_no, -1)) {
				routes.setUser(train_id, first_route_record_no, last_route_record_no, train_id);
				routes.setRailStatus(train_id, first_route_record_no, last_route_record_no, Rails.Status.R);
				calc(train_id, t0, last_route_record_no);
			}
		});
		trains.keySet().stream().filter(train_id -> trains.isActive(train_id)).forEach(train_id -> {
			boolean vonat_all_leptetes_elott= getV(train_id, t0)==0;
			boolean vonat_all_leptetes_utan= getV(train_id, t0+dt)==0;
			double train_head_position_before_step= getPG(train_id, t0);
			double train_head_position_after_step= getPG(train_id, t0+dt);
			double train_tail_position_before_step= train_head_position_before_step-trains.getL(train_id); 
			double train_tail_position_after_step= train_head_position_after_step-trains.getL(train_id);
			//A sárgított vonat pirosítása elinduláskor.
			if (vonat_all_leptetes_elott && !vonat_all_leptetes_utan) { 
				int train_head_route_record_no_after_step= getRouteRecordNoP(train_id, train_head_position_after_step);
				int train_tail_route_record_no_after_step= getRouteRecordNoP(train_id, train_tail_position_after_step);
				routes.setRailStatus(train_id, train_tail_route_record_no_after_step, train_head_route_record_no_after_step, Rails.Status.M);
				dispcorelistener.trainStarted(train_id, t0+dt);
			}
			//A haladó vonat helyének pirosítása
			int train_head_route_record_no_before_step= getRouteRecordNoP(train_id, train_head_position_before_step);
			int train_head_route_record_no_after_step= getRouteRecordNoP(train_id, train_head_position_after_step);
			if (train_head_position_after_step<0) { //A vonat belépés előtt várakozik.
			} else {
				if (train_head_position_before_step<=0 && 0<train_head_position_after_step) { //A vonat a léptetéssel belép. A kezdő Terminal-ra mutató rekord nincs a listán, ezért amikor a vonat eleje átlépi a 0.0-s pozíciót, akkor kell a 0-ás Route rekordra állítani a léptetés előtti Route rekord mutatót. 
					train_head_route_record_no_before_step= 0;
					dispcorelistener.trainHeadPassIn(train_id, t0+dt);
				}
				if (train_head_position_before_step<=getPGMax(train_id) && getPGMax(train_id)<train_head_position_after_step) { //Vonat elejének kihaladása: a vonat listában előforduló max. pg értéke a vonatelej léptetés előtti és léptetés utáni pozíciója közé esik.
					dispcorelistener.trainHeadPassOut(train_id, t0+dt);
				}
				int train_head_rail_no_before_step= routes.getRailNo(train_id, train_head_route_record_no_before_step);
				int train_head_rail_no_after_step= routes.getRailNo(train_id, train_head_route_record_no_after_step);
				if (train_head_rail_no_before_step!=train_head_rail_no_after_step) { //Megfordulásnál a felesleges foglalások-felszabadítások elkerülése miatt hiába különbözőek a Route rekordok, a Rail-oknak is különbözniök kell. 
					routes.setRailStatus(train_id, train_head_route_record_no_before_step+1, train_head_route_record_no_after_step, Rails.Status.M);
				}
			}
			//A haladó vonat által meghaladott Rail-ok felszabadítása.
			int train_tail_route_record_no_before_step= getRouteRecordNoP(train_id, train_tail_position_before_step);
			int train_tail_route_record_no_after_step= getRouteRecordNoP(train_id, train_tail_position_after_step);
			if (train_tail_position_after_step<0) { //A vonat belépés előtt várakozik.
			} else {
				if (train_tail_position_before_step<=0 && 0<train_tail_position_after_step) { //A kezdő Terminal-ra mutató rekord nincs a listán, ezért amikor a vonat vége átlépi a 0.0-s pozíciót, akkor kell a 0-ás Route rekordra állítani a léptetés előtti Route rekord mutatót.
					train_tail_route_record_no_before_step= 0;
					dispcorelistener.trainTailPassIn(train_id, t0+dt);
				}
				if (train_tail_position_before_step<=getPGMax(train_id) && getPGMax(train_id)<train_tail_position_after_step) { //Vonat végének kihaladása: a vonat listában előforduló max. pg értéke a vonatvég léptetés előtti és léptetés utáni pozíciója közé esik.
					train_tail_route_record_no_after_step= getRouteRecordNoMax(train_id)+1;
					trains.setActivity(train_id, false);
					dispcorelistener.trainTailPassOut(train_id, t0+dt);
				}
				int train_tail_rail_no_before_step= routes.getRailNo(train_id, train_tail_route_record_no_before_step);
				int train_tail_rail_no_after_step= routes.getRailNo(train_id, train_tail_route_record_no_after_step);
				if (train_tail_rail_no_before_step!=train_tail_rail_no_after_step) { //Megfordulásnál a felesleges foglalások-felszabadítások elkerülése miatt hiába különbözőek a Route rekordok, a Rail-oknak is különbözniök kell.
					routes.setUser(train_id, train_tail_route_record_no_before_step, train_tail_route_record_no_after_step-1, -1);
					routes.setRailStatus(train_id, train_tail_route_record_no_before_step, train_tail_route_record_no_after_step-1, Rails.Status.F);
				}
			}
			//A megállt vonat sárgítása.
			if (!vonat_all_leptetes_elott && vonat_all_leptetes_utan) {
				routes.setRailStatus(train_id, train_tail_route_record_no_after_step, train_head_route_record_no_after_step, Rails.Status.S);
				dispcorelistener.trainStopped(train_id, t0+dt);
			}
			dispcorelistener.trainUpdate(train_id, t0+dt, getA(train_id, t0+dt), getV(train_id, t0+dt), getPIMax(train_id, t0+dt)-getPI(train_id, t0+dt), routes.isOriginalDirection(train_id,  getRouteRecordNoT(train_id, t0+dt)));	
		});
		//A kihaladás miatt inaktíwá vált vonatok rekordjainak eltávolítása Sections-ból és Routes-ből.
		trains.keySet().stream().filter(train_id -> !trains.isActive(train_id)).forEach(train_id -> {
			routes.entrySet().removeIf(e -> e.getKey().train_id==train_id);
			entrySet().removeIf(e -> e.getKey().train_id==train_id);
		});
		return t0+dt;
	}	
	/**A megadott vonat t-beli állapotánál elvágja annak Section-jait; last_route_record_no-ig levő Route rekordokra felveszi a Section-okat.*/
	private void calc(int train_id, double t, int last_route_record_no) {
		put(train_id, t, last_route_record_no, (routes.isPassOut(train_id, last_route_record_no)) ? Math.min(trains.getVMax(train_id), routes.getRailVMax(train_id, last_route_record_no)) : 0);
	}
	/**Ua. mint calc; annak egy elemi lépése, egy Route rekordra, rekurzívan hiwa a megelőző rekordokat, azoknak vin-t átatdva és azoktól vout-jukat visszakapva.*/
	private double put(int train_id, double t, int route_record_no, double vout) {
		vout= (routes.isBeforeReversion(train_id, route_record_no)) ? 0 : vout;
		double pi= (getRouteRecordNoT(train_id, t)==route_record_no) ? getPI(train_id, t) : routes.getPStart(train_id, route_record_no);
		double li= routes.getPEnd(train_id, route_record_no)-pi;
		SectionRecordValue r0= new SectionRecordValue(train_id, route_record_no);
		SectionRecordValue r1= new SectionRecordValue(train_id, route_record_no);
		SectionRecordValue r2= new SectionRecordValue(train_id, route_record_no);
		r0.a= trains.get(train_id).a_p;
		r1.a= 0;
		r2.a= trains.get(train_id).a_n;
		r1.v= Math.min(trains.getVMax(train_id), routes.getRailVMax(train_id, route_record_no));
		r1.v= Math.min(r1.v, Math.sqrt(vout*vout-2*r2.a*li));
		r0.v= (!containsTrainId(train_id)) ? r1.v : (getRouteRecordNoT(train_id, t)==route_record_no) ? getV(train_id, t) : put(train_id, t, route_record_no-1, r1.v);
		r1.v= Math.min(r1.v, Math.sqrt(r0.v*r0.v+2*r0.a*li));
		r2.v= r1.v= Math.min(r1.v, Math.sqrt((r0.v*r0.v*r2.a-vout*vout*r0.a+2*li*r0.a*r2.a)/(r2.a-r0.a)));
		vout= Math.min(r1.v, vout);
		r0.d= Math.max(0, (r1.v-r0.v)/r0.a);
		r2.d= Math.max(0, (vout-r1.v)/r2.a);
		r0.l= (r0.v+r1.v)*r0.d/2;
		r2.l= (vout+r1.v)*r2.d/2;
		r1.l= li-r0.l-r2.l;
		r1.d= r1.l/r1.v;
		r0.t= (getRouteRecordNoT(train_id, t)==route_record_no || !containsTrainId(train_id)) ? t : getMaxT(train_id);
		r1.t= r0.t+r0.d;
		r2.t= r1.t+r1.d;
		r0.pg= getPG(train_id, r0.t);
		r1.pg= r0.pg+r0.l;
		r2.pg= r1.pg+r1.l;
		r0.pi= pi;
		r1.pi= r0.pi+r0.l;
		r2.pi= r1.pi+r1.l;
		if (containsTrainId(train_id) && getRouteRecordNoT(train_id, t)==route_record_no) { 	//Elvágja a Train t időpontbeli Section-jait (P és T mezők), és törli az utána levőket.
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
	/**A vonat gyorsulása t időpontban. Ha a t túlmutat az utolsó Section vég(idő)pontján vagy a vonat nem szerepel a listában, akkor visszatérési érték 0.*/
	private double getA(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		SectionRecordValue record;
		if (!containsTrainId(train_id) || e==null || (record= e.getValue()).t+record.d<=t) {
			return 0;
		} else {
			return record.a;
		}
	}
	/**A vonat sebessége t időpontban. Ha a t túlmutat az utolsó Section vég(idő)pontján miközben nem kihaladás zajlik, vagy a vonat nem szerepel a listában, akkor visszatérési érték 0.*/
	private double getV(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		SectionRecordValue record;
		if (!containsTrainId(train_id) || e==null || (record= e.getValue()).t+record.d<=t && !routes.isPassOut(train_id, record.route_record_no)) {
			return 0;
		} else {
			return record.v+record.a*(t-e.getKey().value);
		}
	}
	/**A vonat poziciója t időpontban, a vonat indulásához képest. A listában nem szereplő vonat esetén a visszatérési érték -1.*/
	private double getPG(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		if (!containsTrainId(train_id) || e==null) {
			return -1;
		} else {
			SectionRecordValue record= e.getValue();
			t-= record.t;
			t= (routes.isPassOut(train_id, record.route_record_no) || t<record.d) ? t : record.d; //változó sebességű szakasznál nem címezhet a tartam fölé; kihaladáskor viszont ez meg van engedve.  
			return t*(record.v+t*record.a/2)+record.pg;
		}
	}
	/**A vonat poziciója t időpontban, a Section Rail-jának a vonat mögötti kezdőpontjához képest. A listában nem szereplő vonat esetén a visszatérési érték 0.*/
	private double getPI(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		if (!containsTrainId(train_id) || e==null) {
			return 0;
		} else {
			SectionRecordValue record= e.getValue();
			t-= record.t;
			t= (routes.isPassOut(train_id, record.route_record_no) || t<record.d) ? t : record.d; //változó sebességű szakasznál nem címezhet a tartam fölé; kihaladáskor viszont ez meg van engedve.  
			return t*(record.v+t*record.a/2)+record.pi;
		}
	}	
	/**A vonat Routes-beli rekordjának azonosítója t időpontban. Ha a vonat nem szerepel a listában, akkor visszatérési érték -1.*/
	private int getRouteRecordNoT(int train_id, double t) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, t));
		if (!containsTrainId(train_id) || e==null) {
			return -1;
		} else {
			return e.getValue().route_record_no;
		}
	}
	/**A vonat Routes-beli rekordjának azonosítója p pozicióban. Ha a vonat nem szerepel a listában, akkor visszatérési érték -1.*/
	private int getRouteRecordNoP(int train_id, double p) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.P, p));
		if (!containsTrainId(train_id) || e==null) {
			return -1;
		} else {
			return e.getValue().route_record_no;
		}
	}	
	/**A megadott vonat megadott időpontjában az aktuális Rail hossza. Ha a vonat nem szerepel a listában, akkor visszatérési érték -1.*/ 
	private double getPIMax(int train_id, double t) {
		int route_record_no= getRouteRecordNoT(train_id, t);
		if (route_record_no==-1) {
			return -1;
		} else {
			return routes.getRailL(train_id, getRouteRecordNoT(train_id, t));
		}		
	}
	/**A megadott vonat legnagyobb, a pályára eső globális pozíció értékének lekérdezése. Ha a vonat nem szerepel a listában, akkor visszatérési érték -1.*/ 
	private double getPGMax(int train_id) {
		Entry<SectionRecordKey, SectionRecordValue> e= floorEntry(new SectionRecordKey(train_id, Field.T, Double.POSITIVE_INFINITY));
		if (!containsTrainId(train_id) || e==null) {
			return -1;
		} else {
			return e.getValue().pg+e.getValue().l;
		}
	}
	/**A megadott vonat utolsó Route record no értékének lekérdezése. Ha a vonat nem szerepel a listában, akkor visszatérési érték -1.*/ 
	private int getRouteRecordNoMax(int train_id) {
		Entry<SectionRecordKey, SectionRecordValue> esectionrecord= floorEntry(new SectionRecordKey(train_id, Field.T, Double.POSITIVE_INFINITY));
		if (!containsTrainId(train_id) || esectionrecord==null) {
			return -1;
		} else {
			return esectionrecord.getValue().route_record_no;
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
	/** A Section Record pozíciója a vonat indulásához képest.*/
	public double pg;
	/** A Section Record pozíciója az Item-jének a kezdetéhez képest.*/
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