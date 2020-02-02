package com.example.jsketch.Shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

public class Line implements IShape{
    public String name;
    public int color;
    public float originX;
    public float originY;
    public float endX;
    public float endY;
    public boolean selected;

    // According to vogella.com/tutorials/AndroidParcelable/article.html
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        public Line[] newArray(int size) {
            return new Line[size];
        }
    };

    public Line(Parcel in) {
        this.name = in.readString();
        this.color = in.readInt();
        this.originX = in.readFloat();
        this.originY = in.readFloat();
        this.endX = in.readFloat();
        this.endY = in.readFloat();
        this.selected = in.readBoolean();
    }

    public Line(int color, float x, float y) {
        this.name = "line";
        this.color = color;
        this.originX = x;
        this.originY = y;
        this.endX = x;
        this.endY = y;
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
        dest.writeFloat(endX);
        dest.writeFloat(endY);
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
        endX = x;
        endY = y;
    }

    @Override
    public void draw(Canvas canvas, Paint brush, int selectedColor) {
        if (selected) {
            color = selectedColor;
            brush.setStrokeWidth(35);
            brush.setColor(Color.BLACK);
            canvas.drawLine(originX, originY, endX, endY,brush);
            brush.setColor(color);
            brush.setStrokeWidth(15);
            canvas.drawLine(originX, originY, endX, endY, brush);
        }
        else {
            brush.setColor(color);
            brush.setStrokeWidth(20);
            canvas.drawLine(originX, originY, endX, endY, brush);
        }
    }

    @Override
    public void move(float curX, float curY, float lastX, float lastY) {
        float diffX = curX-lastX;
        float diffY = curY-lastY;
        originX = originX + diffX;
        originY = originY + diffY;
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
        float tmpStartX = Math.min(originX, endX);
        float tmpStartY = Math.min(originY, endY);
        float tmpEndX = Math.max(originX, endX);
        float tmpEndY = Math.max(originY, endY);

        Rect area = new Rect((int) tmpStartX, (int) tmpStartY, (int) tmpEndX, (int) tmpEndY);
        if (area.contains((int) x, (int)y)) {
            return true;
        }
        if (Math.abs(tmpEndX-tmpStartX) <= 3) {
            if (y >= tmpStartY && y <= tmpEndY && (Math.abs(tmpEndX-x) <= 10 || Math.abs(tmpStartX-x) <= 10)) {
                return true;
            }
        }
        if (Math.abs(tmpEndY-tmpStartY) <= 3) {
            if (x >= tmpStartX && x <= tmpEndX && (Math.abs(tmpEndY-y) <= 10 || Math.abs(tmpStartY-y) <= 10)) {
                return true;
            }
        }

        return false;
    }
}
