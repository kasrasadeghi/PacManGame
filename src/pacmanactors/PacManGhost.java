/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import java.util.ArrayList;
import pacmangame.Location;
import pacmangame.PacManModel;
import pacmangame.PacManSquare;

/**
 *
 * @author DSTIGANT
 */

// class PacManGhost
// Each ghost has a color (String) like "Red" "Orange" etc. which specifies what
// its color is.  This property can be got or set:
// 
// getColor - returns the color of the ghost
// setColor( String ) - sets the color of the ghost
//
// Most ghosts have the same speed (GHOST_SPEED), so a constructor is provided 
// that doesn't require the speed to be specified.  A second constructor that
// allows the speed to be specified is also provided.
//
// PacManGhost( Location l, double s, PacManModel m, String col ) - creates
//      a ghost in location l with speed s and color col.
//
// PacManGhost( Location l, PacManModel m, String col ) - creates
//      a ghost in location l with the normal speed and color col
//
// The ghosts move about the board according to simple rules:
// Each ghost, depending on what mode it is in, has a target square that it is
// trying to get to.  When a ghost comes to an intersection, it decides which
// hallway to travel down by checking each neighboring square (except the square
// it just came from) to see which square is closest to its destination, and chosing
// to go towards that square.  The ghost will never turn around unless it is in
// a dead end or the mode changes.  
//
// There are four different target locations for each ghost:
//
// Chase Location - the location the ghost tries for when chasing PacMan
// Scatter Location - the location the ghost tries for when scatter mode is active
//     This is usually a corner of the board
// Frightened Location - the location the ghost is trying to reach when PacMan is
//     chasing it.  These locations are randomly generated
// Regeneration Location - this is specified by the Maze configuration file
//
// Each ghost decides its Chase and Scatter locations for itself.  To this end
// each ghost subclass should provide implementations for the abstract functions
//      getChaseLocation
//      getScatterLocation
// Note that it is perfectly acceptable for either or both of these locations to
// be outside the bounds of the maze.
// 
// Additionally, function getNewFrightenedLocation is available to choose a random
// location.
//
// The work of implementing the decision making described above happens in function
//      reachCenterOfSquare
// For this reason, if you override reachCenterOfSquare, make sure you make a call
// to the super-class's implementation
//
// Finally, since several of the algorithms for choosing target squares require
// the distance between Locations, a static function for determining the square
// of the Euclidean distance between two Locations is provided.
//
//      squareDistance( Location, Location )
public abstract class PacManGhost extends PacManActor
{
    public static final int CHASE_MODE = 0;
    public static final int SCATTER_MODE = 1;
    public static final int FRIGHTENED_MODE = 2;
    public static final int EYE_MODE = 3;
    
    public static final double GHOST_SPEED = 5.0;
    
    private String color;
    private int mode;
   
    private Location frightenedTargetLocation;
    
    public PacManGhost(Location l, double s, PacManModel m, String col)
    {
        super(l, s, DIRECTION_LEFT, m);
        initialize( col );
    }
    
    public PacManGhost( Location l, PacManModel m, String col )
    {
        super( l, GHOST_SPEED, DIRECTION_LEFT, m );
        initialize( col );
    }
    
    private void initialize( String col )
    {
        setColor( col );
        mode = CHASE_MODE;
        frightenedTargetLocation = getNewFrightenedLocation();
    }
    
    public String getColor() { return color; }
    
    protected void setColor( String col )
    {
        color = col;
    }
    
    public int getMode() { return mode; }
    
    public void setMode( int m )
    {
        if ( mode != m && mode != EYE_MODE )
        {
            setDirection( (getDirection()+2)%4 );
        }
        
        if ( mode != EYE_MODE )
        {
            mode = m;
        }
    }
    
    public final Location getTargetLocation()
    {
        if ( mode == CHASE_MODE )
        {
            return getChaseTargetLocation(); 
        }
        else if ( mode == SCATTER_MODE )
        {
            return getScatterTargetLocation();
        }
        else if ( mode == FRIGHTENED_MODE )
        {
            return frightenedTargetLocation;
        }
        else
        {
            return getModel().getGhostRegenLocation();
        }
    }
    
    protected abstract Location getChaseTargetLocation();
    protected abstract Location getScatterTargetLocation();
    
    protected Location getNewFrightenedLocation()
    {
        int frightenedTargetRow = (int)(Math.random() * getModel().getMaze().getHeight() );
        int frightenedTargetCol = (int)(Math.random() * getModel().getMaze().getWidth() );
        return new Location( frightenedTargetRow, frightenedTargetCol );
    }
    
    @Override
    protected void reachCenterOfSquare( PacManSquare sq, int dir )
    { 
        Location l = getLocation();
        
        if ( mode == PacManGhost.EYE_MODE )
        {
            Location rl = getModel().getGhostRegenLocation();
            if ( sameLocation(l, rl) )
            {
                mode = CHASE_MODE;
            }
        }
        ArrayList<Integer> validDirections = new ArrayList<>();
        for ( int i = 0; i < 4; i++ )
        {
            if ( i != (dir + 2)%4 )
            {
                PacManSquare sqn = getNeighborSquare( i );
                if ( sqn != null && !sqn.isWall() )
                {
                    validDirections.add( i );
                }
            }
        }
        
        if ( validDirections.size() == 1 )
        {
            setDirection( validDirections.get(0) );
            super.reachCenterOfSquare(sq, dir);
            return;
        }
        
        if ( mode == FRIGHTENED_MODE )
        {
            frightenedTargetLocation = getNewFrightenedLocation();
        }
        
        Location curLoc = getLocation();
        Location targetLoc = getTargetLocation();
        
        int bestDir = -1;
        int bestDist = Integer.MAX_VALUE;
        
        if ( validDirections.isEmpty() )
        {
            bestDir = (dir+2)%4;
        }
        
        for ( int i = 0; i < validDirections.size(); i++ )
        {
            Location newLoc = getNeighborLocation( curLoc, validDirections.get(i));
            int newDist = squaredDistance( newLoc, targetLoc );
            if ( newDist < bestDist )
            {
                bestDir = validDirections.get(i);
                bestDist = newDist;
            }
        }
        setDirection( bestDir );
        super.reachCenterOfSquare(sq, dir);
    }
    
    protected int squaredDistance( Location l1, Location l2 )
    {
        int x1 = (int)(Math.floor(l1.getRow()));
        int y1 = (int)(Math.floor(l1.getColumn()));
        int x2 = (int)(Math.floor(l2.getRow()));
        int y2 = (int)(Math.floor(l2.getColumn()));
        
        int dx = x1 - x2;
        int dy = y1 - y2;
        return dx*dx + dy*dy;
    }
}
