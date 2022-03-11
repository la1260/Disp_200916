package hu.hl.disp_200916.core;

public interface DispCoreListener {
	/**A rail megjelenítése frissítendő.*/
	public void railUpdate(int rail_id);
	/**A vonat adatai frissültek.*/
	public void trainUpdate(int train_id, double t, double a, double v, double pr, boolean isOriginalDir);
	/**A vonat eleje behaladt.*/
	public void trainHeadPassIn(int train_id, double t);
	/**A vonat vége behaladt.*/
	public void trainTailPassIn(int train_id, double t);
	/**A vonat eleje kihaladt.*/
	public void trainHeadPassOut(int train_id, double t);
	/**A vonat vége kihaladt.*/
	public void trainTailPassOut(int train_id, double t);
	/**A vonat megállt.*/
	public void trainStopped(int train_id, double t);
	/**A vonat elindult.*/
	public void trainStarted(int train_id, double t);
}
