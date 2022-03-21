package hu.hl.disp_200916.core;

import java.util.Vector;

public class RouteBuilder extends Vector<Integer> {
	private static final long serialVersionUID = 1L;
	private final Rails rails;
	private final Routes routes;
	/**RouteBuilder létrehozása egy (Terminal) elemből, cél megadása nélkül.*/
	public RouteBuilder(Rails rails, Routes routes, int rail_id_0) {
		this.rails= rails;
		this.routes= routes;
		super.add(rail_id_0);
		this.add((int) rails.getOutRailIds(rail_id_0, -1).firstElement());
	}
	/**RouteBuilder létrehozása két elemből, cél megadása add függvénnyel lehetséges.*/
	public RouteBuilder(Rails rails, Routes routes, int rail_id_0, int rail_id_1) {
		this(rails, routes, rail_id_0);
		super.add(rail_id_1);
	}
	/**Útvonal felépítése a lista két utolsó tagjától a megadott rail_id utáni, következő védett szakaszt megelőző Track-ig. Visszatérési érték: true, ha az útvonal létrehozható volt, false, ha nem (ekkor a lista is üres)*/
	public boolean add(int rail_id, boolean marad) {
		Vector<Integer> out_rail_ids= rails.getOutRailIds(get(size()-1), (size()==1) ? -1 : get(size()-2));
		if (out_rail_ids.size()==1) { 
			super.add(out_rail_ids.firstElement());
			this.add(rail_id, out_rail_ids.firstElement()==rail_id);
		}
		if (!rails.getType(lastElement()).equals(Rails.Type.R)) {
			setSize(size()-1);
			System.out.print("");
		}
		return marad;
	}
}