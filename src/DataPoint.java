import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataPoint {
	private int x, y;
	private Point point;
	DataPoint in, out;
	private boolean visited = false;
	private boolean end = false;
	public DataPoint(int x, int y) {
		this.x = x;
		this.y = y;
		point = new Point(x, y);
		in = null;
		out = null;
	}

	public void addNeighbors(DataPoint... dataPoints) {
		if(dataPoints.length == 1) {
			if(in == null) {
				in = dataPoints[0];
				dataPoints[0].out = this;
			}
			else {
				out = dataPoints[0];
				dataPoints[0].in = this;
			}
		}
		else {
			in = dataPoints[0];
			out = dataPoints[1];
			dataPoints[0].out = this;
			dataPoints[1].in = this;
		}
	}

	public Point getPoint() {
		return point;
	}

	public void draw(Graphics g) {
		if(end) {
			g.setColor(Color.red);
			g.fillOval(x-3, y-3, 6, 6);
		}
		g.setColor(Color.BLACK);
		g.drawOval(x - 3, y - 3, 6, 6);
		
		g.setColor(Color.BLUE);
		if(in != null)
			g.drawLine(x, y, in.x, in.y);
		if(out != null)
			g.drawLine(x, y, out.x, out.y);

	}

	public int dist2(DataPoint a) {
		return (x - a.x) * (x - a.x) + (y - a.y) * (y - a.y);
	}

	public boolean visited() {
		return visited;
	}

	public void setVisited(boolean b) {
		visited = b;
	}

	public List<DataPoint> getNeighbors() {
		List<DataPoint> res = new ArrayList<DataPoint>();
		res.addAll(Arrays.asList(in, out));
		return res;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public void setEnd() {
		end = true;
	}
	
	


}
