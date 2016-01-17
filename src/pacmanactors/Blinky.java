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
// class Blinky represents the red ghost
// When in chase mode, Blinky's target square is PacMan's current square (this
//      can be obtained via the model)
// When in scatter mode, Blinky's target square is the top right corner of the maze
// Additionally, Blinky is 10% faster than the average ghost
public class Blinky extends PacManGhost
{
    public Blinky (Location l, PacManModel m ) {
        super(l, GHOST_SPEED * 1.1, m, "Red");
    }

    @Override
    protected Location getChaseTargetLocation() {
        return getModel().getPacMan().getLocation();
    }

    @Override
    protected Location getScatterTargetLocation() {
        return new Location(0,getModel().getMaze().getWidth() - 1);
    }
}
