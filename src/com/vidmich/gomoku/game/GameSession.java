package com.vidmich.gomoku.game;

import java.util.List;

/**
 * User: alexei
 */
public interface GameSession {
    List<Player> getPlayers();
    Field getField();

    void run();
    void save();
    void load();
    void destroy();

    void subscribePlayerController(PlayerController playerController);

    void subscribeNotificationHandler(NotificationHandler notificationHandler);
}
