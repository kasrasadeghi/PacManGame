/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.util.ArrayList;

/**
 *
 * @author DSTIGANT
 */

// class PacManSquare
// represents a single square on the PacMan board.
// each square may contain a pellet and may be a wall
// getter functions give access to this information:
// getPellet - returns the pellet, if there is one, null if not
// isWall - returns true if the square is a wall, false otherwise
// removePellet - removes the pellet if there is one
public class PacManSquare
{
    private PacManPellet pellet;
    private boolean isWall;
    
    public PacManSquare( boolean iw, PacManPellet p )
    {
        isWall = iw;
        pellet = p;
    }
       
    public PacManPellet getPellet() { return pellet; }
    public void addPellet( PacManPellet p ) { pellet = p; }
    public void removePellet() { pellet = null; }
    
    public boolean isWall() { return isWall; }
}
