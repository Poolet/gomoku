package group5.gomoku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by Tyler on 1/29/2015.
 */
public class BoardView extends View{

    private static MediaPlayer piecePlaced;
    private static MediaPlayer newGame;
    private static MediaPlayer winSound;

    //Determine whether we are in multiplayer or not
    public boolean online;
    private boolean isTurn;
    //Score text views
    private TextView score1;
    private TextView score2;
    //Score variables;
    public int scoreValue1;
    public int scoreValue2;
    //strings for passing scores
    private String strScoreValue1;
    private String strScoreValue2;
    //parent holds the containing board activity
    private Board parent;
    //playerName will hold the text view that contains the current player.
    private TextView playerName;
    //isBlack will track whose turn it is
    private boolean isBlack = true;
    //gridDimension will hold information about the size of the board
    public int gridDimension;
    //count variable for checking the state when no player can win the game
    private int num_empty_spaces;
    //circleSize holds information about the size of the pieces.
    private float circleSize;
    //Store 0 for empty square, 1 for black, 2 for white
    public int[][] boardState;
    public boolean AI = false;

    //Create some paints we will draw with, one for each player piece, and one for the grid lines
    private Paint gridPaint;
    private Paint blackPiece;
    private Paint whitePiece;
    private Chronometer chronometer;

    Button scores =(Button) findViewById(R.id.button_home);



    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Init();
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public BoardView(Context context) {
        super(context);
        Init();
    }

    public String packageGameState(int size, int client)
    {
        String s = "" + '0' + client;
        for(int y = 0; y<size;y++)
        {
            for(int x=0; x < size; x++)
            {
                s = s + boardState[x][y];
            }
        }
        return s;
    }

    public void decodeGameState(int size, String s)
    {
        Scanner in = new Scanner(s);
        for(int y = 0; y<size;y++)
        {
            for(int x=0; x < size; x++)
            {
                boardState[x][y] = Integer.parseInt(String.valueOf(in.findInLine(".").charAt(0)));
            }
        }
    }
    //Show pop up message when player 1 wins
    private void showSimplePopUpBlackWins() {

        chronometer.stop();
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this.getContext());
        helpBuilder.setCancelable(false);
        helpBuilder.setTitle("     Game Over");
        helpBuilder.setMessage("      Player 1 Wins");
        helpBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Bundle gameInfo = new Bundle();
                gameInfo.putInt("boardSize", gridDimension-1);
                gameInfo.putInt("score1", scoreValue1);
                gameInfo.putInt("score2", scoreValue2);
                gameInfo.putBoolean("AI", AI);

                Intent i=new Intent();
                if(!online)
                    i.setClass(getContext(),Board.class);
                else
                    i.setClass(getContext(), MultiPlayer.class);
                i.putExtras(gameInfo);

