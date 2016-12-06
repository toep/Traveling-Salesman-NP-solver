import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JPanel;

public class Screen extends JPanel {

	private List<DataPoint> points;
	private int min;
	private DataPoint minP;
	private DataPoint p1;
	public Screen() {
		points = new ArrayList<DataPoint>();
		setPreferredSize(new Dimension(700, 600));
	}

	public void solve() {

		// partition the space into 4

		List<DataPoint> q1 = points.stream().filter(e -> e.getX() < getWidth() / 2 && e.getY() < getHeight() / 2)
				.collect(Collectors.toList());
		List<DataPoint> q2 = points.stream().filter(e -> e.getX() >= getWidth() / 2 && e.getY() < getHeight() / 2)
				.collect(Collectors.toList());
		List<DataPoint> q3 = points.stream().filter(e -> e.getX() < getWidth() / 2 && e.getY() >= getHeight() / 2)
				.collect(Collectors.toList());
		List<DataPoint> q4 = points.stream().filter(e -> e.getX() >= getWidth() / 2 && e.getY() >= getHeight() / 2)
				.collect(Collectors.toList());
		long start = System.currentTimeMillis();
		TSM(q1);
		TSM(q2);
		TSM(q3);
		TSM(q4);
		
		//TSM(points);
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		
		repaint();
	}

	private void TSM(List<DataPoint> pts) {
		List<DataPoint> original = pts;
		if (pts.size() == 0)
			return;
		// greedy tsm
		int lastT = 0;
		p1 = pts.get(0);
		while (pts.stream().filter(e -> !e.visited()).count() != 0) {
			min = Integer.MAX_VALUE;
			minP = null;
			
			//p1 = pts.get(0);
			pts.forEach(e -> {
				if (e.visited() || e == p1)
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
		original.get(0).setEnd();
		p1.setEnd();
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
