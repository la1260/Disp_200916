package hu.hl.disp_200916;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import hu.hl.disp_200916.core.DispCoreListener;
import hu.hl.disp_200916.core.Rails;
import hu.hl.disp_200916.core.Routes;
import hu.hl.disp_200916.core.Sections;
import hu.hl.disp_200916.core.Trains;

public class Main implements DispCoreListener {
	private Rails rails= new Rails(this);
	private Trains trains= new Trains();
	private Routes routes= new Routes(rails, trains);
	private Sections sections= new Sections(this, trains, routes);
	private TreeMap<Integer, JPanel> gitems= new TreeMap<Integer, JPanel>();
	private JFrame frame= new JFrame();
	private StringBuilder stringbuilder= new StringBuilder(); 
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
		
		frame.setLayout(null);
		frame.setBounds(200, 40, 800, 920);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayeredPane(new JLayeredPane());
//		frame.getLayeredPane().setBackground(Color.BLACK);
		frame.getLayeredPane().setOpaque(true);
		frame.getLayeredPane().setVisible(true);
		
		JTextArea jtextarea= new JTextArea();
		jtextarea.setBounds(500, 20, 200, 200);
		jtextarea.setVisible(true);
		frame.getLayeredPane().add(jtextarea);
		
		rails.put(0, Rails.Type.J, 0, 6, 8, 9, -1, 72/3.6, 36/3.6, 18, 4, 0, 0, "");
		rails.put(1, Rails.Type.J, 0, 7, 11, 10, -1, 72/3.6, 36/3.6, 18, 6, 0, 0, "");
		rails.put(2, Rails.Type.J, 0, 8, 12, 10, -1, 72/3.6, 36/3.6, 20, 4, 0, 0, "");
		rails.put(3, Rails.Type.J, 0, 11, 13, 9, 14, 72/3.6, 36/3.6, 20, 6, 0, 0, "");
		rails.put(4, Rails.Type.J, 0, 15, 16, -1, -1, 72/3.6, -1, 36, 10, 0, 0, "");
		rails.put(5, Rails.Type.J, 0, 16, 17, -1, -1, 72/3.6, -1, 36, 12, 0, 0, "");
		rails.put(6, Rails.Type.L, 25, 18, 0, -1, -1, -1, -1, 16, 4, 2, 0, "");
		rails.put(7, Rails.Type.L, 25, 19, 1, -1, -1, -1, -1, 16, 6, 2, 0, "");
		rails.put(8, Rails.Type.L, 25, 0, 2, -1, -1, -1, -1, 18, 4, 2, 0, "");
		rails.put(9, Rails.Type.L, 25, 0, 3, 10, -1, -1, -1, 18, 4, 2, 2, "");
		rails.put(10, Rails.Type.L, 25, 1, 2, 9, -1, -1, -1, 18, 6, 2, -2, "");
		rails.put(11, Rails.Type.L, 25, 1, 3, -1, -1, -1, -1, 18, 6, 2, 0, "");
		rails.put(12, Rails.Type.L, 25, 2, 20, -1, -1, -1, -1, 20, 4, 2, 0, "");
		rails.put(13, Rails.Type.L, 25, 3, 21, -1, -1, -1, -1, 20, 6, 2, 0, "");
		rails.put(14, Rails.Type.L, 25, 3, 24, -1, -1, -1, -1, 20, 6, 2, 2, "");
		rails.put(15, Rails.Type.L, 25, 24, 4, -1, -1, -1, -1, 34, 8, 2, 2, "");
		rails.put(16, Rails.Type.L, 25, 4, 5, -1, -1, -1, -1, 36, 10, 0, 2, "");
		rails.put(17, Rails.Type.L, 25, 5, 25, -1, -1, -1, -1, 34, 14, 2, -2, "");
		rails.put(18, Rails.Type.R, 500, 27, 6, -1, -1, 90/3.6, -1, 4, 4, 12, 0, "I");
		rails.put(19, Rails.Type.R, 500, 28, 7, -1, -1, 90/3.6, -1, 4, 6, 12, 0, "II");
		rails.put(20, Rails.Type.R, 500, 12, 22, -1, -1, 90/3.6, -1, 22, 4, 12, 0, "III");
		rails.put(21, Rails.Type.R, 500, 13, 23, -1, -1, 90/3.6, -1, 22, 6, 12, 0, "IV");
		rails.put(22, Rails.Type.R, 500, 20, 29, -1, -1, 90/3.6, -1, 34, 4, 12, 0, "V");
		rails.put(23, Rails.Type.R, 500, 21, 30, -1, -1, 90/3.6, -1, 34, 6, 12, 0, "VI");
		rails.put(24, Rails.Type.R, 500, 14, 15, -1, -1, 80/3.6, -1, 22, 8, 12, 0, "VII");
		rails.put(25, Rails.Type.R, 500, 17, 26, -1, -1, 90/3.6, -1, 22, 14, 12, 0, "VIII");
		rails.put(26, Rails.Type.R, 500, 25, 31, -1, -1, 90/3.6, -1, 10, 14, 12, 0, "IX");
		rails.put(27, Rails.Type.T, 0, 18, -1, -1, -1, -1, -1, 4, 4, 0, 0, "");
		rails.put(28, Rails.Type.T, 0, 19, -1, -1, -1, -1, -1, 4, 6, 0, 0, "");
		rails.put(29, Rails.Type.T, 0, 22, -1, -1, -1, -1, -1, 46, 4, 0, 0, "");
		rails.put(30, Rails.Type.T, 0, 23, -1, -1, -1, -1, -1, 46, 6, 0, 0, "");
		rails.put(31, Rails.Type.T, 0, 26, -1, -1, -1, -1, -1, 10, 14, 0, 0, "");
		
