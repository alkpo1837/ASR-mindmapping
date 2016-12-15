package com.polytech.asrproject;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by HP on 14/12/2016.
 */

class Position
{
    public double x, y;

    public Position()
    {

    }
}

public class MapMindView extends RelativeLayout implements View.OnTouchListener
{
    private HashMap<Button, File> periphButtons;
    private Button centralButton;

    private Position[] periphButtonsPositions;

    private MainActivity m_mainActivity;

    private float dX, dY;
    private float dxView, dYView;
    private boolean onLongClick;

    Paint paint = new Paint();


    public MapMindView(Context context) {
        super(context);
    }

    public MapMindView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapMindView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init()
    {
        periphButtons = new HashMap<Button, File>();

        inflate(getContext(), R.layout.map_mind_view, this);

        centralButton = (Button) findViewById(R.id.central_button);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        periphButtonsPositions = new Position[16];

        for (int i = 0; i < 16; i++)
            periphButtonsPositions[i] = new Position();

        Log.d("init", "MapMindView init");

        setWillNotDraw(false);
    }

    public void setActivity(MainActivity mainActivity)
    {
        m_mainActivity = mainActivity;
    }

    // Eb fonction du nombres du children, construire un tableau de positions
    public void constructFromFile(File file)
    {
        int n = 0;
        int y = 0;

        int nbrChildrens = file.listFiles().length;
        Random r = new Random();

        centralButton.setText(file.getName());

        generateButtonsPositions();

        if (periphButtons.keySet().size() > 0)
        {
            clearPeriphButtons();
        }

        for (File childFile : file.listFiles())
        {
            Button button = new Button(getContext());

            button.setTextSize(20.f);

            if (childFile.isDirectory())
            {
                button.setBackgroundColor(Color.MAGENTA);
            }

            button.setText(childFile.getName());

            button.setOnTouchListener(this);

            periphButtons.put(button, childFile);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            if (nbrChildrens < 16)
            {
                params.leftMargin = getXPeriphButtonPosition(n);
                params.topMargin = getYPeriphButtonPosition(n);

                Log.d("margins", n + " avec " + params.leftMargin + " et " + params.topMargin);
            }
            else
            {
                params.leftMargin = n * 350;
                params.topMargin = y;

                if (n % 6 == 0)
                {
                    n = 1;
                    y += 200;
                }
            }

            button.setLayoutParams(params);

            n++;

            this.addView(button);
        }
    }

    /*@Override
    public void onMeasure (int widthMeasureSpec,
                           int heightMeasureSpec)
    {
        final int w = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY);
        final int h = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d("onmesure", w + " et " + h);
        setMeasuredDimension(w, h);
    }*/

    // Touch on view, not a button
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN)
        {
            dxView = this.getX() - event.getRawX();
            dYView = this.getY() - event.getRawY();
        }
        else if (action == MotionEvent.ACTION_MOVE)
        {
            this.setX(event.getRawX() + dxView);
            this.setY(event.getRawY() + dYView);

        }

        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);

                invalidate();

                onLongClick = true;
                break;

            case MotionEvent.ACTION_UP:
                if (!onLongClick)
                {
                    File f = periphButtons.get((Button) view);

                    m_mainActivity.notifyClickOnButton(f);
                }

                onLongClick = false;

                break;

            default:
                return false;
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (periphButtons.keySet().size() > 0)
        {
            for (Button button : periphButtons.keySet())
            {
                canvas.drawLine(centralButton.getX() + centralButton.getWidth() / 2, centralButton.getY() + centralButton.getHeight() / 2,
                                button.getX() + button.getWidth() / 2, button.getY() + button.getHeight() / 2, paint);
            }
        }
    }

    public void clearPeriphButtons()
    {
        for (Button b : periphButtons.keySet())
        {
            this.removeView(b);
        }

        periphButtons.clear();
    }


    public void generateButtonsPositions()
    {
        Log.d("generateButton", "On a " + periphButtonsPositions.length);

        periphButtonsPositions[0].x = -500;//-centralButton.getX() * 1.5;
        periphButtonsPositions[0].y = centralButton.getY() * 1.0;

        periphButtonsPositions[1].x = centralButton.getX() * 0.5;
        periphButtonsPositions[1].y = centralButton.getY() * 1.0;

        periphButtonsPositions[2].x = centralButton.getX() * 1.0;
        periphButtonsPositions[2].y = centralButton.getY() * 0.5;

        periphButtonsPositions[3].x = centralButton.getX() * 1.0;
        periphButtonsPositions[3].y = centralButton.getY() * 1.5;

        periphButtonsPositions[4].x = centralButton.getX() * 0.5;
        periphButtonsPositions[4].y = centralButton.getY() * 0.5;

        periphButtonsPositions[5].x = centralButton.getX() * 1.5;
        periphButtonsPositions[5].y = centralButton.getY() * 0.5;

        periphButtonsPositions[6].x = centralButton.getX() * 0.5;
        periphButtonsPositions[6].y = centralButton.getY() * 1.5;

        periphButtonsPositions[7].x = centralButton.getX() * 1.5;
        periphButtonsPositions[7].y = centralButton.getY() * 1.5;

        periphButtonsPositions[8].x = centralButton.getX() * 1.5;
        periphButtonsPositions[8].y = centralButton.getY() * 1.0;

        periphButtonsPositions[9].x = centralButton.getX() * 1.5;
        periphButtonsPositions[9].y = centralButton.getY() * 1.0;

        periphButtonsPositions[10].x = centralButton.getX() * 1.5;
        periphButtonsPositions[10].y = centralButton.getY() * 1.0;

        periphButtonsPositions[11].x = centralButton.getX() * 1.5;
        periphButtonsPositions[11].y = centralButton.getY() * 1.0;

        periphButtonsPositions[12].x = centralButton.getX() * 1.5;
        periphButtonsPositions[12].y = centralButton.getY() * 1.0;

        periphButtonsPositions[13].x = centralButton.getX() * 1.5;
        periphButtonsPositions[13].y = centralButton.getY() * 1.0;

        periphButtonsPositions[14].x = centralButton.getX() * 1.5;
        periphButtonsPositions[14].y = centralButton.getY() * 1.0;

        periphButtonsPositions[15].x = centralButton.getX() * 1.5;
        periphButtonsPositions[15].y = centralButton.getY() * 1.0;

    }

    public int getXPeriphButtonPosition(int n)
    {
        return (int) periphButtonsPositions[n].x;
    }

    public int getYPeriphButtonPosition(int n)
    {
        return (int) periphButtonsPositions[n].y;
    }
}
