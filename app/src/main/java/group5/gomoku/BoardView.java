package group5.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
                    isBlack = false;
                } else if (!isBlack && boardState[x - 1][y - 1] == 0) {
                    boardState[x - 1][y - 1] = 2;
                    isBlack = true;
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