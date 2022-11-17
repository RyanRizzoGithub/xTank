import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;

/* Author(s): Caitlin Wong & Ryan Rizzo
 * File: Tank.java
 * Class: CSC335
 * Data: November 12, 2022
 */

public class Tank {
    private int health;
    private int xCoord; 
    private int yCoord;
    private int angle;
    private int magazine;
    private int shotsTaken;
    private int kills;
    private Color color;
    private ArrayList<Projectile> bullets;
    private String weapon;

    public Tank(Color color, int x, int y, int angle, int health) { 
        this.health = health;
        this.xCoord = x;
        this.yCoord = y;
        this.angle = angle;
        this.color = color;
        this.magazine = 5;
        this.bullets = new ArrayList<Projectile>();
        this.shotsTaken = 0;
        this.kills = 0;
        this.weapon = "Normal";
    }
    
    /* Sets the weapon for the tank to use */
    public void setWeapon(String weapon) {
    	this.weapon = weapon;
    }
    
    /* Returns the type of weapon this tank is using */
    public String getWeapon() {
    	return this.weapon;
    }
    
    /* Increases this tanks kill count */
    public void kill() {
    	this.kills++;
    }
    
    /* Reloads the tanks weapon */
    public void reload() {
    	this.magazine = 5;
    }
    
    /* Returns number shots this tank has taken */
    public int getShotsTaken() {
    	return shotsTaken;
    }
    
    /* returns all projectiles controled by tank */
    public ArrayList<Projectile> getProjectiles() {
    	return this.bullets;
    }
    
    /* Adds a bullet to list */
    public void addProjectile(Projectile proj) {
    	shotsTaken++;
    	this.magazine--;
    	bullets.add(proj);
    }
    
    /* returns number of bullets in tank's magazine */
    public int getMagazine() {
    	return this.magazine;
    }
    
    /* returns the tank's angle */
    public int getAngle() {
    	return angle;
    }
    
    /* Sets the tank's angle */
    public void setAngle(int i) {
    	angle = i;
    }
    
    /* Returns tank's color */
    public Color getColor(){
        return this.color;
    }

    /* Returns Tank's X Coordinate */
    public int getXCoord(){ 
        return this.xCoord;
    }

    /* Returns Tank's Y Coordinate */
    public int getYCoord(){ 
        return this.yCoord;
    }

    /* Sets tank's location  */
    public void setCoord(int newXCoord, int newYCoord){ 
        xCoord = newXCoord; 
        yCoord = newYCoord;
    }

    /* Returns tank's health status  */
    public int getHealth(){ 
        return this.health;
    }

    /*Checks if the tank is dead */
    public boolean isDead(Player player){ 
        if (getHealth() == 0){ 
            player.kill();
            return true;
        }
        return false;
    }

    /* Checks if projectile has collided with tank */
    public void isHit(){
        this.health--;
    }

    /* Tank info summary */
    public String toString(){ 
        String tankInfo = "Location: "+ String.valueOf(xCoord) + " , " + String.valueOf(yCoord) + "\n";
        tankInfo += "Health Status: "+ String.valueOf(health);
        return tankInfo;

    }
}
