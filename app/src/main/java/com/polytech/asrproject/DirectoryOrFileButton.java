package com.polytech.asrproject;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import java.io.File;

/**
 * Created by HP on 21/12/2016.
 */

public class DirectoryOrFileButton extends Button
{
    public DirectoryOrFileButton(Context context)
    {
        super(context);
    }

    public DirectoryOrFileButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DirectoryOrFileButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void init(File file)
    {
        this.setText(file.getName());

        if (file.getName().length() > 12)
        {
            this.setTextSize(20.0f);
        }
        else
        {
            this.setTextSize(25.0f);
        }

        this.setPadding(10, 10, 10, 10);

        if (file.isDirectory())
        {
            this.setBackgroundResource(R.drawable.directory_button);
        }
        else
        {
            this.setBackgroundResource(R.drawable.file_button);
        }


    }
}
