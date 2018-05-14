package jeu;


import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Grid extends JPanel implements Observer
{

    private IslandView superView;
	private static final int CELL_SIZE = 30;
	private static final int PLAYER_SIZE = 26;
	private static final int PLAYER_BORDER = (CELL_SIZE - PLAYER_SIZE) / 2;

    public Grid(IslandView view){
		super();
		this.superView = view;
		superView.model.addObserver(this);

		this.setPreferredSize(new Dimension(CELL_SIZE*IslandModel.WIDTH, CELL_SIZE*IslandModel.HEIGHT));
	}

    private void paint(Graphics g, int x, int y, Color color, int size){
        g.setColor(color);
        g.fillRect(x, y, size, size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.repaint();
        // Ground drawing
        for (int i = 0; i < IslandModel.HEIGHT; i++) {
            for (int j = 0; j < IslandModel.WIDTH; j++) {
                this.paint( g,
                        j * CELL_SIZE,
                        i * CELL_SIZE,
                        this.superView.model.getGroundColorAtPos(i, j),
                        CELL_SIZE);
                Color artColor = this.superView.model.getArtefactColor(i, j);
                if (artColor != Color.GRAY){
                    g.setColor(artColor);
                    g.fillOval(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        for (Player p : this.superView.model.getPlayers()){

            this.paint(g,
                    p.getxPos() * CELL_SIZE + PLAYER_BORDER,
                    p.getyPos() * CELL_SIZE + PLAYER_BORDER,
                    new Color(130, 36, 36),
                    PLAYER_SIZE);
        }
        Player p = this.superView.model.getCurrentPlayer();
        this.paint(g,
                p.getxPos() * CELL_SIZE + PLAYER_BORDER,
                p.getyPos() * CELL_SIZE + PLAYER_BORDER,
                Color.RED,
                PLAYER_SIZE);
    }


    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}

