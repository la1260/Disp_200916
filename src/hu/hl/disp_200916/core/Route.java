package hu.hl.disp_200916.core;

import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Route extends TreeMap<Integer, TreeMap<Integer, Route.Record>> {
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
	public boolean isBeforeReversion(int train_id, int no) {
		return get(train_id).get(no+1)!=null && get(train_id).get(no).rail_id==get(train_id).get(no+1).rail_id;
	}
	public boolean isAfterReversion(int train_id, int no) {
		return get(train_id).get(no-1)!=null && get(train_id).get(no).rail_id==get(train_id).get(no-1).rail_id;
	}
	private boolean isOriginalDirection(int train_id, int no) {
		return ((get(train_id).entrySet().stream().filter(e -> e.getValue().route_rec_id<=no && isAfterReversion(train_id, e.getValue().route_rec_id)).count() & 1)==0) ? true : false;
	}
	/** No+1-tól a legközelebbi track indexe; min. no+1. Megfordulás esetén az azonosak közül az utolsó, amin nincs megfordulás. Utolsó Track esetén -1.
	 * @param train_id
	 * @param no
	 * @return
	 */
	public int getNearestTrackNo(int train_id, int no) {
		Optional<java.util.Map.Entry<Integer, Record>> o_nearest_track_rail_no= get(train_id).tailMap(no, false).entrySet().stream().filter(e -> rail.getType(e.getValue().rail_id).equals(Rail.Type.R) && !isBeforeReversion(train_id, e.getKey())).findFirst();
		return (o_nearest_track_rail_no.isPresent()) ? o_nearest_track_rail_no.get().getKey() : -1;
	}	
	/** 
	 * @param train_id
	 * @param no Route Record id
	 * @return True= no+1-es Route Record-tól a legközelebbi Track-ra muatató Route Record-ig az összes Route Record által mutatott Rail Record User-je train_id.
	 */
	private boolean isUserTrainId(int train_id, int no) {
		int nearest_track_no= getNearestTrackNo(train_id, no);
		return -1<nearest_track_no && get(train_id).subMap(no, false, nearest_track_no, true).entrySet().stream().allMatch(e -> rail.getUser(e.getValue().rail_id)==train_id);
	}
	/** No+1-ik Route Record által mutatott Rail Record-tól a legközelebbi Track-ra mutató Route Record-ig lévő Route Record-ok Rail Record-jai User-ének train_id megadása.
	 * @param train_id
	 * @param no Route Record id
	 * @return Az utoljára lefoglalt Rail Record (Track) Route Record-jának indexe, vagy -1, ha a foglalás nem volt elvégezhető. 
	 */
	public int setUser(int train_id, int no) {
		int nearest_track_no= getNearestTrackNo(train_id, no);
		if (-1<nearest_track_no) {
			Set<Entry<Integer, Record>> foglalando= get(train_id).subMap(no, false, nearest_track_no, true).entrySet();
			if (foglalando.stream().allMatch(e -> rail.getUser(e.getValue().rail_id)==-1)) {
				foglalando.stream().forEach(e -> rail.setUser(e.getValue().rail_id, train_id));
				return nearest_track_no;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}
	/**
	 * @param train_id
	 * @param no Route record id
	 * @return True= A vonat eleje a targetet közveltenül megelőző Track-on van.
	 */
	public boolean isKihaladas(int train_id, int no) {
		Integer kovetkezo_route_record_id= get(train_id).higherKey(no);
		Integer kovetkezo_utani_route_record_id= get(train_id).higherKey(no+1);
		return kovetkezo_route_record_id!=null && kovetkezo_utani_route_record_id==null && get(train_id).get(kovetkezo_route_record_id).rail_id==train.getTargetItemId(train_id);
	}
	/** Item-ek user-jének törlése first_route_rec_id-től last_route_rec_id-ig. 
	 * @param train_id
	 * @param first_route_rec_id
	 * @param last_route_rec_id
	 * @param forced False esetén a last_route_rec_id-hez tartozó Rail Record-ot nem szabadítja fel, True esetén azt is (utóbbi a pályáról a teljes vonat kihaladásakor szükséges).
	 * @return forced értékét adja vissza.
	 */
	public boolean resetUser(int train_id, int first_route_rec_id, int last_route_rec_id, boolean forced) { 
		get(train_id).subMap(first_route_rec_id, true, last_route_rec_id, true).entrySet().stream().filter(e0 -> {
			Integer item_id= e0.getValue().rail_id;
			return rail.getUser(item_id)==train_id && (get(train_id).get(last_route_rec_id).rail_id!=item_id || forced);
		}).forEach(e1 -> {
			rail.setUser(e1.getValue().rail_id, -1);
		});
		return forced;
	}
	public double getPStart(int train_id, int no) {
		return (isAfterReversion(train_id, no)) ? 10+train.getL(train_id) : 0;
	}
	public double getPEnd(int train_id, int no) {
		return Math.max(rail.getL(get(train_id).get(no).rail_id)-((isBeforeReversion(train_id, no) || !isUserTrainId(train_id, no)) ? 10 : 0), 0);
	}
	public double getItemVMax(int train_id, int no) {
		Rail.Type type= rail.getType(get(train_id).get(no).rail_id);
		switch (type) {
		case T:
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
//				header.append("frs\t"); body.append(isFirst(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
//				header.append("ikh\t"); body.append(isKihaladas(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
//				header.append("lst\t"); body.append(isLast(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("bfr\t"); body.append(isBeforeReversion(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("afr\t"); body.append(isAfterReversion(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("ord\t"); body.append(isOriginalDirection(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
//				header.append("ltm\t"); body.append(isLastTerminal(e_1.getValue().train_id)+"\t");
//				header.append("ltg\t"); body.append(isLastTarget(e_1.getValue().train_id)+"\t");
				header.append("ntr\t"); body.append(getNearestTrackNo(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
//				header.append("sli\t"); body.append(getStopLevelOnItem(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.append("ivm\t"); body.append(getItemVMax(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
//				header.append("pst\t"); body.append(getPStart(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
//				header.append("pen\t"); body.append(getPEnd(e_1.getValue().train_id, e_1.getValue().route_rec_id)+"\t");
				header.setCharAt(header.length()-1, '\n'); body.setCharAt(body.length()-1, '\n');
			});
		});
		return header.toString()+body.toString();
	}	
}