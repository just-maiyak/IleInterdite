package jeu;


import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Grid extends JPanel implements Observer
{

    public IslandView superView;
	public static final int CELL_SIZE = 30;

    public Grid(IslandView view){
		super();
		this.superView = view;
		superView.model.addObserver(this);

		this.setPreferredSize(new Dimension(CELL_SIZE*IslandModel.WIDTH, CELL_SIZE*IslandModel.HEIGHT));
	}

    private void paint(Graphics g, int x, int y, ZoneState state){
        switch (state) {
            case DRY:
                g.setColor(Color.white);
                break;
            case WET:
                g.setColor(new Color(90, 140, 190));
                break;
            case SUBMERGED:
                g.setColor(new Color(30, 30, 50));
                break;
        }
        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.repaint();
        for (int i = 0; i < IslandModel.HEIGHT; i++) {
            for (int j = 0; j < IslandModel.WIDTH; j++) {
                ZoneState currentState = this.superView.model.getState(i, j);
                this.paint( g,
                        (j) * CELL_SIZE,
                        (i) * CELL_SIZE,
                        currentState);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}

