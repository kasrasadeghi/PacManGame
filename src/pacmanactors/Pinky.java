/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import static pacmanactors.PacManGhost.GHOST_SPEED;
import pacmangame.Location;
import pacmangame.PacManModel;

/**
 *
 * @author DSTIGANT
 */

// class Pinky represents the pink ghost
// Pinky's chase target location is the square 4 squares in front of PacMan's
//      current location based on PacMan's current direction (ie if PacMan is
//      facing right, the target square is 4 squares to the right of PacMan)
// Pinky's scatter target location is the top left corner of the maze
public class Pinky extends PacManGhost
{
    public Pinky (Location l, PacManModel m ) {
        super(l, GHOST_SPEED, m, "Pink");
    }

    @Override
    protected Location getChaseTargetLocation() {
        Location l = getModel().getPacMan().getLocation();
        switch (getModel().getPacMan().getDirection()) {
            case DIRECTION_UP:    return new Location(l.getRow() - 4, l.getColumn());
            case DIRECTION_DOWN:  return new Location(l.getRow() + 4, l.getColumn());
            case DIRECTION_LEFT:  return new Location(l.getRow(), l.getColumn() - 4);
            case DIRECTION_RIGHT: return new Location(l.getRow(), l.getColumn() + 4);
            default: return l;
        }
    }

    @Override
    protected Location getScatterTargetLocation() {
        return new Location(getModel().getMaze().getHeight() - 1,0);
    }
}
