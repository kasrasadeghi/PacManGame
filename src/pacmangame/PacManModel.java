/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pacmanactors.Fruit;
import pacmanactors.PacMan;
import pacmanactors.PacManActor;
import pacmanactors.PacManGhost;

/**
 *
 * @author DSTIGANT
 */

// class PacManModel
// groups all the game items together
// primarily, it contains pointers to the Maze object, and each of the actors, and
// keeps track of whether PacMan can eat ghosts or they can eat him
// getters allow access to these items.
// additionally, there is an update function to handle time passage and
// a function to handle eating a power-pellet
//
// getMaze - returns a pointer to the maze
// getPacMan - returns a pointer to PacMan
// getGhost(int) - returns a pointer to the ghost (using the ghost indices from PacManMaze)
// getMode - returns the current mode.  This could be FRIGHTENED_MODE, CHASE_MODE or SCATTER_MODE
// update( int ) - handles time passage.  Calls update on all actors and keeps track of mode timers
// eatPowerPellet() - eats a power pellet.  Places ghosts in FRIGHTENED_MODE
// getActor( String ) - returns the actor of the given name
// getActor( String, int ) - returns the actor of a given name (if there are more than
//    one actors with that name, then the index can be provided).
// getActorCount( String ) - returns the number of actors with the given name

public class PacManModel
{
    private PacManMaze maze;
    
    private PacMan pacman;
    private ArrayList<PacManGhost> ghosts;
    private Fruit fruit;
    private HashMap<String, Constructor> fruitConstructors;
    
    private Location fruitSpawnLocation;
    private Location ghostRegenLocation;
    private Location pacManSpawnLocation;
    
    private HashMap<String, ArrayList<PacManActor>> actors;
    
    private int globalGhostModeTimer;
    private int frightenedGhostModeTimer;
    private boolean frightenedMode;
    private int fruitTimer;
    
    private Constructor getConstructor( String className )
    {            
        String cn = className;
        if ( !className.contains(".") )
        {
            cn = "pacmanactors." + className;
        }
        
        try
        {
            Class cls = Class.forName( cn );
            Constructor constr = cls.getConstructor( Location.class, PacManModel.class );
            return constr;
        } catch (ClassNotFoundException ex)
        {
            System.out.println( "Class " + className + " not found");
            ex.printStackTrace();
        } catch (NoSuchMethodException ex)
        {
            System.out.println("Class " + className + " does not have a constructor which consumes a Location and a PacManModel" );
            ex.printStackTrace();
        } catch (SecurityException ex)
        {
            System.out.println("Class " + className + "'s constructor is not accessible (hint: make it public)" );
            ex.printStackTrace();
        }
            
        return null;    
    }
    
    private void addActor( String actorName, PacManActor actor )
    {
        ArrayList<PacManActor> l = actors.get( actorName );
        if ( l == null )
        {
            l = new ArrayList<>();
            actors.put( actorName, l );
        }
        l.add( actor );
    }
    
    private void removeActor( String actorName, PacManActor actor )
    {
        ArrayList<PacManActor> l = actors.get( actorName );
        if ( l != null )
        {
            l.remove( actor );
        }
    }
    
