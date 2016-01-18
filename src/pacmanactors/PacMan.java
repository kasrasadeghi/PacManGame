/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import pacmangame.Location;
import pacmangame.PacManModel;
import pacmangame.PacManPowerPellet;
import pacmangame.PacManSquare;

/**
 *
 * @author DSTIGANT
 */

// class PacMan
// represents our hero
// PacMan is a yellow circle which moves around the board eating pellets and sometimes
// ghosts while opening and closing his mouth
// 
// PacMan moves around the board... is there another class which does this?
// How can we get the code which handles moving without having to duplicate it?
//
// PacMan needs to open and close his mouth as time passes
// At any given time, PacMan is either opening or closing his mouth
// and his mouth will be at a particular angle
// Add member variables to keep track of this information
//
// PacMan should start with his mouth closed but (getting ready to be) openING.
public class PacMan extends PacManActor
{
    // add any member variables here
    public double angle;
    public boolean opening;
    // the speed at which the mouth opens/closes (in degrees per second)
    protected static final double dAdt = 360;       // degrees per second
    
    // the speed of the PacMan
    protected static final double PM_SPEED = 6.0;   // squares per second
    
    // Constructor
    // to create PacMan, specify his starting location and the model
    public PacMan(Location l, PacManModel m)
    {
        
        // the PacMan should start out in the given location,
        // with speed PM_SPEED, facing left, with the given model
        super(l, PM_SPEED, DIRECTION_LEFT, m);
        angle = 0;
        opening = true;
        
        // initially his mouth should be closed, but (getting ready to be) openING
    }

    // getAngle
    // returns the current angle formed by the mouth (in degrees)
    public double getAngle() { return angle; }
    
    // as time goes by, in addition to moving forward, 
    // the PacMan should change the angle of its mouth
    // and potentially change whether it is opening or closing
    //
    // Is there a function which gets called to let us know that time has passed?
    // What is it called?
    //
    // Does this function already have a useful default implementation?  What does
    // it do?  And if so, do we want to do only the new functionality or do the
    // old functionality in addition to the new?  What should the first line of our
    // new implementation of this function be?
    // 
    // If the mouth is opening, then the angle should increase by dAdt times the
    // amount of time that has passed.  The maximum angle should be 90.  If we
    // have exceeded this amount, then we should change to closing mode
    //
    // Otherwise (if the mouth is closing), then the angle should DEcrease by dAdt
    // times the amount of time that has passed.  The minimum angle should be 0.
    // If we have passed that amount, then we should change to opening mode.
    @Override
    public void update( double dt) {
        super.update(dt);
        if (angle > 90) 
            opening = false;
        if (angle < 0)
            opening = true;
        if (opening) 
            angle += dt * dAdt;
        else angle -= dt * dAdt;
    }
    
    // When the PacMan reaches the center of the square, in addition to (potentially)
    // changing direction, it should eat any pellet that is in the square.  
    // If the square contains a power-pellet, we should tell the model that we ate 
    // the power-pellet so that it can take care of putting us in chase mode
    //
    // Is there a function that gets called when we reach the center of a square?
    // What is it called?
    // 
    // Does this function already have a useful default implementation?  What does
    // it do?  And if so, do we want to do only the new functionality or do the
    // old functionality in addition to the new?  What should the first line of our
    // new implementation of this function be?
    //
    // If the square contains a pellet, remove it.
    // If the pellet happened to be a power-pellet, notify the model that we ate
    // a power pellet.
    @Override
    public void reachCenterOfSquare(PacManSquare sq, int dir) {
        super.reachCenterOfSquare(sq, dir);
        if (sq.getPellet() != null) {
            if (sq.getPellet() instanceof PacManPowerPellet )
                getModel().eatPowerPellet();
            sq.removePellet();
        }
    }
    
    // These functions are called by the Control/View object in response to the
    // user pressing the arrow keys.  Uncomment when instructed
    
    
    // goUp - change the direction to up
    public void goUp() { 
        setDirection( DIRECTION_UP );
    }
    
    // goDown - change the direction to down
    public void goDown() { 
        setDirection( DIRECTION_DOWN );
    }

    // goLeft - change the direction to left
    public void goLeft() { 
        setDirection( DIRECTION_LEFT );    
    }
    
    // goRight - change the direction to right
    public void goRight() { 
        setDirection( DIRECTION_RIGHT );
    }
    
}
