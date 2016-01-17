/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import apcscvm.CVMProgram;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pacmanactors.Blinky;
import pacmanactors.Cherry;

/**
 *
 * @author DSTIGANT
 */
public class PacManGame
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        PacManView v = new PacManView();
        PacManModel m = new PacManModel();
        
        new CVMProgram( "PacMan", 576, 658, v, v, m ).start();
    }
    
}
