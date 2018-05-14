package jeu;

import java.awt.*;
import java.util.*;

public class IslandModel extends Observable {

	public static final int HEIGHT = 10;
	public static final int WIDTH = 15;
	public static Random randGen;

	private Zone[][] ground;
	private ArrayList<Zone> floodableZones;

	private IslandView view;

	private ArrayList<Player> players;
	private int currentPlayerIndex = 0;
	protected Direction currentPlayerDirection;
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
        ArrayList<Zone> zoneSelector = new ArrayList<>();
        this.floodableZones = new ArrayList<>();
		this.ground = new Zone[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++){
			for (int j = 0 ; j < WIDTH ; j++){
				this.ground[i][j] = new Zone(this, Artefact.NONE);
				this.floodableZones.add(this.ground[i][j]);
				zoneSelector.add(this.ground[i][j]);
			}
		}

		//... and spawning Artefacts randomly on the board ...
        ArrayList<Artefact> artefacts = new ArrayList<>(Arrays.asList(Artefact.A_AIR,
                Artefact.A_FIRE,
                Artefact.A_GROUND,
                Artefact.A_WATER));
		for (Artefact a : artefacts){
		    Zone zone = zoneSelector.get(randGen.nextInt(zoneSelector.size()));
		    zone.placeArtefact(a);
		    zoneSelector.remove(zone);
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
        if (this.gameover()){
            System.out.println("GAMEOVER");
        }
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

    private boolean gameover(){
	    for (Player p : this.players){
	        if (this.ground[p.getyPos()][p.getxPos()].getState() == ZoneState.SUBMERGED)
	            return true;
        }
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (this.ground[i][j].getArtefact() != Artefact.NONE && this.getStateAtPos(i, j) == ZoneState.SUBMERGED)
                    return true;
            }
        }
        return false;
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

    protected Color getArtefactColor(int line, int col){
	    Color color = Color.GRAY;
	    switch (this.ground[line][col].getArtefact()) {
            case A_AIR:
                color = new Color(98, 248, 255);
                break;
            case A_FIRE:
                color = new Color(255, 100, 40);
                break;
            case A_WATER:
                color = new Color(0, 123, 211);
                break;
            case A_GROUND:
                color = new Color(131, 96, 77);
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
        Scanner scan = new Scanner(System.in);
        System.out.println("How many players do you wish to play with ?");
        int nop = scan.nextInt();
        System.out.println("All right. Let's go.");
	    IslandModel model = new IslandModel(true, nop);
	    System.out.println(model);
    }

}

