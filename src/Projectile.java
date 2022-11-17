/* Author(s): Ryan Rizzo & Caitlin Wong
 * File: Projectile.java
 * Class: CSC335
 * Data: November 12, 2022
 */
public class Projectile {
    
    private int xCoord; 
    private int yCoord; 
    private int damage; 
    private int time;
    private float angle;
    private boolean impacted;
    private Tank shooter;

    /* Constructor */
    public Projectile(int time, float angle, Tank shooter){ 
        this.xCoord = shooter.getXCoord();
        this.yCoord = shooter.getYCoord(); 
        this.damage = 50;
        this.time = time;
        this.angle = angle;
        this.impacted = false;
        this.shooter = shooter;
    }
    
    /* Returns the tank who shot this projectile */
    public Tank getShooter() {
    	return this.shooter;
    }
    
    /* True if projectile is still moving */
    public boolean isActive() {
    	return !impacted;
    }
    
    /* makes the projectile impacted */
    public void impact() {
    	this.impacted = true;
    }
    
    /* returns time bulelt was shot */
    public int getTime() {
    	return this.time;
    }
    
    /* Returns the angle bullet shot from */
    public float getAngle() {
    	return this.angle;
    }

    /* Returns Projectile's X Coordinate */
    public int getXCoord(){
        return this.xCoord;
    }

    /* Returns Projectile's Y Coordinate */
    public int getYCoord(){
        return this.yCoord;
    }

    /* Sets Projectile's location */
    public void updateCoord(int newXCoord, int newYCoord){
        xCoord = newXCoord;
        yCoord = newYCoord;
    }
    
    /* Returns Projectile's damage amount */
    public int getDamage(){
        return this.damage;
    }
    
    /*Projectile Info Summary */
    public String toString(){
        String projectileInfo = "Location: " + String.valueOf(xCoord) +" , " 
        + String.valueOf(yCoord) + "\n Damage Amount: " + String.valueOf(damage);
        return projectileInfo;
    }
}
