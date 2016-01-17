/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DSTIGANT
 */

// class PacManMaze
// stores all the information about the board
// Primarily, the board consists of an array of squares each of which may contain
// a wall or a pellet etc.
// In addition, several squares are marked as points of interest.  These include
// the starting location of PacMan and each ghost as well as the regeneration
// location for the ghosts.
// Getter functions are provided for each of these attributes as well as to
// get the dimensions of the board and individual squares:
//
// getWidth(), getHeight() - get the dimensions of the board
// getSquare( Location ) - gets the square at the indicated Location (row, column pair)
// getActorLocations() - returns a HashMap of characters to spawn locations
public class PacManMaze
{
    // storage variables for all the data
    private PacManSquare [][] board;
    private HashMap<Character, ArrayList<Location>> actorLocations;
    private HashMap<Character, String> actorClasses;
    private ArrayList<String> fruitClasses;
    
    private void addLocation( Character c, Location l )
    {
        ArrayList<Location> locs = actorLocations.get( c );
        if ( locs == null )
        {
            locs = new ArrayList<>();
            actorLocations.put( c, locs );
        }
        
        locs.add( l );
    }
        
    // Constructor
    // provide the name of the file which contains the board configuration
    public PacManMaze( String file )
    {
        actorLocations = new HashMap<>();
        
        try
        {
            Scanner in = new Scanner( new File(file) );
            int nr = in.nextInt();
            int nc = in.nextInt();
            in.nextLine();
            
            board = new PacManSquare[nr][nc];
            
            for ( int i = 0; i < nr; i++ )
            {
                String row = in.nextLine();
                for ( int j = 0; j < nc; j++ )
                {
                    if ( row.charAt(j) == '.' )
                    {
                        board[i][j] = new PacManSquare( false, new PacManPellet() );
                    }
                    else if ( row.charAt(j) == '*' )
                    {
                        board[i][j] = new PacManSquare( false, new PacManPowerPellet() );
                    }
                    else if ( row.charAt(j) == 'X' )
                    {
                        board[i][j] = new PacManSquare( true, null );
                    }
                    else
                    {
                        board[i][j] = new PacManSquare( false, null );
                        
                        if ( row.charAt(j) != ' ' )
                        {
                            addLocation( row.charAt(j), new Location(i, j) );
                        }
                    }
                }
            }
            
            fruitClasses = new ArrayList<>();
            String fruitLine = in.nextLine();
            Scanner fruitScanner = new Scanner( fruitLine.substring( "Fruit:".length()) );
            while ( fruitScanner.hasNext() )
            {
                fruitClasses.add( fruitScanner.next() );
            }
            
            actorClasses = new HashMap<>();
            while ( in.hasNextLine() )
            {
                String actorClassLine = in.nextLine();
                if ( actorClassLine.length() > 4 && 
                        actorClassLine.substring(1, 4).equals(" = "))
                {
                    actorClasses.put( actorClassLine.charAt(0), actorClassLine.substring(4) );
                }
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(PacManMaze.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // getters for the dimensions
    public int getWidth() { return board[0].length; }
    public int getHeight() { return board.length; }
    
    // getter for individual squares
    public PacManSquare getSquare( Location l )
    {
        return board[(int)(Math.floor(l.getRow()))][(int)(Math.floor(l.getColumn()))];
    }
    
    public HashMap<Character,ArrayList<Location>> getActorLocations() { return actorLocations; }
    public HashMap<Character,String> getActorClasses() { return actorClasses; }
    public ArrayList<String> getFruitClasses() { return fruitClasses; }
}
