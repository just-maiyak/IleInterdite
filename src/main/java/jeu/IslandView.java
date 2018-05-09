package jeu;


import javax.swing.*;
import java.awt.*;

public class IslandView
{
    /**
     * Model used to control the GUI
     */
	protected IslandModel model;

    /**
     * Component used for interacting with the game
     */
    protected class Controller extends JPanel {

        private IslandView superView;

    	public Controller(IslandView superView){
    	    this.superView = superView;

    	    //Movements
    	    JButton up = new JButton("Up");
    	    up.addActionListener(e -> model.currentPlayer.move(Direction.NORTH));
    	    this.add(up);

            JButton down = new JButton("Down");
            down.addActionListener(e -> model.currentPlayer.move(Direction.SOUTH));
            this.add(down);

            JButton left = new JButton("Left");
            left.addActionListener(e -> model.currentPlayer.move(Direction.WEST));
            this.add(left);

            JButton right = new JButton("Right");
            right.addActionListener(e -> model.currentPlayer.move(Direction.EAST));
            this.add(right);

            //End of round
            JButton end = new JButton("End of round");
            end.addActionListener(e -> model.endRound());
            this.add(end);
        }
	}
	private Controller controller;
    /**
     * Grid displays the state of the game
     */
	private Grid grid;

    /**
     * Main program window
     */
	private JFrame window;

	public IslandView(String name, IslandModel model, boolean gui){
		super();

		this.model = model;

		/** Setting up window */
		this.window = new JFrame(name);
	    this.window.setName(name);
        this.window.setLocation(10, 200);
        this.window.setLayout(new FlowLayout());
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /** Creating the Components */
	    this.controller = new Controller(this);
	    this.grid = new Grid(this);

        /** Adding them to the frame */
	    this.window.add(this.grid);
	    this.window.add(this.controller);

        /** Pack and show */
	    this.window.pack();
	    this.window.setVisible(gui);
	}

}

