package jeu;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
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
	private int currentPlayerIndex = 0;
    /** As Java encodes chars with 4 bits
     *  we can use that to model the artefacts players have acquired */
    private char snatchedArtefacts;

	public IslandModel(boolean gui, int nOfPlayers){
		super();

		//Init the random generator
		randGen = new Random();

		//Create as many players as desired
		this.players = new ArrayList<>(nOfPlayers);
        for (int i = 0; i < nOfPlayers; i++) {
            this.players.add(new Player(this));
        }
        //Create reference to the current player
        this.currentPlayerIndex = 0;

		//Initialising the zones first...
        this.floodableZones = new ArrayList<>();
		this.ground = new Zone[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++){
			for (int j = 0 ; j < WIDTH ; j++){
				this.ground[i][j] = new Zone(this, Artefact.NONE);
				this.floodableZones.add(this.ground[i][j]);
			}
		}

		//... and spawning Artefacts randomly on the board ...
        ArrayList<Artefact> artefacts = new ArrayList<>(Arrays.asList(Artefact.A_AIR,
                Artefact.A_FIRE,
                Artefact.A_GROUND,
                Artefact.A_WATER));
		ArrayList<Zone> zoneSelector = new ArrayList<>(this.floodableZones);
		for (Artefact a : artefacts){
		    zoneSelector.get(randGen.nextInt(zoneSelector.size())).placeArtefact(a);
        }
        //(cleanup for the JVM not to retain usable memory)
        artefacts.clear();
		zoneSelector.clear();

	    // ... so we can then create the interface
		this.view = new IslandView("Ile Interdite", this, gui);
	}

	public void endRound() {
        if (this.floodableZones.size() == 0) return;

        //Giving out keys
        this.distributeKey();

        //Changing player
        this.getCurrentPlayer().resetActions();
        this.nextPlayer();

        //Flooding zones
        for (int i = 0; i < 3; i++) {
            Zone newZone = this.floodableZones.get(randGen.nextInt(this.floodableZones.size()));
            ZoneState newState = newZone.floodSelf();
            if (newState == ZoneState.SUBMERGED)
                this.floodableZones.remove(newZone);
        }

        //Notify obs.
        notifyObservers();
        System.out.println(this);
    }

    protected void dryZone(int x, int y){
	    this.ground[y][x].drySelf();
	    notifyObservers();
    }

    protected void fetchArtefact(int x, int y){
        Artefact a = this.ground[y][x].giveArtefact();
        switch (a){
            case NONE:
                break;
            case A_AIR:
                this.snatchedArtefacts+=0b1000;
                break;
            case A_FIRE:
                this.snatchedArtefacts+=0b0100;
                break;
            case A_WATER:
                this.snatchedArtefacts+=0b0010;
                break;
            case A_GROUND:
                this.snatchedArtefacts+=0b0001;
                break;
        }
        notifyObservers();
    }

    protected Artefact checkForArtefacts(int x, int y){
	    return this.ground[y][x].getArtefact();
    }

    private void distributeKey(){
	    switch (randGen.nextInt(3)){
            case 1:
                //Key
                this.getCurrentPlayer().addKey(Key.values()[randGen.nextInt(4)]);
                break;
            case 2:
                //Flood
                this.ground[this.getCurrentPlayer().getyPos()][this.getCurrentPlayer().getxPos()].floodSelf();
                break;
            default:
                break;
        }
    }

    protected Color getGroundColorAtPos(int line, int col){
	    Color color = Color.GRAY;

        switch (this.ground[line][col].getState()) {
            case DRY:
                color = Color.white;
                break;
            case WET:
                color = new Color(90, 140, 190);
                break;
            case SUBMERGED:
                color = new Color(30, 30, 50);
                break;
        }

        return color;
    }

    protected Player getCurrentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }

    protected ZoneState getStateAtPos(int line, int col){
	    return this.ground[line][col].getState();
    }

    private void nextPlayer(){
	    this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
    }

    public ArrayList<Player> getPlayers() {
        return players;
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

