package group5.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Tyler on 1/29/2015.
 */
public class BoardView extends View {
    private int gridDimension;

    private Paint gridPaint;
    private Paint axisPaint;

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

    protected void onDraw(Canvas canvas) {
        // Parent draw
        super.onDraw(canvas);

        // Background color
        canvas.drawColor(Color.TRANSPARENT);

        // Draw vertical lines
        for (int x = -this.getGridDimension(); x <= this.getGridDimension(); x = x+2)
            canvas.drawLine(interpX(x), interpY(this.getGridDimension()), interpX(x), interpY(-this.getGridDimension()), gridPaint);

        // Draw horizontal lines
        for (int y = -this.getGridDimension(); y <= this.getGridDimension(); y = y+2)
            canvas.drawLine(interpX(-this.getGridDimension()), interpY(y), interpX(this.getGridDimension()), interpY(y), gridPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int measureWidth(int widthMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec) -
                this.getPaddingLeft() - this.getPaddingRight();

        return specSize;
    }
    private int measureHeight(int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        return specSize;
    }
    public int getGridDimension() {
        return gridDimension;
    }

    public void setGridDimension(int gridDimension) {
        this.gridDimension = gridDimension;
    }

    public void Init() {
        // Set initial grid dimension
        setGridDimension(20);
        setGridDimension(getGridDimension() + 1);
        // Grid line paint
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(3);
        gridPaint.setColor(Color.BLACK);
    }

    private float interpX(double x) {
        double width = (double) this.getWidth();
        return (float) ((x + this.getGridDimension())
                / (this.getGridDimension() * 2) * width);
    }

    private float interpY(double y) {
        double height = (double) this.getHeight();
        return (float) ((y + this.getGridDimension())
                / (this.getGridDimension() * 2) * -height + height);
    }
}
