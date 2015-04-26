//package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import other.PitShape;
//import other.ShapeFormatter;
//
//import model.GameModel;

//import PitShape;
//import ShapeFormatter;
//
//import GameModel;

/**
 * Creates the component that the shapes/board/buttons are painted on.
 * Serves as both Controller and View in the MVC Pattern.
 *
 */
@SuppressWarnings("serial")
public class MancalaComponent extends JComponent implements ChangeListener
{
    /**
     * Creates a new MancalaComponent
     */
    public MancalaComponent(GameModel aModel, ShapeFormatter formatter)
    {
        model = aModel;
        stones = new int[14];
        this.formatter = formatter;
        setVisible(false);
        model.attach(this);
        
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                for(int i = 0; i < pits.size(); i++)
                {
                    if(pits.get(i).contains(e.getPoint()) &&
                                model.canMakeMove(i))
                    {
                        model.makeMove(i);
                        return;
                    }
                }
            }
        });
    }
    
    /**
     * Creates the board view.
     */
    private void computeBoard()
    {
        pits = new ArrayList<PitShape>();
        shapes = new ArrayList<Shape>();
        Rectangle2D.Double boardShape = new Rectangle2D.Double(5, 20, 965, 670); //the board shape

        //variables for pit locations
        final int PIT_WIDTH = 100;
        final int PIT_HEIGHT = 150;
        final int TOP_PIT_Y = 75;
        final double BOTTOM_PIT_Y = boardShape.getHeight() - TOP_PIT_Y - PIT_HEIGHT;

        //setSize(920, 600);

        Color c = formatter.formatPitColor();
        //top pits
        PitShape p0 = new PitShape(PIT_WIDTH + PIT_WIDTH/4, TOP_PIT_Y, PIT_WIDTH, PIT_HEIGHT, c);
        p0.setShape(formatter.formatPitShape(p0));
        p0.setStones(stones[0]);

        PitShape p1 = new PitShape(2*PIT_WIDTH + PIT_WIDTH/2, TOP_PIT_Y, PIT_WIDTH, PIT_HEIGHT, c); //adds a pit 1
        p1.setShape(formatter.formatPitShape(p1));
        p1.setStones(stones[1]);

        PitShape p2 = new PitShape(3*PIT_WIDTH + 3*PIT_WIDTH/4, TOP_PIT_Y, PIT_WIDTH, PIT_HEIGHT,c ); //adds a pit 2
        p2.setShape(formatter.formatPitShape(p2));
        p2.setStones(stones[2]);

        PitShape p3 = new PitShape(5*PIT_WIDTH, TOP_PIT_Y, PIT_WIDTH, PIT_HEIGHT,c); //adds a pit 3
        p3.setShape(formatter.formatPitShape(p3));
        p3.setStones(stones[3]);

        PitShape p4 = new PitShape(6*PIT_WIDTH + PIT_WIDTH/4, TOP_PIT_Y, PIT_WIDTH, PIT_HEIGHT,c); //adds a pit 4
        p4.setShape(formatter.formatPitShape(p4));
        p4.setStones(stones[4]);

        PitShape p5 = new PitShape(7*PIT_WIDTH + PIT_WIDTH/2, TOP_PIT_Y, PIT_WIDTH, PIT_HEIGHT,c); //adds a pit 5
        p5.setShape(formatter.formatPitShape(p5));
        p5.setStones(stones[5]);

        PitShape mancala1 = new PitShape(10, 120, PIT_WIDTH, 3*PIT_HEIGHT, c);
        mancala1.setShape(formatter.formatPitShape(mancala1));
        mancala1.setStones(stones[6]);

        //bottom pits
        PitShape p7 = new PitShape(PIT_WIDTH + PIT_WIDTH/4, (int)BOTTOM_PIT_Y, PIT_WIDTH, PIT_HEIGHT, c);
        p7.setShape(formatter.formatPitShape(p7));
        p7.setStones(stones[7]);

        PitShape p8 = new PitShape(2*PIT_WIDTH + PIT_WIDTH/2, (int)BOTTOM_PIT_Y, PIT_WIDTH, PIT_HEIGHT, c);
        p8.setShape(formatter.formatPitShape(p8));
        p8.setStones(stones[8]);

        PitShape p9 = new PitShape(3*PIT_WIDTH + 3*PIT_WIDTH/4, (int)BOTTOM_PIT_Y, PIT_WIDTH, PIT_HEIGHT, c);
        p9.setShape(formatter.formatPitShape(p9));
        p9.setStones(stones[9]);

        PitShape p10 = new PitShape(5*PIT_WIDTH, (int)BOTTOM_PIT_Y, PIT_WIDTH, PIT_HEIGHT, c);
        p10.setShape(formatter.formatPitShape(p10));
        p10.setStones(stones[10]);

        PitShape p11 = new PitShape(6*PIT_WIDTH + PIT_WIDTH/4, (int)BOTTOM_PIT_Y, PIT_WIDTH, PIT_HEIGHT, c);
        p11.setShape(formatter.formatPitShape(p11));
        p11.setStones(stones[11]);

        PitShape p12 = new PitShape(7*PIT_WIDTH + PIT_WIDTH/2, (int)BOTTOM_PIT_Y, PIT_WIDTH, PIT_HEIGHT, c);
        p12.setShape(formatter.formatPitShape(p12));
        p12.setStones(stones[12]);

        PitShape mancala2 = new PitShape((int)boardShape.getWidth()-PIT_WIDTH, 120, PIT_WIDTH, 3*PIT_HEIGHT, c);
        mancala2.setShape(formatter.formatPitShape(mancala2));
        mancala2.setStones(stones[13]);

        // Add Shapes in right order
        addShape(p7);
        addShape(p8);
        addShape(p9);
        addShape(p10);
        addShape(p11);
        addShape(p12);   
        addShape(mancala2);
        addShape(p5);
        addShape(p4);
        addShape(p3);
        addShape(p2);
        addShape(p1);
        addShape(p0);
        addShape(mancala1);

        shapes.add(boardShape);
    }
    
    /**
     * Adds a new shape (pit or mancala) to the component
     * @param shape the shape to be added
     */
    public void addShape(PitShape shape)
    {
        pits.add(shape);        
    }

    /**
     * Paints the component
     * @param g the Graphics
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        for(Shape s: shapes)
            g2.draw(s);

        for(PitShape p: pits)
        {
            p.fill(g2);
        }
        // Center label:
        String centerText = "";
        if (model.getCurrentState() == GameModel.GameState.ONGOING)
        {
            if (model.getCurrentPlayer() == GameModel.Player.A)
            {
                centerText = "Turn: Player 1";
            }
            else
            {
                centerText = "Turn: Player 2";
            }
        }
        else if (model.getCurrentState() == GameModel.GameState.ENDED)
        {
            centerText = "Final Score: " + model.getScore(GameModel.Player.A) + " vs " + model.getScore(GameModel.Player.B);
        }
        g.setColor(Color.RED);

        Font font = new Font("Arial", Font.PLAIN, 26);

        g2.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawString(centerText, this.getX() + this.getWidth() / 2 - fm.stringWidth(""+stones) / 2,
                this.getY() + this.getHeight() / 2 - fm.getAscent() / 2);

    }

    /**
     * Updates the pits when a change occurs
     */
    @Override
    public void stateChanged(ChangeEvent c)
    {
        computeBoard();
        stones = model.getPits(); //array containing stones from gameModel

        int pitSize = pits.size(); //the size of pits
        //update the pits with the stones from the model
        for(int i = 0; i < pitSize; i++)
        {
            pits.get(i).setStones(stones[i]);
        }

        repaint();
    }

    /**
     * Sets the board visibility.
     * @param visible true if visible
     */
    public void setBoardVisible(boolean visible)
    {
        setVisible(visible);
    }
    
    /**
     * Sets the formatter depending on the button clicked. Strategy pattern.
     * @param sf the ShapeFormatter
     */
    public void setFormatter(ShapeFormatter sf)
    {
        formatter = sf;
        computeBoard();
    }

    /**
     * Starts the game
     */
    public void startGame()
    {
        setBoardVisible(true);
        model.setCurrentState("ONGOING");
    }

    private ArrayList<Shape> shapes;
    private ArrayList<PitShape> pits;
    private GameModel model;
    private ShapeFormatter formatter;
    private int[] stones;
}