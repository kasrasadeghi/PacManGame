/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import java.util.ArrayList;
import static pacmanactors.PacManActor.sameLocation;
import static pacmanactors.PacManGhost.CHASE_MODE;
import static pacmanactors.PacManGhost.FRIGHTENED_MODE;
import pacmangame.Location;
import pacmangame.PacManModel;
import pacmangame.PacManSquare;

/**
 *
 * @author DSTIGANT
 */
// class Fruit represents a piece of fruit that floats around the maze
// The fruit should move like other actors do with a speed of FRUIT_SPEED
// When it reaches the center of a square, the fruit should randomly choose a
// new direction based on the following rules:
//      1.  The direction should be chosen from those that lead to non-wall squares
//      2.  It should not reverse direction unless it is in a dead end
// Bonus: as the fruit moves around the maze, it should bob up and down a little bit.
public class Fruit extends PacManActor
{
    public static final double FRUIT_SPEED = 3.0;

    public Fruit(Location l, PacManModel m) {
        super(l, FRUIT_SPEED, DIRECTION_LEFT, m);
        
    }
    
    @Override
    protected void reachCenterOfSquare( PacManSquare sq, int dir )
    {
        ArrayList<Integer> validDirections = new ArrayList<>();
        for ( int i = 0; i < 4; i++ )
            if ( i != (dir + 2)%4 ) {
                PacManSquare sqn = getNeighborSquare( i );
                if ( sqn != null && !sqn.isWall() )
                    validDirections.add( i );
            }
        
        int bestDir;
        switch(validDirections.size()) {
            case 0: bestDir = (dir+2)%4; break;
            case 1: bestDir = validDirections.get(0); break;
            default: bestDir = validDirections.get((int)Math.floor(Math.random() * validDirections.size())); break;
        }
        
        setDirection( bestDir );
        super.reachCenterOfSquare(sq, dir);
    }
}
