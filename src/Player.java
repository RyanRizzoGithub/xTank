/* Author(s): Ryan Rizzo & Caitlin Wong
 * File: XTankSelection.java
 * Class: CSC335
 * Data: November 12, 2022
 */
public class Player {

    private int kills; 
    private String name;
    private Tank tank;

    /* Constructor */
    public Player(String name, Tank tank){ 
        this.name = name;
        this.kills = 0;
        this.tank = tank;
    }
    
    /* Returns the tank this player controls */
    public Tank getTank() {
    	return tank;
    }
    
    /* Returns player's name */
    public String getName(){ 
        return this.name;
    }

    /* Keeps track of the number tanks the player has killed*/
    public void kill(){ 
        kills +=1;
    }

    /* returns the number of kills the player done */
    public int getKills(){ 
        return this.kills;
    }

    /*Player Info Summary */
    public String toString(){ 
        String playerInfo = name + " has killed " + String.valueOf(kills) + " tanks.";
        return playerInfo;
    }
    
}