package com.polytech.asrproject;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by HP on 14/12/2016.
 */

public class MapMindView extends RelativeLayout implements View.OnTouchListener
{
    private HashMap<DirectoryOrFileButton, File> periphButtons;
    private Button centralButton;

    private Point[] periphButtonsPositions;

    private MainActivity m_mainActivity;

    private float dX, dY;
    private float dxView, dYView;
    private boolean onLongClick;

    private int screenWidth, screenHeigth;
    private int viewWidth, viewHeigth;

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

    public void init(MainActivity mainActivity)
    {
        periphButtons = new HashMap<DirectoryOrFileButton, File>();

        this.m_mainActivity = mainActivity;

        inflate(getContext(), R.layout.map_mind_view, this);

        centralButton = (Button) findViewById(R.id.central_button);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(4.0f);

        periphButtonsPositions = new Point[16];

        for (int i = 0; i < 16; i++)
            periphButtonsPositions[i] = new Point();

        Log.d("init", "MapMindView init");

        // Used to draw lines
        setWillNotDraw(false);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeigth = size.y;

        Log.d("init", screenWidth + " et " + screenHeigth);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.getLayoutParams();

        viewHeigth = params.height;
        viewWidth = params.width;

        params.setMargins(-(params.width - screenWidth) / 2, -(params.height - screenHeigth) / 2, 0, 0);

        this.setLayoutParams(params);
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
            DirectoryOrFileButton button = new DirectoryOrFileButton(getContext());

            button.init(childFile);

            button.setOnTouchListener(this);

            periphButtons.put(button, childFile);

            /* Layout params */
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.leftMargin = getXPeriphButtonPosition(n);
            params.topMargin = getYPeriphButtonPosition(n);

            Log.d("margins", n + " avec " + params.leftMargin + " et " + params.topMargin);

            button.setLayoutParams(params);

            n++;

            this.addView(button);

            if (n == 9)
                break;
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
            float newX = event.getRawX() + dxView;
            float newY = event.getRawY() + dYView;

            //if (newX < 0 && newX > -(viewWidth -screenWidth) && newY < 0 && newY > -(viewHeigth - screenHeigth))
            {
                Log.d("onTouchEvent", "On a " + newX + " et " + newY);

                this.setX(newX);
                this.setY(newY);
            }

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

                m_mainActivity.notifyMoveButton((Button) view);

                invalidate();

                onLongClick = true;
                break;

            case MotionEvent.ACTION_UP:
                if (!onLongClick)
                {
                    Button buttonClicked = (Button) view;

                    m_mainActivity.notifyClickOnButton(buttonClicked, periphButtons.get(buttonClicked));
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


    public void centerView()
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.getLayoutParams();

        this.setX(-(params.width - screenWidth) / 2);
        this.setY(-(params.height - screenHeigth) / 2);
    }


    public void generateButtonsPositions()
    {
        float xFactors[] = {-0.25f, 0.25f, 0.0f, 0.0f, -0.25f, 0.25f, -0.25f, 0.25f, 0.33f};
        float yFactors[] = {0.0f, 0.0f, -0.25f, 0.25f, -0.25f, -0.25f, 0.25f, 0.25f, 0.125f};

        for (int i = 0; i < 9; i++)
        {
            periphButtonsPositions[i].x = (int) (centralButton.getX() + screenWidth * xFactors[i]);
            periphButtonsPositions[i].y = (int) (centralButton.getY() + screenHeigth * yFactors[i]);
        }

       /* periphButtonsPositions[0].x = (int) centralButton.getX() - screenWidth * 1 / 2;//-leftMargin  + 100;//-centralButton.getX() * 1.5;
        periphButtonsPositions[0].y = (int) centralButton.getY();

        periphButtonsPositions[1].x = (int) centralButton.getX() + screenWidth * 1 / 4;
        periphButtonsPositions[1].y = (int) centralButton.getY();

        periphButtonsPositions[2].x = (int) (centralButton.getX() * 1.0);
        periphButtonsPositions[2].y = (int) (centralButton.getY() * 0.5);

        periphButtonsPositions[3].x = (int) (centralButton.getX() * 1.0);
        periphButtonsPositions[3].y = (int) (centralButton.getY() * 1.5);

        periphButtonsPositions[4].x = (int) (centralButton.getX() * 0.5);
        periphButtonsPositions[4].y = (int) (centralButton.getY() * 0.5);

        periphButtonsPositions[5].x = (int) (centralButton.getX() * 1.5);
        periphButtonsPositions[5].y = (int) (centralButton.getY() * 0.5);

        periphButtonsPositions[6].x = (int) (centralButton.getX() * 0.5);
        periphButtonsPositions[6].y = (int) (centralButton.getY() * 1.5);

        periphButtonsPositions[7].x = (int) (centralButton.getX() * 1.5);
        periphButtonsPositions[7].y = (int) (centralButton.getY() * 1.5);
*/

        /*periphButtonsPositions[9].x = centralButton.getX() * 1.5;
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
        periphButtonsPositions[15].y = centralButton.getY() * 1.0;*/

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
