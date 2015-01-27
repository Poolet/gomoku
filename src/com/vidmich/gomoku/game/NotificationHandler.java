package com.vidmich.gomoku.game;

/**
 * User: alexei
 */
public interface NotificationHandler {
    void illegalMove(Move move, Player player); // should not happen - but just in case of whatever error conditions - we need a way to display them to user.
}
