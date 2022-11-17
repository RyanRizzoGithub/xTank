import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/* Author(s): Ryan Rizzo
 * File: XTankSelection.java
 * Class: CSC335
 * Data: November 12, 2022
 */

public class XTankSelectionUI {
	private Display display;
	private int numPlayers;
	private String map;
	private String armor;
	private String weapon;

	/* Creates a pop-up window where the user can select the number of players,
	 * the map they want to play, the weapon to be used, and the amount of armor each
	 * tank will have
	 */
	public void start() {
		display = new Display();
		Shell shell = new Shell(display);
		shell.setText("xtank");
		shell.setLayout(new FillLayout());
		shell.setSize(150, 275);
		shell.setLayout(new GridLayout());
		
		// Number of players
		Label numPlayerLabel = new Label(shell, SWT.NONE);
		numPlayerLabel.setText("Number of Players: ");
		
		String[] playerOptions = {"1", "2", "3", "4"};
		Combo numPlayerCombo = new Combo(shell, SWT.DROP_DOWN);
		numPlayerCombo.setItems(playerOptions);
		
		// The map to play
		Label mapLabel = new Label(shell, SWT.NONE);
		mapLabel.setText("Map: ");
		
		String[] mapOptions = {"Default", "Split", "Cross", "Maze"};
		Combo mapOptionCombo = new Combo(shell, SWT.DROP_DOWN);
		mapOptionCombo.setItems(mapOptions);
		
		// The amount of armor
		Label armorLabel = new Label(shell, SWT.NONE);
		armorLabel.setText("Armor: ");
		
		String[] armorOptions = {"None", "Low", "Medium", "High"};
		Combo armorOptionCombo = new Combo(shell, SWT.DROP_DOWN);
		armorOptionCombo.setItems(armorOptions);
		
		// The type of weapon
		Label weaponLabel = new Label(shell, SWT.NONE);
		weaponLabel.setText("Weapon: ");
		
		String[] weaponOptions = {"Default", "Shotgun", "Cannon"};
		Combo weaponOptionCombo = new Combo(shell, SWT.DROP_DOWN);
		weaponOptionCombo.setItems(weaponOptions);
		
		Button submitButton = new Button(shell, SWT.PUSH);
		submitButton.setText("Submit");
		
		/* Selection listener for the submit button */
		submitButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				if (!numPlayerCombo.getText().equals("") && !mapOptionCombo.getText().equals("")
						&& !armorOptionCombo.getText().equals("") && !weaponOptionCombo.getText().equals("")) {
					numPlayers = Integer.parseInt(numPlayerCombo.getText());
					map = mapOptionCombo.getText();
					weapon = weaponOptionCombo.getText();
					armor = armorOptionCombo.getText();
					shell.dispose();
					
				}
			}
			public void widgetDefaultSelected(SelectionEvent event) {
				
			}
		});
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();	
	}
	
	/* Returns the number of players after selected */
	public int getNumPlayer() {
		return this.numPlayers;
	}
	
	/* Returns the map after selected */
	public String getMap() {
		return this.map;
	}
	
	/* Return the armor selected */
	public String getArmor() {
		return this.armor;
	}
	
	/* Return the weapon selected */
	public String getWeapon() {
		return this.weapon;
	}
}
