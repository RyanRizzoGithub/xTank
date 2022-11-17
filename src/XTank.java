
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;


/* Author(s): Ryan Rizzo
 * File: XTank.java
 * Class: CSC335
 * Data: November 12, 2022
 */

public class XTank {
	/* Main function for the program */
	public static void main(String[] args) throws Exception {
        try (var socket = new Socket("127.0.0.1", 59896)) {
        	// Create server sockets
        	DataInputStream in = new DataInputStream(socket.getInputStream());
        	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        	
        	// Start the selection ui
        	var selectionUI = new XTankSelectionUI();
        	selectionUI.start();
        	int numPlayers = selectionUI.getNumPlayer();
        	String map = selectionUI.getMap();
        	String armor = selectionUI.getArmor();
        	String weapon = selectionUI.getWeapon();
        	
        	// start the game
            var ui = new XTankUI(in, out, numPlayers, map, armor, weapon);
            ui.start();
        }
    }
}


