package hu.hl.disp_200916.core;

import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.Map.Entry;

public class Routes extends TreeMap<RouteRecordKey, Integer> {
	private static final long serialVersionUID= 1L;
	public final Rails rails;
	public final Trains trains;
	public Routes(Rails rails, Trains trains) {
		this.rails= rails;
		this.trains= trains;
	}
	public void put(int train_id, int route_record_no, int rail_id) {
		put(new RouteRecordKey(train_id, route_record_no), rail_id);
	}
	//Statikus függvények (visszatérési értékük csak az útvonaltól függ). 
	/**True, ha a route_record_no által megadott rekordon végighaladás után megfordulás történik.*/
	public boolean isBeforeReversion(int train_id, int route_record_no) {
		RouteRecordKey routerecordkey= higherKey(new RouteRecordKey(train_id, route_record_no));
		return routerecordkey!=null && get(routerecordkey)==get(lowerKey(routerecordkey));
	}
	/**True, ha a route_record_no által megadott rekordon az elindulás megfordulás után történik.*/
	public boolean isAfterReversion(int train_id, int route_record_no) {
		RouteRecordKey routerecordkey= lowerKey(new RouteRecordKey(train_id, route_record_no));
		return routerecordkey!=null && get(routerecordkey)==get(higherKey(routerecordkey));
	}
	/**True, ha a vonat a route_record_no által megadott rekord Rail-ján az elindulásnak megfelelő irányban közlkedik (a megjelenítést nem veszi figyelembe).*/
	private boolean isOriginalDirection(int train_id, int route_record_no) {
		return (entrySet().stream().filter(e -> e.getKey().train_id==train_id && e.getKey().route_record_no<=route_record_no && isAfterReversion(e.getKey().train_id, e.getKey().route_record_no)).count() & 1)==0;
	}
	/**True, ha a route_record_no által megadott rekordot a Train target-jére mutató rekord követi (a vonat kihaladása zajlik).*/
	public boolean isPassOut(int train_id, int route_record_no) {
		RouteRecordKey routerecordkey= higherKey(new RouteRecordKey(train_id, route_record_no));
		return routerecordkey!=null && routerecordkey.train_id==train_id && get(routerecordkey)==trains.getTargetItemId(train_id);
	}
	/**A route_record_no által megadott rekordot követő legközelebbi, R tipusú Rail-ra mutató route_record_no értéke; minimum route_record_no+1.<br/>Ha nincs ilyen, akkor -1.<br/>Megfordulás esetén az azonosak közül az utolsó, amin nincs megfordulás.*/
	private int getNearestTrackRouteRecordNo(int train_id, int route_record_no) {
		Optional<java.util.Map.Entry<RouteRecordKey, Integer>> o_result= entrySet().stream().filter(e ->
			e.getKey().train_id==train_id &&
			route_record_no<e.getKey().route_record_no &&
			rails.getType(e.getValue()).equals(Rails.Type.R) &&
			!isBeforeReversion(e.getKey().train_id, e.getKey().route_record_no)).findFirst();
		return (o_result.isPresent()) ? o_result.get().getKey().route_record_no : -1;
	}	
	/**A route_record_no által megadott rekord Rail-ján a mozgás kezdetének pozíciója. Értéke megfordulás után 10m+vonathossz, egyébként 0.*/
	public double getPStart(int train_id, int route_record_no) {
		return (isAfterReversion(train_id, route_record_no)) ? 10+trains.getL(train_id) : 0;
	}
	/**A route_record_no által megadott rekord Rail-ján a mozgás végének pozíciója. Értéke a Rail hossza, mínusz megfordulás vagy Train számára le nem foglalt Rail előtt 10m, kihaladáskor, ill. előbbiektől eltérő esetekben 0m.*/
	public double getPEnd(int train_id, int route_record_no) {
		return Math.max(rails.getL(get(new RouteRecordKey(train_id, route_record_no)))-(((isBeforeReversion(train_id, route_record_no) || !isUserTrainId(train_id, route_record_no)) && !isPassOut(train_id, route_record_no)) ? 10 : 0), 0);
	}
	/**A route_record_no által megadott rekord Rail-jának max. haladási sebessége. J és L tipusú Rail-ok esetén a teljes váltókörzeten belül előforduló értékek minimuma.*/
	public double getRailVMax(int train_id, int route_record_no) {
		RouteRecordKey routerecord= new RouteRecordKey(train_id, route_record_no);
		Rails.Type type= rails.getType(get(routerecord));
		switch (type) {
		case T:
		case R:
			return rails.getVMax(get(routerecord), 0);
		case J:
		case L:
			TreeSet<Double> result= new TreeSet<Double>();
			result.add(rails.getVMax(get(routerecord), 0));
			RouteRecordKey nala_kisebb_r_ek_legkozelebbike= entrySet().stream().filter(e0 -> e0.getKey().train_id==train_id && e0.getKey().route_record_no<=route_record_no && rails.getType(e0.getValue()).equals(Rails.Type.R)).max((e1, e2) -> Integer.compare(e1.getKey().route_record_no, e2.getKey().route_record_no)).get().getKey();
			RouteRecordKey nala_nagyobb_r_ek_legkozelebbike= entrySet().stream().filter(e0 -> e0.getKey().train_id==train_id && route_record_no<=e0.getKey().route_record_no && rails.getType(e0.getValue()).equals(Rails.Type.R)).min((e1, e2) -> Integer.compare(e1.getKey().route_record_no, e2.getKey().route_record_no)).get().getKey();
			entrySet().stream().filter(e0 -> e0.getKey().train_id==train_id && nala_kisebb_r_ek_legkozelebbike.route_record_no<e0.getKey().route_record_no && e0.getKey().route_record_no<nala_nagyobb_r_ek_legkozelebbike.route_record_no).forEach(e1 ->
				result.add(rails.getVMax(e1.getValue(), rails.getJunctionDir(e1.getValue(), lowerEntry(e1.getKey()).getValue(), higherEntry(e1.getKey()).getValue())))
			);
			return result.first();
		default:
			return -1;
		}
	}
	//Dinamikus függvények (visszatérési értékük az aktuális user-től is függ).
	/**True, ha route_record_no által megadottat követő rekordtól a legközelebbi R tipusú Rail-ra mutató recordig az összes rekord által mutatott Rail User-je train_id.*/
	private boolean isUserTrainId(int train_id, int route_record_no) {
		int nearest_track_route_record_no= getNearestTrackRouteRecordNo(train_id, route_record_no);
		return -1<nearest_track_route_record_no && entrySet().stream().filter(e0 -> e0.getKey().train_id==train_id && route_record_no<e0.getKey().route_record_no && e0.getKey().route_record_no<=nearest_track_route_record_no).allMatch(e1 -> rails.getUser(e1.getValue())==train_id);
	}
	/**First_route_rec_id-től last_route_rec_id-ig levő rekordok Rail-jai Userjének törlése (-1-re állítása), ahol az User a train_id és az adott rekord nem a last_route_rec_id rekord Rail-jára mutat.<br/>Last_incl = true esetén a last_route_rec_id által mutatott Rail user-je is törlésre kerül (csak kihaladásnál; egyébként last_route_rec_id a vonat által aktuálisan használt route_rec_id-ra mutat, így az nem törlendő).<br/>Visszatérési érték: a megadott last_incl értéke.*/
	public boolean resetUser(int train_id, int first_route_record_no, int last_route_record_no, boolean last_incl) {
		entrySet().stream().filter(e0 -> e0.getKey().train_id==train_id && first_route_record_no<=e0.getKey().route_record_no && e0.getKey().route_record_no<=last_route_record_no && rails.getUser(e0.getValue())==train_id && (get(new RouteRecordKey(train_id, last_route_record_no))!=e0.getValue() || last_incl)).forEach(e1 -> rails.setUser(e1.getValue(), -1));
		return last_incl;
	}
	/**A route_record_no által megadottat követő rekordtól a legközelebbi R tipusú Rail-ra mutató rekordig lévő rekordok User-ét train_id-re állítja.<br/>Visszatérési érték az utoljára lefoglalt rekord route_record_no-ja, vagy -1, ha a foglalás másik vonat miatt nem volt lehetséges.*/
	public int setUser(int train_id, int route_record_no) {
		int nearest_track_route_record_no= getNearestTrackRouteRecordNo(train_id, route_record_no);
		if (-1<nearest_track_route_record_no) {
			Set<Entry<RouteRecordKey, Integer>> set= entrySet().stream().filter(e -> e.getKey().train_id==train_id && route_record_no<e.getKey().route_record_no && e.getKey().route_record_no<=nearest_track_route_record_no).collect(Collectors.toSet());
			if (set.stream().allMatch(e -> rails.getUser(e.getValue())==-1)) {
				set.stream().forEach(e -> rails.setUser(e.getValue(), train_id));
				return nearest_track_route_record_no;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		entrySet().forEach(e -> {
			header.setLength(0);
			header.append(e.getKey().toString().split("\r")[0]+"\t"); body.append(e.getKey().toString().split("\r")[1]+"\t");
			header.append("rid\t"); body.append(e.getValue()+"\t");
			header.append("rtp\t"); body.append(rails.getType(e.getValue())+"\t");
			header.append("usr\t"); body.append(rails.getUser(e.getValue())+"\t");
			header.append("ikh\t"); body.append(isPassOut(e.getKey().train_id, e.getKey().route_record_no)+"\t");
			header.append("bfr\t"); body.append(isBeforeReversion(e.getKey().train_id, e.getKey().route_record_no)+"\t");
			header.append("afr\t"); body.append(isAfterReversion(e.getKey().train_id, e.getKey().route_record_no)+"\t");
			header.append("ord\t"); body.append(isOriginalDirection(e.getKey().train_id, e.getKey().route_record_no)+"\t");
			header.append("ntr\t"); body.append(getNearestTrackRouteRecordNo(e.getKey().train_id, e.getKey().route_record_no)+"\t");
			header.append("pst\t"); body.append(getPStart(e.getKey().train_id, e.getKey().route_record_no)+"\t");
			header.append("pen\t"); body.append(getPEnd(e.getKey().train_id, e.getKey().route_record_no)+"\t");
			header.append("ivm\t"); body.append(getRailVMax(e.getKey().train_id, e.getKey().route_record_no)+"\t");
			header.setCharAt(header.length()-1, '\n'); body.setCharAt(body.length()-1, '\n');
		});
		return header.toString()+body.toString();
	}	
}

class RouteRecordKey implements Comparable<RouteRecordKey> {
	public final int train_id;
	public final int route_record_no;
	public RouteRecordKey(int train_id, int route_record_no) {
		this.train_id= train_id;
		this.route_record_no= route_record_no;
	}
	public int compareTo(RouteRecordKey routerecordkey) {
		int compare0= Integer.compare(train_id, routerecordkey.train_id)*2;
		int compare1= Integer.compare(route_record_no, routerecordkey.route_record_no);
		return compare0+compare1;
	}
	public String toString() {
		StringBuilder header= new StringBuilder();
		StringBuilder body= new StringBuilder();
		header.setLength(0);
		header.append("tid\t"); body.append(train_id+"\t");
		header.append("rri\t"); body.append(route_record_no+"\t");
		header.setCharAt(header.length()-1, '\r'); body.setCharAt(body.length()-1, '\r');
		return header.toString()+body.toString();
	}	
}