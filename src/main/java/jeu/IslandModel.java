package jeu;

public class IslandModel {

	public static final int HAUTEUR = 6;
	public static final int LARGEUR = 6;
	public static final int SCALE = 60;
	private Zone[][] ground;

	public Grid gridView;

	public IslandModel(){
		super();
		this.ground = new Zone[HAUTEUR][LARGEUR];
		for (int i = 0 ; i <= HAUTEUR ; i++){
			for (int j = 0 ; j <= LARGEUR ; j++){
				this.ground[i][j] = new Zone();
			}
		}
	}

	public void cell(){
		
	}

}

