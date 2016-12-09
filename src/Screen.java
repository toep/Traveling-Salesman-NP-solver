import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JPanel;

public class Screen extends JPanel {

	private List<DataPoint> points;

	private int min;
	private DataPoint minP;
	private DataPoint p1;
	private List<Rectangle> recs;
	private List<GridInfo> grids;
	private final int POINT_COUNT = 30;

	public Screen() {
		points = new ArrayList<DataPoint>();
		recs = new ArrayList<Rectangle>();
		grids = new ArrayList<GridInfo>();
		setPreferredSize(new Dimension(512, 512));
	}

	public void solve() {
		solve(0, 0, getWidth(), getHeight(), points);
		connectGrids();
	}

	private void connectGrids() {
		if (grids.size() == 0)
			return;
		int gridsVisited = 0;
		boolean[] visited = new boolean[grids.size()];
		GridInfo first = grids.get(0);
		GridInfo nextGrid = null;
		GridInfo grd = null;// the current grid in the loop
		int minDistance = Integer.MAX_VALUE;

		// handle first grid connection
		for (int i = 1; i < grids.size(); i++) {
			GridInfo g = grids.get(i);
			if (first.p1.dist2(g.p1) < minDistance) {
				minDistance = first.p1.dist2(g.p1);
				nextGrid = g;
			}
		}
		if (nextGrid != null) {
			visited[grids.indexOf(nextGrid)] = true;
			first.p1.out = nextGrid.p1;
			gridsVisited++;
			grd = nextGrid;
		}

		// handle the rest -1
		while (gridsVisited < grids.size() - 1) {
			minDistance = Integer.MAX_VALUE;
			for (int i = 1; i < grids.size(); i++) {
				if (visited[i])
					continue;
				GridInfo g = grids.get(i);
				if (g == grd)
					continue;
				if (grd.p1.dist2(g.p1) < minDistance) {
					minDistance = grd.p1.dist2(g.p1);
					nextGrid = g;
				}
			}
			if (nextGrid != null) {
				visited[grids.indexOf(nextGrid)] = true;
				grd.p2.out = nextGrid.p1;
				gridsVisited++;
				grd = nextGrid;
			}
		}

		// connect last to first
		grd.p2.out = first.p2;
	}

	public void solve(int x, int y, int width, int height, List<DataPoint> list) {

		// partition the space into 4
		Rectangle r1 = new Rectangle(x, y, width / 2+1, height / 2+1);
		Rectangle r2 = new Rectangle(x + width / 2, y, width / 2+1, height / 2+1);
		Rectangle r3 = new Rectangle(x, y + height / 2, width / 2+1, height / 2+1);
		Rectangle r4 = new Rectangle(x + width / 2, y + height / 2, width / 2+1, height / 2+1);
		recs.add(r1);
		recs.add(r2);
		recs.add(r3);
		recs.add(r4);
		List<DataPoint> q1 = list.stream().filter(e -> r1.contains(e.getPoint())).collect(Collectors.toList());
		
		List<DataPoint> q2 = list.stream().filter(e -> r2.contains(e.getPoint())).collect(Collectors.toList());
		List<DataPoint> q3 = list.stream().filter(e -> r3.contains(e.getPoint())).collect(Collectors.toList());
		List<DataPoint> q4 = list.stream().filter(e -> r4.contains(e.getPoint())).collect(Collectors.toList());
		
		
		int minCount = 6;
		if (q1.size() <= minCount)
			TSM(q1);
		else
			solve(x, y, width / 2, height / 2, q1);

		if (q2.size() <= minCount)
			TSM(q2);
		else
			solve(x + width / 2, y, width / 2, height / 2, q2);

		if (q3.size() <= minCount)
			TSM(q3);
		else
			solve(x, y + height / 2, width / 2, height / 2, q3);

		if (q4.size() <= minCount)
			TSM(q4);
		else
			solve(x + width / 2, y + height / 2, width / 2, height / 2, q4);

	

		repaint();
	}

	private void TSM(List<DataPoint> pts) {
		List<DataPoint> original = pts;
		if (pts.size() <= 1) {
			pts.forEach(e -> {
				e.setVisited(true);
				e.setEnd();
				grids.add(new GridInfo(e, null));
			});
			return;
		}
		// greedy tsm
		p1 = pts.get(0);
		while (pts.stream().filter(e -> !e.visited()).count() != 0) {
			min = Integer.MAX_VALUE;
			minP = null;
			pts.forEach(e -> {
				if (e.visited() || e == p1) {
					e.setVisited(false);
					e.addNeighbors(p1);
					p1 = e;
					return;
					
				}
				int dist = p1.dist2(e);
				if (dist < min) {
					min = dist;
					minP = e;
				}
			});
			if (minP != null) {
				p1.addNeighbors(minP);
				p1.setVisited(true);
				minP.setVisited(true);
				
			}
			pts.sort((a, b) -> {

				if (b.visited())
					return 1;
				return 0;
			});
			pts = pts.stream().filter(e -> !e.visited()).collect(Collectors.toList());
			
			p1 = minP;
	

		}
		DataPoint p0 = original.get(0);

		p0.setEnd();
		grids.add(new GridInfo(p0, p1));
		if (p1 != null) {
			p1.setEnd();
		}

	}

	public void generate() {
		points.clear();
		recs.clear();
		grids.clear();
		Random r = new Random();
		for (int i = 0; i < POINT_COUNT; i++) {
			int x = r.nextInt(getWidth() - 20) + 10;
			int y = r.nextInt(getHeight() - 20) + 10;
			points.add(new DataPoint(x, y));
		}

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.clearRect(0, 0, getWidth(), getHeight());
		points.forEach(e -> e.draw(g));
	//recs.forEach(r -> g.drawRect(r.x, r.y, r.width, r.height));
	}

	class GridInfo {
		DataPoint p1, p2;

		public GridInfo(DataPoint p1, DataPoint p2) {
			this.p1 = p1;
			this.p2 = p2;
			if (p2 == null) {
				this.p2 = p1;// for instances where there's only 1 point
			}
		}
	}
}
