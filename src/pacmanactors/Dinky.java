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
import pacmangame.PacManModel;

/**
 *
 * @author DSTIGANT
 */

// Dinky is a yellow ghost who targets two squares behind pacman
public class Dinky extends PacManGhost
{
    public Dinky (Location l, PacManModel m ) {
        super(l, GHOST_SPEED, m, "250 70 180");
    }
    
    @Override
    protected Location getChaseTargetLocation() {
        Location l = getModel().getPacMan().getLocation();
        int s = -2;
        switch (getModel().getPacMan().getDirection()) {
            case DIRECTION_UP:    return new Location(l.getRow() - s, l.getColumn());
            case DIRECTION_DOWN:  return new Location(l.getRow() + s, l.getColumn());
            case DIRECTION_LEFT:  return new Location(l.getRow(), l.getColumn() - s);
            case DIRECTION_RIGHT: return new Location(l.getRow(), l.getColumn() + s);
            default: return l;
        }
    }

    @Override //Undefined Scatter Location
    protected Location getScatterTargetLocation() {
        return new Location( getModel().getMaze().getHeight() - 1,getModel().getMaze().getWidth() - 1);
    }
}
