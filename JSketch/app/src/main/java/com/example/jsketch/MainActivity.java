package com.example.jsketch;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Functional Buttons
    public ImageButton cursorBtn;
    public ImageButton eraserBtn;
    public ImageButton circleBtn;
    public ImageButton rectBtn;
    public ImageButton lineBtn;

    // Color Buttons
    public Button blueBtn;
    public Button yellowBtn;
    public Button redBtn;

    public ImageButton shareBtn;

    // Sketch Pad
    public SketchPadView sketchPadView;

    // Model
    public int blue;
    public int yellow;
    public int red;
    public int darkBlue;
    public int darkYellow;
    public int darkRed;
    public Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Buttons
        InitView();
        InitModel();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("shapeList", model.shapeList);
        outState.putInt("selectedColor", model.selectedColor);
        outState.putString("selectedTool", model.selectedTool);
        outState.putParcelable("selectedShape", model.selectedShape);
        outState.putParcelable("drawingShape", model.drawingShape);
        outState.putFloat("lastX", model.lastX);
        outState.putFloat("lastY", model.lastY);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        model.shapeList = savedInstanceState.getParcelableArrayList("shapeList");
        model.selectedColor = savedInstanceState.getInt("selectedColor");
        model.selectedTool = savedInstanceState.getString("selectedTool");
        model.selectedShape = savedInstanceState.getParcelable("selectedShape");
        model.drawingShape = savedInstanceState.getParcelable("drawingShape");
        model.lastX = savedInstanceState.getFloat("lastX");
        model.lastY = savedInstanceState.getFloat("lastY");
    }

    private void InitView() {
        cursorBtn = (ImageButton) findViewById(R.id.cursorBtn);
        eraserBtn = (ImageButton) findViewById(R.id.eraserBtn);
        circleBtn = (ImageButton) findViewById(R.id.circleBtn);
        rectBtn = (ImageButton) findViewById(R.id.rectBtn);
        lineBtn = (ImageButton) findViewById(R.id.lineBtn);
        blueBtn = (Button) findViewById(R.id.blueBtn);
        yellowBtn = (Button) findViewById(R.id.yellowBtn);
        redBtn = (Button) findViewById(R.id.redBtn);
        shareBtn = (ImageButton) findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBtnOnClick(shareBtn);
            }
        });
        sketchPadView = (SketchPadView) findViewById(R.id.sketchPadView);
        sketchPadView.mainActivity = this;
    }

    private void InitModel() {
        blue = Color.parseColor("#00ACC1");
        yellow = Color.parseColor("#FDD835");
        red = Color.parseColor("#E53935");
        darkBlue = Color.parseColor("#137785");
        darkYellow = Color.parseColor("#CFA229");
        darkRed = Color.parseColor("#A83938");
        model = new Model();
        sketchPadView.model = this.model;
    }

    public void cursorBtnOnClick(View v) {
        cursorBtn.setImageResource(R.drawable.cursor_clicked);
        eraserBtn.setImageResource(R.drawable.eraser);
        circleBtn.setImageResource(R.drawable.circle);
        rectBtn.setImageResource(R.drawable.rect);
        lineBtn.setImageResource(R.drawable.line);

        model.selectedTool = "cursor";
    }

    public void eraserBtnOnClick(View v) {
        if (model.selectedTool == "cursor" && model.selectedShape != null) {
            eraserBtn.setImageResource(R.drawable.eraser);

            blueBtn.setBackgroundColor(blue);
            yellowBtn.setBackgroundColor(yellow);
            redBtn.setBackgroundColor(red);
            model.selectedColor = -1;

            model.shapeList.remove(model.selectedShape);
            model.selectedShape = null;
            model.selectedTool = "eraser";
            sketchPadView.invalidate();
            cursorBtn.callOnClick();
        }
    }

    public void circleBtnOnClick(View v) {
        if (model.selectedTool == "cursor" && model.selectedShape != null) {
            model.selectedShape.unSelect();
            model.selectedShape = null;
            sketchPadView.invalidate();
        }
        cursorBtn.setImageResource(R.drawable.cursor);
        eraserBtn.setImageResource(R.drawable.eraser);
        circleBtn.setImageResource(R.drawable.circle_clicked);
        rectBtn.setImageResource(R.drawable.rect);
        lineBtn.setImageResource(R.drawable.line);

        model.selectedTool = "circle";
    }

    public void rectBtnOnClick(View v) {
        if (model.selectedTool == "cursor" && model.selectedShape != null) {
            model.selectedShape.unSelect();
            model.selectedShape = null;
            sketchPadView.invalidate();
        }
        cursorBtn.setImageResource(R.drawable.cursor);
        eraserBtn.setImageResource(R.drawable.eraser);
        circleBtn.setImageResource(R.drawable.circle);
        rectBtn.setImageResource(R.drawable.rect_clicked);
        lineBtn.setImageResource(R.drawable.line);

        model.selectedTool = "rect";
    }

    public void lineBtnOnClick(View v) {
        if (model.selectedTool == "cursor" && model.selectedShape != null) {
            model.selectedShape.unSelect();
            model.selectedShape = null;
            sketchPadView.invalidate();
        }
        cursorBtn.setImageResource(R.drawable.cursor);
        eraserBtn.setImageResource(R.drawable.eraser);
        circleBtn.setImageResource(R.drawable.circle);
        rectBtn.setImageResource(R.drawable.rect);
        lineBtn.setImageResource(R.drawable.line_clicked);

        model.selectedTool = "line";
    }

    public void blueBtnOnClick(View v) {
        if (model.selectedTool == "eraser") {
            blueBtn.setBackgroundColor(blue);
            yellowBtn.setBackgroundColor(yellow);
            redBtn.setBackgroundColor(red);

            model.selectedColor = -1;
        }
        else {
            if (model.selectedTool == "cursor" && model.selectedShape != null) {
                sketchPadView.invalidate();
            }

            blueBtn.setBackgroundColor(darkBlue);
            yellowBtn.setBackgroundColor(yellow);
            redBtn.setBackgroundColor(red);

            model.selectedColor = blue;
        }
    }

    public void yellowBtnOnClick(View v) {
        if (model.selectedTool == "eraser") {
            blueBtn.setBackgroundColor(blue);
            yellowBtn.setBackgroundColor(yellow);
            redBtn.setBackgroundColor(red);

            model.selectedColor = -1;
        }
        else {
            if (model.selectedTool == "cursor" && model.selectedShape != null) {
                sketchPadView.invalidate();
            }

            blueBtn.setBackgroundColor(blue);
            yellowBtn.setBackgroundColor(darkYellow);
            redBtn.setBackgroundColor(red);

            model.selectedColor = yellow;
        }
    }

    public void redBtnOnClick(View v) {
        if (model.selectedTool == "eraser") {
            blueBtn.setBackgroundColor(blue);
            yellowBtn.setBackgroundColor(yellow);
            redBtn.setBackgroundColor(red);

            model.selectedColor = -1;
        }
        else {
            if (model.selectedTool == "cursor" && model.selectedShape != null) {
                sketchPadView.invalidate();
            }

            blueBtn.setBackgroundColor(blue);
            yellowBtn.setBackgroundColor(yellow);
            redBtn.setBackgroundColor(darkRed);

            model.selectedColor = red;
        }
    }

    public void shareBtnOnClick(View v) {
        requestPermissions();
        Bitmap screenshot = screenshot(sketchPadView);
        storeShare(screenshot);
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, 1);
        }
    }

    // Referring to https://stackoverflow.com/questions/2661536/how-to-programmatically-take-a-screenshot-on-android
    private Bitmap screenshot(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        v.draw(c);
        return bitmap;
    }

    private void storeShare(Bitmap bitmap) {
        DateFormat dateFormat = new DateFormat();
        String curTime = dateFormat.format("MM-dd-yyyy-HH-mm-ss", new Date()).toString();
        String imagePath = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                curTime,
                "Image of JSketch"
        );

        Uri imageUri = Uri.parse(imagePath);
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setData(imageUri);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent, "Share JSketch"));
    }
}