		trains.put(0, 27, 100, 120/3.6, 2.5, -5);
		routes.put(0, 0, 27);
        routes.put(0, 1, 18);
		routes.put(0, 2, 6);
        routes.put(0, 3, 0);
		routes.put(0, 4, 8);
		routes.put(0, 5, 2);
		routes.put(0, 6, 12);
		routes.put(0, 7, 20);
		routes.put(0, 8, 22);
		routes.put(0, 9, 22);
		routes.put(0, 10, 20);
		routes.put(0, 11, 12);
		routes.put(0, 12, 2);
		routes.put(0, 13, 8);
		routes.put(0, 14, 0);
		routes.put(0, 15, 6);
		routes.put(0, 16, 18);
		routes.put(0, 17, 27);
		
		trains.put(1, 31, 100, 90/3.6, 2.5, -5);
		routes.put(1, 0, 27);
		routes.put(1, 1, 18);
		routes.put(1, 2, 6);
		routes.put(1, 3, 0);
		routes.put(1, 4, 9);
		routes.put(1, 5, 3);
		routes.put(1, 6, 14);
		routes.put(1, 7, 24);
		routes.put(1, 8, 24);
		routes.put(1, 9, 24);
		routes.put(1, 10, 15);
		routes.put(1, 11, 4);
		routes.put(1, 12, 16);
		routes.put(1, 13, 5);
		routes.put(1, 14, 17);
		routes.put(1, 15, 25);
		routes.put(1, 16, 26);
		routes.put(1, 17, 26);
		routes.put(1, 18, 25);
		routes.put(1, 19, 25);
		routes.put(1, 20, 26);
		routes.put(1, 21, 31);
		
		trains.put(2, 31, 100, 90/3.6, 2.5, -5);
		routes.put(2, 0, 27);
		routes.put(2, 1, 18);
		routes.put(2, 2, 6);
		routes.put(2, 3, 0);
		routes.put(2, 4, 9);
		routes.put(2, 5, 3);
		routes.put(2, 6, 14);
		routes.put(2, 7, 24);
		routes.put(2, 8, 24);
		routes.put(2, 9, 24);
		routes.put(2, 10, 15);
		routes.put(2, 11, 4);
		routes.put(2, 12, 16);
		routes.put(2, 13, 5);
		routes.put(2, 14, 17);
		routes.put(2, 15, 25);
		routes.put(2, 16, 26);
		routes.put(2, 17, 26);
		routes.put(2, 18, 25);
		routes.put(2, 19, 25);
		routes.put(2, 20, 26);
		routes.put(2, 21, 31);

