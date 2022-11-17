import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/* Author(s): Ryan Rizzo
 * File: XTankUI.java
 * Class: CSC335
 * Data: November 12, 2022
 */

public class XTankUI {
	// The location and direction of the "tank"
	private int x = 400;
	private int y = 500;
	private int directionX = 10;
	private int directionY = -10;
	
	// Game variables
	private int numPlayers;
	private String mapName;
	private String armor;
	private String weapon;
	private String execution = "";
	private int time;
	private ArrayList<Projectile> bullets;
	private boolean[][] map;
	private boolean gameOver;
	
	// Ui objects
	private Canvas canvas;
	private Display display;
	
	// Server objects
	DataInputStream in; 
	DataOutputStream out;
	
	/* Constructor */
	public XTankUI(DataInputStream in, DataOutputStream out, int numPlayers, String map, String armor, String weapon) {
		this.in = in;
		this.out = out;
		this.execution = "";
		this.bullets = new ArrayList<Projectile>();
		this.time = 0;
		this.numPlayers = numPlayers;
		this.mapName = map;
		this.weapon = weapon;
		this.armor = armor;
		this.gameOver = false;
	}
	
	/* Launches the user interface for the xTank game */
	public void start() {
		// Create display and shells
		display = new Display();
		Shell shell = new Shell(display);
		shell.setText("xtank");
		shell.setLayout(new FillLayout());
		shell.setSize(800, 600);
		
		// Determine which map is being played
		int mapCode = 0;
		if (mapName.equals("Cross")) {
			mapCode = 1;
		}
		if (mapName.equals("Split")) {
			mapCode = 2;
		} 
		if (mapName.equals("Maze")) {
			mapCode = 3;
		}
		Map map = new Map(mapCode);
		this.map = map.getMap();
		map.toString();
		//map = new Map(0).getMap();
		
		// Setup the health using armor value
		int health;
		if (armor.equals("None")) {
			health = 1;
		}
		if (armor.equals("Low")) {
			health = 2;
		}
		if (armor.equals("Medium")) {
			health = 3;
		} else {
			health = 4;
		}
		
		// Create the tanks
		Tank player1 = new Tank(shell.getDisplay().getSystemColor(SWT.COLOR_RED), 20, 20, 135, health);
		player1.setWeapon(weapon);
		Tank player2 = new Tank(shell.getDisplay().getSystemColor(SWT.COLOR_BLUE), 780, 20, 225, health);
		player2.setWeapon(weapon);
		Tank player3 = new Tank(shell.getDisplay().getSystemColor(SWT.COLOR_GREEN), 780, 520, 305, health);
		player3.setWeapon(weapon);
		Tank player4 = new Tank(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW), 20, 520, 35, health);
		player4.setWeapon(weapon);
		
		// Create the players
		Player one = new Player("one", player1);
		Player two = new Player("two", player2);
		Player three = new Player("three", player3);
		Player four = new Player("four", player4);
		Player[] players = {one, two, three, four};
		
		canvas = new Canvas(shell, SWT.NO_BACKGROUND);
		canvas.setSize(800, 600);
		
		// Add paint listener for key strokes
		canvas.addPaintListener(event -> {			
			event.gc.fillRectangle(canvas.getBounds());		
			
			// Draw each tank
			for(int i=0; i<numPlayers; i++) {
				drawTank(players[i].getTank(), event, shell);
			}
			
			// Draw each projectile
			for(int i=0; i<bullets.size(); i++) {
				if (bullets.get(i) != null) {
					if (bullets.get(i).isActive()) {
						drawProjectile(player1, event, shell, bullets.get(i));
					} else {
						bullets.remove(i);
					}
				}
			}
			// Draw the heads up display
			drawHUD(players, event, shell);
			
			// Draw the map
			drawMap(event, shell);
			
			findHits(players);
			
			
		});	

