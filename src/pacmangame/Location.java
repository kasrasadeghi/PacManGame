/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

/**
 *
 * @author DSTIGANT
 */

// Class Location - stores a pair of doubles which represents a location (row, col)
// within the game
public class Location
{
    private double row, col;
    
    // Constructor - specify the row and the column
    public Location( double r, double c )
    {
        row = r;
        col = c;
    }
    
    // getters - access the row and the column
    public double getRow() { return row; }
    public double getColumn() { return col; }

    // toString - returns a string representation of the location: "<r, c>"
    public String toString() { return "<" + row + ", " + col + ">"; }
}