		double t= 0;
		while (Math.rint(t*10)<6000) {
			t= Math.rint(sections.step(t, 0.1)*10)/10;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		jtextarea.setText(stringbuilder.toString());

	}
	public void railUpdate(int rail_id) {
		if (gitems.containsKey(rail_id)) {
			gitems.get(rail_id).repaint();
		} else {
			JPanel p;
			gitems.put(rail_id, p= new JPanel() {
				private static final long serialVersionUID = 1L;
				public void paint(Graphics g) {
					int rail_id= gitems.entrySet().stream().filter(e -> e.getValue().equals(this)).findFirst().get().getKey();
					super.paint(g);
					switch (rails.getStatus(rail_id)) {
					case F: g.setColor(Color.BLACK); break;
					case M: g.setColor(Color.RED); break;
					case P: g.setColor(Color.CYAN); break;
					case R: g.setColor(Color.GREEN); break;
					case S: g.setColor(Color.YELLOW); break;
					}
					switch (rails.getType(rail_id)) {
					case T: break;
					case R:
						g.fillRect(5, 7, getWidth()-10, 2);
						String text= rails.getText(rail_id);
						FontMetrics fontmetrics= g.getFontMetrics();
						if (text!="" && fontmetrics.stringWidth(text)<=getWidth()-17) { // csak akkor írjuk bele a vonat nevét, ha belefér a vonal területére.
							Color foregroundcolor= g.getColor();
							g.setColor(new Color(240, 240, 240));
							g.fillRect((getWidth()>>1)-(fontmetrics.stringWidth(text)>>1), 0, fontmetrics.stringWidth(text), getHeight()-1);
							g.setColor(foregroundcolor);
							g.drawString(text, (getWidth()>>1)-(fontmetrics.stringWidth(text)>>1), (fontmetrics.getAscent()+(getHeight()-(fontmetrics.getAscent()+fontmetrics.getDescent()))/2)-1);
						}
						break;
					case J:
						// setBorder(BorderFactory.createLineBorder(Color.BLACK));
						break;
					case L:
						if (rails.getWidth(rail_id)==0) { // vertical (|)
							g.drawLine(3, 9, 3, getHeight()-10);
							g.drawLine(4, 9, 4, getHeight()-10);
						} else if (rails.getHeight(rail_id)==0) { // horizontal (-)
							g.drawLine(5, 7, getWidth()-6, 7);
							g.drawLine(5, 8, getWidth()-6, 8);
						} else if (0<rails.getHeight(rail_id)/rails.getWidth(rail_id)) { // left (\)
							int i= -1;
							while (++i+4<getWidth()-5) {
								g.drawRect(i+4, 2*i+9, 1, 1);
							}
						} else { // right (/)
							int i= -1;
							while (++i+4<getWidth()-5) {
								g.drawRect(i+4, getWidth()-2*i+13, 1, 1);
							}
						}
						if (g.getColor().equals(Color.BLACK)) {
							frame.getLayeredPane().moveToBack(this);
						}
						break;
//					default:
//					  g.drawLine(0, 0, getWidth(), getHeight());
//					  g.drawLine(0, getHeight(), getWidth(), 0);					  
					}
				}
			});
			frame.getLayeredPane().add(p);
			p.setBounds(
				Integer.min(rails.getX(rail_id), rails.getX(rail_id)+rails.getWidth(rail_id)+1)*8,
				Integer.min(rails.getY(rail_id), rails.getY(rail_id)+rails.getHeight(rail_id))*16,
				Integer.max(rails.getWidth(rail_id)+1, -rails.getWidth(rail_id)+1)*8,
				Integer.max(rails.getHeight(rail_id)+1, -rails.getHeight(rail_id)+1)*16
			);
			p.setOpaque(false);
			p.setVisible(true);
		}
	}
	public void trainUpdate(int train_id, double t, double a, double v, double pr, boolean isOriginalDir) {
		stringbuilder.append(String.format("Train id_%d_t_%.2f_a_%.2f_v_%.2f_pr_%.2f_oridir_%b\n", train_id, t, a, v, pr, isOriginalDir).replace('_', '\t'));
	}
	public void trainHeadPassIn(int train_id, double t) {
		System.out.println(String.format("Eleje behaladt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*10)/10).replace('_', '\t'));
	}
	public void trainTailPassIn(int train_id, double t) {
		System.out.println(String.format("Vége behaladt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*10)/10).replace('_', '\t'));
	}
	public void trainHeadPassOut(int train_id, double t) {
		System.out.println(String.format("Eleje kihaladt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*10)/10).replace('_', '\t'));
	}
	public void trainTailPassOut(int train_id, double t) {
		System.out.println(String.format("Vége kihaladt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*10)/10).replace('_', '\t'));
	}
	public void trainStopped(int train_id, double t) {
		System.out.println(String.format("Megállt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*10)/10).replace('_', '\t'));
	}
	public void trainStarted(int train_id, double t) {
		System.out.println(String.format("Elindult:_%1$d_t:_%2$.2f", train_id, Math.rint(t*10)/10).replace('_', '\t'));
	}
}