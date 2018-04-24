package jeu;

public class Zone {
	public ZoneState state;
	public int counter = 0;
	public String name;
	public IslandModel model;

	public Zone(){
		super();
		this.state = ZoneState.DRY;
	}

	public IslandModel getIsland(){ return this.model; }
	public String getName(){ return this.name; }
	public String toString(){ return ""; }
	
	public void wet() {
		return;
	}

}

