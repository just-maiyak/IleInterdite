package jeu;

import java.util.Scanner;

public class Player {

    private IslandModel model;
    private String name;
    private int xPos, yPos;

    private class PositionOutOfBoundsException extends IndexOutOfBoundsException {
        PositionOutOfBoundsException(Direction d){
            super("Position out of bounds : " + d.name());
        }
    }

    public Player(IslandModel m){
       this.model = m;

       Scanner scanner = new Scanner(System.in);
       System.out.println("What's your name ?");
       this.name = scanner.next();
       System.out.println("Cool name, " + this.name + " !");

       this.xPos = this.yPos = 0;

    }

    protected void move(Direction d) {

        checkDirection(d);
        switch (d) {
            case NORTH:
                this.xPos++;
                break;
            case EAST:
                this.yPos++;
                break;
            case WEST:
                this.yPos--;
                break;
            case SOUTH:
                this.xPos--;
                break;
        }
        System.out.println(this);
    }

    private void checkDirection(Direction d){
        switch (d) {
            case NORTH:
                if (this.xPos + 1 > IslandModel.HEIGHT) throw new PositionOutOfBoundsException(d);
                break;
            case EAST:
                if (this.yPos + 1 > IslandModel.WIDTH) throw new PositionOutOfBoundsException(d);
                break;
            case WEST:
                if (this.yPos - 1 < 0) throw new PositionOutOfBoundsException(d);
                break;
            case SOUTH:
                if (this.xPos - 1 < 0) throw new PositionOutOfBoundsException(d);
                break;
        }
    }

    public String toString(){
        return this.name + " @ (" + this.xPos + ", " + this.yPos + ")";
    }

}
