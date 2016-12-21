package com.polytech.asrproject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;

/**
 * Created by HP on 15/12/2016.
 */

public class ActionsOnButtonView extends LinearLayout
{
    private MainActivity m_mainActivity;

    private Button m_quitButton;
    private Button m_openFileButton;

    public ActionsOnButtonView(Context context)
    {
        super(context);
    }

    public ActionsOnButtonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ActionsOnButtonView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void init(MainActivity activity)
    {
        inflate(getContext(), R.layout.actions_on_button_view, this);

        m_mainActivity = activity;

        m_quitButton = (Button) findViewById(R.id.closeViewButton);
        m_openFileButton = (Button) findViewById(R.id.openButton);

        m_quitButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_mainActivity.closeActionsOnButtonView();
            }
        });

        m_openFileButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_mainActivity.openDirectoryOrFile();
            }
        });

        setVisibility(INVISIBLE);
    }
}
