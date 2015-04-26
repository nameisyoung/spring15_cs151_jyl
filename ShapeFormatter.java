//package other;

import java.awt.Color;
import java.awt.Shape;

/**
 * Interface for defining a ShapeFormatter. Part of Strategy Pattern.
 * 
 */
public interface ShapeFormatter
{
        /**
         * Format of the color.
         * 
         * @return the color
         */
    Color formatPitColor();
    
    /**
     * Format of the PitShape.
     * 
     * @param p - the pit that needs to be formated.
     * @return the shape
     */
    Shape formatPitShape(PitShape p);
}