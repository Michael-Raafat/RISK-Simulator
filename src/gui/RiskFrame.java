package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.panel.EndGamePanel;
import gui.panel.EndPanel;
import gui.panel.GamePanel;
import gui.panel.GameWindow;
import gui.panel.LogPanel;
import gui.panel.StartPanel;

public class RiskFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EndPanel endPanel;
	private GamePanel gamePanel;
	private StartPanel startPanel;
	private LogPanel logPanel;
	private int current_state;
	private JLabel background;
	
	public RiskFrame() {
		current_state = 0;
		startPanel = new StartPanel(this);
		logPanel = new LogPanel(this);
		gamePanel = new GamePanel(logPanel);
		endPanel = new EndPanel(this);
		this.setPreferredSize(new Dimension(1000, 1000));
		this.setSize(1000, 1000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
        background = new JLabel(new ImageIcon("img/risk_bg.jpg"));
        this.add(background);
		background.setLayout(new BorderLayout());
		background.add(startPanel, BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void next_state() {
		this.getContentPane().removeAll();
		background.removeAll();
		current_state = (current_state + 1) % 3;
		if (current_state == 0) {
			startPanel.reset();
			this.setLayout(new BorderLayout());
			this.add(background);
			background.setLayout(new BorderLayout());
			background.add(startPanel, BorderLayout.CENTER);
		} else if (current_state == 1) {
			this.setLayout(new BorderLayout());
			this.add(gamePanel, BorderLayout.CENTER);
			this.add(logPanel, BorderLayout.EAST);
		} else if (current_state == 2) {
			this.setLayout(new BorderLayout());
			this.add(endPanel, BorderLayout.CENTER);
		}
		this.revalidate();
		this.repaint();
	}
	
	public GameWindow getGameWindow() {
		return gamePanel;
	}
	
	public EndGamePanel getEndGamePanel() {
		return endPanel;
	}
}
