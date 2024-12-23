package hu.hl.disp_200916.core;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class Routes extends TreeMap<RouteRecordKey, Integer> {
	private static final long serialVersionUID= 1L;
	public final Rails rails;
	public final Trains trains;
	public Routes(Rails rails, Trains trains) {
		this.rails= rails;
		this.trains= trains;
	}
	//Statikus függvények (visszatérési értékük csak az útvonaltól függ).
	/**True, ha a route_record_no által megadott rekordon végighaladás után megfordulás történik.*/
	public boolean isBeforeReversion(int train_id, int route_record_no) {
		Optional<Map.Entry<RouteRecordKey, Integer>> o_route_record_no= entrySet().stream().filter(e -> e.getKey().active && e.getKey().train_id==train_id && e.getKey().route_record_no==route_record_no).findFirst();
		Optional<Map.Entry<RouteRecordKey, Integer>> o_route_record_no_next= entrySet().stream().filter(e -> e.getKey().active && e.getKey().train_id==train_id && e.getKey().route_record_no==route_record_no+1).findFirst();
		return o_route_record_no.isPresent() && o_route_record_no_next.isPresent() && o_route_record_no.get().getValue()==o_route_record_no_next.get().getValue();
	}
	/**True, ha a route_record_no által megadott rekordon az elindulás megfordulás után történik.*/
	public boolean isAfterReversion(int train_id, int route_record_no) {
		Optional<Map.Entry<RouteRecordKey, Integer>> o_route_record_no= entrySet().stream().filter(e -> e.getKey().active && e.getKey().train_id==train_id && e.getKey().route_record_no==route_record_no).findFirst();
		Optional<Map.Entry<RouteRecordKey, Integer>> o_route_record_no_prev= entrySet().stream().filter(e -> e.getKey().active && e.getKey().train_id==train_id && e.getKey().route_record_no==route_record_no-1).findFirst();
		return o_route_record_no.isPresent() && o_route_record_no_prev.isPresent() && o_route_record_no.get().getValue()==o_route_record_no_prev.get().getValue();
	}
	/**True, ha a vonat a route_record_no által megadott rekord Rail-ján az elindulásnak megfelelő irányban közlkedik (a megjelenítést nem veszi figyelembe).*/
	public boolean isOriginalDirection(int train_id, int route_record_no) {
		return (entrySet().stream().filter(e -> e.getKey().active && e.getKey().train_id==train_id && e.getKey().route_record_no<=route_record_no && isAfterReversion(e.getKey().train_id, e.getKey().route_record_no)).count() & 1)==0;
	}
	/**True, ha a route_record_no által megadott rekordot a Train target-jére mutató rekord követi (a vonat kihaladása zajlik).*/
	public boolean isPassOut(int train_id, int route_record_no) {
		return entrySet().stream().anyMatch(e0 -> e0.getKey().active && e0.getKey().train_id==train_id && e0.getKey().route_record_no==route_record_no+1 && e0.getValue()==trains.getDestItemId(train_id));
	}
	/**True, ha a vonat route_record_no által megadott rekordja utolsó a listában és Rail.*/
	public boolean isLastAndRail(int train_id, int route_record_no) {
		TreeMap<RouteRecordKey, Integer> result= entrySet().stream().filter(e -> e.getKey().active && e.getKey().train_id==train_id && route_record_no<=e.getKey().route_record_no).collect(
			() -> new TreeMap<RouteRecordKey, Integer>(),
			(treemap, entry) -> treemap.put(entry.getKey(), entry.getValue()),
			(treemap, entries) -> treemap.putAll(entries)
		);
		return result.size()==1 && rails.getType(result.lastEntry().getValue()).equals(Rails.Type.R);
	}
	/**A route_record_no által megadott rekord és utána levők közül a legközelebbi, R tipusú Rail-ra mutató route_record_no értéke.<br/>Ha nincs ilyen, akkor -1.<br/>Megfordulás esetén ez eredmény az azonosak közül az utolsó, amin nincs megfordulás.*/
	public int getNearestTrackRouteRecordNo(int train_id, int route_record_no) {
		Optional<Map.Entry<RouteRecordKey, Integer>> result= entrySet().stream().filter(e ->
			e.getKey().active &&
			e.getKey().train_id==train_id &&
			route_record_no<=e.getKey().route_record_no &&
			rails.getType(e.getValue()).equals(Rails.Type.R) &&
			!isBeforeReversion(e.getKey().train_id, e.getKey().route_record_no)).findFirst();
		return (result.isPresent()) ? result.get().getKey().route_record_no : -1;
	}	
	/**A route_record_no által megadott rekord Rail-ján a mozgás kezdetének pozíciója. Értéke megfordulás után 10m+vonathossz, egyébként 0.*/
	public double getPStart(int train_id, int route_record_no) {
		return (isAfterReversion(train_id, route_record_no)) ? 10+trains.getL(train_id) : 0;
	}
	/**A route_record_no által megadott rekord Rail-ján a mozgás végének pozíciója. Értéke a Rail hossza, mínusz megfordulás vagy Train számára le nem foglalt Rail előtt vagy utolsó elemen, ha az Rail 10m, kihaladáskor, ill. előbbiektől eltérő esetekben 0m.*/
	public double getPEnd(int train_id, int route_record_no) {
		return Math.max(rails.getL(get(new RouteRecordKey(train_id, route_record_no)))-(((isBeforeReversion(train_id, route_record_no) || !isUser(train_id, route_record_no+1, route_record_no+1, train_id) || isLastAndRail(train_id, route_record_no)) && !isPassOut(train_id, route_record_no)) ? 10 : 0), 0);
	}
	/**A route_record_no által megadott rekord Rail-jának max. haladási sebessége. J és L tipusú Rail-ok esetén a teljes váltókörzeten belül előforduló értékek minimuma.*/
	public double getRailVMax(int train_id, int route_record_no) {
		RouteRecordKey routerecordkey= new RouteRecordKey(train_id, route_record_no);
		Rails.Type type= rails.getType(get(routerecordkey));
		switch (type) {
		case T:
		case R:
			return rails.getVMax(get(routerecordkey), 0);
		case J:
		case L:
			TreeSet<Double> result= new TreeSet<Double>();
			result.add(rails.getVMax(get(routerecordkey), 0));
			RouteRecordKey nala_kisebb_r_ek_legkozelebbike= entrySet().stream().filter(e0 -> e0.getKey().active && e0.getKey().train_id==train_id && e0.getKey().route_record_no<=route_record_no && rails.getType(e0.getValue()).equals(Rails.Type.R)).max((e1, e2) -> Integer.compare(e1.getKey().route_record_no, e2.getKey().route_record_no)).get().getKey();
			RouteRecordKey nala_nagyobb_r_ek_legkozelebbike= entrySet().stream().filter(e0 -> e0.getKey().active && e0.getKey().train_id==train_id && route_record_no<=e0.getKey().route_record_no && rails.getType(e0.getValue()).equals(Rails.Type.R)).min((e1, e2) -> Integer.compare(e1.getKey().route_record_no, e2.getKey().route_record_no)).get().getKey();
			entrySet().stream().filter(e0 -> e0.getKey().active && e0.getKey().train_id==train_id && nala_kisebb_r_ek_legkozelebbike.route_record_no<e0.getKey().route_record_no && e0.getKey().route_record_no<nala_nagyobb_r_ek_legkozelebbike.route_record_no).forEach(e1 ->
				result.add(rails.getVMax(e1.getValue(), rails.getJunctionDir(e1.getValue(), lowerEntry(e1.getKey()).getValue(), higherEntry(e1.getKey()).getValue())))
			);
			return result.first();
		default:
			return -1;
		}
	}
	/**A route_record_no által megadott rekord Rail-jának hossza.*/
	public double getRailL(int train_id, int route_record_no) {
		RouteRecordKey routerecordkey= new RouteRecordKey(train_id, route_record_no);
		return rails.getL(get(routerecordkey));
	}
	/**A route_record_no által megadott rekord Rail-jának aznosítója. Ha nem található, akkor -1.*/
	public int getRailNo(int train_id, int route_record_no) {
		Optional<Map.Entry<RouteRecordKey, Integer>> result= entrySet().stream().filter(e -> e.getKey().train_id==train_id && e.getKey().route_record_no==route_record_no).findFirst();
		return (result.isPresent()) ? result.get().getValue(): -1;
	}
	/**Megadja, hogy a train_id-nek hány rekordja van a listában.*/
	public int getRecordCount(int train_id) {
		return (int) keySet().stream().filter(routerecordkey -> routerecordkey.train_id==train_id).count();
	}		
	/** A megadott vonat megadott route rekordjainak aktivitását a megadott értékre állítja.*/
	public void setActivity(int train_id, int first_route_record_no, int last_route_record_no, boolean activity) {
		entrySet().stream().filter(e0 -> e0.getKey().train_id==train_id && first_route_record_no<=e0.getKey().route_record_no && e0.getKey().route_record_no<=last_route_record_no).forEach(e1 -> e1.getKey().active=activity);
	}
	/**A megadott vonat következő rekordjaként a megadott target_rail_id-ig levő rail_id-k rekordjainak felvétele.
	 * Ha target_rail_id= -1-gyel hívjuk meg, és a Train-nak nincs rekordja, akkor kezdő útvonalat hoz létre (az induló Terminal-t és utána amíg Track-ok jönnek, felvesszük a listára); ha van már rekordja, akkor hibát dob.
	 * Ha target_rail_id Track, és azt a pályán Terminal követi, akkor utóbbi is felkerül a listára záró elemként (függetlenül attól, hogy Train targetje-e vagy sem) 
	  */
	public boolean addRouteRecords(int train_id, int target_rail_id, boolean found) throws Exception {
		if (target_rail_id==-1) { //aktiválás előtt álló vonatnak -1-es targettel kezdő útvonal létrehozása
			if (0<getRecordCount(train_id)) { //van-e a megadott vonatnak eleme a listán
				throw new Exception("-1 van megadva olyan vonathoz targetnek, aminek már van rekordja. train_id: "+train_id);
			} else {
				RouteRecordKey routerecordkey= new RouteRecordKey(train_id, 0);
				int rail_id;
				put(routerecordkey, rail_id= trains.getEntryItemId(train_id));
				routerecordkey= new RouteRecordKey(train_id, 1);
				put(routerecordkey, rails.getOutRailIds(rail_id, -1).firstElement());
				while (rails.getType(rail_id= rails.getOutRailIds(getRailNo(train_id, getRecordCount(train_id)-1), getRailNo(train_id, getRecordCount(train_id)-2)).firstElement()).equals(Rails.Type.R)) {
					put(new RouteRecordKey(train_id, getRecordCount(train_id)), rail_id);
				}
			}
			return true;
		} else {
			int utsoelotti= getRailNo(train_id, getRecordCount(train_id)-2);
			int utso= getRailNo(train_id, getRecordCount(train_id)-1);
			return rails.getOutRailIds(utso, utsoelotti).stream().anyMatch(i -> {
				if (train_id==1 && i==27) {
					System.out.print("");
				}
				try {
					RouteRecordKey routerecordkey= new RouteRecordKey(train_id, getRecordCount(train_id));
					put(routerecordkey, i);
					boolean result= addRouteRecords(train_id, target_rail_id, i==target_rail_id || found) || found;
					if (!result) {
						remove(routerecordkey); 
//						keySet().removeIf(k -> routerecordkey.train_id==k.train_id && routerecordkey.route_record_no<=k.route_record_no);
					}
					return result;
				} catch (Exception e) {
					return found;
				}
			});
		}
	}
	/** teszteléshez */
	public String getTrainRouteRecords(int train_id) {
		return entrySet()
		  .stream()
		  .filter(entry -> entry.getKey().train_id==train_id)
		  .map(entry -> entry.getValue())
		  .collect(() -> new StringBuilder(), (stringbuilder, value) -> stringbuilder.append(value+","), null)
		  .toString();
	}
	/**A meadott vonatnak legalább 2 elemének kell lennie a listában.*/
	//Dinamikus függvények (visszatérési értékük az aktuális user-től is függ).
	/**A megadott Train first_route_rec_id-jétől last_route_rec_id-jáig levő rekordok Rail-jai user_train_id számára foglaltak. user_train_id= -1 esetén megkapjuk, hogy mind szabadok-e.*/
	public boolean isUser(int train_id, int first_route_record_no, int last_route_record_no, int user_train_id) {
		return entrySet().stream().filter(e0 -> e0.getKey().active && e0.getKey().train_id==train_id && first_route_record_no<=e0.getKey().route_record_no && e0.getKey().route_record_no<=last_route_record_no).allMatch(e1 -> rails.getUser(e1.getValue())==user_train_id);
	}
	/**A megadott Train first_route_rec_id-jétől last_route_rec_id-jáig levő rekordok Rail-jainak user_train_id user-t állít be.*/
	public void setUser(int train_id, int first_route_record_no, int last_route_record_no, int user_train_id) {
		entrySet().stream().filter(e0 -> e0.getKey().active && e0.getKey().train_id==train_id && first_route_record_no<=e0.getKey().route_record_no && e0.getKey().route_record_no<=last_route_record_no).forEach(e1 -> rails.setUser(e1.getValue(), user_train_id));
	}
	/**A megadott Train first_route_rec_id-jétől last_route_rec_id-jáig levő rekordok Rail-jainak status státuszt (színt) állít be.*/
	public void setRailStatus(int train_id, int first_route_record_no, int last_route_record_no, Rails.Status status) {
		entrySet().stream().filter(e0 -> e0.getKey().active && e0.getKey().train_id==train_id && first_route_record_no<=e0.getKey().route_record_no && e0.getKey().route_record_no<=last_route_record_no).forEach(e1 -> rails.setStatus(e1.getValue(), status));
	}
	/**A megadott Train listában szereplő két utolsó railjének id-jét tartalmazó lista.*/
	public Vector<Integer> getNewRoute(int train_id) {
		return entrySet().stream().filter(e -> e.getKey().train_id==train_id).collect(() -> new Vector<Integer>(), (v, e) -> {
				v.add(e.getValue());
				if (v.size()==3) v.remove(0);
			}, (a, b) -> {});
	}
	/**A megadott routéhoz a megadott rail hozzáadása az útvonal felépítésével.*/
	public boolean addtoNewRoute(Vector<Integer> route, int target_rail_id, boolean found) {
		int utsoelotti= route.get(route.size()-2);
		int utso= route.get(route.size()-1);
		return rails.getOutRailIds(utso, utsoelotti).stream().anyMatch(i -> {
			route.add(i);
			boolean f= i==target_rail_id;
			boolean result= f && !rails.getType(target_rail_id).equals(Rails.Type.R) || addtoNewRoute(route, target_rail_id, f || found) || found;
			if (!result) {
				route.remove(route.size()-1);
			}
			return result;
		});
	}
	public String toString() {
		StringBuilder[] result= entrySet().stream().collect(
			() -> new StringBuilder[] {new StringBuilder(), new StringBuilder()},
			(stringbuilders, entry) -> {
				stringbuilders[0].setLength(0);
				stringbuilders[0].append(entry.getKey().toString().split("\r")[0]+"\t"); stringbuilders[1].append(entry.getKey().toString().split("\r")[1]+"\t");
				stringbuilders[0].append("rid\t"); stringbuilders[1].append(entry.getValue()+"\t");
				stringbuilders[0].append("rtp\t"); stringbuilders[1].append(rails.getType(entry.getValue())+"\t");
				stringbuilders[0].append("usr\t"); stringbuilders[1].append(rails.getUser(entry.getValue())+"\t");
				stringbuilders[0].append("stt\t"); stringbuilders[1].append(rails.getStatus(entry.getValue())+"\t");
				stringbuilders[0].append("act\t"); stringbuilders[1].append((entry.getKey().active)+"\t");
				stringbuilders[0].append("ikh\t"); stringbuilders[1].append(isPassOut(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append("bfr\t"); stringbuilders[1].append(isBeforeReversion(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append("afr\t"); stringbuilders[1].append(isAfterReversion(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append("ord\t"); stringbuilders[1].append(isOriginalDirection(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append("lar\t"); stringbuilders[1].append(isLastAndRail(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append("ntr\t"); stringbuilders[1].append(getNearestTrackRouteRecordNo(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append("pst\t"); stringbuilders[1].append(getPStart(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append("pen\t"); stringbuilders[1].append(getPEnd(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append("ivm\t"); stringbuilders[1].append(getRailVMax(entry.getKey().train_id, entry.getKey().route_record_no)+"\t");
				stringbuilders[0].append(System.getProperty("line.separator")); stringbuilders[1].append(System.getProperty("line.separator"));
			},
			(stringbuilders, entries) -> {}
		);
		return result[0].toString()+result[1].toString().replace('.', ',');
	}
}

class RouteRecordKey implements Comparable<RouteRecordKey> {
	public final int train_id;
	public final int route_record_no;
	public boolean active= false;
	public RouteRecordKey(int train_id, int route_record_no) {
		this.train_id= train_id;
		this.route_record_no= route_record_no;
	}
	public int compareTo(RouteRecordKey routerecordkey) {
		int compare0= Integer.compare(route_record_no, routerecordkey.route_record_no)*2;
		int compare1= Integer.compare(train_id, routerecordkey.train_id);
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