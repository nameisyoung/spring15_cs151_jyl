//package model;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * The model of the Mancala game. Part of the MVC Pattern.
 *
 */
public class GameModel
{
    public static final int PITS_NUM = 14;
    public static final int PLAYER_A_MANCALA = 6;
    public static final int PLAYER_B_MANCALA = 13;
    private final int MAX_UNDOS = 3;
    public static enum GameState {PLACING, ONGOING, ENDED};
    public static enum Player {A, B};

    private int[] pits;
    private int[] lastTurnPits;
    private Player currentPlayer;
    private GameState currentState;
    private int aUndos;
    private int bUndos;
    private boolean lastInMancala;
    private boolean canUndo;

    private ArrayList<ChangeListener> listeners;

    /**
     * Basic Constructor of a GameModel.
     */
    public GameModel()
    {
        lastInMancala = false;
        aUndos = 0;
        bUndos = 0;
        lastTurnPits = new int[PITS_NUM];
        pits = new int[PITS_NUM];
        currentPlayer = Player.A;
        currentState = GameState.PLACING;
        listeners = new ArrayList<ChangeListener>();
        canUndo = false;
    }

    /**
     * Attach a listener to the model.
     * 
     * @param c
     */
    public void attach(ChangeListener c)
    {
        listeners.add(c);
    }

