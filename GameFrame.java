//package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//import other.BlueSquareFormatter;
//import other.RedRoundFormatter;
//
//import model.GameModel;

//import BlueSquareFormatter;
//import RedRoundFormatter;
//
//import GameModel;



/**
 * Frame to hold everything.
 *
 */
public class GameFrame
{
    /**
     * Creates a new GameFrame
     */
    public GameFrame()
    {
        buttonsVisible = true; //holds if the buttons are visible. if they are both false display board
                
        final JFrame frame = new JFrame();
        final GameModel model = new GameModel();
        mancalaComponent = new MancalaComponent(model, new RedRoundFormatter());
        
        final JPanel lowerPanel = new JPanel();
        JButton undo = new JButton("undo");
        undo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                model.undo();
            }
        });
        
        final JPanel panel = new JPanel();        

        JLabel label = new JLabel("Select style and number of stones");
        panel.add(label);
        
        //strategy pattern buttons
        JButton redButton3 = new JButton("Red, round, 3 stones");
        JButton redButton4 = new JButton("Red, round, 4 stones");

        redButton3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                model.setNumStones(3);
                model.setCurrentState("STARTED");

                //set to red squares
                buttonsVisible = false;
                setButtonVisible(false, panel);
                mancalaComponent.setFormatter(new RedRoundFormatter());
                setButtonVisible(true, lowerPanel);
            }
        });
        
        redButton4.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                model.setNumStones(4);
                model.setCurrentState("STARTED");
                //set to red squares
                buttonsVisible = false;
                setButtonVisible(false, panel);
                mancalaComponent.setFormatter(new RedRoundFormatter());
                setButtonVisible(true, lowerPanel);
            }
        });
        
        JButton blueButton3 = new JButton("Blue, square, 3 stones");
        JButton blueButton4 = new JButton("Blue, square, 4 stones");

        blueButton3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                model.setNumStones(3);
                model.setCurrentState("STARTED");
                //set to blue circles
                buttonsVisible = false;
                setButtonVisible(false, panel);
                mancalaComponent.setFormatter(new BlueSquareFormatter());
                setButtonVisible(true, lowerPanel);
            }
        });
        
        blueButton4.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                model.setNumStones(4);
                model.setCurrentState("STARTED");
                //set to blue circles
                buttonsVisible = false;
                setButtonVisible(false, panel);
                mancalaComponent.setFormatter(new BlueSquareFormatter());
                setButtonVisible(true, lowerPanel);
            }
        });
        
        panel.add(redButton3);
        panel.add(redButton4);

        panel.add(blueButton3);
        panel.add(blueButton4);
        
        lowerPanel.add(undo);
        frame.setSize(1000, 750);
        frame.setTitle("Mancala");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mancalaComponent, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(lowerPanel, BorderLayout.SOUTH);
        setButtonVisible(false, lowerPanel);
        frame.setVisible(true);
    }
    
    /**
     * sets the buttons on the frame visible or not depending on if the game is in session
     * @param visible true if visible
     */
    public void setButtonVisible(boolean visible, JPanel p)
    {
        p.setVisible(visible);     
        //if both are false, start the Game.
        if(!buttonsVisible)
        {
            mancalaComponent.startGame();
        }
    }
    private boolean buttonsVisible;
    private MancalaComponent mancalaComponent;
}