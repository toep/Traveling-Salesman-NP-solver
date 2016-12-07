import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JPanel;

public class Screen extends JPanel {

	private List<DataPoint> points, ins, outs;
	
	private int min;
	private DataPoint minP;
	private DataPoint p1;
	public Screen() {
		points = new ArrayList<DataPoint>();
		ins = new ArrayList<DataPoint>();
		outs = new ArrayList<DataPoint>();
		setPreferredSize(new Dimension(700, 600));
	}

	public void solve() {
		solve(0, 0, getWidth(), getHeight(), points);
		
		//combine the groups
		for(DataPoint in: ins) {
			
		}
	}
	public void solve(int x, int y, int width, int height, List<DataPoint> list) {

		// partition the space into 4
		Rectangle r1 = new Rectangle(x, y, width/2+1, height/2+1);
		Rectangle r2 = new Rectangle(x+width/2, y, width/2, height/2+1);
		Rectangle r3 = new Rectangle(x, y+height/2, width/2+1, height/2);
		Rectangle r4 = new Rectangle(x+width/2, y+height/2, width/2+1, height/2+1);
		List<DataPoint> q1 = list.stream().filter(e -> r1.contains(e.getPoint()))
				.collect(Collectors.toList());
		List<DataPoint> q2 = list.stream().filter(e -> r2.contains(e.getPoint()))
				.collect(Collectors.toList());
		List<DataPoint> q3 = list.stream().filter(e -> r3.contains(e.getPoint()))
				.collect(Collectors.toList());
		List<DataPoint> q4 = list.stream().filter(e -> r4.contains(e.getPoint()))
				.collect(Collectors.toList());
		long start = System.currentTimeMillis();
		int minCount = 10;
		if(q1.size() <= minCount) {
			TSM(q1);			
		}else {
			solve(x, y, width/2, height/2, q1);
		}
		if(q2.size() <= minCount) {
			TSM(q2);			
		}else {
			solve(x+width/2, y, width/2, height/2, q2);
		}
		if(q3.size() <= minCount) {
			TSM(q3);			
		}else {
			solve(x, y+height/2, width/2, height/2, q3);
		}
		if(q4.size() <= minCount) {
			TSM(q4);			
		}else {
			solve(x+width/2, y+height/2, width/2, height/2, q4);
		}
	
		
		//TSM(points);
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		
		repaint();
	}

	private void TSM(List<DataPoint> pts) {
		List<DataPoint> original = pts;
		if (pts.size() <= 1) {
			pts.forEach(e -> {
				e.setVisited(true);
				e.setEnd();
			});
			return;
		}
		// greedy tsm
		int lastT = 0;
		p1 = pts.get(0);
		while (pts.stream().filter(e -> !e.visited()).count() != 0) {
			min = Integer.MAX_VALUE;
			minP = null;
			
			//p1 = pts.get(0);
			pts.forEach(e -> {
				if (e.visited() || e == p1 || p1 == null)
					return;
				int dist = p1.dist2(e);
				//System.out.println("ch");
				if (dist < min) {
					min = dist;
					minP = e;
				}
			});
			if (minP != null) {
				p1.addNeighbors(minP);
				p1.setVisited(true);
				minP.setVisited(true);
				lastT+=1;
				
			}
			pts.sort((a, b) -> {
				
				if (b.visited())
					return 1;
				return 0;
			});
			pts = pts.stream().filter(e -> !e.visited()).collect(Collectors.toList());
			p1 = minP;
			//pts.stream().map(e -> e.visited()).forEach(System.out::print);
			//System.out.println();
			
		}
		DataPoint p0 = original.get(0);

		p0.setEnd();
		ins.add(p0);
		if(p1 != null) {
			p1.setEnd();
			outs.add(p1);
		} else {
			outs.add(p0);
		}

	}

	public void generate() {
		points.clear();
		Random r = new Random();
		for (int i = 0; i < 200; i++) {
			int x = r.nextInt(getWidth() - 20) + 10;
			int y = r.nextInt(getHeight() - 20) + 10;
			points.add(new DataPoint(x, y));
		}

		// points.forEach(e -> e.addNeighbors(points));

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.clearRect(0, 0, getWidth(), getHeight());
		points.forEach(e -> e.draw(g));
	}
}
