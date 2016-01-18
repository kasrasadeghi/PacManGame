/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import pacmanactors.PacManActor;
import pacmanactors.PacMan;
import pacmanactors.PacManGhost;
import apcscvm.DefaultControl;
import apcscvm.View;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import pacmanactors.Fruit;

/**
 *
 * @author DSTIGANT
 */

// class PacManView
// handles displaying the board and actors and processing user input
// Primary functions are:
// paint - handles painting the display to the screen
// handleTimePassage - handles time passing
// handleKeyPress - handles key presses
public class PacManView extends DefaultControl<PacManModel> implements View<PacManModel>
{
    private int width, height;
    private boolean showGhostTargets;
    private boolean paused;

    private HashMap<String, BufferedImage> fruitImages;
    
    private BufferedImage getFruitImage( String fruitName )
    {
        BufferedImage scarlet = fruitImages.get( fruitName );
        if ( scarlet != null )
        {
            return scarlet;
        }
        
        String file = fruitName + ".png";
        try
        {
            BufferedImage cherry = ImageIO.read( new File(file));
            scarlet = new BufferedImage( cherry.getWidth(), cherry.getHeight(), BufferedImage.TYPE_4BYTE_ABGR );
            for ( int i = 0; i < scarlet.getWidth(); i++ )
            {
                for ( int j = 0; j < scarlet.getHeight(); j++ )
                {
                    int pix = cherry.getRGB(i, j );
                    int a = pix % 256;
                    int b = (pix/256)%256;
                    int c = (pix/256/256)%256;
                    int d = (pix/256/256/256)%256;
                    
                    if ( a == 0 && b == 0 && c == 0 )
                    {
                        scarlet.setRGB(i, j, 0);
                    }
                    else
                    {
                        scarlet.setRGB(i, j, pix );
                    }
                }
            }
            fruitImages.put( fruitName, scarlet );
            
            return scarlet;
        } catch (IOException ex)
        {
            Logger.getLogger(PacManView.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public PacManView()
    {
        width = 0;
        height = 0;
        showGhostTargets = false;
        paused = false;
        fruitImages = new HashMap<>();
    }
    
    private Color stringToColor( String colorString )
    {
        if ( colorString.equals("Red") ) { return Color.RED; }
        else if ( colorString.equals("Pink") ) { return Color.PINK; }
        else if ( colorString.equals("Blue") ) { return Color.CYAN; }
        else if ( colorString.equals("Orange") ) { return Color.ORANGE; }
        else if ( colorString.equals("Magenta") ) { return Color.MAGENTA; }
        else if ( colorString.equals("Yellow") ) { return Color.YELLOW; }
        else if ( colorString.equals("Green") ) { return Color.GREEN; }
        else if ( colorString.equals("Cyan") ) { return Color.CYAN; }
        else if ( colorString.equals("White") ) { return Color.WHITE; }
        else {
            Scanner colscan = new Scanner( colorString );
            if ( colscan.hasNextInt() )
            {
                int r = colscan.nextInt();
                if ( 0 <= r && r <= 255 && colscan.hasNextInt() )
                {
                    int g = colscan.nextInt();
                    if ( 0 <= g && g <= 255 && colscan.hasNextInt() )
                    {
                        int b = colscan.nextInt();
                        if ( 0 <= b && b <= 255 )
                        {
                            int a = 255;
                            if ( colscan.hasNextInt() )
                            {
                                a = colscan.nextInt();
                            }
                            return new Color( r, g, b, a );
                        }
                    }
                }
            }
        }
        return Color.BLUE;
    }
    
    private void paintGhost( Graphics g, PacManGhost gh, int w, int h )
    {
        Location gl = gh.getLocation();
        double row = gl.getRow();
        double col = gl.getColumn();
        double cx = (col)*w;
        double cy = (row)*h;
        double hr = .8*w;
        double vr = .8*h;
        
        String colorString = gh.getMode() == PacManGhost.FRIGHTENED_MODE ? "Fright" : gh.getColor();
        Color color = stringToColor( colorString );
            
        if ( gh.getMode() != PacManGhost.EYE_MODE )
        {
            g.setColor( color );
            g.fillArc( (int)(cx-hr), (int)(cy-vr), (int)(2*hr), (int)(2*vr), 0, 180 );
            int [] xs = new int[9];
            int [] ys = new int[9];
            xs[0] = (int)(cx - hr);          ys[0] = (int)cy;
            xs[1] = (int)(cx - hr);          ys[1] = (int)(cy + vr);
            xs[2] = (int)(cx - 2*hr/3);      ys[2] = (int)(cy + 2*vr/3 );
            xs[3] = (int)(cx - hr/3);        ys[3] = (int)(cy + vr);
            xs[4] = (int)(cx);              ys[4] = (int)(cy + 2*vr/3);
            xs[5] = (int)(cx + hr/3);        ys[5] = (int)(cy + vr);
            xs[6] = (int)(cx + 2*hr/3);      ys[6] = (int)(cy + 2*vr/3);
            xs[7] = (int)(cx + hr);          ys[7] = (int)(cy + vr);
            xs[8] = (int)(cx + hr);          ys[8] = (int)(cy);
            g.fillPolygon( xs, ys, 9 );
        }
        
        g.setColor( Color.WHITE );
        g.fillOval( (int)(cx-hr/2.0), (int)(cy-vr/2.0), (int)(hr/2), (int)(vr/2) );
        g.fillOval( (int)(cx), (int)(cy-vr/2.0), (int)(hr/2), (int)(vr/2));
        
        g.setColor( Color.BLUE );
        int dir = gh.getDirection();
        if ( dir == PacManActor.DIRECTION_UP )
        {
            g.fillOval( (int)(cx - 3*hr/8.0), (int)(cy - vr/2.0), (int)(hr/4), (int)(vr/4) );
            g.fillOval( (int)(cx + hr/8.0), (int)(cy - vr/2.0), (int)(hr/4), (int)(vr/4) );
        }
        else if ( dir == PacManActor.DIRECTION_RIGHT )
        {
            g.fillOval( (int)(cx - hr/4.0), (int)(cy - 3*vr/8.0), (int)(hr/4), (int)(vr/4) );
            g.fillOval( (int)(cx + hr/4.0), (int)(cy - 3*vr/8.0), (int)(hr/4), (int)(vr/4) );
        }
        else if ( dir == PacManActor.DIRECTION_DOWN )
        {
            g.fillOval( (int)(cx - 3*hr/8.0), (int)(cy - vr/4.0), (int)(hr/4), (int)(vr/4) );
            g.fillOval( (int)(cx + hr/8.0), (int)(cy - vr/4.0), (int)(hr/4), (int)(vr/4) );
        }
        else if ( dir == PacManActor.DIRECTION_LEFT )
        {
            g.fillOval( (int)(cx - hr/2.0), (int)(cy - 3*vr/8.0), (int)(hr/4), (int)(vr/4) );
            g.fillOval( (int)(cx), (int)(cy - 3*vr/8.0), (int)(hr/4), (int)(vr/4) );
        }
        g.setColor( color );
        if ( showGhostTargets )
        {
            Location gt = gh.getTargetLocation();
            int tgtRow = (int)(Math.floor(gt.getRow()));
            int tgtCol = (int)(Math.floor(gt.getColumn()));
            
            g.fillRect( tgtCol*w, tgtRow*h, w, h );
        }
    }
    
    private void paintPacMan( Graphics g, PacMan pm, int w, int h )
    {
        
        int dir = pm.getDirection();
        Location pml = pm.getLocation();
        double row = pml.getRow();
        double col = pml.getColumn();
        double cx = (col)*w;
        double cy = (row)*h;
        double hr = .8*w;
        double vr = .8*h;
        
        int startAngle = 0;
        if ( dir == PacManActor.DIRECTION_DOWN )
        {
            startAngle = (int)(270 + pm.getAngle()/2);
        }
        else if ( dir == PacManActor.DIRECTION_LEFT )
        {
            startAngle = (int)(180 + pm.getAngle()/2);
        }
        else if ( dir == PacManActor.DIRECTION_RIGHT )
        {
            startAngle = (int)(pm.getAngle()/2);
        }
        else if ( dir == PacManActor.DIRECTION_UP )
        {
            startAngle = (int)(90 + pm.getAngle()/2);
        }
        g.setColor( Color.YELLOW );
        g.fillArc( (int)(cx-hr), (int)(cy-vr), (int)(2*hr), (int)(2*vr), startAngle, (int)(360 - pm.getAngle()));
        
    }
    
    private void paintFruit( Graphics g, Fruit c, int w, int h )
    {
        String fruitName = c.getClass().getSimpleName();
        BufferedImage fimg = getFruitImage( fruitName );
        if ( fimg != null )
        {
            Location l = c.getLocation();
            double cx = l.getColumn() * w;
            double cy = l.getRow() * h;
            g.drawImage( fimg, 
                    (int)(cx - fimg.getWidth()/2), 
                    (int)(cy - fimg.getHeight()/2), 
                    null 
            );
        }
    }
    
    private static PacManSquare [][] getBorderSquares( PacManMaze m, int r, int c )
    {
        PacManSquare [][] scarlet = new PacManSquare[3][3];
        for ( int i = -1; i < 2; i++ )
        {
            for ( int j = -1; j < 2; j++ )
            {
                int nr = r+i;
                int nc = c+j;
                if ( nr < 0 || nr >= m.getHeight() || nc < 0 || nc >= m.getWidth() )
                {
                    scarlet[i+1][j+1] = null;
                }
                else
                {
                    scarlet[i+1][j+1] = m.getSquare( new Location(nr, nc) );
                }
            }
        }
        return scarlet;
    }
    
    private boolean isWall( PacManSquare sq )
    {
        return sq == null || sq.isWall();
    }
            
    private boolean [][] getFillParts( PacManMaze m, int r, int c )
    {
        PacManSquare [][] borders = getBorderSquares( m, r, c );
        boolean [][] scarlet = new boolean[2][2];
        scarlet[0][0] = isWall( borders[0][0] ) && isWall( borders[1][0] ) && isWall( borders[0][1] );
        scarlet[0][1] = isWall( borders[0][1] ) && isWall( borders[0][2] ) && isWall( borders[1][2] );
        scarlet[1][0] = isWall( borders[1][0] ) && isWall( borders[2][0] ) && isWall( borders[2][1] );
        scarlet[1][1] = isWall( borders[1][2] ) && isWall( borders[2][1] ) && isWall( borders[2][2] );
        return scarlet;
    }
    
    private void paintSquare( Graphics g, PacManMaze m, int r, int c, int ULCx, int ULCy, int w, int h )
    {
        
        PacManSquare sq = m.getSquare( new Location(r,c) );
        if ( sq.isWall() )
        {
            g.setColor( Color.BLUE.darker() );
            boolean [][] fills = getFillParts( m, r, c );
            int numFills = 0;
            for ( int i = 0; i < 2; i++ )
            {
                for ( int j = 0; j < 2; j++ )
                {
                    if ( fills[i][j] )
                        numFills++;
                }
            }
            
            if ( numFills == 1 )
            {
                for ( int i = 0; i < 2; i++ )
                {
                    for ( int j = 0; j < 2; j++ )
                    {
                        if ( fills[i][j] )
                        {
                            if ( i == 0 && j == 0 )
                                g.drawArc( ULCx - w/2, ULCy - w/2, w, h, 270, 90 );
                            
                            else if ( i == 0 && j == 1 )
                                g.drawArc( ULCx + w/2, ULCy - h/2, w, h, 180, 90 );
                            else if ( i == 1 && j == 0 )
                                g.drawArc( ULCx - w/2, ULCy + h/2, w, h, 0, 90 );
                            else
                                g.drawArc( ULCx + w/2, ULCy + h/2, w, h, 90, 90 );                                    
                        }
                    }
                }
            }
            else if ( numFills == 2  )
            {
                if ( fills[0][0] && fills[0][1] || fills[1][0] && fills[1][1] )
                    g.drawLine( ULCx, ULCy + h/2, ULCx + w, ULCy + h/2 );
                else
                    g.drawLine( ULCx + w/2, ULCy, ULCx + w/2, ULCy + h );
            }
            else if ( numFills == 3 )
            {
                if ( !fills[0][0] || !fills[0][1] ) 
                {
                    g.drawLine( ULCx + w/2, ULCy, ULCx + w/2, ULCy + h/2 );
                }
                else 
                {
                    g.drawLine( ULCx + w/2, ULCy + h/2, ULCx + w/2, ULCy + h );
                }
                if ( !fills[0][0] || !fills[1][0] )
                {
                    g.drawLine( ULCx, ULCy + h/2, ULCx + w/2, ULCy + h/2 );
                }
                else
                {
                    g.drawLine( ULCx + w/2, ULCy + h/2, ULCx + w, ULCy + h/2 );
                }
            }
        }
        else
        {
            PacManPellet p = sq.getPellet();
            if ( p != null )
            {
                if ( p instanceof PacManPowerPellet )
                {
                    g.setColor( Color.YELLOW );
                    g.fillOval( ULCx + w/4, ULCy + h/4, w/2, h/2 );
                }
                else
                {
                    g.setColor( Color.WHITE );
                    g.fillOval( ULCx + 2*w/5, ULCy + 2*h/5, w/5, h/5 );
                }
            }
            
            
        }
    }
    
    @Override
    public void paint(PacManModel m, Graphics g, int w, int h)
    {
        width = w;
        height = h;
        
        g.setColor(Color.BLACK );
        g.fillRect( 0, 0, w, h );
        PacManMaze maze = m.getMaze();
        int sqw = w / maze.getWidth();
        int sqh = h / maze.getHeight();
        
        Graphics2D g2D = (Graphics2D)g;
        Stroke s = g2D.getStroke();
        g2D.setStroke( new BasicStroke(5) );
        for ( int i = 0; i < maze.getHeight(); i++ )
        {
            for ( int j = 0; j < maze.getWidth(); j++ )
            {
                paintSquare( g, maze, i, j, j*sqw, i*sqh, sqw, sqh );
            }
        }
        g2D.setStroke( s );
    
        if ( m.getFruit() != null )
            paintFruit( g, m.getFruit(), sqw, sqh );
        
        for ( int i = 0; i < m.getNumGhosts(); i++ )
        {
            paintGhost( g, m.getGhost(i), sqw, sqh );
        }
               
        paintPacMan( g, m.getPacMan(), sqw, sqh );
    }

    public void handleTimePassage( PacManModel m, int ea, int dt )
    {
        
        if ( !paused )
        {
            m.update( dt );
        }
    }
    
    @Override
    public void handleKeyPress( PacManModel m, int ea, KeyEvent ke )
    {
        if ( ke.getKeyCode() == KeyEvent.VK_UP )
        {
            m.getPacMan().goUp();
        }
        else if ( ke.getKeyCode() == KeyEvent.VK_DOWN )
        {
            m.getPacMan().goDown();
        }
        else if ( ke.getKeyCode() == KeyEvent.VK_RIGHT )
        {
            m.getPacMan().goRight();
        }
        else if ( ke.getKeyCode() == KeyEvent.VK_LEFT )
        {
            m.getPacMan().goLeft();
        }
        else if ( ke.getKeyCode() == KeyEvent.VK_T )
        {
            showGhostTargets = !showGhostTargets;
        }
        else if ( ke.getKeyCode() == KeyEvent.VK_SPACE )
        {
            paused = !paused;
        }
    }
    
    public void pause()
    {
        paused = true;
    }
    
    public void unpause()
    {
        paused = false;
    }
}
