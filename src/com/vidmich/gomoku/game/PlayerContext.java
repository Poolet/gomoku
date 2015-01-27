package com.vidmich.gomoku.game;

/**
 * User: alexei
 */
public class PlayerContext {
    private int turn;

    public int getTurn() {
        return turn;
    }

    public int incrementTurn() {
        return ++turn;
    }
}
