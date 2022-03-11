package hu.hl.disp_200916.core;

public interface DispCoreListener {
	public void graphicsUpdate(int rail_id);
	public void trainPassIn(int train_id, double t);
	public void trainPassOut(int train_id, double t);
	public void trainStopped(int train_id, double t);
	public void trainStarted(int train_id, double t);
}
