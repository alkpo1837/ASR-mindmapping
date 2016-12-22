package com.polytech.asrproject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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

        this.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    hideEditTextAndKeyboard();
                    return true;
                } else
                {
                    return false;
                }
            }
        });
    }

    public void hideEditTextAndKeyboard()
    {


        mainActivity.hideKeyboard();
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Log.d("On passe ici", "Hide keyboard");

            hideEditTextAndKeyboard();

            return true;
        }

        return false;
    }


}
