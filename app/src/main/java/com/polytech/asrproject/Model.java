package com.polytech.asrproject;

import android.os.Environment;

import java.io.File;

/**
 * Created by HP on 14/12/2016.
 */

public class Model
{
    private final String ROOT_PATH = Environment.getRootDirectory().toString();

    private File currentFile;

    public Model()
    {
        currentFile = new File(ROOT_PATH);
    }

    public File[] getChildrens()
    {
        return currentFile.listFiles();
    }

    public String getCurrentDirectoryName()
    {
        return currentFile.getName();
    }

    public void setCurrentFile(File file)
    {
        currentFile = file;
    }

    public File getParentDirectory()
    {
        return currentFile.getParentFile();
    }

    public void goToParentDirectory()
    {
        currentFile = currentFile.getParentFile();
    }

    public boolean hasParentDirectory()
    {
        return currentFile.getParentFile() != null;
    }

    public File getCurrentFile()
    {
        return currentFile;
    }
}
