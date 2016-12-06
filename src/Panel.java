import javax.swing.JButton;
import javax.swing.JPanel;

public class Panel extends JPanel {

	private JButton generateRandom, solve;
	private Screen screen;

	public Panel(Screen screen) {
		this.screen = screen;
		generateRandom = new JButton("Generate");

		generateRandom.addActionListener((e) -> {
			screen.generate();
		});

		solve = new JButton("Solve");

		solve.addActionListener((e) -> {
			screen.solve();
		});

		add(generateRandom);
		add(solve);
	}
}