    public PacManModel()
    {
        maze = new PacManMaze( "PacManBoard.txt" );
        
        actors = new HashMap<>();
        ghosts = new ArrayList<>();
        
        HashMap<Character, ArrayList<Location>> actorLocations = maze.getActorLocations();
        HashMap<Character, String> actorClasses = maze.getActorClasses();
        
        for ( Character a : actorLocations.keySet() )
        {
            if ( a == 'P' )
            {
                for ( Location l : actorLocations.get('P') )
                {
                    pacman = new PacMan(l, this);
                    addActor( "PacMan", pacman );
                    pacManSpawnLocation = l;
                }
            }
            else if ( a == 'F' )
            {
                fruitSpawnLocation = actorLocations.get( 'F' ).get(0);
            }
            else if ( a == 'R' )
            {
                ghostRegenLocation = actorLocations.get( 'R' ).get(0);
            }
            else
            {
                String clsName = actorClasses.get( a );
                if ( clsName != null )
                {
                    Constructor constr = getConstructor( clsName );
                    if ( constr != null )
                    {
                        for ( Location l : actorLocations.get(a) )
                        {
                            PacManActor act = null;
                            try
                            {
                                act = (PacManActor)constr.newInstance( l, this );
                            } catch (InstantiationException ex)
                            {
                                Logger.getLogger(PacManModel.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex)
                            {
                                Logger.getLogger(PacManModel.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalArgumentException ex)
                            {
                                Logger.getLogger(PacManModel.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex)
                            {
                                Logger.getLogger(PacManModel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if ( act != null )
                            {
                                addActor( clsName, act );
                                if ( act instanceof PacManGhost )
                                {
                                    ghosts.add( (PacManGhost)act );
                                }
                            }
                        }
                    }
                }
                else {
                    System.out.println("No class for " + a );
                }
            }
        }
        int ng = 0;
        globalGhostModeTimer = 0;
        frightenedGhostModeTimer = 0;
        frightenedMode = false;
        
        fruitTimer = 10000;
        
        fruitConstructors = new HashMap<>();
        for ( String fcn : maze.getFruitClasses() )
        {
            Constructor fcon = getConstructor( fcn );
            if ( fcon != null )
            {
                 fruitConstructors.put( fcn, fcon );
            }
        }
    }
    
    public PacManMaze getMaze() { return maze; }
    public PacMan getPacMan() { return pacman; }
    public Fruit getFruit() { return fruit; }
    public PacManGhost getGhost( int i ) { return ghosts.get(i); }
    public int getNumGhosts() { return ghosts.size(); }
    
    private int getMode()
    {
        if ( frightenedMode )
        {
            return PacManGhost.FRIGHTENED_MODE;
        }
        else
        {
            if ( globalGhostModeTimer % 13000 < 10000 )
            {
                return PacManGhost.CHASE_MODE;
            }
            else
            {
                return PacManGhost.SCATTER_MODE;
            }
        }
    }
    
    protected void updateGhostMode( int dt )
    {
        if ( frightenedMode )
        {
            frightenedGhostModeTimer -= dt;
            if ( frightenedGhostModeTimer <= 0 )
            {
                frightenedGhostModeTimer = 0;
                frightenedMode = false;
                for ( PacManGhost g : ghosts )
                {
                    g.setMode( PacManGhost.CHASE_MODE );
                }
            }
        }
        else
        {
            globalGhostModeTimer += dt;
            for ( PacManGhost g : ghosts )
            {
                g.setMode( getMode() );
            }
        }
    }
    
    protected void updateActors( int dt )
    {
        for ( ArrayList<PacManActor> actl : actors.values() )
        {
            for ( PacManActor act : actl )
            {
                act.update( dt/1000.0 );
            }
        }
    }
    
    protected void updateFruitTimer( int dt )
    {
        if ( fruit == null )
        {
            fruitTimer -= dt;
            if ( fruitTimer <= 0 && maze.getFruitClasses().size() > 0 )
            {
                int findex = (int)(Math.random() * maze.getFruitClasses().size() );
                Constructor fcon = fruitConstructors.get( maze.getFruitClasses().get(findex) );
                if ( fcon != null )
                {
                    try
                    {
                        fruit = (Fruit)fcon.newInstance( fruitSpawnLocation, this );
                    } catch (InstantiationException ex)
                    {
                        Logger.getLogger(PacManModel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex)
                    {
                        Logger.getLogger(PacManModel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex)
                    {
                        Logger.getLogger(PacManModel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex)
                    {
                        Logger.getLogger(PacManModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if ( fruit != null )
                    {
                        addActor( "Fruit", fruit );
                    }
                }
            }
        }
    }
    
    protected void checkPacManIntersections()
    {
        
        Location pl = pacman.getLocation();
        int pmr = (int)Math.floor( pl.getRow() );
        int pmc = (int)Math.floor( pl.getColumn() );
        for ( PacManGhost g : ghosts )
        {
            Location gl = g.getLocation();
            int gr = (int)Math.floor( gl.getRow() );
            int gc = (int)Math.floor( gl.getColumn() );
            
            if ( gr == pmr && gc == pmc )
            {
                if ( g.getMode() == PacManGhost.FRIGHTENED_MODE )
                {
                    g.setMode( PacManGhost.EYE_MODE );
                }
                else if ( g.getMode() != PacManGhost.EYE_MODE )
                {
                    
                }
            }
        }
        if ( fruit != null )
        {
            int cr = (int)(Math.floor(fruit.getLocation().getRow()));
            int cc = (int)(Math.floor(fruit.getLocation().getColumn()));
            if ( cr == pmr && cc == pmc )
            {
                removeActor( "Fruit", fruit );
                fruit = null;
                fruitTimer = 10000;
            }
        }
    }
    
    
    // dt in milliseconds
    public void update(int dt)
    {
        updateGhostMode(dt);

        updateActors( dt );
        
        updateFruitTimer( dt );
        
        checkPacManIntersections();
    }
    
    public void eatPowerPellet()
    {
        frightenedMode = true;
        frightenedGhostModeTimer = 5000;
        
        for ( PacManGhost g : ghosts )
        {
            if ( g != null )
                g.setMode( PacManGhost.FRIGHTENED_MODE );
        }
    }
    
    public PacManActor getActor( String actorName )
    {
        return getActor( actorName, 0 );
    }
    
    public PacManActor getActor( String actorName, int index )
    {
        ArrayList<PacManActor> l = actors.get( actorName );
        if ( l != null && l.size() > index )
        {
            return l.get( index );
        }
        return null;
    }
    
    public int getActorCount( String actorName )
    {
        ArrayList<PacManActor> l = actors.get( actorName );
        if ( l != null )
        {
            return l.size();
        }
        return 0;
    }
    
    public Location getGhostRegenLocation() { return ghostRegenLocation; }
}
