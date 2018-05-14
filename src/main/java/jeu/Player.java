package jeu;

import java.util.ArrayList;
import java.util.Observable;

public class Player extends Observable{

    private IslandModel model;
    private String name;
    private int xPos, yPos;
    private int actions = 3;
    private ArrayList<Key> keychain;

    private class PositionOutOfBoundsException extends IndexOutOfBoundsException {
        PositionOutOfBoundsException(Direction d){
            super("Position out of bounds : " + d.name());
        }
    }

    public Player(IslandModel m){
       this.model = m;

       this.name = "  ";

       this.xPos = IslandModel.randGen.nextInt(IslandModel.WIDTH);
       this.yPos = IslandModel.randGen.nextInt(IslandModel.HEIGHT - 1);

       this.keychain = new ArrayList<>();

    }

    protected void move(Direction d) {

        if (this.checkActionCount() || this.checkDirection(d) == ZoneState.SUBMERGED) return;

        switch (d) {
            case NORTH:
                this.yPos--;
                break;
            case EAST:
                this.xPos++;
                break;
            case WEST:
                this.xPos--;
                break;
            case SOUTH:
                this.yPos++;
                break;
        }
        this.actions--;

        setChanged();
        this.notifyObservers();
        System.out.println(this);
    }

    protected void dry(Direction d){

        // CHeck if the player still has enough actions to play
        ZoneState zs = this.checkDirection(d);
        if (this.actions == 0 || !(zs == ZoneState.WET)) return;

        //Passing the right position to the IslandModel.dryZone() function
        switch (d) {
            case NORTH:
                this.model.dryZone(this.xPos, this.yPos - 1);
                break;
            case EAST:
                this.model.dryZone(this.xPos + 1, this.yPos);
                break;
            case WEST:
                this.model.dryZone(this.xPos - 1, this.yPos);
                break;
            case SOUTH:
                this.model.dryZone(this.xPos, this.yPos + 1);
                break;
            case NONE:
                this.model.dryZone(this.xPos, this.yPos);
                break;
        }

        //Update actions count
        this.actions--;

        setChanged();
        notifyObservers();
        System.out.println(this);
    }

    protected void snatchArtefact(){
        Artefact a = this.model.checkForArtefacts(xPos, yPos);
        Key k = this.artefactToKey(a);
        if (a != Artefact.NONE && this.keychain.contains(k)){
            this.model.fetchArtefact(xPos, yPos);
            this.actions--;
            setChanged();
            notifyObservers();
        }
        System.out.println(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    private Key artefactToKey(Artefact a){
        switch (a) {
            case A_AIR:
                return Key.K_AIR;
            case A_FIRE:
                return Key.K_FIRE;
            case A_WATER:
                return Key.K_WATER;
            case A_GROUND:
                return Key.K_GROUND;
            default:
                return null;
        }
    }

    protected void resetActions(){
        this.actions = 3;
        setChanged();
        notifyObservers();
    }

    protected void addKey(Key k){
        this.keychain.add(k);
        setChanged();
        notifyObservers();
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    private ZoneState checkDirection(Direction d){
        ZoneState dZoneState = ZoneState.UNKNOWN;
        switch (d) {
            case NORTH:
                if (this.yPos - 1 < 0) throw new PositionOutOfBoundsException(d);
                dZoneState = this.model.getStateAtPos(this.yPos - 1, this.xPos);
                break;
            case EAST:
                if (this.xPos + 1 > IslandModel.WIDTH) throw new PositionOutOfBoundsException(d);
                dZoneState = this.model.getStateAtPos(this.yPos, this.xPos + 1);
                break;
            case WEST:
                if (this.xPos - 1 < 0) throw new PositionOutOfBoundsException(d);
                dZoneState = this.model.getStateAtPos(this.yPos, this.xPos - 1);
                break;
            case SOUTH:
                if (this.yPos + 1 > IslandModel.HEIGHT) throw new PositionOutOfBoundsException(d);
                dZoneState = this.model.getStateAtPos(this.yPos + 1, this.xPos);
                break;
            case NONE:
                dZoneState = this.model.getStateAtPos(this.yPos, this.xPos);
                break;
        }
        return dZoneState;
    }

    protected boolean checkActionCount(){
        return this.actions < 1;
    }

    public String getName() {
        return name;
    }

    public int getActions() {
        return actions;
    }

    public ArrayList<Key> getKeychain() {
        return keychain;
    }

    public String toString(){
        return this.name + " @ (" + this.xPos + ", " + this.yPos + ") " + " | Actions left : " + this.actions
                + "\nKeys : " + this.keychain;
    }

}
