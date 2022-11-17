/* Caitlin Wong */

import java.util.ArrayList;




public class Map {
    private boolean [][] map; 
    private int row;
    private int col;
    private ArrayList<int[]> obstacles;


    public Map(int choice){ 
        this.map = new boolean[80][60];
        this.row = 80;
        this.col = 60;
        this.obstacles = new ArrayList<int[]>();
        buildMap();
        if (choice == 1) { 
        	buildCross();
        }
        if (choice == 2) {
        	buildTunnel();
        }
        if (choice == 3) {
        	buildMaze();
        }
    }

    /* Builds the initial map where the outer edge is marked as occupied
     * Occupied spots = false
     * Unoccupied spots = true
     */
    private void buildMap(){
        for (int i = 0; i< map.length; i+=1){ 
            for (int j = 0; j< map[i].length; j++){ 
                // Sets borders (walls) as occupied
                if (i == 0 || j == 0 || i == map.length - 1
                    || j == map[i].length - 1) {
                	obstacles.add(new int[] {i,j});
                        map[i][j] = false;
                }
                else {
                    map[i][j] = true;
                }
            }
        }
    }
    
    
    private void buildCross() {
    	// build vertical barrier
		for (int y = 0; y < 10; y++) {
			map[40][y] = false;
			obstacles.add(new int[] {40,y});
			map[40][y + 50] = false;
			obstacles.add(new int[] {40,y+50});
		}
		// builds horizontal barrier
		for (int x = 0; x< 20; x++) { 
			map[x][30] = false;
			obstacles.add(new int[] {x,30});
			map[79-x][30] = false;
			obstacles.add(new int[] {79-x,30});
		}
    }
    
    private void buildTunnel() {
        // build vertical barrier
        for (int y = 0; y < 10; y++) {
            // upper tunnel
            map[35][y] = false;
            obstacles.add(new int[] {35,y});
            map[45][y] = false;
            obstacles.add(new int[] {45,y});

            // lower tunnel
            map[35][y + 50] = false;
            obstacles.add(new int[] {35,y+50});
            map[45][y + 50] = false;
            obstacles.add(new int[] {45,y+50});
        }

        // builds horizontal barrier
        for (int x = 0; x< 20; x++) { 
            // right tunnel
            map[x][25] = false;
            obstacles.add(new int[] {x,25});
            map[x][35] = false;
            obstacles.add(new int[] {x,35});

            // left tunnel
            map[79-x][25] = false;
            obstacles.add(new int[] {79-x,25});
            map[79-x][35] = false;
            obstacles.add(new int[] {79-x,35});

        }
    }
    
    private void buildMaze() {
        // builds Horizontal barrier
        for (int x = 0; x < 15; x++) {
            // Upper Right
            map[x+10][13] = false;
            obstacles.add(new int[] {x+10,13});
            // Upper Left
            map[69 - x][13] = false;
            obstacles.add(new int[] { 69 - x, 13 });
            // Lower Right
            map[x + 10][40] = false;
            obstacles.add(new int[] { x + 10, 40 });
            // Lower Left
            map[69 - x][40] = false;
            obstacles.add(new int[] { 69 - x, 40 });

        }
        // builds vertical barrier
        for (int y = 0; y < 15; y++) {
            //Upper Right 
            map[25][5 + y] = false;
            obstacles.add(new int[] { 25, 5+y});
            // Lower Right 
            map[25][33 + y] = false;
            obstacles.add(new int[] { 25, 33+y });
            // Upper Left 
            map[54][5 + y] = false;
            obstacles.add(new int[] {54, 5+y});
            // Lower Left 
            map[54][33 + y] = false;
            obstacles.add(new int[] { 54, 33+y});
        }

    }

    
    /* Returns the map's row amount */
    public int getRow(){ 
        return this.row;
    }


    /* Returns the map's column amount */
    public int getCol(){
        return this.col;
    }

    /* Determines if a cell is an opsticle */
    public boolean isObstacle(int r, int c) { 
    	for(int[] coord : obstacles) { 
    		if ((coord[0] == r) && (coord[1] == c)){ 
    			return true;
    		}
    	}
    	return false;
    }
   

    /* Returns false if there is an object at that specific location and true if there is not */
    public boolean isOccupied(int r, int c){ 
    	if ((r > 0)  || (r < row) && (c > 0) || (c < col)){ 
    		return map[r][c];
    	}
    	System.out.println("Coordinates are out of bounds");
    	return true;
    }


    /* Marks where an object is in the game */
    public void updateLoc(int r, int c){
    	if ((r > 0)  || (r < row) && (c > 0) || (c < col)){ 
    		map[r][c] = false;
    	}
    	System.out.println("Coordinates are out of bounds");
    }


    /* Returns the map  */
    public boolean[][] getMap(){
        return map;
    }


    /* Rotates map so it matches the UI screen orientation */
    private boolean[][] rotateMap(){ 
        boolean[][] rotated = new boolean[col][row];
        for (int r = 0; r < row; r +=1){
            for( int c = 0; c< col; c+=1){ 
                rotated[c][row-1-r] = map[r][c];
            }
        }
        return rotated;
    }

    
    /* Prints out a String representation of the Map */
    public String toString(){
        String mapInfo = "";
        boolean[][] rotated = rotateMap(); 
        for (int i  = 0; i< rotated.length;i+=1){ 
            for (int j = 0; j<rotated[i].length; j+=1){ 
                if (rotated[i][j] == true){ 
                    mapInfo += ".";
                }else{ 
                    mapInfo += "X";
                }
            }
            mapInfo+= "\n";
        }
        return mapInfo;
    }
}