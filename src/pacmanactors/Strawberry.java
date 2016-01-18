/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import pacmangame.Location;
import pacmangame.PacManModel;
import pacmangame.PacManPellet;
import pacmangame.PacManSquare;

/**
 *
 * @author DSTIGANT
 */

// Class Strawberry represents the Strawberry Fruit
// Whenever a strawberry reaches the center of a square which doesn't have a pellet
// in it, it drops a pellet there.
public class Strawberry extends Fruit
{
    public Strawberry(Location l, PacManModel m) {
        super(l, m);
    }
    
    @Override
    protected void reachCenterOfSquare( PacManSquare sq, int dir ) {
        super.reachCenterOfSquare(sq, dir);
        
        if (getSquare().getPellet() == null)
            getSquare().addPellet(new PacManPellet());
    }
}
