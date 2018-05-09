package jeu;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class IslandModel extends Observable {

	public static final int HEIGHT = 10;
	public static final int WIDTH = 15;
	public static Random randGen;

	private Zone[][] ground;
	private ArrayList<Zone> floodableZones;

	private IslandView view;

	private ArrayList<Player> players;
	protected Player currentPlayer;
    /** As Java encodes chars with 4 bits
     *  we can use that to model the artefacts players have acquired */
    private char artefacts;

	public IslandModel(boolean gui, int nOfPlayers){
		super();

		randGen = new Random();

		//Create as many players as desired
		this.players = new ArrayList<>(nOfPlayers);
        for (int i = 0; i < nOfPlayers; i++) {
            this.players.add(new Player(this));
        }
        //Create reference to the current player
        this.currentPlayer = this.players.get(0);

		//Initialising the zones first...
        this.floodableZones = new ArrayList<>();
		this.ground = new Zone[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++){
			for (int j = 0 ; j < WIDTH ; j++){
				this.ground[i][j] = new Zone(this);
				this.floodableZones.add(this.ground[i][j]);
			}
		}
	    // ... so we can then create the interface and link those to the tiles from the Grid
		this.view = new IslandView("Ile Interdite", this, gui);
	}

	public void endRound() {
        if (this.floodableZones.size() == 0) return;

        for (int i = 0; i < 3; i++) {
            Zone newZone = this.floodableZones.get(randGen.nextInt(this.floodableZones.size()));
            ZoneState newState = newZone.floodSelf();
            if (newState == ZoneState.SUBMERGED)
                this.floodableZones.remove(newZone);
        }
        notifyObservers();
        System.out.println(this);
    }

	public ZoneState getState(int x, int y){
	    return this.ground[x][y].getState();
    }

	public String toString(){

	    StringBuilder sb = new StringBuilder();

	    for (Zone[] zLine : this.ground){
	        sb.append("| ");
            for (Zone z : zLine) {
                sb.append(z).append(" | ");
            }
            sb.append("\n");
        }
		return sb.toString();
	}

	public static void main(String... args){
	    IslandModel model = new IslandModel(true, 2);
	    System.out.println(model);
    }

}

