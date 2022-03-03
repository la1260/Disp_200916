package hu.hl.disp_200916;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import hu.hl.disp_200916.core.DispCore;
import hu.hl.disp_200916.core.Rails;
import hu.hl.disp_200916.core.Routes;
import hu.hl.disp_200916.core.Sections;
import hu.hl.disp_200916.core.Trains;

// a kihaladó, utolsó Track-on (Target előtt közvetlenül) ne engedd megfordulni (nem is lenne értelme)

public class Main implements DispCore {
	private Rails rail= new Rails(this);
	private Trains train= new Trains();
	private Routes route= new Routes(rail, train);
	private Sections section= new Sections(this, train, route);
	private TreeMap<Integer, JPanel> gitems= new TreeMap<Integer, JPanel>();
	private JFrame frame= new JFrame();
	public static long nextms(long currentms, long refms, int periodmonth) { 
		GregorianCalendar gc= new GregorianCalendar();
		gc.setTimeInMillis(refms);
		int rmm= gc.get(Calendar.MONTH)%periodmonth;
		int rdm= gc.get(Calendar.DATE);
		gc.setTimeInMillis(currentms);
		gc.add(Calendar.DATE, 1);
		while (gc.get(Calendar.MONTH)%periodmonth!=rmm || gc.get(Calendar.DATE)!=rdm) {
			gc.add(Calendar.DATE, 1);
		}
		return gc.getTimeInMillis();
	}
	public static void main(String[] args) {
		new Main();
/*		TreeMap<Long, String> tm= new TreeMap<Long, String>();
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTimeInMillis(), 2), "v��zsz��mla");
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.OCTOBER, 15).getTimeInMillis(), 1), "villany��ra, g��zsz��mla");
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.NOVEMBER, 4).getTimeInMillis(), 1), "g��z��ra, villanysz��mla");
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.NOVEMBER, 1).getTimeInMillis(), 1), "flipsz��mla");
		tm.put(nextms(new GregorianCalendar(2020, Calendar.DECEMBER, 24).getTimeInMillis(), new GregorianCalendar(2020, Calendar.NOVEMBER, 7).getTimeInMillis(), 3), "szem��td��j, biztos��t��s");
		System.out.println(
			tm
//			String.format("%ty-%1$tm-%1$td %1$tH:%1$tM:%1$tS", nextms)+"\t"
		); */
	}
	public Main() {
		
/*		frame.setLayout(null);
		frame.setBounds(200, 40, 800, 920);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayeredPane(new JLayeredPane());
		frame.getLayeredPane().setBackground(Color.YELLOW);
		frame.getLayeredPane().setOpaque(true);
		frame.getLayeredPane().setVisible(true);*/
		
		rail.put(0, Rails.Type.J, 0, 6, 8, 9, -1, 72/3.6, 36/3.6, 18, 4, 0, 0);
		rail.put(1, Rails.Type.J, 0, 7, 11, 10, -1, 72/3.6, 36/3.6, 18, 6, 0, 0);
		rail.put(2, Rails.Type.J, 0, 8, 12, 10, -1, 72/3.6, 36/3.6, 20, 4, 0, 0);
		rail.put(3, Rails.Type.J, 0, 11, 13, 9, 14, 72/3.6, 36/3.6, 20, 6, 0, 0);
		rail.put(4, Rails.Type.J, 0, 15, 16, -1, -1, 72/3.6, -1, 36, 9, 0, 0);
		rail.put(5, Rails.Type.J, 0, 16, 17, -1, -1, 72/3.6, -1, 36, 11, 0, 0);
		rail.put(6, Rails.Type.L, 25, 18, 0, -1, -1, -1, -1, 16, 4, 2, 0);
		rail.put(7, Rails.Type.L, 25, 19, 1, -1, -1, -1, -1, 16, 6, 2, 0);
		rail.put(8, Rails.Type.L, 25, 0, 2, -1, -1, -1, -1, 18, 4, 2, 0);
		rail.put(9, Rails.Type.L, 25, 0, 3, 10, -1, -1, -1, 18, 4, 2, 2);
		rail.put(10, Rails.Type.L, 25, 1, 2, 9, -1, -1, -1, 18, 6, 2, -2);
		rail.put(11, Rails.Type.L, 25, 1, 3, -1, -1, -1, -1, 18, 6, 2, 0);
		rail.put(12, Rails.Type.L, 25, 2, 20, -1, -1, -1, -1, 20, 4, 2, 0);
		rail.put(13, Rails.Type.L, 25, 3, 21, -1, -1, -1, -1, 20, 6, 2, 0);
		rail.put(14, Rails.Type.L, 25, 3, 24, -1, -1, -1, -1, 20, 6, 2, 2);
		rail.put(15, Rails.Type.L, 25, 24, 4, -1, -1, -1, -1, 34, 8, 2, 2);
		rail.put(16, Rails.Type.L, 25, 4, 5, -1, -1, -1, -1, 36, 10, 0, 2);
		rail.put(17, Rails.Type.L, 25, 5, 25, -1, -1, -1, -1, 34, 14, 2, -2);
		rail.put(18, Rails.Type.R, 500, 27, 6, -1, -1, 90/3.6, -1, 4, 4, 12, 0);
		rail.put(19, Rails.Type.R, 500, 28, 7, -1, -1, 90/3.6, -1, 4, 6, 12, 0);
		rail.put(20, Rails.Type.R, 500, 12, 22, -1, -1, 90/3.6, -1, 22, 4, 12, 0);
		rail.put(21, Rails.Type.R, 500, 13, 23, -1, -1, 90/3.6, -1, 22, 6, 12, 0);
		rail.put(22, Rails.Type.R, 500, 20, 29, -1, -1, 90/3.6, -1, 34, 4, 12, 0);
		rail.put(23, Rails.Type.R, 500, 21, 30, -1, -1, 90/3.6, -1, 34, 6, 12, 0);
		rail.put(24, Rails.Type.R, 500, 14, 15, -1, -1, 80/3.6, -1, 22, 8, 12, 0);
		rail.put(25, Rails.Type.R, 500, 17, 26, -1, -1, 90/3.6, -1, 22, 14, 12, 0);
		rail.put(26, Rails.Type.R, 500, 25, 31, -1, -1, 90/3.6, -1, 10, 14, 12, 0);
		rail.put(27, Rails.Type.T, 0, 18, -1, -1, -1, -1, -1, 4, 4, 0, 0);
		rail.put(28, Rails.Type.T, 0, 19, -1, -1, -1, -1, -1, 4, 6, 0, 0);
		rail.put(29, Rails.Type.T, 0, 22, -1, -1, -1, -1, -1, 46, 4, 0, 0);
		rail.put(30, Rails.Type.T, 0, 23, -1, -1, -1, -1, -1, 46, 6, 0, 0);
		rail.put(31, Rails.Type.T, 0, 26, -1, -1, -1, -1, -1, 10, 14, 0, 0);
		train.put(0, 31, 100, 120/3.6, 2.5, -5);
		train.put(1, 27, 100, 90/3.6, 2.5, -5);
		train.put(2, 27, 100, 90/3.6, 2.5, -5);
		route.put(0, 0, 27);
        route.put(0, 1, 18);
		route.put(0, 2, 6);
        route.put(0, 3, 0);
		route.put(0, 4, 9);
		route.put(0, 5, 3);
		route.put(0, 6, 14);
		route.put(0, 7, 24);
		route.put(0, 8, 24);
		route.put(0, 9, 24);
		route.put(0, 10, 15);
		route.put(0, 11, 4);
		route.put(0, 12, 16);
		route.put(0, 13, 5);
		route.put(0, 14, 17);
		route.put(0, 15, 25);
		route.put(0, 16, 26);
		route.put(0, 17, 26);
		route.put(0, 18, 25);
		route.put(0, 19, 25);
		route.put(1, 0, 27);
		route.put(1, 1, 18);
		route.put(1, 2, 6);
		route.put(1, 3, 0);
		route.put(1, 4, 9);
		route.put(1, 5, 3);
		route.put(1, 6, 14);
		route.put(1, 7, 24);
		route.put(1, 8, 24);
		route.put(1, 9, 24);
		route.put(1, 10, 15);
		route.put(1, 11, 4);
		route.put(1, 12, 16);
		route.put(1, 13, 5);
		route.put(1, 14, 17);
		route.put(1, 15, 25);
		route.put(1, 16, 26);
		route.put(1, 17, 26);
		route.put(1, 18, 25);
		route.put(1, 19, 25);
		route.put(1, 20, 26);
		route.put(1, 21, 31);
		route.put(2, 0, 27);
		route.put(2, 1, 18);
		route.put(2, 2, 6);
		route.put(2, 3, 0);
		route.put(2, 4, 9);
		route.put(2, 5, 3);
		route.put(2, 6, 14);
		route.put(2, 7, 24);
		route.put(2, 8, 24);
		route.put(2, 9, 24);
		route.put(2, 10, 15);
		route.put(2, 11, 4);
		route.put(2, 12, 16);
		route.put(2, 13, 5);
		route.put(2, 14, 17);
		route.put(2, 15, 25);
		route.put(2, 16, 26);
		route.put(2, 17, 26);
		route.put(2, 18, 25);
		route.put(2, 19, 25);
		route.put(2, 20, 26);
		route.put(2, 21, 31);

//		System.out.println("\rRoute:\r"+route); //bukik az eltávolított vonatok esetén
		double t= 0;
		while (Math.rint(t*10)<6000) {
			if (Math.rint(t*10)==2490) {
				route.put(0, 20, 26);
				route.put(0, 21, 31);		
			}
			t= Math.rint(section.step(t, 0.1)*10)/10;
//			System.out.println("t: "+(t-0.1)+"\rSection:\r"+section);
//			System.out.println("t: "+(t-0.1)+"\nRoute:\n"+route); //bukik az eltávolított vonatok esetén
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		System.out.println("t: "+Math.rint(t*10)/10+"\rSection:\r"+section);
	}
	public void update(int id) {
		if (gitems.containsKey(id)) {
			gitems.get(id).repaint();
		} else {
			JPanel p;
			gitems.put(id, p= new JPanel() {
				private static final long serialVersionUID = 1L;
				public void paint(Graphics g) {
					super.paint(g);
					int train_id= rail.getUser(id);
					int color= rail.getColor(id);
					g.setColor(Color.BLACK);
					g.drawString(String.valueOf(train_id), getWidth()>>1, 12);
//					System.out.println("e: "+id+": "+train_id);
				}
			});
			frame.getLayeredPane().add(p);
			p.setBounds(rail.getX(id)*8, rail.getY(id)*16, (rail.getWidth(id)+1)*8, (rail.getHeight(id)+1)*16);
			p.setVisible(true);
		}
	}
	public void passout(double t, int train_id) {
		System.out.println("Kihaladt: "+train_id+" t: "+Math.rint(t*10)/10);
	}
}