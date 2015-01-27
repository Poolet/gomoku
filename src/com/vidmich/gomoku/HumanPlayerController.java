package com.vidmich.gomoku;

import com.vidmich.gomoku.game.Field;
import com.vidmich.gomoku.game.Move;
import com.vidmich.gomoku.game.PlayerContext;
import com.vidmich.gomoku.game.PlayerController;

/**
 * User: alexei
 */
public class HumanPlayerController implements PlayerController {
    @Override
    public Move makeYourMove(Field field, PlayerContext context) {
        // todo: rendezvous with UI thread to take the next move from UI controls.
        return new Move(0,0);
    }

    @Override
    public PlayerContext createPlayerContext() {
        return new PlayerContext(); // probably no custom context needed
    }
}
