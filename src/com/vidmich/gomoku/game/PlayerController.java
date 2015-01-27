package com.vidmich.gomoku.game;

/**
 * User: alexei
 */
public interface PlayerController {
    Move makeYourMove(Field field, PlayerContext context);
    PlayerContext createPlayerContext();
}
