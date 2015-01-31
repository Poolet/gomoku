package group5.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Tyler on 1/29/2015.
 */
public class BoardView extends View implements View.OnClickListener{
    private int gridDimension;
    private int[][] boardState;
    private Paint gridPaint;
    private Paint circlePaint;

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

    public void onClick(View v)
    {

    }
    public boolean onTouchEvent(MotionEvent event) {
        int ratioX = getWidth()/(gridDimension);
        int ratioY = getHeight()/(gridDimension);
        int x = Math.round(event.getX()/ratioX);
        int y = Math.round(event.getY()/ratioY);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                boardState[x-1][y-1] = 1;
                this.invalidate();
                String text = "X: " + x + "Y: " + y;

                Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
                Toast toast2 = Toast.makeText(getContext(), "Width: " + event.getX() + " Height: " + event.getY(), Toast.LENGTH_SHORT);
                toast.show();
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
                    canvas.drawCircle(interpX(x-(gridDimension-1)), interpY(-y - 1), (this.getWidth() * (float)0.03), circlePaint);
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

        setGridDimension(getGridDimension() + 1);
        // Grid line paint
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(3);
        gridPaint.setColor(Color.BLACK);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.BLACK);
    }

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