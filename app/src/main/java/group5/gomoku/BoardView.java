package group5.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by Tyler on 1/29/2015.
 */
public class BoardView extends View{
    //isBlack will track whose turn it is
    private boolean isBlack = true;
    //gridDimension will hold information about the size of the board
    private int gridDimension;
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

    //Show pop up message when player wins
    private void showSimplePopUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Message")
                .setMessage("Success!")
                .setNeutralButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
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
            //Check that we are not on the very edges of the gameboard
            if(x >= 1 && y >= 1 && x<=gridDimension - 1 && y <= gridDimension - 1)
            {
                //Check whose turn it is and then make sure they aren't trying to put a piece somewhere that a piece already exists
                if (isBlack && boardState[x - 1][y - 1] == 0) {
                    boardState[x - 1][y - 1] = 1;
                    //showSimplePopUp();
                    isBlack = false;

                } else if (!isBlack && boardState[x - 1][y - 1] == 0) {
                    boardState[x - 1][y - 1] = 2;
                    isBlack = true;
                }

                if (checkSuccessHorizontal(x-1, y-1, isBlack ? 2: 1) ||
                        checkSuccessVertical(x-1, y-1, isBlack ? 2: 1) ||
                        checkSuccessDiagonal1(x-1, y-1, isBlack ? 2: 1) ||
                    checkSuccessDiagonal2(x-1, y-1, isBlack ? 2: 1))
                    showSimplePopUp();

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
        for(int x = 0; x < gridDimension - 1; x++)
        {
            for(int y = 0; y < gridDimension - 1; y++)
            {
                if(boardState[x][y] == 1)
                {
                    canvas.drawCircle(interpX(x-(gridDimension-1)), interpY(-y - 1), (this.getWidth() * (float)0.04), blackPiece);
                }
                else if(boardState[x][y] == 2)
                {
                    canvas.drawCircle(interpX(x-(gridDimension-1)), interpY(-y - 1), (this.getWidth() * (float)0.04), whitePiece);
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
        // Set initial grid dimension
        setGridDimension(10);
        boardState = new int[gridDimension][gridDimension];
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
}

//Found some information on the math used here and the general method of grid drawing using custom views at http://www.csit.parkland.edu/~dbock/Class/csc212/Lecture/AndroidDrawing2D.html