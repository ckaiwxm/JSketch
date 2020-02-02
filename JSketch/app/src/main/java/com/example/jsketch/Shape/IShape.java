package com.example.jsketch.Shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcelable;

public interface IShape extends Parcelable {
    String getName();
    int getColor();
    void setDrawInfo(float x, float y);
    void draw(Canvas canvas, Paint brush, int selectedColor);
    void move(float curX, float curY, float lastX, float lastY);
    void select();
    void unSelect();
    boolean ifHit(float x, float y);
}
