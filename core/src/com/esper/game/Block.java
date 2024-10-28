package com.esper.game;

/**
 * Created by jorge on 5/10/2014.
 */
public class Block {
    private int xTransform;
    private int yTransform;
    private int zTransform;

    private int xCoord;
    private int yCoord;
    private int zCoord;
    int [] faces;
    public Block(int xc, int yc, int zc, int xt, int yt, int zt, int [] f)
    {
        xCoord = xc;
        yCoord = yc;
        zCoord = zc;

        xTransform = xt;
        yTransform = yt;
        zTransform = zt;
        faces = f;
    }
    public Block(int x, int y, int z, int [] f)
    {
        xTransform = x;
        yTransform = y;
        zTransform = z;
        faces = f;
    }


    public int getxTransform()
    {
        return xTransform;
    }
    public int getyTransform()
    {
        return yTransform;
    }
    public int getzTransform()
    {
        return zTransform;
    }

    public int getxPosition()  { return xCoord+xTransform;}
    public int getyPosition()
    {
        return yCoord+yTransform;
    }
    public int getzPosition()
    {
        return zCoord+zTransform;
    }

    public int getxCoord()  { return xCoord;}
    public int getyCoord()
    {
        return yCoord;
    }
    public int getzCoord()
    {
        return zCoord;
    }


    public void push(int xd, int yd, int zd)
    {
        xTransform += xd;
        yTransform += yd;
        zTransform += zd;
    }
    public String toString()
    {
        return "cords: "+ xCoord + ":" + yCoord+":"+ zCoord+" at x:"+getxPosition()+"|y:"+getyPosition()+"|z"+getzPosition();
    }
}
