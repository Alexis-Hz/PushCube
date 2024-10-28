package com.esper.game;

/**
 * Created by jorge on 5/27/2014.
 */
public class SaveFile {
    boolean [] unlockedLevels;
    int lastLevel;
    public SaveFile()
    {
        unlockedLevels =  new boolean[50];
        for (int i = 0; i < unlockedLevels.length; i++)
            unlockedLevels[i] = false;
        unlockedLevels[0] = true;
        unlockedLevels[1] = true;
        lastLevel = 1;
    }

}
