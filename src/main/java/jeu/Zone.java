package jeu;

public class Zone {
	private ZoneState state;
	private Artefact artefact;
	private String name;
	private IslandModel model;
	private boolean heliport = false;

	public Zone(IslandModel model, Artefact a){
		super();
		this.state = ZoneState.DRY;
		this.name = this.state.name();
		this.model = model;

		this.artefact = a;
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
	protected void drySelf(){
	    this.state = ZoneState.DRY;
	    this.name = this.state.name();
    }
    protected Artefact giveArtefact(){
        // The game is supposed to be over if an artefact is submerged
        // we're checking just in case
	    assert (this.state != ZoneState.SUBMERGED) : "Zone is submerged, the game is over.";
	    Artefact a = this.artefact;
        this.artefact = Artefact.NONE;
        return a;
    }
    protected void placeArtefact(Artefact a){
	    this.artefact = a;
    }
    protected void placeHeliport(){
	    this.heliport = true;
    }
    protected boolean isHeliport(){
	    return this.heliport;
    }

	public IslandModel getIsland(){ return this.model; }
	public String getName(){ return this.name; }
    public Artefact getArtefact() {
        return artefact;
    }
    public ZoneState getState() { return state; }
	public String toString(){ return this.name + " "
            + ((this.artefact != Artefact.NONE) ? this.artefact.name() : "")
            + ((this.heliport) ? "Heliport" : "") ; }

}

