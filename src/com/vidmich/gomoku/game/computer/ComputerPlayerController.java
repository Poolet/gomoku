package com.vidmich.gomoku.game.computer;

import com.vidmich.gomoku.game.Field;
import com.vidmich.gomoku.game.Move;
import com.vidmich.gomoku.game.PlayerContext;
import com.vidmich.gomoku.game.PlayerController;

/**
 * User: alexei
 */
public class ComputerPlayerController implements PlayerController {
    @Override
    public Move makeYourMove(Field field, PlayerContext context) {
        return new Move(0, 0); // todo: implement thinking
    }

    @Override
    public PlayerContext createPlayerContext() {
        return new ComputerPlayerContext();
    }
}
