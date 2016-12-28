package com.polytech.asrproject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import static android.view.inputmethod.InputMethodManager.SHOW_FORCED;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

public class MainActivity extends AppCompatActivity
{
    // MindMapView
    private MapMindView m_mapMindView;
    private ActionsOnButtonView m_actionsOnButtonView;

    private Model m_Model;

    private Button m_clickedButton;
    private File m_clickedFile;

    private CustomEditText m_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Model = new Model();

        m_editText = (CustomEditText) findViewById(R.id.edit_text);
        m_editText.init(this);

        m_editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                Log.d("CHANGEONS LE TEXTE ", m_editText.getText().toString()); //Here you will get what you want

                if (m_clickedButton != null)
                {
                    m_clickedButton.setText(charSequence);
                }

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }

        });



        m_mapMindView = (MapMindView) findViewById(R.id.mapMindView);
        m_mapMindView.init(this);

        m_actionsOnButtonView = (ActionsOnButtonView) findViewById(R.id.actions_on_button_view);
        m_actionsOnButtonView.init(this);

        // We have to wait a little bit, otherwise the view doesn't know its size or whatever
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                m_mapMindView.constructFromFile(m_Model.getCurrentFile());
            }
        }, 200);
    }

    public void notifyClickOnButton(Button buttonClicked, File file)
    {
        Log.d("activity notify", "Button name = " + file.getName());
        m_clickedFile = file;
        m_clickedButton = buttonClicked;

        m_actionsOnButtonView.setX(buttonClicked.getX() - 150);
        m_actionsOnButtonView.setY(buttonClicked.getY() - 150);

        m_actionsOnButtonView.setVisibility(View.VISIBLE);



        m_actionsOnButtonView.canDelete(!file.isDirectory());


    }

    public void notifyMoveButton(Button buttonMoved)
    {
        if (m_actionsOnButtonView != null)
        {

            m_actionsOnButtonView.setX(buttonMoved.getX() - 150);
            m_actionsOnButtonView.setY(buttonMoved.getY() - 150);

            /*m_mapMindView.bringChildToFront(m_actionsOnButtonView);
            m_mapMindView.setAllButtonChildsToBack();
            m_mapMindView.invalidate();
            */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.centerItem:
                m_mapMindView.centerView();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void closeActionsOnButtonView()
    {
        m_actionsOnButtonView.setVisibility(View.INVISIBLE);
    }

    public void openDirectoryOrFile()
    {
        closeActionsOnButtonView();

        if (m_clickedFile.isDirectory())
        {
            m_Model.setCurrentFile(m_clickedFile);
            m_mapMindView.constructFromFile(m_clickedFile);

            m_clickedFile = null;
        }
        else
        {
            clickOnFile(m_clickedFile);
        }
    }

    public void renameFile()
    {
        m_editText.setVisibility(View.VISIBLE);
        m_editText.setFocusableInTouchMode(true);

        if (m_editText.requestFocus())
        {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            boolean shown = imm.showSoftInput(m_editText, InputMethodManager.SHOW_IMPLICIT);
        }
        else
        {
            Log.d("Focus", "Cant focus");
        }
    }

    public void deleteFile()
    {
        m_mapMindView.destroyButton(m_clickedButton);

        m_actionsOnButtonView.setVisibility(View.INVISIBLE);

        // Delete the file here


    }

    public void hideKeyboard()
    {
        if (m_editText.getVisibility() == View.VISIBLE)
        {
            // hide virtual keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(m_editText.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);

            // Rename file or directory here

            m_editText.setVisibility(View.INVISIBLE);

            m_actionsOnButtonView.setVisibility(View.INVISIBLE);

        }
    }

    public void clickOnFile(File file)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);

        //On récupére le type MIME du fichier que l'on affecte au nouvel intente
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext2 = file.getName().substring(file.getName().indexOf(".") + 1).toLowerCase();
        String type = mime.getMimeTypeFromExtension(ext2);
        i.setDataAndType(Uri.fromFile(file), type);

        try {
            startActivity(i);
            // Et s'il n'y a pas d'activité qui puisse gérer ce type de fichier
        } catch (ActivityNotFoundException e) {

            Toast.makeText(this, "Oups, vous n'avez pas d'application qui puisse lancer ce fichier", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (m_Model.hasParentDirectory())
        {
            m_Model.goToParentDirectory();

            m_mapMindView.constructFromFile(m_Model.getCurrentFile());
        }
        else
        {
            super.onBackPressed();
        }
    }

}






