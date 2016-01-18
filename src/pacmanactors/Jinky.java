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
// class Jinky represents a new ghost
// Jinky acts just like Pinky except:
//      1.  His scatter square is the middle of the far right side of the maze
//      2.  His color (starts as) Magenta
//      3.  Whenever he reaches the center of a square
//          he switches colors.  He has two colors: Green and Magenta.
public class Jinky extends Pinky
{
    public Jinky (Location l, PacManModel m ) {
        super(l, m);
        setColor("Magenta"); 
    }
    
    @Override
    protected void reachCenterOfSquare( PacManSquare sq, int dir ) {
        super.reachCenterOfSquare(sq, dir);
        if (getColor().equals("Magenta"))
            setColor("Green");
        else setColor("Magenta");
    }
    
    @Override
    protected Location getScatterTargetLocation() {
        return new Location(getModel().getMaze().getHeight()/2,getModel().getMaze().getWidth() - 1);
    }
}
