//package other;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * Concrete Formatter, part of the Strategy pattern.
 * Blue Color with rectangles pits.
 *
 */
public class BlueSquareFormatter implements ShapeFormatter
{
    @Override
    public Color formatPitColor()
    {
        return Color.BLUE;
    }

    @Override
    public Shape formatPitShape(PitShape p)
    {
        return new Rectangle2D.Double(p.getX(), p.getY(), p.getWidth(), p.getHeight());
    }    
}