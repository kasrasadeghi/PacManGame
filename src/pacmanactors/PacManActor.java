/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import pacmangame.Location;
import pacmangame.PacManModel;
import pacmangame.PacManSquare;

/**
 *
 * @author DSTIGANT
 */

// Class PacManActor
// An actor in the PacMan game is a character which moves around the board
// This might be PacMan himself, or one of the ghosts or a piece of fruit etc.
// The PacManActor class takes care of moving an actor smoothly around the board
//
// All actors have the following properties:
//  - a location on the board
//  - a direction in which they are currently moving (0 = up, 1 = right, 2 = down, 3 = left)
//          there are constants (DIRECTION_UP etc) defined to keep track of this code
//  - the speed at which they move
//  - a pointer to the model object so that they can check the state of the rest of the game
//
// The values of these properties can be obtained through standard getter functions
//
// Of these values, only direction can be changed and note that the change does NOT
// necessarily take effect imediately.  The process is two steps:
//  - Call setDirection with the desired direction
//  - When the actor reaches the center of the next square, commitDirectionChange will
//    be called automatically to actually change the direction
//
// In addition the following getter functions are provided for convenience:
//  - getSquare - gets the square that the actor currently occupies
//  - getNextSquare - gets the square that the actor will occupy next.  This could
//     be either the next square after the current square in the current direction
//     or, if the actor will turn at the center of the current square, the square
//     in the new direction
//  - pastCenterOfSquare - returns true if the actor has passed the center of the
//     current square or false if the actor has not passed the center.
//
// As an actor moves about the board, there are several noteworthy events:
//  - update - this method is called externally to let the actor know that time has
//    passed and its location needs to be updated.  If a subclass overrides this method,
//    be sure to call super.update so that the movement code will still execute.  During
//    a call to update, the following methods may also get called:
//  - enterSquare - called when the actor first enters a new square
//  - leaveSquare - called when the actor leaves a square
//  - reachCenterOfSquare - called when the actor reaches the center of the square
//      This is typically when the new direction (if setDirection has been called)
//      goes into effect
//
// Finally, two static functions are provided for convenience:
//  - getNeighborLocation - returns the location next to another loction in the given direction
//  - sameLocation - checks to see if two locations refer to the same square
public abstract class PacManActor
{
    // Member variables to keep track of the properties mentioned above
    private Location location;      // in squares
    private int direction;        // 0 = up, 1 = right, 2 = down, 3 = left
    private int nextDirection;
    private double speed;         // in squares/second
    private PacManModel model;
    
    // direction constants
    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_LEFT = 3;
    
    // Constructor
    // to create a PacManActor, specify the starting location, speed, and 
    // direction and the model
    public PacManActor( Location l, double s, int dir, PacManModel m )
    {
        location = l;
        speed = s;
        model = m;
        direction = dir;
        nextDirection = dir;
    }

    // getModel
    // returns the model 
    
    protected PacManModel getModel() { return model; }
    
    // getLocation
    // returns the current location of the actor
    public Location getLocation() { return location; }
    
    // getDirection
    // returns the current direction of the actor
    public int getDirection() { return direction; }
   
    // setDirection
    // sets the next direction of the actor (may not take immediate effect)
    // dir - the new direction (one of DIRECTION_UP, DIRECTION_RIGHT,
    // DIRECTION_DOWN, or DIRECTION_LEFT)
    protected void setDirection( int dir )
    { 
        nextDirection = dir;
        
        double row = location.getRow();
        double col = location.getColumn();
        
        if ( row - Math.floor(row) == 0.5 && col - Math.floor(col) == 0.5 )
        {
            //direction = dir;
            commitDirectionChange();
        }
        if ( Math.abs( direction - dir ) == 2 )
        {
            direction = dir;
        }
    }
    
