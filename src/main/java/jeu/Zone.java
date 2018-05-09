package jeu;

public class Zone {
	private ZoneState state;
	private int counter = 0;
	private String name = "Default";
	private IslandModel model;

	public Zone(IslandModel model){
		super();
		this.state = ZoneState.DRY;
		this.name = this.state.name();
		this.model = model;
	}

	protected ZoneState floodSelf(){
        switch (this.state) {
            case DRY:
                this.state = ZoneState.WET;
                break;
            case WET:
                this.state = ZoneState.SUBMERGED;
                break;
        }
        this.name = this.state.name();
        return this.state;
	}


	public IslandModel getIsland(){ return this.model; }
	public String getName(){ return this.name; }
	public ZoneState getState() { return state; }
	public String toString(){ return this.name + "(" + this.counter + ")"; }

}

