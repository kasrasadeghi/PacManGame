/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import static pacmanactors.PacManGhost.GHOST_SPEED;
import pacmangame.Location;
import pacmangame.PacManMaze;
import pacmangame.PacManModel;

/**
 *
 * @author DSTIGANT
 */
// class Clyde represents the orange ghost
// Clyde's scatter target location is the bottom left corner of the maze
// Clyde's target location is either PacMan's current location or, if PacMan
//      is within 8 squares of Clyde, Clyde's scatter target location.
public class Clyde extends PacManGhost
{
    public Clyde (Location l, PacManModel m ) {
        super(l, GHOST_SPEED * 1.1, m, "Orange");
    }

    @Override
    protected Location getChaseTargetLocation() {
        
        return getModel().getPacMan().getLocation();
    }

    @Override
    protected Location getScatterTargetLocation() {
        return new Location(getModel().getMaze().getHeight() - 1,0);
    }
}
