package com.example.jsketch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jsketch.Shape.Circle;
import com.example.jsketch.Shape.IShape;
import com.example.jsketch.Shape.Line;
import com.example.jsketch.Shape.Rect;

public class SketchPadView extends View {
    public Paint brush;
    public Model model;
    public MainActivity mainActivity;
    public int blue;
    public int yellow;
    public int red;

    public SketchPadView(Context context)
    {
        super(context);
        DefaultInit();
        invalidate();
    }

    public SketchPadView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        DefaultInit();
        invalidate();
    }

    public SketchPadView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        DefaultInit();
        invalidate();
    }

    private void DefaultInit() {
        brush = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue = Color.parseColor("#00ACC1");
        yellow = Color.parseColor("#FDD835");
        red = Color.parseColor("#E53935");
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);

        float x = e.getX();
        float y = e.getY();
        String tool = model.selectedTool;
        int color = model.selectedColor;
        int gesture = e.getActionMasked();

        if (gesture == MotionEvent.ACTION_DOWN) {
            if (tool.equals("circle") && model.selectedColor != -1) {
                if (color != -1) {
                    Circle c = new Circle(color, x, y);
                    model.drawingShape = c;
                }
            }
            else if (tool.equals("rect") && model.selectedColor != -1) {
                Rect r = new Rect(color, x, y);
                model.drawingShape = r;
            }
            else if (tool.equals("line") && model.selectedColor != -1) {
                Line l = new Line(color, x, y);
                model.drawingShape = l;
            }
            else if (tool.equals("cursor")) {
                if (model.selectedShape != null) {
                    mainActivity.eraserBtn.setImageResource(R.drawable.eraser);
                    model.selectedShape.unSelect();
                    model.selectedShape = null;
                    model.lastX = -1;
                    model.lastY = -1;
                }
                grandHitTest(x, y);
                if (model.selectedShape != null) {
                    model.lastX = x;
                    model.lastY = y;
                    mainActivity.eraserBtn.setImageResource(R.drawable.eraser_clicked);
                    if (model.selectedShape.getColor() == blue) {
                        mainActivity.blueBtn.callOnClick();
                    }
                    else if (model.selectedShape.getColor() == yellow) {
                        mainActivity.yellowBtn.callOnClick();
                    }
                    if (model.selectedShape.getColor() == red) {
                        mainActivity.redBtn.callOnClick();
                    }
                }
            }
        }
        else if (gesture == MotionEvent.ACTION_MOVE) {
            if (tool.equals("circle") || tool.equals("rect") || tool.equals("line")) {
                if (model.drawingShape != null && model.selectedColor != -1) {
                    IShape s = model.drawingShape;
                    s.setDrawInfo(x, y);
                }
            }
            else if (tool.equals("cursor")) {
            if (model.selectedShape != null && model.lastX != -1 && model.lastY != -1) {
                    IShape s = model.selectedShape;
                    s.move(x, y, model.lastX, model.lastY);
                    model.lastX = x;
                    model.lastY = y;
                }
            }
        }
        else if (gesture == MotionEvent.ACTION_UP) {
            if (tool.equals("circle") || tool.equals("rect") || tool.equals("line")) {
                if (model.drawingShape != null && model.selectedColor != -1) {
                    IShape s = model.drawingShape;
                    model.shapeList.add(s);
                    model.drawingShape = null;
                }
            }
            else if (tool.equals("cursor")) {
                if (model.selectedShape != null && model.lastX != -1 && model.lastY != -1) {
                    model.lastX = -1;
                    model.lastY = -1;
                }
            }
        }

        invalidate();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        brush.setColor(Color.WHITE);
        brush.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0,canvas.getMaximumBitmapWidth(), canvas.getMaximumBitmapHeight(), brush);

        for (IShape shape:model.shapeList) {
            shape.draw(canvas, brush, model.selectedColor);
        }

        IShape ds = model.drawingShape;
        if (ds != null) {
            ds.draw(canvas, brush, model.selectedColor);
        }
    }

    private void grandHitTest(float x, float y) {
        for(int i = model.shapeList.size()-1; i >= 0; i--) {
            IShape s = model.shapeList.get(i);
            if (s.ifHit(x, y)) {
                model.selectedShape = s;
                s.select();
                break;
            }
        }
    }

}
