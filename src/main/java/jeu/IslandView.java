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
        private JButton moveUp, moveDown, moveRight, moveLeft;
        private JButton dryUp, dryDown, dryLeft, dryRight;

    	public Controller(IslandView superView){
    	    this.superView = superView;

    	    //Movements
    	    this.moveUp = new JButton("Up");
    	    moveUp.addActionListener(e -> {
                model.getCurrentPlayer().move(Direction.NORTH);
                if (model.getCurrentPlayer().checkActionCount()) disableMovements();
            });
    	    this.add(moveUp);

            this.moveDown = new JButton("Down");
            moveDown.addActionListener(e -> {
                model.getCurrentPlayer().move(Direction.SOUTH);
                if (model.getCurrentPlayer().checkActionCount()) disableMovements();
            });
            this.add(moveDown);

            this.moveLeft = new JButton("Left");
            moveLeft.addActionListener(e -> {
                model.getCurrentPlayer().move(Direction.WEST);
                if (model.getCurrentPlayer().checkActionCount()) disableMovements();
            });
            this.add(moveLeft);

            this.moveRight = new JButton("Right");
            moveRight.addActionListener(e -> {
                model.getCurrentPlayer().move(Direction.EAST);
                if (model.getCurrentPlayer().checkActionCount()) disableMovements();
            });
            this.add(moveRight);


            this.dryUp = new JButton("Dry Up");
            dryUp.addActionListener(e -> { model.getCurrentPlayer().dry(Direction.NORTH); });
            this.add(dryUp);

            this.dryDown = new JButton("Dry Down");
            dryDown.addActionListener(e -> { model.getCurrentPlayer().dry(Direction.SOUTH); });
            this.add(dryDown);

            this.dryLeft = new JButton("Dry Left");
            dryLeft.addActionListener(e -> { model.getCurrentPlayer().dry(Direction.WEST); });
            this.add(dryLeft);

            this.dryRight = new JButton("Dry Right");
            dryRight.addActionListener(e -> { model.getCurrentPlayer().dry(Direction.EAST); });
            this.add(dryRight);

            JButton art = new JButton("Artefact");
            art.addActionListener(e -> model.getCurrentPlayer().snatchArtefact());
            this.add(art);

            //End of round
            JButton end = new JButton("End of round");
            end.addActionListener(e -> {
                model.endRound();
                this.moveDown.setEnabled(true);
                this.moveUp.setEnabled(true);
                this.moveRight.setEnabled(true);
                this.moveLeft.setEnabled(true);
            });
            this.add(end);
        }

        protected void disableMovements(){
    	    this.moveLeft.setEnabled(false);
    	    this.moveRight.setEnabled(false);
    	    this.moveUp.setEnabled(false);
    	    this.moveDown.setEnabled(false);
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

