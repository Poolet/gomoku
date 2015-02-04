package group5.gomoku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Tyler on 1/29/2015.
 */
public class BoardView extends View{

    private Board parent;
    //playerName will hold the text view that contains the current player.
    TextView playerName;
    //isBlack will track whose turn it is
    private boolean isBlack = true;
    //gridDimension will hold information about the size of the board
    private int gridDimension;
    //circleSize holds information about the size of the pieces.
    private float circleSize;
    //Store 0 for empty square, 1 for black, 2 for white
    private int[][] boardState;

    //Create some paints we will draw with, one for each player piece, and one for the grid lines
    private Paint gridPaint;
    private Paint blackPiece;
    private Paint whitePiece;

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

    //Show pop up message when player 1 wins
    private void showSimplePopUpBlackWins() {


        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this.getContext());
        helpBuilder.setCancelable(false);
        helpBuilder.setTitle("     Game Over");
        helpBuilder.setMessage("      Player 1 Wins");
        helpBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent();
                i.setClass(getContext(),Board.class);
                getContext().startActivity(i);
            }

        });
        helpBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i=new Intent();
                i.setClass(getContext(),Scores.class);
                getContext().startActivity(i);
            }
        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }


    //Show pop up message when player 2 wins
    private void showSimplePopUpWhiteWins() {


        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this.getContext());
        helpBuilder.setCancelable(false);
        helpBuilder.setTitle("     Game Over");
        helpBuilder.setMessage("      Player 2 Wins");
        helpBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent();
                i.setClass(getContext(),Board.class);
                getContext().startActivity(i);
            }

        });
        helpBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent();
                i.setClass(getContext(),Scores.class);
                getContext().startActivity(i);
            }


        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

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


    public boolean onTouchEvent(MotionEvent event) {
        int ratioX = getWidth()/(gridDimension);
        int ratioY = getHeight()/(gridDimension);
        int x = Math.round(event.getX()/ratioX);
        int y = Math.round(event.getY()/ratioY);
        switch (event.getAction()) {
            //If the user clicked somewhere...
            case MotionEvent.ACTION_DOWN:
            //Check that we are not on the very edges of the game board
            if(x >= 1 && y >= 1 && x<=gridDimension - 1 && y <= gridDimension - 1)
            {
                //Check whose turn it is and then make sure they aren't trying to put a piece somewhere that a piece already exists
                if (isBlack && boardState[x - 1][y - 1] == 0) {
                    boardState[x - 1][y - 1] = 1;
                    isBlack = false;
                    if(parent != null)
                        playerName.setText("Player 2");

                } else if (!isBlack && boardState[x - 1][y - 1] == 0) {
                    boardState[x - 1][y - 1] = 2;
                    isBlack = true;
                    if(parent != null)
                        playerName.setText("Player 1");
                }
                // check adjacent stones in the vertical,horizontal and diagonal direction and pop up message if success
                if (isBlack) {
                    if (checkSuccessHorizontal(x - 1, y - 1, 2) ||
                            checkSuccessVertical(x - 1, y - 1, 2) ||
                            checkSuccessDiagonal1(x - 1, y - 1, 2) ||
                            checkSuccessDiagonal2(x - 1, y - 1, 2))
                        showSimplePopUpWhiteWins();
                } else if (!isBlack) {
                    if (checkSuccessHorizontal(x - 1, y - 1, 1) ||
                            checkSuccessVertical(x - 1, y - 1, 1) ||
                            checkSuccessDiagonal1(x - 1, y - 1, 1) ||
                            checkSuccessDiagonal2(x - 1, y - 1, 1))
                        showSimplePopUpBlackWins();
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

    public void setGridDimension(int gridDimension) {
        this.gridDimension = gridDimension;
    }

    public void Init() {
        if(parent != null)
        {
            playerName = (TextView)parent.findViewById(R.id.playerName);
            playerName.setText("Player 1");
        }
        // Set initial grid dimension
        boardState = new int[gridDimension][gridDimension];
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
        initGameBoard();

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

    private float interpY(double y) {
        double height = (double) this.getHeight();
        return (float) ((y + this.getGridDimension())
                / (this.getGridDimension()) * -height + height);
    }

    //Set up our initial empty game board
    private void initGameBoard(){
        for(int x = 0; x < gridDimension; x++)
        {
            for(int y = 0; y < gridDimension; y++)
            {
                boardState[x][y] = 0;
            }
        }
    }
    //Used to set parent of this view
    public void setParent(Board parent) {
        this.parent = parent;
    }
}

//Found some information on the math used here and the general method of grid drawing using custom views at http://www.csit.parkland.edu/~dbock/Class/csc212/Lecture/AndroidDrawing2D.html