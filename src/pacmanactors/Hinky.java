/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import static pacmanactors.PacManGhost.GHOST_SPEED;
import pacmangame.Location;
import pacmangame.PacManModel;
import pacmangame.PacManSquare;

/**
 *
 * @author DSTIGANT
 */

// Hinky is a purple ghost (RGB = 128, 0, 128) which targets a random location
// (which must NOT be a wall square).
// he ONLY changes his target location when he reaches the center of the
// current target location.  His chase and scatter targets are the same (ie the 
// randomly chosen target location). 
public class Hinky extends PacManGhost
{
    Location chaseTarget;
    
    public Hinky (Location l, PacManModel m ) {
        super(l, GHOST_SPEED, m, "128 0 128");
        getNewChaseTarget();
    }
    
    private Location getNewChaseTarget() {
        PacManSquare sq = getModel().getMaze().getSquare(
                chaseTarget = new Location(Math.random()*getModel().getMaze().getHeight(), Math.random()*getModel().getMaze().getWidth())
        );
        chaseTarget = (sq.isWall())? getNewChaseTarget() : chaseTarget;
        return chaseTarget;
    }
    
    @Override
    protected void reachCenterOfSquare( PacManSquare sq, int dir ) {
        super.reachCenterOfSquare(sq, dir);
        if (getModel().getMaze().getSquare(chaseTarget).equals(sq))
            getNewChaseTarget();
    }
    
    @Override
    protected Location getChaseTargetLocation() {
        return chaseTarget;
    }
    
    @Override 
    protected Location getScatterTargetLocation() {
        return getChaseTargetLocation();
    }
}