    // pastCenterOfCurrentSquare
    // returns true if the actor has passed the center of the current square
    // false otherwise
    protected boolean pastCenterOfCurrentSquare()
    {
        double row = location.getRow();
        double col = location.getColumn();
        
        if ( direction == DIRECTION_UP )
        {
            return row - Math.floor(row) <= 0.5;
        }
        else if ( direction == DIRECTION_DOWN )
        {
            return row - Math.floor(row) >= 0.5;
        }
        else if ( direction == DIRECTION_LEFT )
        {
            return col - Math.floor(col) <= 0.5;
        }
        else
        {
            return col - Math.floor(col) >= 0.5;
        }
    }
    
    // update
    // moves the actor along
    // called automatically every few milliseconds
    // dt - elapsed time in seconds
    public void update( double dt )
    {
        
        PacManSquare curSq = getSquare();
        PacManSquare nextSq = getNextSquare();
        boolean reachedNextSquare = false;
        boolean reachedCenterOfSquare = false;
        boolean alreadyPastCenterOfSquare = pastCenterOfCurrentSquare();
        
        double row = location.getRow();
        double col = location.getColumn();
        
        double newRow = row;
        double newCol = col;
        
        if ( direction == DIRECTION_UP )
        {
            newRow = row - dt * speed;
            
            if ( Math.floor(newRow) < Math.floor(row) )
            {
                reachedNextSquare = true;
            }
            else if ( newRow - Math.floor(newRow) <= 0.5 && (row - Math.floor(row) > 0.5) )
            {
                reachedCenterOfSquare = true;
            }
            
        }
        else if ( direction == DIRECTION_DOWN )
        {
            newRow = row + dt * speed;
            
            if ( Math.floor(newRow) > Math.floor(row) )
            {
                reachedNextSquare = true;
            }
            else if ( newRow - Math.floor(newRow) >= 0.5 && (row - Math.floor(row) < 0.5) )
            {
                reachedCenterOfSquare = true;
            }
        }
        else if ( direction == DIRECTION_LEFT )
        {
            newCol = col - dt * speed;
            
            if ( Math.floor(newCol) < Math.floor(col) )
            {
                reachedNextSquare = true;
            }
            else if ( newCol - Math.floor(newCol) <= 0.5 && (col - Math.floor(col) > 0.5) )
            {
                reachedCenterOfSquare = true;
            }
        }
        else if ( direction == DIRECTION_RIGHT )
        {
            newCol = col + dt * speed;
            
            if ( Math.floor(newCol) > Math.floor(col) )
            {
                reachedNextSquare = true;
            }
            else if ( newCol - Math.floor(newCol) >= 0.5 && (col - Math.floor(col) < 0.5) )
            {
                reachedCenterOfSquare = true;
            }
        }
        
        if ( reachedNextSquare )
        {
            if ( nextSq == null || nextSq.isWall() )
            {
                row = Math.floor( row ) + 0.5;
                col = Math.floor( col ) + 0.5;
            }
            else
            {
                leaveSquare( curSq, direction );
                row = newRow;
                col = newCol;
                enterSquare( nextSq, direction );
            }
        }
        else if ( reachedCenterOfSquare )
        {
            row = Math.floor( row ) + 0.5;
            col = Math.floor( col ) + 0.5;
            double remTime = (Math.abs(row-newRow) + Math.abs(col-newCol))/speed;
            
            reachCenterOfSquare( curSq, direction );
            nextSq = getNextSquare();
            
            if ( nextSq != null && !nextSq.isWall() && remTime > 0 )
            {
                location = new Location( row, col );
                update( remTime );
            }
        }
        else
        {
            if ( alreadyPastCenterOfSquare && (nextSq == null || nextSq.isWall()) )
            {
                row = Math.floor( row ) + 0.5;
                col = Math.floor( col ) + 0.5;
            }
            else
            {
                row = newRow;
                col = newCol;
            }
        }
        
        location = new Location( row, col );
    }
    
