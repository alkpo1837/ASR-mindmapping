package com.polytech.asrproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private Button m_buttonInit;
    private Button m_previousButton;

    private Model m_Model;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Model = new Model();

        m_mapMindView = (MapMindView) findViewById(R.id.mapMindView);
        m_buttonInit = (Button) findViewById(R.id.initButton);
        m_previousButton = (Button) findViewById(R.id.previousButton);

        m_mapMindView.init();
        m_mapMindView.setActivity(this);




        m_buttonInit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_mapMindView.constructFromFile(m_Model.getCurrentFile());
                m_buttonInit.setEnabled(false);
            }
        });

        m_previousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_Model.goToParentDirectory();

                m_mapMindView.constructFromFile(m_Model.getCurrentFile());

                if (!m_Model.hasParentDirectory())
                {
                    m_previousButton.setEnabled(false);
                }
            }
        });
    }

    public void notifyClickOnButton(File file)
    {
        Log.d("activity notify", "Button name = " + file.getName());

        if (file.isDirectory())
        {
            if (!m_Model.hasParentDirectory())
            {
                m_previousButton.setEnabled(true);
            }

            m_Model.setCurrentFile(file);
            m_mapMindView.constructFromFile(file);
        }
        /*else
        {
            clickOnFile(f);
        }*/


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
}
