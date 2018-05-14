package jeu;


import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

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
        private JButton dryUp, dryDown, dryLeft, dryRight, dryHere;

    	public Controller(IslandView superView){
    	    this.superView = superView;

    	    this.setLayout(new GridLayout(0, 4));

    	    //Movements
    	    this.moveUp = new JButton("Up");
    	    moveUp.addActionListener(e -> {
                model.getCurrentPlayer().move(Direction.NORTH);
                if (model.getCurrentPlayer().checkActionCount()) disableActions();
            });
    	    this.add(moveUp);

            this.moveDown = new JButton("Down");
            moveDown.addActionListener(e -> {
                model.getCurrentPlayer().move(Direction.SOUTH);
                if (model.getCurrentPlayer().checkActionCount()) disableActions();
            });
            this.add(moveDown);

            this.moveLeft = new JButton("Left");
            moveLeft.addActionListener(e -> {
                model.getCurrentPlayer().move(Direction.WEST);
                if (model.getCurrentPlayer().checkActionCount()) disableActions();
            });
            this.add(moveLeft);

            this.moveRight = new JButton("Right");
            moveRight.addActionListener(e -> {
                model.getCurrentPlayer().move(Direction.EAST);
                if (model.getCurrentPlayer().checkActionCount()) disableActions();
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

            this.dryHere = new JButton("Dry Here");
            dryHere.addActionListener(e -> { model.getCurrentPlayer().dry(Direction.NONE); });
            this.add(dryHere);

            JButton art = new JButton("Artefact");
            art.addActionListener(e -> model.getCurrentPlayer().snatchArtefact());
            this.add(art);

            //End of round
            JButton end = new JButton("End of round");
            end.addActionListener(e -> {
                model.endRound();
                enableActions();
            });
            this.add(end);
        }

        protected void enableActions(){
            this.moveDown.setEnabled(true);
            this.moveUp.setEnabled(true);
            this.moveRight.setEnabled(true);
            this.moveLeft.setEnabled(true);
            this.dryDown.setEnabled(true);
            this.dryUp.setEnabled(true);
            this.dryHere.setEnabled(true);
            this.dryLeft.setEnabled(true);
            this.dryRight.setEnabled(true);
        }

        protected void disableActions(){
    	    this.moveLeft.setEnabled(false);
    	    this.moveRight.setEnabled(false);
    	    this.moveUp.setEnabled(false);
    	    this.moveDown.setEnabled(false);
    	    this.dryDown.setEnabled(false);
    	    this.dryUp.setEnabled(false);
    	    this.dryHere.setEnabled(false);
    	    this.dryLeft.setEnabled(false);
    	    this.dryRight.setEnabled(false);
        }
	}
	private Controller controller;
    /**
     * Grid displays the state of the game
     */
	private Grid grid;

	protected  class InfoDisplay extends JPanel implements Observer {

        private JTextField name;
        private JLabel actions;
        private JLabel keys;

        private Player p;

        public InfoDisplay(Player p){
            this.p = p;

            p.addObserver(this);

            this.actions = new JLabel(Integer.toString(p.getActions()));
            this.keys = new JLabel(p.getKeychain().toString());
            this.name = new JTextField(p.getName());
            this.name.addActionListener(e -> {
                this.p.setName(this.name.getText());
            });

            this.setLayout(new GridLayout(0, 2));

            this.add(new JLabel("Name :"));
            this.add(this.name);

            this.add(new JLabel("Actions left : "));
            this.add(this.actions);

            this.add(new JLabel("Keys : "));
            this.add(this.keys);
        }

        @Override
        public void update(Observable o, Object arg) {
            this.keys.setText(p.getKeychain().toString());
            this.actions.setText(Integer.toString(p.getActions()));
        }
    }

    /**
     * Main program mainWindow
     */
	private JFrame mainWindow, people;

	public IslandView(String name, IslandModel model, boolean gui){
		super();

		this.model = model;

		/** Setting up mainWindow */
        this.mainWindow = new JFrame(name);
        this.mainWindow.setName(name);
        this.mainWindow.setLocation(10, 300);
        this.mainWindow.setLayout(new FlowLayout());
        this.mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /**  */
        this.people = new JFrame(name);
        this.people.setName(name);
        this.people.setLocation(10, 100);
        this.people.setLayout(new GridLayout(2, 0));
        this.people.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        /** Creating the Components */
	    this.controller = new Controller(this);
	    this.grid = new Grid(this);

        /** Adding them to the frame */
	    this.mainWindow.add(this.grid);

	    for (Player p : this.model.getPlayers()){
	        this.people.add(new InfoDisplay(p));
        }

	    this.mainWindow.add(this.controller);

        /** Pack and show */
	    this.mainWindow.pack();
	    this.people.pack();
	    this.people.setVisible(gui);
	    this.mainWindow.setVisible(gui);
	}

}

