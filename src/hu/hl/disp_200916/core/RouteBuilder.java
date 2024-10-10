package hu.hl.disp_200916.core;

import java.util.Vector;

public class RouteBuilder {
	private static final long serialVersionUID = 1L;
	private final Rails rails;
	private Vector<Integer> route= null;
	public RouteBuilder(Rails rails) {
		this.rails= rails;
	}
	public void initRoute(int rail_id_0, int rail_id_1) {
		route= new Vector<Integer>();
		route.add(rail_id_0);
		route.add(rail_id_1);
	}
	/**Útvonal felépítése a lista két utolsó tagjától a megadott rail_id utáni, következő védett szakaszt megelőző Track-ig. Visszatérési érték: true, ha az útvonal létrehozható volt, false, ha nem (ekkor a lista is üres)*/
	public boolean appendRail(int rail_id) {
		route.add(rail_id);
		return false;
	}
	public Vector<Integer> getRoute() {
		Vector<Integer> route= this.route;
		this.route= null;
		return route;
	}
	
}