    /**
     * Used when there is a change in the model,
     * to notify all listeners.
     */
    private void notifyAllListeners()
    {
        for (ChangeListener c : listeners)
        {
            c.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Given a valid pitIndex, determine who owns it.
     * 
     * @param pitIndex
     * @return the player that owns the pit
     */
    private Player getPitOwner(int pitIndex)
    {
        if (pitIndex >= 0 && pitIndex <= PLAYER_A_MANCALA)
            return Player.A;
        // If not assume it's player B's
        return Player.B;
    }

    /**
     * Checks if moving from pitIndex is possible.
     * 
     * @param pitIndex the index of the pit
     * @return true if move can be made, false otherwise
     */
    public boolean canMakeMove(int pitIndex)
    {
        // Moves can be made only from valid pits
        if (pitIndex < 0 || pitIndex > PITS_NUM
                || pitIndex == PLAYER_A_MANCALA
                || pitIndex == PLAYER_B_MANCALA)
            return false;

        // Moves can be made only if the state of the game is ONGOING
        if (currentState != GameState.ONGOING)
            return false;

        // Moves can be made only from non-empty pits
        if (pits[pitIndex] == 0)
            return false;

        // Moves can be made only if the player owns the pit
        if (getPitOwner(pitIndex) != currentPlayer)
            return false;

        return true;
    }

    /**
     * Give the turn to the next Player.
     */
    private void nextTurn()
    {
        if (currentPlayer == Player.A)
        {
            currentPlayer = Player.B;
        }
        else
        {
            currentPlayer = Player.A;
        }
    }

    /**
     * Return if the given pit is a mancala or not.
     * 
     * @param pitIndex
     * @return if the given pit is a mancala or not.
     */
    private boolean isMancala(int pitIndex)
    {
        return (pitIndex == PLAYER_A_MANCALA ||
                pitIndex == PLAYER_B_MANCALA);
    }

    /**
     * Compute the pit on the opposite.
     * Pre-Condition: is not a Mancala
     * @param pitIndex
     * @return the pit index of opposite pit
     */
    private int oppositePit(int pitIndex)
    {
        // 0 - 12
        // 1 - 11
        return Math.abs(12 - pitIndex);
    }

    /**
     * Collects the stones at the end of the game and puts them in the Mancalas.
     */
    private void collectStones()
    {
        // Clean up the pits by adding the stones in the mancalas
        for (int i = 0; i < PITS_NUM; ++i) if (!isMancala(i))
        {
            if (getPitOwner(i) == Player.A)
            {
                pits[PLAYER_A_MANCALA] += pits[i];
                pits[i] = 0;
            }
            else
            {
                pits[PLAYER_B_MANCALA] += pits[i];
                pits[i] = 0;
            }
        }
    }

    /**
     * Checks if the game is over. This is private,
     * the viewers should check via currentStatus.
     * @return whether the game is over or not
     */
    private boolean isOver()
    {
        int totalA = 0;
        int totalB = 0;
        for (int i = 0; i < PITS_NUM; ++i) if (!isMancala(i))
        {
            if (getPitOwner(i) == Player.A)
            {
                totalA += pits[i];
            }
            else
            {
                totalB += pits[i];
            }
        }
        return (totalA == 0 || totalB == 0);
    }

    /**
     * Given a pitIndex, makes the move
     * and notify that the model is changed.
     * 
     * Pre-condition: is a valid move.
     * 
     * @param pitIndex - the pit
     */
    public void makeMove(int pitIndex)
    {
        /**
         * Take stones in hand and move counter-clockwise
         * and place them.
         */
        
        // First save the state of pits
        this.save();
        
        canUndo = true;
        //reset player b's undos
        if(currentPlayer.equals(Player.A))
            bUndos = 0;
        else
            aUndos = 0;
        
        int inHand = pits[pitIndex];
        pits[pitIndex] = 0;

        int where = pitIndex;
        while (inHand > 0)
        {
            // update where
            where++;
            if (where >= PITS_NUM) where = 0;

            // Skip if not currentPlayer's mancala
            if (isMancala(where) && getPitOwner(where) != currentPlayer)
            {
                // skip it
                continue;
            }

            // Place stone in pit
            pits[where]++;
            inHand--;
        }

        // Compute who is next
        // where is the last pit where a piece was placed

        // own mancala
        if (getPitOwner(where) == currentPlayer && isMancala(where))
        {
            //free turn
            lastInMancala = true;
        }
        // empty pit on your side
        else if (getPitOwner(where) == currentPlayer
                && pits[where] == 1
                && pits[oppositePit(where)] > 0)
        {
            // capture pieces
            int captured = pits[where] + pits[oppositePit(where)];
            pits[where] = pits[oppositePit(where)] = 0;
            if (currentPlayer == Player.A)
            {
                pits[PLAYER_A_MANCALA] += captured;
            }
            else {
                pits[PLAYER_B_MANCALA] += captured;
            }
            // Give turn to next player
            lastInMancala = false;
            nextTurn();
        }
        else
        {
            // Give turn to next player.
            lastInMancala = false;
            nextTurn();
        }

        // Check if the game is done.
        if (isOver())
        {
            collectStones();
            currentState = GameState.ENDED;
        }
        // Notify all Listeners
        this.notifyAllListeners();
    }

    /**
     * Sets the number of stones to be in each pit originally. Called when the game starts after button is clicked.
     * @param num the number of stones
     */
    public void setNumStones(int num)
    {
        int pitLen = pits.length;
        for(int i = 0; i < pitLen; i++) if (!isMancala(i))
            pits[i] = num;

        this.notifyAllListeners();
    }

    /**
     * Sets the current state of the game
     * @param state the state
     */
    public void setCurrentState(String state)
    {
        if(state.equals("PLACING"))
            currentState = GameState.PLACING;
        else if(state.equals("ONGOING"))
            currentState = GameState.ONGOING;
        else
            currentState = GameState.ENDED;
    }

    /**
     * Getter for current player.
     * @return the current player.
     */
    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    /**
     * Getter for current state.
     * @return the current state
     */
    public GameState getCurrentState()
    {
        return currentState;
    }

    /**
     * Gets the pit array with amount of stones in each
     * @return the pit array
     */
    public int[] getPits()
    {
        return pits;
    }

    /**
     * Return the score of the requested player.
     * @param who requester player
     * @return the score
     */
    public int getScore(GameModel.Player who)
    {
        if (who == GameModel.Player.A)
            return pits[PLAYER_A_MANCALA];
        if (who == GameModel.Player.B)
            return pits[PLAYER_B_MANCALA];
        return 0;
    }

    /**
     * Undos the last move, if possible.
     */
    public void undo()
    {
        if (!canUndo)
                return;
        
        boolean possible = false;
        
        switch (currentPlayer)
        {
                case A:
                {
                        if (lastInMancala && aUndos < MAX_UNDOS)
                        {
                                aUndos++;
                                possible = true;
                        }
                        else if (!lastInMancala && bUndos < MAX_UNDOS)
                        {
                                bUndos++;
                                currentPlayer = Player.B;
                                possible = true;
                        }
                        break;
                }
                case B:
                {
                        if (lastInMancala && bUndos < MAX_UNDOS)
                        {
                                bUndos++;
                                possible = true;
                        }
                        else if (!lastInMancala && aUndos < MAX_UNDOS)
                        {
                                aUndos++;
                                possible = true;
                                currentPlayer = Player.A;
                        }
                        break;
                }
        }
        
        if (aUndos < MAX_UNDOS && currentState.equals(GameState.ENDED)  || 
                bUndos < MAX_UNDOS && currentState.equals(GameState.ENDED))
        {
            currentState = GameState.ONGOING;
        }
        if (possible)
        {
                canUndo = false;
            pits = lastTurnPits.clone();
                notifyAllListeners();
        }
    }

    /**
     * Saves the board before the move occurs
     */
    public void save()
    {
        lastTurnPits = pits.clone();
    }
}