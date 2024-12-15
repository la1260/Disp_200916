package hu.hl.disp_200916;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import hu.hl.disp_200916.core.DispCoreListener;
import hu.hl.disp_200916.core.Rails;
import hu.hl.disp_200916.core.Rails.Type;
import hu.hl.disp_200916.core.RouteBuilder;
import hu.hl.disp_200916.core.Routes;
import hu.hl.disp_200916.core.Sections;
import hu.hl.disp_200916.core.Trains;

public class DispMain implements DispCoreListener, MouseListener, KeyListener {
	private Rails rails= new Rails(this);
	private Trains trains= new Trains();
	private Routes routes= new Routes(rails, trains);
	private Sections sections= new Sections(rails, trains, routes, this);
	private TreeMap<Integer, RailSymbol> railsymbols= new TreeMap<Integer, RailSymbol>();
	private JFrame frame= new JFrame();
	private RailSymbol focusedrailsymbol= null;
	private StringBuilder stringbuilder= new StringBuilder();
	private double t;
	private Timer timer;
	public static void main(String[] args) throws Exception {
		new DispMain();
	}
	public DispMain() throws Exception {
		frame.setLayout(null);
		frame.setBounds(200, 40, 800, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.setLayeredPane(new JLayeredPane());
//		frame.getLayeredPane().setBackground(Color.BLACK);
		frame.getLayeredPane().setOpaque(true);
		frame.getLayeredPane().setVisible(true);
		
		rails.put(0, Rails.Type.J, 0, 8, 6, 9, -1, 72/3.6, 36/3.6, 18, 4, 0, 0, "");
		rails.put(1, Rails.Type.J, 0, 11, 7, 10, -1, 72/3.6, 36/3.6, 18, 6, 0, 0, "");
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

		JTextArea jtextarea= new JTextArea();
		jtextarea.setBounds(500, 20, 200, 200);
		jtextarea.setVisible(true);
		frame.getLayeredPane().add(jtextarea);

/*		System.out.println(rails.getOutRailIds(0, 6));
		System.out.println(rails.getOutRailIds(0, 7));
		System.out.println(rails.getOutRailIds(0, 8));
		System.out.println(rails.getOutRailIds(0, 9));
		System.out.println(rails.getOutRailIds(0, 10));
		
		System.out.println(rails.getOutRailIds(9, 0));
		System.out.println(rails.getOutRailIds(9, 3));
		System.out.println(rails.getOutRailIds(9, 7));
		System.out.println(rails.getOutRailIds(9, 10));

		System.out.println(rails.getOutRailIds(3, 9));
		System.out.println(rails.getOutRailIds(3, 11));
		System.out.println(rails.getOutRailIds(3, 13));
		System.out.println(rails.getOutRailIds(3, 14));
		System.out.println(rails.getOutRailIds(3, 10));
		
		System.out.println(rails.getOutRailIds(18, 27));
		System.out.println(rails.getOutRailIds(18, 6));
		System.out.println(rails.getOutRailIds(18, 0));
		
		System.out.println(rails.getOutRailIds(27, -1));*/
		
//		RouteBuilder routebuilder= new RouteBuilder(rails, routes, 31);
//		routebuilder.clear();
//		System.exit(16);
		
		trains.put(0, 28, 27, 100, 120/3.6, 0.398, -0.796);
		trains.put(1, 29, 27, 100, 21.6/3.6, 0.398, -0.796);
		trains.put(2, 30, 31, 100, 90/3.6, 0.398, -0.796);
	
		// 20, 12, 2, 9, 0, 6, 18, 27
	
		t= 0;

		timer= new Timer(250, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					t= Math.rint(sections.step(t, 0.25)*100)/100;
					if (10000<=Math.rint(t*10)) {
						timer.stop();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		timer.start();
		jtextarea.setText(stringbuilder.toString());

	}
	public void railUpdate(int rail_id) {
		if (railsymbols.containsKey(rail_id)) {
			railsymbols.get(rail_id).repaint();
		} else {
			RailSymbol railsymbol= new RailSymbol(rails, rail_id, frame.getLayeredPane());
			railsymbol.addMouseListener(this);
			railsymbols.put(rail_id, railsymbol);
		}
	}
	public void trainUpdate(int train_id, double t, double a, double v, double pr, boolean isOriginalDir) {
		if (focusedrailsymbol!=null) {
			int rail_id= railsymbols.entrySet().stream().filter(e -> e.getValue().equals(focusedrailsymbol)).findFirst().get().getKey();
			if (rails.getUser(rail_id)==train_id) {
				frame.setTitle(String.format("rail_id:%d, train id:%d, t:%.2f, a:%.2f, v:%.2f, pr:%.2f, d:%b", rail_id, train_id, t, a, v, pr, isOriginalDir).replace('_', '\t'));
			}
		}
//		stringbuilder.append(String.format("Train id_%d_t_%.2f_a_%.2f_v_%.2f_pr_%.2f_oridir_%b\n", train_id, t, a, v, pr, isOriginalDir).replace('_', '\t'));
	}
	public void trainHeadPassIn(int train_id, double t) {
		System.out.println(String.format("Eleje behaladt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*1000)/1000).replace('_', '\t'));
	}
	public void trainTailPassIn(int train_id, double t) {
		System.out.println(String.format("Vége behaladt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*1000)/1000).replace('_', '\t'));
	}
	public void trainHeadPassOut(int train_id, double t) {
		System.out.println(String.format("Eleje kihaladt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*1000)/1000).replace('_', '\t'));
	}
	public void trainTailPassOut(int train_id, double t) {
		System.out.println(String.format("Vége kihaladt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*1000)/1000).replace('_', '\t'));
	}
	public void trainStopped(int train_id, double t) {
		System.out.println(String.format("Megállt:_%1$d_t:_%2$.2f", train_id, Math.rint(t*1000)/1000).replace('_', '\t'));
	}
	public void trainStarted(int train_id, double t) {
//		System.out.println(String.format("Elindult:_%1$d_t:_%2$.2f", train_id, Math.rint(t*1000)/1000).replace('_', '\t'));
	}
	public void mouseEntered(MouseEvent mouseevent) {
		focusedrailsymbol= (RailSymbol) mouseevent.getSource();
//		frame.setTitle(String.format("rail_id:%d", focusedrailsymbol.rail_id).replace('_', '\t'));
	}
	private Vector<Integer> newroute= null;
	public void mouseClicked(MouseEvent mouseevent) {
		switch (mouseevent.getButton()) {
		case MouseEvent.BUTTON1: //left
			if (mouseevent.getSource() instanceof RailSymbol) {
				RailSymbol railsymbol= (RailSymbol) mouseevent.getSource();
				int rail_id= railsymbol.rail_id;
				int train_id= rails.getUser(rail_id);
				if (newroute==null) { //Nincs folyamatban útvonal létrehozás.
					if (-1<train_id) {
						newroute= routes.getNewRoute(train_id); //Nincs folyamatban útvonal létrehozás, és van Train a klikkentett Rail-on -> indul az útvonal létrehozás.
					} else {
						//Nincs folyamatban útvonal létrehozás, és nincs Train a klikkentett Rail-on -> nem csinál semmit. 
					}
				} else {
					if (!routes.addtoNewRoute(newroute, rail_id, false)) { //Folyamatban van útvonal létrehozás + új elem hozzáadása épülő Route-hoz. Ha sikertelen, a felépítés megszakad. 
						newroute= null;
					}
				}
			}
			break;
		case MouseEvent.BUTTON2: //centr
			frame.setTitle("centr");
			break;
		case MouseEvent.BUTTON3: //Jobbgomb nyomásra az útvonal építés (ha van) megszakad.
			if (newroute!=null) {
				newroute= null;
			}
			frame.setTitle("right");
			break;
		}
		System.out.println(((RailSymbol) mouseevent.getSource()).getRail_id()+", "+newroute);
	}
	
	public void mouseExited(MouseEvent mouseevent) {
		focusedrailsymbol= null;
//		frame.setTitle("");
	}
	public void mousePressed(MouseEvent mouseevent) {}
	public void mouseReleased(MouseEvent mouseevent) {}

	public void keyPressed(KeyEvent keyevent) {
		try {
			switch (keyevent.getKeyCode()) {
			case KeyEvent.VK_RIGHT: t= Math.rint(sections.step(t, 0.1)*10)/10; break;
			case KeyEvent.VK_LEFT: t= Math.rint(sections.step(t, -0.1)*10)/10; break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void keyReleased(KeyEvent keyevent) {}
	public void keyTyped(KeyEvent keyevent) {}	
}

class RailSymbol extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Rails rails;
	public final int rail_id;
	private final JLayeredPane layeredpane;
	private static final TreeMap<Rails.Status, Color> colors= new TreeMap<Rails.Status, Color>();
	static {
		colors.put(Rails.Status.F, Color.BLACK);
		colors.put(Rails.Status.M, Color.RED);
		colors.put(Rails.Status.P, Color.CYAN);
		colors.put(Rails.Status.R, Color.GREEN);
		colors.put(Rails.Status.S, Color.YELLOW);
	}
	public RailSymbol(Rails rails, int rail_id, JLayeredPane layeredpane) {
		this.rails= rails;
		this.rail_id= rail_id;
		this.layeredpane= layeredpane;
		layeredpane.add(this);
		switch (rails.getType(rail_id)) {
		case L:
		case R:
			layeredpane.moveToBack(this);
			break;
		case J:
		case T:
			layeredpane.moveToFront(this);
			break;
		}
		setBounds(
			Integer.min(rails.getX(rail_id), rails.getX(rail_id)+rails.getWidth(rail_id)+1)*8,
			Integer.min(rails.getY(rail_id), rails.getY(rail_id)+rails.getHeight(rail_id))*16,
			Integer.max(rails.getWidth(rail_id)+1, -rails.getWidth(rail_id)+1)*8,
			Integer.max(rails.getHeight(rail_id)+1, -rails.getHeight(rail_id)+1)*16
		);
		setOpaque(false);
		setVisible(true);
	}
	public void paint(Graphics graphics) {
		super.paint(graphics);
		int i;
		switch (rails.getType(rail_id)) {
		case T:
			graphics.setColor(Color.BLACK);
			i= -1;
			while (++i<getWidth()-1) {
				graphics.drawRect(i, 2*i+1, 1, 1);
				graphics.drawRect(getWidth()-i-2, 2*i+1, 1, 1);
			}
			break;
		case J:
			// setBorder(BorderFactory.createLineBorder(Color.BLACK));
			break;
		case R:
			graphics.setColor(colors.get(rails.getStatus(rail_id)));
			graphics.fillRect(5, 7, getWidth()-10, 2);
			String text= rails.getText(rail_id);
			FontMetrics fontmetrics= graphics.getFontMetrics();
			if (text!="" && fontmetrics.stringWidth(text)<=getWidth()-17) { // csak akkor írunk bele scöveget, ha belefér a vonal területére.
				graphics.setColor(new Color(240, 240, 240));
				graphics.fillRect((getWidth()>>1)-(fontmetrics.stringWidth(text)>>1), 0, fontmetrics.stringWidth(text), getHeight()-1);
				graphics.setColor(colors.get(rails.getStatus(rail_id)));
				graphics.drawString(text, (getWidth()>>1)-(fontmetrics.stringWidth(text)>>1), (fontmetrics.getAscent()+(getHeight()-(fontmetrics.getAscent()+fontmetrics.getDescent())>>1))-1);
			}
			break;
		case L:
			graphics.setColor(colors.get(rails.getStatus(rail_id)));
			if (rails.getWidth(rail_id)==0) { // vertical (|)
				graphics.drawLine(3, 9, 3, getHeight()-10);
				graphics.drawLine(4, 9, 4, getHeight()-10);
			} else if (rails.getHeight(rail_id)==0) { // horizontal (-)
				graphics.drawLine(5, 7, getWidth()-6, 7);
				graphics.drawLine(5, 8, getWidth()-6, 8);
			} else if (0<rails.getHeight(rail_id)/rails.getWidth(rail_id)) { // left (\)
				i= -1;
				while (++i<getWidth()-9) {
					graphics.drawRect(i+4, 2*i+9, 1, 1);
				}
			} else { // right (/)
				i= -1;
				while (++i<getWidth()-9) {
					graphics.drawRect(i+4, getWidth()-2*i+13, 1, 1);
				}
			}
			if (graphics.getColor().equals(Color.BLACK)) {
				layeredpane.moveToBack(this);
			}
			break;
		}
	}
	public int getRail_id() {
		return rail_id;
	}	
}