/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanactors;

import java.util.ArrayList;
import static pacmanactors.PacManGhost.EYE_MODE;
import pacmangame.Location;
import pacmangame.PacManModel;

/**
 *
 * @author DSTIGANT
 */

// class Apple represents the Apple fruit
// the Apple moves just like a normal fruit
// if the Apple can "see" a ghost - that is, it is in the same row or column
// as a ghost and there are are no wall squares between it and the ghost -
// then the Apple will turn the ghost to eye mode.
public class Apple extends Fruit 
{
    private ArrayList<PacManGhost> ghosts = new ArrayList<>();
    
    public Apple(Location l, PacManModel m) {
        super(l, m);
        for (int i = 0; i < m.getNumGhosts(); ++i) 
            ghosts.add(m.getGhost(i));
    }
    
    @Override
    public void update(double dt) {
        super.update(dt);
        if(!ghosts.isEmpty())
            ghosts.stream()
                    .filter(ghost -> isVisible(ghost))
                    .filter(ghost -> ghost.getMode() != EYE_MODE)
                    .forEach(ghost -> ghost.setMode(EYE_MODE));
    }
    
    private boolean isVisible(PacManGhost ghost) {
        Location gl = ghost.getLocation();
        Location fl = getLocation();
        
        if (gl.getColumn() == fl.getColumn())
            return !checkRowForWall(gl, fl);
        else if (gl.getRow()    == fl.getRow()   )
            return !checkColForWall(gl, fl);
        
        return false;
    }
    
    private boolean checkRowForWall(Location l1, Location l2) {
        if ( l1.getRow() > l2.getRow()) {
            Location temp = l1;
            l1 = l2;
            l2 = temp;
        }
        for (double i = l1.getRow(); i < l2.getRow(); ++i)
            if (getModel().getMaze().getSquare(new Location(i, l1.getColumn())).isWall())
                return true;
        return false;
    }
    
    private boolean checkColForWall(Location l1, Location l2) {
        if ( l1.getColumn() > l2.getColumn()) {
            Location temp = l1;
            l1 = l2;
            l2 = temp;
        }
        for (double i = l1.getColumn(); i < l2.getColumn(); ++i)
            if (getModel().getMaze().getSquare(new Location(i, l1.getRow())).isWall())
                return true;
        return false;
    }
}