    // enterSquare
    // called by update whenever the actor enters a new square
    // sq - the new square that the actor is entering
    // dir - the direction the actor is traveling
    // does nothing by default
    protected void enterSquare( PacManSquare sq, int dir )
    {
    }

    // leaveSquare
    // called by update whenever the actor leaves the current square
    // sq - the square the actor is leaving
    // dir - the direction the actor is travelling
    // does nothing by default
    protected void leaveSquare( PacManSquare sq, int dir )
    {
    }
    
    // reachCenterOfSquare
    // called by update when the actor reaches the center of the current square
    // sq - the current square
    // dir - the direction the actor is travelling
    // by default, this function takes care of commiting a direction change when
    // appropriate (ie the direction can be changed to the new direction)
    // **** if you override this function, make sure that you take care of
    // changing the direction appropriately OR call this one as well
    protected void reachCenterOfSquare( PacManSquare sq, int dir )
    {
        commitDirectionChange();
    }
    
    // commitDirectionChange
    // called by reachCenterOfSquare to commit the direction change
    // **** if you override this function, make sure you take care of changing the
    // direction (which means you probably ought to call this function)
    protected void commitDirectionChange()
    {
        PacManSquare ns = getNeighborSquare( nextDirection );
        PacManSquare nscd = getNeighborSquare( direction );
        if ( (ns != null && !ns.isWall()) || nscd == null || nscd.isWall() )
        {
            direction = nextDirection;
        }
    }
    
    // getSquare
    // gets the current square of the actor
    public PacManSquare getSquare()
    {
        return model.getMaze().getSquare( location );
    }

    // getNeighborLocation
    // a static function for convenience.  Returns a location for the neighbor
    // square in the given direction
    // l - the location
    // dir - the direction
    // returns the neighbor of l in the direction dir
    protected static Location getNeighborLocation( Location l, int dir )
    {
        if ( dir == DIRECTION_UP ) 
            return new Location( l.getRow() - 1, l.getColumn() );
        else if ( dir == DIRECTION_DOWN )
            return new Location( l.getRow() + 1, l.getColumn() );
        else if ( dir == DIRECTION_LEFT )
            return new Location( l.getRow(), l.getColumn() - 1 );
        else if ( dir == DIRECTION_RIGHT )
            return new Location( l.getRow(), l.getColumn() + 1 );
        else
            return l;
    }
    
    // getNeighborSquare
    // returns the square next to this actor's current square in the given direction
    // or null if the actor is on the edge of the board in that direction
    // dir - the direction
    protected final PacManSquare getNeighborSquare( int dir )
    {
        Location newLoc = getNeighborLocation( location, dir );
        int newRow = (int)(Math.floor(newLoc.getRow()));
        int newCol = (int)(Math.floor(newLoc.getColumn() ) );
        if ( newRow >= 0 && newCol >= 0 && newRow < model.getMaze().getHeight() && newCol < model.getMaze().getWidth() )
        {
            return model.getMaze().getSquare( newLoc );
        }
        
        return null;
    }

    // getNextSquare
    // gets the next square that the actor would like to enter (in the next direction
    // rather than the current direction) or null if the actor is on the edge of the
    // board in that direction
    protected final PacManSquare getNextSquare()
    {
        if ( pastCenterOfCurrentSquare() && direction != nextDirection )
        {
            PacManSquare ns = getNeighborSquare( nextDirection );
            if ( ns == null || ns.isWall() )
            {
                return getNeighborSquare( direction );
            }
            else
            {
                return ns;
            }
        }
        return getNeighborSquare( direction );
    }
    
    // sameLocation
    // a static function for convenience.
    // checks two locations to see if they refer to the same square
    // l1, l2 - the locations
    protected static boolean sameLocation( Location l1, Location l2 )
    {
        return (int)(Math.floor(l1.getRow())) == (int)(Math.floor(l2.getRow())) &&
                (int)(Math.floor(l1.getColumn() )) == (int)(Math.floor(l2.getColumn()));
    }
}
