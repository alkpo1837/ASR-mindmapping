package com.polytech.asrproject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by HP on 22/12/2016.
 */

public class CustomEditText extends EditText
{

    Context context;
    MainActivity mainActivity;

    public CustomEditText(Context context)
    {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }



    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            mainActivity.hideKeyboard();

            return true;
        }

        return false;
    }


}
