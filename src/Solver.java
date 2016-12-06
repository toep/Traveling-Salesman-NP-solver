import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Solver extends JFrame {

	public static void main(String[] args) {
		new Solver();
	}
	
	
	public Solver() {
		Screen screen = new Screen();
		Panel panel = new Panel(screen);
		add(panel, BorderLayout.NORTH);
		add(screen, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		pack();
	}
}
