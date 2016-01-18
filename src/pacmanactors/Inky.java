/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import static pacmanactors.PacManActor.DIRECTION_DOWN;
import static pacmanactors.PacManActor.DIRECTION_LEFT;
import static pacmanactors.PacManActor.DIRECTION_RIGHT;
import static pacmanactors.PacManActor.DIRECTION_UP;
import static pacmanactors.PacManGhost.GHOST_SPEED;
import pacmangame.Location;
import pacmangame.PacManMaze;
import pacmangame.PacManModel;

/**
 *
 * @author DSTIGANT
 */
// class Inky represents the light blue ghost (use color "Blue")
// Inky's scatter target is the bottom right corner of the maze
// Inky's chase target square is found as follows:  Let Blinky's location be B
//      Let M be the Location 2 squares in front of PacMan's current location
//      Let A be the point such that M is the midpoint of AB.  A is Inky's chase
//      target.  For example, if Blinky is at B(5, 3) and PacMan is at (7, 5) and
//      is facing right, then M is (7, 7), so A should be (9, 11)
public class Inky extends PacManGhost
{
    public Inky (Location l, PacManModel m ) {
        super(l, GHOST_SPEED, m, "Blue");
    }
    
    protected Location getChaseTargetMidpoint() {
        Location l = getModel().getPacMan().getLocation();
        int s = 2;
        switch (getModel().getPacMan().getDirection()) {
            case DIRECTION_UP:    return new Location(l.getRow() - s, l.getColumn());
            case DIRECTION_DOWN:  return new Location(l.getRow() + s, l.getColumn());
            case DIRECTION_LEFT:  return new Location(l.getRow(), l.getColumn() - s);
            case DIRECTION_RIGHT: return new Location(l.getRow(), l.getColumn() + s);
            default: return l;
        }
    }
    
    @Override
    protected Location getChaseTargetLocation() {
        Location m = getChaseTargetMidpoint();
        Location l = getLocation();
        return new Location (2 * m.getRow() - l.getRow(), 2 * m.getColumn() - l.getColumn());
    }

    @Override
    protected Location getScatterTargetLocation() {
        return new Location( getModel().getMaze().getHeight() - 1,getModel().getMaze().getWidth() - 1);
    }
}
