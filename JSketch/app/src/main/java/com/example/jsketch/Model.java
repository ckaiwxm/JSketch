package com.example.jsketch;

import com.example.jsketch.Shape.IShape;

import java.util.ArrayList;

public class Model {
    public ArrayList<IShape> shapeList;

    public int selectedColor;
    public String selectedTool;
    public IShape selectedShape;
    public IShape drawingShape;

    public float lastX;
    public float lastY;

    public Model() {
        shapeList = new ArrayList<IShape>();

        selectedColor = -1;
        selectedTool = "";
        selectedShape = null;
        drawingShape = null;

        lastX = -1;
        lastY = -1;
    }
}
