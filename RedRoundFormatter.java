//package other;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;


/**
 * Concrete Formatter, part of the Strategy pattern.
 * Red Color with Round pits.
 *
 */
public class RedRoundFormatter implements ShapeFormatter
{
    @Override
    public Color formatPitColor()
    {
        return Color.RED;
    }

    @Override
    public Shape formatPitShape(PitShape p)
    {
        return new Ellipse2D.Double(p.getX(), p.getY(), p.getWidth(), p.getHeight());
    }
}