		canvas.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e) {
				System.out.println("mouseDown in canvas");
			} 
			public void mouseUp(MouseEvent e) {} 
			public void mouseDoubleClick(MouseEvent e) {} 
		});

		canvas.addKeyListener(new KeyListener() {
			
			public void keyPressed(KeyEvent e) {
				float angle = player1.getAngle();
				// If UP ARROW
				if (e.keyCode == 16777217) {
					try {
						out.writeInt(y + directionY);
						execution = "NONE";
						int newXCoord = (int) (player1.getXCoord() + Math.round(directionX * (Math.sin(Math.toRadians(angle)))));
						int newYCoord = (int) (player1.getYCoord() + Math.round(directionY * (Math.cos(Math.toRadians(angle)))));
						
						if (map.getMap()[newXCoord/10][newYCoord/10] != false) {
							player1.setCoord(newXCoord, newYCoord);
						}
						
						

						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				
				// If RIGHT ARROW
				else if (e.keyCode == 16777220) {
					try {
						out.writeInt(x + directionX);
						execution = "NONE";
						player1.setAngle(player1.getAngle() + 15);
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				// If DOWN ARROW
				else if (e.keyCode == 16777218) {
					try {
						out.writeInt(y - directionY);
						execution = "NONE";
						player1.setCoord((int) (player1.getXCoord() - Math.round(directionX * (Math.sin(Math.toRadians(angle))))),
								 		 (int) (player1.getYCoord() - Math.round(directionY * (Math.cos(Math.toRadians(angle))))));
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				// If LEFT ARROW
				else if (e.keyCode == 16777219) {	
					try {
						out.writeInt(x - directionX);
						execution = "NONE";
						angle -= 15;
						player1.setAngle(player1.getAngle() - 15);
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} 
				
				// If SPACE BAR
				else if(e.character == ' ') {
					if (player1.getProjectiles().size() == 0) {
						if (player1.getMagazine() > 0)
							System.out.println("Player 1 shot a bullet from (" + x +", " + y + ") at an angle of + " + angle + " degrees");
							Projectile proj = new Projectile(time, angle, player1);
							
							player1.addProjectile(proj);
							bullets.add(proj);
							if (player1.getWeapon().equals("Shotgun")) {
								Projectile projL = new Projectile(time, angle - 15, player1);
								player1.addProjectile(projL);
								bullets.add(projL);
								Projectile projR = new Projectile(time, angle  + 15, player1);
								player1.addProjectile(projR);
								bullets.add(projR);
							}
					}
					else {
						if (time - player1.getProjectiles().get(player1.getShotsTaken() - 1).getTime() > 10) {
							if (player1.getMagazine() > 0) {
								System.out.println("Player 1 shot a bullet from (" + x +", " + y + ") at an angle of + " + angle + " degrees");
								Projectile proj = new Projectile(time, angle, player1);
								player1.addProjectile(proj);
								bullets.add(proj);
							}
							if (player1.getWeapon().equals("Shotgun")) {
								if (player1.getMagazine() > 2) {
									Projectile projL = new Projectile(time, angle - 15, player1);
									player1.addProjectile(projL);
									bullets.add(projL);
									Projectile projR = new Projectile(time, angle  + 15, player1);
									player1.addProjectile(projR);
									bullets.add(projR);
								}
							}
						} else {
							System.out.println("Player 1 tried to shoot too early");
						}					
					}  
				}
				
				// If R KEY
				else if(e.character == 'r') {
					player1.reload();
				}
				
				// Clamp x
				if (x > 800) {
					x = 800;
				} 
				if (x < 0) {
					x = 0;
				}
				
				// Clamp y
				if (y > 575) {
					y = 575;
				}
				if (y < 0) {
					y = 0;
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
					
		Runnable runnable = new Runner();
		display.asyncExec(runnable);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
				try {
					TimeUnit.MILLISECONDS.sleep(100);
					//System.out.println("Game Tick: ");
					time++;
					canvas.redraw();
					int deadTanks = 0;
					for (int i=0; i<players.length; i++) {
						if (players[i].getTank().getHealth() == 0) {
							deadTanks++;
						}
					}
					if (numPlayers - 1 == deadTanks) {
						this.gameOver = true;
						shell.dispose();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
			}
		}
		display.dispose();		
	}
	
	/* Draws a tank onto the canvas */
	public void drawTank(Tank tank, PaintEvent event, Shell shell) {
		// Make sure tank is not dead
		if (tank.getHealth() > 0) {
			float angle = tank.getAngle();
			int xCoord = tank.getXCoord();
			int yCoord = tank.getYCoord();
			
			event.gc.setBackground(tank.getColor());
			
			// Draw the tank
			int topLeftX = (int) (((-15) * Math.cos(Math.toRadians(angle))) - ((-30) * Math.sin(Math.toRadians(angle))));
			int topLeftY = (int) (((-15) * Math.sin(Math.toRadians(angle))) + ((-30) * Math.cos(Math.toRadians(angle))));
			
			int topRightX = (int) (((15) * Math.cos(Math.toRadians(angle))) - ((-30) * Math.sin(Math.toRadians(angle))));
			int topRightY = (int) (((15) * Math.sin(Math.toRadians(angle))) + ((-30) * Math.cos(Math.toRadians(angle))));
			
			int botRightX = (int) (((15) * Math.cos(Math.toRadians(angle))) - ((30) * Math.sin(Math.toRadians(angle))));
			int botRightY = (int) (((15) * Math.sin(Math.toRadians(angle))) + ((30) * Math.cos(Math.toRadians(angle))));
			
			int botLeftX = (int) (((-15) * Math.cos(Math.toRadians(angle))) - ((30) * Math.sin(Math.toRadians(angle))));
			int botLeftY = (int) (((-15) * Math.sin(Math.toRadians(angle))) + ((30) * Math.cos(Math.toRadians(angle))));
			
			int topCannonX = (int) (((1) * Math.cos(Math.toRadians(angle))) - ((-40) * Math.sin(Math.toRadians(angle))));
			int topCannonY = (int) (((1) * Math.sin(Math.toRadians(angle))) + ((-40) * Math.cos(Math.toRadians(angle))));
			
			int[] rectCoords = {topLeftX + xCoord, topLeftY + yCoord, topRightX + xCoord, topRightY + yCoord, botRightX + xCoord,
					botRightY + yCoord, botLeftX + xCoord, botLeftY + yCoord};
			
			event.gc.fillPolygon(rectCoords);
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			event.gc.fillOval(xCoord-15, yCoord-15, 30, 30);
			event.gc.setLineWidth(4);
			event.gc.drawLine(xCoord, yCoord, topCannonX + xCoord, topCannonY + yCoord);
		}
	}
	
	/* Draws a projectile onto the canvas */
	public void drawProjectile(Tank tank, PaintEvent event, Shell shell, Projectile proj) {
			int xCoord = tank.getXCoord();
			int yCoord = tank.getYCoord();
			int dist = (time - proj.getTime()) * 10;
			int topCannonX = (int) (((1) * Math.cos(Math.toRadians(proj.getAngle()))) - ((-40) * Math.sin(Math.toRadians(proj.getAngle()))));
			int topCannonY = (int) (((1) * Math.sin(Math.toRadians(proj.getAngle()))) + ((-40) * Math.cos(Math.toRadians(proj.getAngle()))));
			int transformedX = (int) (((topCannonX + xCoord) + Math.round(dist * (Math.sin(Math.toRadians(proj.getAngle()))))));
			int transformedY = (int) ((topCannonY + yCoord) - Math.round(dist * (Math.cos(Math.toRadians(proj.getAngle())))));
			proj.updateCoord(transformedX, transformedY);
			if (!tank.getWeapon().equals("Cannon")) {
				event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
				event.gc.fillOval(transformedX - 2, transformedY, 5, 5);
			} else {
				event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
				event.gc.fillOval(transformedX - 5, transformedY, 20, 20);
			}
	}
	
	/* Draws the heads up display onto the canavs */
	public void drawHUD(Player[] players, PaintEvent event, Shell shell) {
		Image heart = new Image(display, "images/heart.png");
		Image bullet = new Image(display, "images/bullet.png");
		
		if (players.length > 0) {
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_RED));
			event.gc.fillRectangle(30, 540, 20, 20);
			for (int i=0; i<players[0].getTank().getHealth() - 1; i++) {
				event.gc.drawImage(heart, 60 + (i * 30), 540);
			}	
			for (int i=0; i<players[0].getTank().getMagazine(); i++) {
				event.gc.drawImage(bullet, 30 + (i * 30), 510);
			}
		}
		
		if (players.length > 1) {
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLUE));
			event.gc.fillRectangle(230, 540, 20, 20);
			for (int i=0; i<players[1].getTank().getHealth() - 1; i++) {
				event.gc.drawImage(heart, 260 + (i * 30), 540);
			}
			for (int i=0; i<players[1].getTank().getMagazine(); i++) {
				event.gc.drawImage(bullet, 230 + (i * 30), 510);
			}
		}
		
		if (players.length > 2) {
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_GREEN));
			event.gc.fillRectangle(430, 540, 20, 20);
			for (int i=0; i<players[2].getTank().getHealth() - 1; i++) {
				event.gc.drawImage(heart, 460 + (i * 30), 540);
			}	
			for (int i=0; i<players[2].getTank().getMagazine(); i++) {
				event.gc.drawImage(bullet, 430 + (i * 30), 510);
			}
		}
		
		if (players.length > 3) {
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
			event.gc.fillRectangle(630, 540, 20, 20);
			for (int i=0; i<players[3].getTank().getHealth() - 1; i++) {
				event.gc.drawImage(heart, 660 + (30 * i), 540);
			}
			for (int i=0; i<players[3].getTank().getMagazine(); i++) {
				event.gc.drawImage(bullet, 630 + (i * 30), 510);
			}
		}
	}
	
	/* Determines if any projectiles are colliding with and tanks */
	public void findHits(Player[] players) {
		for (int x = 0; x < players.length; x++) {
			Tank tank = players[x].getTank();
			ArrayList<Projectile> projectiles = tank.getProjectiles(); 
			for (int i=0; i < projectiles.size(); i++) {	
				Projectile proj = projectiles.get(i);
				int projX = proj.getXCoord();
				int projY = proj.getYCoord();
				for (int j = 0; j < players.length; j++) {					
					if (j != 0) {
						if (!tank.getWeapon().equals("cannon")) {
							if (players[j].getTank().getXCoord() > (projX - 20) && players[j].getTank().getXCoord() < (projX + 20)
									&& players[j].getTank().getYCoord() > (projY - 20) && players[j].getTank().getYCoord() < (projY + 20)) {
									if (proj.isActive()) {
										players[j].getTank().isHit();
										proj.getShooter().kill();
										proj.impact();
										System.out.println(proj.getShooter().getColor().toString() + " hit " + players[j].getTank().getColor().toString());
									}
							}
						} else {
							if (players[j].getTank().getXCoord() > (projX - 60) && players[j].getTank().getXCoord() < (projX + 60)
									&& players[j].getTank().getYCoord() > (projY - 60) && players[j].getTank().getYCoord() < (projY + 60)) {
									if (proj.isActive()) {
										players[j].getTank().isHit();
										proj.getShooter().kill();
										proj.impact();
										System.out.println(proj.getShooter().getColor().toString() + " hit " + players[j].getTank().getColor().toString());
									}
							}
						}
					}
				}
			}
		}
	}
	
	public void drawMap(PaintEvent event, Shell shell) {
		for (int i=0; i<map.length; i++) {
			for (int j=0; j<map[0].length; j++) {
				if (map[i][j] == false) {
					event.gc.drawRectangle((i * 10) - 5, (j * 10) - 5, 10, 10);
				}
			}
		}
	}
	
	
	class Runner implements Runnable {
		public void run() {
			try {
				if (in.available() > 0) {
					
					if (execution == "UP" || execution == "DOWN") {
						y = in.readInt();
					}
					if (execution == "RIGHT" || execution == "LEFT") {
						x = in.readInt();
						System.out.println(x);
					}
					canvas.redraw();
				}
			}
			
			catch(IOException ex) {
				System.out.println("The server did not respond (async).");
			}				
            display.timerExec(150, this);
		}
	};	
}


