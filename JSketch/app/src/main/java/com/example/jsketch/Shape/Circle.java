package com.example.jsketch.Shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

public class Circle implements IShape {
    public String name;
    public int color;
    public float originX;
    public float originY;
    public float radius;
    public boolean selected;

    // According to vogella.com/tutorials/AndroidParcelable/article.html
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Circle createFromParcel(Parcel in) {
            return new Circle(in);
        }

        public Circle[] newArray(int size) {
            return new Circle[size];
        }
    };

    public Circle(Parcel in) {
        this.name = in.readString();
        this.color = in.readInt();
        this.originX = in.readFloat();
        this.originY = in.readFloat();
        this.radius = in.readFloat();
        this.selected = in.readBoolean();
    }

    public Circle(int color, float x, float y) {
        this.name = "circle";
        this.color = color;
        this.originX = x;
        this.originY = y;
        this.radius = 0;
        this.selected = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(color);
        dest.writeFloat(originX);
        dest.writeFloat(originY);
        dest.writeFloat(radius);
        dest.writeBoolean(selected);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setDrawInfo(float x, float y) {
        float diffX = Math.abs(x-originX);
        float diffY = Math.abs(y-originY);
        double powDiffX = Math.pow((double)diffX, 2);
        double powDiffY = Math.pow((double)diffY, 2);
        radius = (float) (Math.sqrt(powDiffX + powDiffY));
    }

    @Override
    public void draw(Canvas canvas, Paint brush, int selectedColor) {
        if (selected) {
            color = selectedColor;
            brush.setStrokeWidth(15);
            brush.setColor(Color.BLACK);
            brush.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(originX, originY, radius, brush);
            brush.setColor(color);
            brush.setStyle(Paint.Style.FILL);
            canvas.drawCircle(originX, originY, radius, brush);
        }
        else {
            brush.setColor(color);
            brush.setStyle(Paint.Style.FILL);
            canvas.drawCircle(originX, originY, radius, brush);
        }
    }

    @Override
    public void move(float curX, float curY, float lastX, float lastY) {
        float diffX = curX-lastX;
        float diffY = curY-lastY;
        originX = originX+diffX;
        originY = originY+diffY;
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public void unSelect() {
        selected = false;
    }

    @Override
    public boolean ifHit(float x, float y) {
        float diffX = Math.abs(x-originX);
        float diffY = Math.abs(y-originY);
        double powDiffX = Math.pow((double)diffX, 2);
        double powDiffY = Math.pow((double)diffY, 2);
        float distance = (float) (Math.sqrt(powDiffX + powDiffY));
        if (distance <= radius) {
            return true;
        }
        else {
            return false;
        }
    }
}