                getContext().startActivity(i);
            }

        });
        helpBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle gameInfo = new Bundle();
                gameInfo.putInt("boardSize", gridDimension-1);
                gameInfo.putInt("score1", scoreValue1);
                gameInfo.putInt("score2", scoreValue2);
                gameInfo.putBoolean("AI", AI);

                Intent i=new Intent();
                i.setClass(getContext(),Scores.class);
                i.putExtras(gameInfo);

                getContext().startActivity(i);
            }
        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }


    //Show pop up message when player 2 wins
    private void showSimplePopUpWhiteWins() {

        chronometer.stop();
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this.getContext());
        helpBuilder.setCancelable(false);
        helpBuilder.setTitle("     Game Over");
        helpBuilder.setMessage("      Player 2 Wins");
        helpBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Bundle gameInfo = new Bundle();
                gameInfo.putInt("boardSize", gridDimension-1);
                gameInfo.putInt("score1", scoreValue1);
                gameInfo.putInt("score2", scoreValue2);
                gameInfo.putBoolean("AI", AI);
                Intent i=new Intent();
                if(!online)
                    i.setClass(getContext(),Board.class);
                else
                    i.setClass(getContext(), MultiPlayer.class);
                i.putExtras(gameInfo);
                getContext().startActivity(i);
            }

        });
        helpBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent();
                Bundle gameInfo = new Bundle();
                i.setClass(getContext(),Scores.class);
                gameInfo.putInt("boardSize", gridDimension-1);
                gameInfo.putInt("score1", scoreValue1);
                gameInfo.putInt("score2", scoreValue2);
                gameInfo.putBoolean("AI", AI);
                i.putExtras(gameInfo);
                getContext().startActivity(i);
            }


        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }

    //Show pop up message when board is full
    private void showSimplePopUpGameTie() {


        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this.getContext());
        helpBuilder.setCancelable(false);
        helpBuilder.setTitle("     Game Over");
        helpBuilder.setMessage("      Game Tie");
        helpBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Bundle gameInfo = new Bundle();
                gameInfo.putInt("boardSize", gridDimension-1);
                gameInfo.putInt("score1", scoreValue1);
                gameInfo.putInt("score2", scoreValue2);
                gameInfo.putBoolean("AI", AI);
                Intent i=new Intent();
                i.setClass(getContext(),Board.class);
                i.putExtras(gameInfo);
                getContext().startActivity(i);
            }

        });
        helpBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent();
                i.setClass(getContext(),Scores.class);
                i.putExtra("score1",scoreValue1);
                i.putExtra("score2",scoreValue2);
                getContext().startActivity(i);
            }


        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }

    public boolean checkGameStatus(int start_x, int start_y,
                                   int end_x, int end_y) {
        int i, j, x, y, num_uniq = 5;
        int b_count = 0, w_count = 0;
        int black = 1, white = 2;

        //check horizontal
        for (i = start_y; i <= end_y; i++) {

            b_count = 0;
            w_count = 0;

            for (j = start_x; j <= end_x; j++) {
                if (boardState[i][j] == black) {
                    b_count++;
                    w_count = 0;
                } else if (boardState[i][j] == white) {
                    w_count++;
                    b_count = 0;
                } else {
                    w_count++;
                    b_count++;
                }

                if (b_count == num_uniq || w_count == num_uniq) {
                    if (j == end_x || j - (num_uniq - 1) == start_x)
                        return true;
                }

                if (b_count == num_uniq) {
                    if ((j + 1 <= end_x && boardState[i][j + 1] != white) ||
                            (j - num_uniq >= start_x && boardState[i][j - num_uniq] != white))
                        return true;
                }

                if (w_count == num_uniq) {
                    if ((j + 1 <= end_x && boardState[i][j + 1] != black) ||
                            (j - num_uniq >= start_x && boardState[i][j - num_uniq] != black))
                        return true;
                }
            }
        }

        //check vertical
        for (j = start_x; j <= end_x; j++) {
            b_count = 0;
            w_count = 0;
            for (i = start_y; i <= end_y; i++) {
                if (boardState[i][j] == black) {
                    b_count++;
                    w_count = 0;
                } else if (boardState[i][j] == white) {
                    w_count++;
                    b_count = 0;
                } else {
                    w_count++;
                    b_count++;
                }

                if (b_count == num_uniq || w_count == num_uniq) {
                    if (i == end_y || i - (num_uniq - 1) == start_y)
                        return true;
                }

                if (b_count == num_uniq) {
                    if ((i+1 <= end_y && boardState[i + 1][j] != white) ||
                            (i - num_uniq >= start_y && boardState[i - num_uniq][j] != white))
                        return true;
                }

                if (b_count == num_uniq) {
                    if ((i+1 <= end_y && boardState[i + 1][j] != black) ||
                            (i - num_uniq >= start_y && boardState[i - num_uniq][j] != black))
                        return true;
                }
            }
        }

        // check diagonal1
        for (x = start_x; x <= end_x; x++) {
            for (y = start_y; y <= end_y; y++) {
                if (x != start_x && y != start_y)
                    continue;

                b_count = 0;
                w_count = 0;

                for (i = y, j = x; i <= end_y && j <= end_x; i++, j++) {

                    if (boardState[i][j] == black) {
                        b_count++;
                        w_count = 0;
                    } else if (boardState[i][j] == white) {
                        w_count++;
                        b_count = 0;
                    } else {
                        w_count++;
                        b_count++;
                    }

                    if ((b_count == num_uniq || w_count == num_uniq) &&
                            (i == end_y || j == end_x ||
                            i - num_uniq - 1 == start_y ||
                            j - num_uniq - 1 == start_x))
                        return true;

                    if (b_count == num_uniq){
                        if ((i+1 <= end_y && j+1 <= end_x &&
                                boardState[i+1][j+1] != white) ||
                                (i-num_uniq >= start_y && j-num_uniq >= start_x &&
                                        boardState[i-num_uniq][j-num_uniq] != white))
                            return true;
                    }

                    if (w_count == num_uniq){
                        if ((i+1 <= end_y && j+1 <= end_x &&
                                boardState[i+1][j+1] != black) ||
                                (i-num_uniq >= start_y && j-num_uniq >= start_x &&
                                        boardState[i-num_uniq][j-num_uniq] != black))
                            return true;
                    }

                }
            }
        }

        // check diagonal2
        for (x = start_x; x <= end_x; x++) {
            for (y = start_y; y <= end_y; y++) {
                if (x != end_x && y != end_y)
                    continue;

                b_count = 0;
                w_count = 0;

                for (i = y, j = x; i >= start_y && j <= end_x; i--, j++) {

                    if (boardState[i][j] == black) {
                        b_count++;
                        w_count = 0;
                    } else if (boardState[i][j] == white) {
                        w_count++;
                        b_count = 0;
                    } else {
                        w_count++;
                        b_count++;
                    }

                    if ((b_count == num_uniq || w_count == num_uniq) &&
                            (i == start_y || j == end_x ||
                            i + num_uniq - 1 == end_y ||
                            j - num_uniq - 1 == start_x))
                        return true;

                    if (b_count == num_uniq) {
                        if ((i - 1 >= start_y && j + 1 <= end_x &&
                                boardState[i - 1][j + 1] != white) ||
                                (i + num_uniq <= end_y && j - num_uniq >= start_x &&
                                        boardState[i + num_uniq][j - num_uniq] != white))
                            return true;
                    }

                    if (w_count == num_uniq) {
                        if ((i - 1 >= start_y && j + 1 <= end_x &&
                                boardState[i - 1][j + 1] != black) ||
                                (i + num_uniq <= end_y && j - num_uniq >= start_x &&
                                        boardState[i + num_uniq][j - num_uniq] != black))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkSuccess(int color, int isClient)
    {
        for(int x = 0; x < gridDimension - 1; x++)
        {
            for(int y = 0; y < gridDimension - 1; y++)
            {
                if( checkSuccessDiagonal1(x, y, color) ||
                    checkSuccessDiagonal2(x, y, color) ||
                    checkSuccessHorizontal(x, y, color) ||
                    checkSuccessVertical(x, y, color)
                   )
                {
                   if(color == 1)
                   {
                       scoreValue1++;
                       score1.setText("" + scoreValue1);
                   }
                   else if(color == 2)
                   {
                       scoreValue2++;
                       score2.setText("" + scoreValue2);
                   }
                   winSound = MediaPlayer.create(this.getContext(), R.raw.start_tone);
                    winSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();

                        };
                    });
                   winSound.start();
                   return true;

                }
            }
        }
        return false;
    }

    private boolean checkSuccessHorizontal(int x, int y, int color) {

        int i, pos_start, pos_end;
        int num_uniq = 5;
        int oppon_color = (color == 1) ? 2 : 1;

        //Check horizontal
        i = 0;
        while ((x - i) >= 0 && boardState[x - i][y] == color)
            i++;

        if (i > num_uniq)
            return false;

        pos_start = x - i + 1;
        i = 0;
        while ((pos_start + i < gridDimension-1) &&
                boardState[pos_start + i][y] == color)
            i++;

        if (i != num_uniq)
            return false;

        pos_end = pos_start + i - 1;
        if (pos_start != 0 && pos_end != gridDimension-2 &&
                boardState[pos_start-1][y] == oppon_color &&
                boardState[pos_end+1][y] == oppon_color)
            return false;

        if (pos_start == 0 || pos_end == gridDimension - 2 ||
            boardState[pos_start-1][y] != color || boardState[pos_end+1][y] != color)
            return true;

        return false;
    }

    private boolean checkSuccessVertical(int x, int y, int color) {

        int i, pos_start, pos_end;
        int num_uniq = 5;
        int oppon_color = (color == 1) ? 2 : 1;

        //Check vertical
        i = 0;
        while ((y - i) >= 0 && boardState[x][y-i] == color)
            i++;

        if (i > num_uniq)
            return false;

        pos_start = y - i + 1;
        i = 0;
        while ((pos_start+i < gridDimension-1) &&
                boardState[x][pos_start + i] == color)
            i++;

        if (i != num_uniq)
            return false;

        pos_end = pos_start + i - 1;
        if (pos_start != 0 && pos_end != gridDimension-2 &&
                boardState[x][pos_start-1] == oppon_color &&
                boardState[x][pos_end+1] == oppon_color)
            return false;

        if (pos_start == 0 || pos_end == gridDimension - 2 ||
                boardState[x][pos_start-1] != color || boardState[x][pos_end+1] != color)
            return true;

        return false;
    }

    private boolean checkSuccessDiagonal1(int x, int y, int color) {

        int i;
        int pos_start_x, pos_end_x;
        int pos_start_y, pos_end_y;
        int num_uniq = 5;
        int oppon_color = (color == 1) ? 2 : 1;

        //Check vertical
        i = 0;
        while ((x - i) >= 0 && (y - i) >= 0 &&
                boardState[x-i][y-i] == color)
            i++;

        if (i > num_uniq)
            return false;

        pos_start_x = x - i + 1;
        pos_start_y = y - i + 1;
        i = 0;
        while ((pos_start_x + i < gridDimension - 1) &&
            (pos_start_y + i < gridDimension - 1) &&
                boardState[pos_start_x + i][pos_start_y + i] == color)
            i++;

        if (i != num_uniq)
            return false;

        pos_end_x = pos_start_x + i - 1;
        pos_end_y = pos_start_y + i - 1;

        if (pos_start_x != 0 && pos_start_y != 0 &&
                pos_end_x != gridDimension-2 && pos_end_y != gridDimension-2 &&
                boardState[pos_start_x-1][pos_start_y-1] == oppon_color &&
                boardState[pos_end_x+1][pos_end_y+1] == oppon_color)
            return false;

        if (pos_start_x == 0 || pos_end_x == gridDimension - 2 ||
                pos_start_y == 0 || pos_end_y == gridDimension - 2 ||
                boardState[pos_start_x-1][pos_start_y-1] != color ||
                boardState[pos_end_x+1][pos_end_y+1] != color)
            return true;

        return false;
    }

    private boolean checkSuccessDiagonal2(int x, int y, int color) {

        int i;
        int pos_start_x, pos_end_x;
        int pos_start_y, pos_end_y;
        int num_uniq = 5;
        int oppon_color = (color == 1) ? 2 : 1;

        i = 0;
        while ((x - i) >= 0 && (y + i) < gridDimension - 1 &&
                boardState[x-i][y+i] == color)
            i++;

        if (i > num_uniq)
            return false;

        pos_start_x = x - i + 1;
        pos_start_y = y + i - 1;
        i = 0;
        while ((pos_start_x + i < gridDimension - 1) &&
                (pos_start_y - i >= 0) &&
                boardState[pos_start_x + i][pos_start_y - i] == color)
            i++;

        if (i != num_uniq)
            return false;

        pos_end_x = pos_start_x + i - 1;
        pos_end_y = pos_start_y - i + 1;

        if (pos_start_x != 0 && pos_start_y != gridDimension-2 &&
                pos_end_x != gridDimension-2 && pos_end_y != 0 &&
                boardState[pos_start_x-1][pos_start_y+1] == oppon_color &&
                boardState[pos_end_x+1][pos_end_y-1] == oppon_color)
            return false;

        if (pos_start_x == 0 || pos_end_x == gridDimension - 2 ||
                pos_start_y == gridDimension-2 || pos_end_y == 0 ||
                boardState[pos_start_x-1][pos_start_y+1] != color ||
                boardState[pos_end_x+1][pos_end_y-1] != color)
            return true;

        return false;
    }
    public void setTurn(boolean b)
    {
        isTurn = b;
    }
    public boolean getTurn()
    {
        return isTurn;
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (online && isTurn || !online) {
            int ratioX = getWidth() / (gridDimension);
            int ratioY = getHeight() / (gridDimension);
            int x = Math.round(event.getX() / ratioX);
            int y = Math.round(event.getY() / ratioY);
            switch (event.getAction()) {
                //If the user clicked somewhere...
                case MotionEvent.ACTION_DOWN:
                    if (chronometer.isActivated()) {
                        chronometer.stop();
                    }
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    //Check that we are not on the very edges of the game board
                    if (x >= 1 && y >= 1 && x <= gridDimension - 1 && y <= gridDimension - 1) {
                        //Check whose turn it is and then make sure they aren't trying to put a piece somewhere that a piece already exists
                        if (boardState[x - 1][y - 1] == 0) {
                            num_empty_spaces = num_empty_spaces - 1;

                            if (isBlack) {
                                boardState[x - 1][y - 1] = 1;
                                if (parent != null && AI == false && !online)
                                    playerName.setText("Player 2");
                                if (AI == true) {
                                    playerName.setText("Computer is thinking...");
                                    if (isBlack) {
                                        if (checkSuccessHorizontal(x - 1, y - 1, 1) ||
                                                checkSuccessVertical(x - 1, y - 1, 1) ||
                                                checkSuccessDiagonal1(x - 1, y - 1, 1) ||
                                                checkSuccessDiagonal2(x - 1, y - 1, 1)) {
                                            scoreValue1++;
                                            score1.setText("" + scoreValue1);
                                            winSound = MediaPlayer.create(this.getContext(), R.raw.start_tone);
                                            winSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                public void onCompletion(MediaPlayer mp) {
                                                    mp.release();

                                                };
                                            });
                                            winSound.start();
                                            showSimplePopUpBlackWins();
                                        }else {
                                            piecePlaced = MediaPlayer.create(getContext(), R.raw.piece_placed);
                                            piecePlaced.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                public void onCompletion(MediaPlayer mp) {
                                                    mp.release();

                                                };
                                            });
                                            piecePlaced.start();
                                        }
                                    }
                                }
                                if(!online)
                                    isBlack = false;
                            } else if (!isBlack && boardState[x - 1][y - 1] == 0 && AI == false) {
                                boardState[x - 1][y - 1] = 2;
                                if(!online)
                                    isBlack = true;
                                if (parent != null && !online)
                                    playerName.setText("Player 1");
                            }

                            if (AI == true) {
                                position move = findMove(x - 1, y - 1);
                                boardState[move.getX()][move.getY()] = 2;

                                if (!isBlack) {
                                    if (checkSuccessHorizontal(move.getX(), move.getY(), 2) ||
                                            checkSuccessVertical(move.getX(), move.getY(), 2) ||
                                            checkSuccessDiagonal1(move.getX(), move.getY(), 2) ||
                                            checkSuccessDiagonal2(move.getX(), move.getY(), 2)) {
                                        scoreValue2++;
                                        winSound = MediaPlayer.create(this.getContext(), R.raw.start_tone);
                                        winSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            public void onCompletion(MediaPlayer mp) {
                                                mp.release();

                                            };
                                        });
                                        winSound.start();
                                        score2.setText("" + scoreValue2);
                                        showSimplePopUpWhiteWins();
                                    } else {
                                        piecePlaced = MediaPlayer.create(getContext(), R.raw.piece_placed);
                                        piecePlaced.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            public void onCompletion(MediaPlayer mp) {
                                                mp.release();

                                            };
                                        });
                                        piecePlaced.start();
                                    }
                                }

                                isBlack = true;

                                if (parent != null && !online)
                                    playerName.setText("Player 1");

                            } else {
                                // check adjacent stones in the vertical,horizontal and diagonal direction and pop up message if success
                                if (isBlack) {
                                    if (checkSuccessHorizontal(x - 1, y - 1, 2) ||
                                            checkSuccessVertical(x - 1, y - 1, 2) ||
                                            checkSuccessDiagonal1(x - 1, y - 1, 2) ||
                                            checkSuccessDiagonal2(x - 1, y - 1, 2)) {
                                        scoreValue2++;
                                        winSound = MediaPlayer.create(this.getContext(), R.raw.start_tone);
                                        winSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            public void onCompletion(MediaPlayer mp) {
                                                mp.release();

                                            };
                                        });
                                        winSound.start();
                                        score2.setText("" + scoreValue2);
                                        showSimplePopUpWhiteWins();
                                    }
                                    else {
                                        piecePlaced = MediaPlayer.create(getContext(), R.raw.piece_placed);
                                        piecePlaced.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            public void onCompletion(MediaPlayer mp) {
                                                mp.release();

                                            };
                                        });
                                        piecePlaced.start();
                                    }
                                } else if (!isBlack) {
                                    if (checkSuccessHorizontal(x - 1, y - 1, 1) ||
                                            checkSuccessVertical(x - 1, y - 1, 1) ||
                                            checkSuccessDiagonal1(x - 1, y - 1, 1) ||
                                            checkSuccessDiagonal2(x - 1, y - 1, 1)) {
                                        scoreValue1++;
                                        winSound = MediaPlayer.create(this.getContext(), R.raw.start_tone);
                                        winSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            public void onCompletion(MediaPlayer mp) {
                                                mp.release();

                                            };
                                        });
                                        winSound.start();
                                        score1.setText("" + scoreValue1);
                                        showSimplePopUpBlackWins();
                                    } else {
                                        piecePlaced = MediaPlayer.create(getContext(), R.raw.piece_placed);
                                        piecePlaced.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            public void onCompletion(MediaPlayer mp) {
                                                mp.release();

                                            };
                                        });
                                        piecePlaced.start();
                                    }
                                }

                            }

                            if (!checkGameStatus(0, 0, gridDimension-2, gridDimension-2))
                                showSimplePopUpGameTie();

                            //check if board is full
                            if (num_empty_spaces == 0)
                                showSimplePopUpGameTie();


                            if(online)
                            {
                                ((MultiPlayer)parent).swapTurns();
                            }
                        }

                        //Redraw the game board screen to reflect new pieces.
                        this.invalidate();

                        //These are some toast messages I used for testing.
                        //String text = "X: " + x + "Y: " + y;

                        //Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
                        //Toast toast2 = Toast.makeText(getContext(), "Width: " + event.getX() + " Height: " + event.getY(), Toast.LENGTH_SHORT);
                        //toast.show();
                    }
            }
        }
        return false;
    }

    protected void onDraw(Canvas canvas) {
        // Parent draw
        super.onDraw(canvas);
        // Background color
            canvas.drawColor(Color.TRANSPARENT);

            // Draw grid lines
            for (int x = -this.getGridDimension(); x <= 0; x++) {
                canvas.drawLine(interpX(x), interpY(0), interpX(x), interpY(-this.getGridDimension()), gridPaint);
                for (int y = -this.getGridDimension(); y <= 0; y++) {
                    canvas.drawLine(interpX(-this.getGridDimension()), interpY(y), interpX(this.getGridDimension()), interpY(y), gridPaint);
                }
            }
        // Draw pieces
        for(int x = 0; x < gridDimension - 1 && x>=0; x++)
        {
            for(int y = 0; y < gridDimension - 1 &&y>=0; y++)
            {
                if(boardState[x][y] == 1)
                {
                    canvas.drawCircle(interpX(x-(gridDimension-1)), interpY(-y - 1), (this.getWidth() * circleSize), blackPiece);
                }
                else if(boardState[x][y] == 2)
                {
                    canvas.drawCircle(interpX(x-(gridDimension-1)), interpY(-y - 1), (this.getWidth() * circleSize), whitePiece);
                }
            }
        }
    }

    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int measuredHeight = MeasureSpec.getSize(width) - this.getPaddingLeft() - this.getPaddingRight();
        int measuredWidth = MeasureSpec.getSize(height);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public int getGridDimension() {
        return gridDimension;
    }

    public void setIsBlack(boolean b)
    {
        isBlack = b;
    }

    public boolean getIsBlack()
    {
        return isBlack;
    }

    public void setGridDimension(int gridDimension) {
        this.gridDimension = gridDimension;
    }

    public void Init() {
        if(parent != null)
        {
            score1 = (TextView)parent.findViewById(R.id.score1);
            score1.setText("" + scoreValue1);
            score2 = (TextView)parent.findViewById(R.id.score2);
            score2.setText("" + scoreValue2);
            playerName = (TextView)parent.findViewById(R.id.playerName);
            playerName.setText("Player 1");
        }
        // Set initial grid dimension
        boardState = new int[gridDimension][gridDimension];
        num_empty_spaces=(gridDimension*gridDimension);

        if(gridDimension < 15)
        {
            circleSize = (float)0.04;
        }
        else if(gridDimension < 20)
        {
            circleSize = (float)0.03;
        }
        else if(gridDimension < 25)
        {
            circleSize = (float)0.02;
        }
        initGameBoard(this.gridDimension);

        //Add 1 to grid dimension so that when we draw, we have the right number of intersections
        setGridDimension(getGridDimension() + 1);


        // Grid line paint
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(3);
        gridPaint.setColor(Color.BLACK);

        //black piece paint
        blackPiece = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPiece.setStyle(Paint.Style.FILL);
        blackPiece.setColor(Color.BLACK);

        //White piece paint
        whitePiece = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePiece.setStyle(Paint.Style.FILL);
        whitePiece.setColor(Color.WHITE);

    }

    //Convert screen clicks to the right spot on the game board
    private float interpX(double x) {
        double width = (double) this.getWidth();
        return (float) ((x + this.getGridDimension())
                / (this.getGridDimension()) * width);
    }

    public void setOnline(boolean b)
    {
        online=b;
    }
    public boolean getOnline(boolean b)
    {
        return online;
    }
    private float interpY(double y) {
        double height = (double) this.getHeight();
        return (float) ((y + this.getGridDimension())
                / (this.getGridDimension()) * -height + height);
    }

    //Set up our initial empty game board
    public void initGameBoard(int gridSize){
        for(int x = 0; x < gridSize; x++)
        {
            for(int y = 0; y < gridSize; y++)
            {
                boardState[x][y] = 0;
            }
        }
    }
    public void setParent(Board parent, Chronometer chronometer) {
        this.parent = parent;
        if(!(parent instanceof MultiPlayer))
        {
            newGame = MediaPlayer.create(this.getContext(), R.raw.new_game);
            newGame.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();

                };
            });
            newGame.start();

        }
        this.chronometer = chronometer;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void playPiecePlaced()
    {
        piecePlaced = MediaPlayer.create(this.getContext(), R.raw.piece_placed);
        piecePlaced.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();

            };
        });
        piecePlaced.start();
    }
    public void resetChronometer()
    {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }
    public void setScoreValue1(int x) { scoreValue1 = x;}

    public void setScore1(String s){score1.setText(s);}

    public void setScore2(String s){score2.setText(s);}

    public void setScoreValue2(int x) { scoreValue2 = x;}

    public void setAI(boolean b) {AI = b;}

    public void setPlayerName(String s)
    {
        playerName.setText(s);
    }
    public position findMove(int x, int y)
    {
        position move;
        //If we can win, do so.
        for(int currentX = 0; currentX < gridDimension - 1; currentX++)
        {
            for(int currentY = 0; currentY < gridDimension - 1; currentY++)
            {
                move = findThreat(currentX, currentY, 4, 2);

                if (checkValidity(move))
                    return move;
            }
        }
        //If we cannot win, find a block to a four in a row;
        move = findThreat(x, y, 4, 1);
        if(checkValidity(move))
            return move;

        //If no four in a row, block a three in a row;
        move = findThreat(x, y, 3, 1);
        if(checkValidity(move))
            return move;

        //If no three in a row, see if we have any three in a rows;
        for(int currentX = 0; currentX < gridDimension - 1; currentX++)
        {
            for(int currentY = 0; currentY < gridDimension - 1; currentY++)
            {
                move = findThreat(currentX, currentY, 3, 2);
                if(checkValidity(move))
                    return move;
            }
        }
        //If we have no three in a rows, find a two in a row to build on.
        for(int currentX = 0; currentX < gridDimension - 1; currentX++)
        {
            for(int currentY = 0; currentY < gridDimension - 1; currentY++)
            {
                move = findThreat(currentX, currentY, 3, 2);
                if(checkValidity(move))
                    return move;
            }
        }

        //If no two in a rows, block enemy two in a row.
        move = findThreat(x, y, 2, 1);
        if(checkValidity(move))
            return move;

        //Otherwise place a piece next to the piece the opponent just placed.
        while(true)
        {
            move = getRandomPosition(x, y);
            if(checkValidity(move))
                return move;

        }
    }

    public position getRandomPosition(int x, int y)
    {
        int tempX;
        int tempY;
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int direction = rand.nextInt(8) + 1;
        //SE
        if(direction == 1)
        {
            tempX = x + 1;
            tempY = y - 1;
            return new position(tempX, tempY);
        }
        //Right
        if(direction == 2)
        {
            tempX = x + 1;
            tempY = y;
            return new position(tempX, tempY);
        }
        //Up
        if(direction == 3)
        {
            tempX = x;
            tempY = y + 1;
            return new position(tempX, tempY);
        }
        //Left
        if(direction == 4)
        {
            tempX = x - 1;
            tempY = y;
            return new position(tempX, tempY);
        }
        //Down
        if(direction == 5)
        {
            tempX = x;
            tempY = y - 1;
            return new position(tempX, tempY);
        }
        //NE
        if(direction == 6)
        {
            tempX = x + 1;
            tempY = y + 1;
            return new position(tempX, tempY);
        }
        //SW
        if(direction == 7)
        {
            tempX = x - 1;
            tempY = y - 1;
            return new position(tempX, tempY);
        }
        //NW
        if(direction == 7)
        {
            tempX = x - 1;
            tempY = y + 1;
            return new position(tempX, tempY);
        }
        return new position(-1, -1);
    }
    public position findThreat(int x, int y, int threatSize, int color)
    {
        int i = 0;
        position chosenMove;
        //Check left horizontal
        while ((x - i) >= 0 && boardState[x - i][y] == color) {
            i++;
            if (i == threatSize)
            {
                chosenMove = new position(x-i, y);
                if(checkValidity(chosenMove))
                    return chosenMove;
                else
                {
                    chosenMove = new position(x + 1, y);
                    if(checkValidity(chosenMove))
                        return chosenMove;
                }
            }
        }

        //Check right horizontal
        i = 0;
        while ((x + i < gridDimension-1) && boardState[x + i][y] == color) {
            i++;
            if (i == threatSize)
            {
                chosenMove = new position(x + i, y);
                if (checkValidity(chosenMove))
                    return chosenMove;
                else {
                    chosenMove = new position(x - 1, y);
                    if (checkValidity(chosenMove))
                        return chosenMove;
                }
            }
        }

        //Check down vertical
        i = 0;
        while ((y - i) >= 0 && boardState[x][y-i] == color)
        {
            i++;
            if (i == threatSize)
            {
                chosenMove = new position(x, y - i);
                if (checkValidity(chosenMove))
                    return chosenMove;
                else {
                    chosenMove = new position(x, y + 1);
                    if (checkValidity(chosenMove))
                        return chosenMove;
                }
            }
        }

        //Check up vertical
        i = 0;
        while ((y+i < gridDimension-1) && boardState[x][y + i] == color)
        {
            i++;
            if (i == threatSize)
            {
                chosenMove = new position(x, y + i);
                if (checkValidity(chosenMove))
                    return chosenMove;
                else {
                    chosenMove = new position(x, y - 1);
                    if (checkValidity(chosenMove))
                        return chosenMove;
                }
            }
        }

        //Check SW diagonal
        i = 0;
        while ((x - i) >= 0 && (y - i) >= 0 && boardState[x-i][y-i] == color)
        {
            i++;
            if (i == threatSize)
            {
                chosenMove = new position(x - i, y - i);
                if (checkValidity(chosenMove))
                    return chosenMove;
                else {
                    chosenMove = new position(x + 1, y + 1);
                    if (checkValidity(chosenMove))
                        return chosenMove;
                }
            }
        }

        //Check NE diagonal
        while ((x + i) < gridDimension - 1 && (y + i) < gridDimension - 1 && boardState[x+i][y+i] == color)
        {
            i++;
            if (i == threatSize)
            {
                chosenMove = new position(x + i, y + i);
                if (checkValidity(chosenMove))
                    return chosenMove;
                else {
                    chosenMove = new position(x - 1, y - 1);
                    if (checkValidity(chosenMove))
                        return chosenMove;
                }
            }
        }

        //Check NW diagonal
        while ((x - i) >= 0 && (y + i) < gridDimension - 1 && boardState[x-i][y+i] == color) {
            i++;

            if (i == threatSize)
            {
                chosenMove = new position(x - i, y + i);
                if (checkValidity(chosenMove))
                    return chosenMove;
                else {
                    chosenMove = new position(x + 1, y - 1);
                    if (checkValidity(chosenMove))
                        return chosenMove;
                }
            }
        }

        //Check SE diagonal
        while ((x + i) < gridDimension -1 && (y - i) >= 0 && boardState[x+i][y-i] == color) {
            i++;

            if (i == threatSize)
            {
                chosenMove = new position(x + i, y - i);
                if (checkValidity(chosenMove))
                    return chosenMove;
                else {
                    chosenMove = new position(x - 1, y + 1);
                    if (checkValidity(chosenMove))
                        return chosenMove;
                }
            }
        }
        return null;
    }

    public boolean checkValidity(position positionToCheck)
    {
        if(positionToCheck != null)
        {
            if (positionToCheck.x >= 0 && positionToCheck.x < gridDimension - 1 && positionToCheck.y >= 0 && positionToCheck.y < gridDimension - 1) {
                if (boardState[positionToCheck.x][positionToCheck.y] == 0)
                    return true;
            }
        }
        return false;
    }

    public boolean getAI()
    {
        return AI;
    }

    public int getScoreValue1(){
        return scoreValue1;
    }

    public int getScoreValue2(){
        return scoreValue2;
    }

    //Hold a board position
    class position
    {
        int x;
        int y;
        public position(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        public int getX()
        {
            return this.x;
        }
        public int getY()
        {
            return this.y;
        }
    }
}
//Found some information on the math used here and the general method of grid drawing using custom views at http://www.csit.parkland.edu/~dbock/Class/csc212/Lecture/AndroidDrawing2D.html