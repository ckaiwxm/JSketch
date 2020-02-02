package com.example.jsketch.Shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

public class Rect implements IShape {
    public String name;
    public int color;
    public float originX;
    public float originY;
    public float startX;
    public float startY;
    public float endX;
    public float endY;
    public float width;
    public float height;
    public boolean selected;

    // According to vogella.com/tutorials/AndroidParcelable/article.html
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Rect createFromParcel(Parcel in) {
            return new Rect(in);
        }

        public Rect[] newArray(int size) {
            return new Rect[size];
        }
    };

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
        dest.writeFloat(startX);
        dest.writeFloat(startY);
        dest.writeFloat(endX);
        dest.writeFloat(endY);
        dest.writeFloat(width);
        dest.writeFloat(height);
        dest.writeBoolean(selected);
    }

    public Rect(Parcel in) {
        this.name = in.readString();
        this.color = in.readInt();
        this.originX = in.readFloat();
        this.originY = in.readFloat();
        this.startX = in.readFloat();
        this.startY = in.readFloat();
        this.endX = in.readFloat();
        this.endY = in.readFloat();
        this.width = in.readFloat();
        this.height = in.readFloat();
        this.selected = in.readBoolean();
    }

    public Rect(int color, float x, float y) {
        this.name = "rect";
        this.color = color;
        this.originX = x;
        this.originY = y;
        this.startX = x;
        this.startY = x;
        this.endX = x;
        this.endY = y;
        this.width = Math.abs(endX-startX);
        this.height= Math.abs(endY-startY);
        this.selected = false;
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
        if (x < originX && y < originY) {
            width = Math.abs(x-originX);
            height= Math.abs(y-originY);
            startX = originX - width;
            startY = originY - height;
            endX = originX;
            endY = originY;
        }
        else if (x < originX && y >= originY) {
            width = Math.abs(x-originX);
            height= Math.abs(y-originY);
            startX = originX - width;
            startY = originY;
            endX = originX;
            endY = originY + height;
        }
        else if (x >= originX && y < originY) {
            width = Math.abs(x-originX);
            height= Math.abs(y-originY);
            startX = originX;
            startY = originY - height;
            endX = originX + width;
            endY = originY;
        }
        else {
            width = Math.abs(x-originX);
            height= Math.abs(y-originY);
            startX = originX;
            startY = originY;
            endX = x;
            endY = y;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint brush, int selectedColor) {
        if (selected) {
            color = selectedColor;
            brush.setStrokeWidth(15);
            brush.setColor(Color.BLACK);
            brush.setStyle(Paint.Style.STROKE);
            canvas.drawRect(startX, startY, endX, endY, brush);
            brush.setColor(color);
            brush.setStyle(Paint.Style.FILL);
            canvas.drawRect(startX, startY, endX, endY, brush);
        }
        else {
            brush.setColor(color);
            brush.setStyle(Paint.Style.FILL);
            canvas.drawRect(startX, startY, endX, endY, brush);
        }
    }

    @Override
    public void move(float curX, float curY, float lastX, float lastY) {
        float diffX = curX-lastX;
        float diffY = curY-lastY;
        startX = startX + diffX;
        startY = startY + diffY;
        endX = endX + diffX;
        endY = endY + diffY;
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
        if (x >= startX && x <= endX && y >= startY && y <= endY) {
            return true;
        }
        else {
            return false;
        }
    }
}
