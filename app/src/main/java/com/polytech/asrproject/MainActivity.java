package com.polytech.asrproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    // MindMapView
    private MapMindView m_mapMindView;
    private ActionsOnButtonView m_actionsOnButtonView = null;

    private Model m_Model;
    private File m_clickedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Model = new Model();

        m_mapMindView = (MapMindView) findViewById(R.id.mapMindView);

        m_mapMindView.init(this);

        m_actionsOnButtonView = new ActionsOnButtonView(this.getApplicationContext());

        m_actionsOnButtonView.init(this);
        m_mapMindView.addView(m_actionsOnButtonView);

        m_actionsOnButtonView.setVisibility(View.INVISIBLE);

    }

    public void notifyClickOnButton(Button buttonClicked, File file)
    {
        Log.d("activity notify", "Button name = " + file.getName());
        m_clickedFile = file;

        m_actionsOnButtonView.setX(buttonClicked.getX() - 150);
        m_actionsOnButtonView.setY(buttonClicked.getY() - 150);

        m_actionsOnButtonView.setVisibility(View.VISIBLE);

    }

    public void notifyMoveButton(Button buttonMoved)
    {
        if (m_actionsOnButtonView != null)
        {
            m_actionsOnButtonView.setX(buttonMoved.getX() - 150);
            m_actionsOnButtonView.setY(buttonMoved.getY() - 150);
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
            case R.id.initItem:
                m_mapMindView.constructFromFile(m_Model.getCurrentFile());
                //m_buttonInit.setEnabled(false);
                return true;
            case R.id.previousItem:
                m_Model.goToParentDirectory();

                m_mapMindView.constructFromFile(m_Model.getCurrentFile());

                if (!m_Model.hasParentDirectory())
                {
                    //m_previousButton.setEnabled(false);
                }
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
        /*else
        {
            clickOnFile(f);
        }*/
    }
}




/*
        public void clickOnFile(File file)
        {
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            String fileExt = fileExt(file.getName());
            if (fileExt != null)
            {
                String mimeType = myMime.getMimeTypeFromExtension(fileExt.substring(1));
                newIntent.setDataAndType(Uri.fromFile(file), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    getContext().startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "No handler for this type of file.", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getContext(), "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }
        }
        private String fileExt(String url) {
            if (url.indexOf("?") > -1) {
                url = url.substring(0, url.indexOf("?"));
            }
            if (url.lastIndexOf(".") == -1) {
                return null;
            } else {
                String ext = url.substring(url.lastIndexOf(".") + 1);
                if (ext.indexOf("%") > -1) {
                    ext = ext.substring(0, ext.indexOf("%"));
                }
                if (ext.indexOf("/") > -1) {
                    ext = ext.substring(0, ext.indexOf("/"));
                }
                return ext.toLowerCase();

            }
        }
        */
