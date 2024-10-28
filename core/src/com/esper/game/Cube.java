package com.esper.game;

/**
 * Created by jorge on 5/10/2014.
 * This class represents the cube in the game
 */
import com.esper.game.Block;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class Cube {
    public Block[][][] cube;
    public int [] lastDeform;
    public int sideLenght;

    //undo stuff
    Block undoBlock;
    int undoX;
    int undoY;
    int undoZ;
    public Cube(int size)
    {
        cube = new Block[size][size][size];
        sideLenght = size;
        for (int i = 0 ; i < size; i++)
        {
            for (int j =0; j < size; j++)
            {
                for(int k = 0; k < size; k++)
                {

                    int [] faces = {0,0,0,0,0,0};//0: outer 1:inner
                    if(i == 0)
                    {
                        faces[3] = 1;
                    }
                    if (i == sideLenght -1)
                    {
                        faces[4] = 1;
                    }
                    if(k == 0)
                    {
                       faces[5] = 1;
                    }
                    if (k == sideLenght -1)
                    {
                        faces[2] = 1;
                    }
                    if(j == 0)
                    {
                        faces[1] = 1;
                    }
                    if (j == sideLenght -1)
                    {
                        faces[0] = 1;
                    }


                    cube [i][j][k] = new Block(i ,j ,k ,0, 0, 0, faces);
                }
            }
        }
    }
    public void turn(int difficulty)
    {
        //todo implement turn
        //find a suitable cube
        Random randomgen = new Random();
        for(int i = 0; i < difficulty; i++) {
            boolean toggle = true;
            while (toggle) {
                int rx = randomgen.nextInt(sideLenght);
                int ry = randomgen.nextInt(sideLenght);
                int rz = randomgen.nextInt(sideLenght);

                int xt = 0;
                int yt = 0;
                int zt = 0;

                Block b = cube[rx][ry][rz];

                int rf = randomgen.nextInt(6);
                switch (rf) {
                    case 0:
                        xt = 1;
                        break;
                    case 1:
                        xt = -1;
                        break;
                    case 2:
                        yt = 1;
                        break;
                    case 3:
                        yt = -1;
                        break;
                    case 4:
                        zt = 1;
                        break;
                    case 5:
                        zt = -1;
                        break;
                }
                if (getBlock(b.getxPosition() + (xt * -1), b.getyPosition() + (yt * -1), b.getzPosition() + (zt * -1)) == null)//there is not a block to the side
                {
                    deform(b.getxCoord(), b.getyCoord(), b.getzCoord(), xt, yt, zt);
                    toggle = false;
                }
            }
        }



        //find a suitable direction
        //deform the cube
    }
    public  int [] deform(int xGlobalCoord, int yGlobalCoord, int zGlobalCoord, int xTrans, int yTrans, int zTrans)//these are coords fpr tje actual block in the array
    {

        System.out.println("pushing : "+xGlobalCoord+":"+yGlobalCoord +":"+zGlobalCoord + " for  "+ xTrans + ":" + yTrans +":"+zTrans);

        //while there is a next

        //pushBlock(xGlobalCoord, yGlobalCoord, zGlobalCoord, xTrans, yTrans, zTrans);
        Stack<Block> moving = new Stack<Block>();
        moving.push(cube[xGlobalCoord][yGlobalCoord][zGlobalCoord]);
        Block b = getBlock(cube[xGlobalCoord][yGlobalCoord][zGlobalCoord].getxPosition()+xTrans, cube[xGlobalCoord][yGlobalCoord][zGlobalCoord].getyPosition()+yTrans, cube[xGlobalCoord][yGlobalCoord][zGlobalCoord].getzPosition()+zTrans);
        while(b != null)//there is a next Block
        {
            moving.push(b);
            b = getBlock(b.getxPosition()+xTrans, b.getyPosition()+yTrans, b.getzPosition()+zTrans);
        }
        undoBlock = moving.peek();
        undoX = xTrans*-1;
        undoY = yTrans*-1;
        undoZ = zTrans*-1;
        while(!moving.empty())
        {
            moving.pop().push(xTrans,yTrans,zTrans);
        }
        return null;
    }
    public Block getBlock(int x, int y, int z)
    {
        for (int i = 0 ; i < sideLenght; i++) {
            for (int j = 0; j < sideLenght; j++) {
                for (int k = 0; k < sideLenght; k++) {
                    //System.out.println(cube[i][j][k].toString());
                    if((cube[i][j][k].getxPosition() == x) && (cube[i][j][k].getyPosition() == y) && (cube[i][j][k].getzPosition() == z))
                        return cube[i][j][k];
                }
            }
        }
        return null;//not found
    }
    public boolean isProper()
    {
        int x = cube[0][0][0].getxTransform();
        int y = cube[0][0][0].getyTransform();
        int z = cube[0][0][0].getzTransform();
        for (int i = 0 ; i < sideLenght; i++)
        {
            for (int j =0; j < sideLenght; j++)
            {
                for(int k = 0; k < sideLenght; k++)
                {
                    /*
                    System.out.println(cube[i][j][k].getxTransform());
                    System.out.println(cube[i][j][k].getyTransform());
                    System.out.println(cube[i][j][k].getzTransform());
                    System.out.println();
                    */
                    //check for the same transfor in the all the cubes to see if the cube is solved
                    //2 cases:
                    //if it's an outside block
                    if(i == 0 || j == 0 || k == 0 || i == sideLenght-1 || j == sideLenght-1 || k == sideLenght-1)//this is an edge block
                    {
                        if (x != cube[i][j][k].getxTransform())
                            return false;
                        else if (y != cube[i][j][k].getyTransform())
                            return false;
                        else if (z != cube[i][j][k].getzTransform())
                            return false;
                        //else this cubes trnasform is the same as the 0,0,0


                    }
                    else {
                        //bounds
                        //x -> x + sidelenght
                        if ((cube[i][j][k].getxTransform() >= x + sideLenght -1) || (cube[i][j][k].getxTransform() <= x))
                            return false;
                        else if ((cube[i][j][k].getyTransform() >= y + sideLenght -1) || (cube[i][j][k].getyTransform() <= y))
                            return false;
                        else if ((cube[i][j][k].getzTransform() >= z + sideLenght -1) || (cube[i][j][k].getzTransform() <= z))
                            return false;
                    }
                }
            }
        }
        return true;
    }
    public void undo()
    {
        if(undoBlock != null)
        {
            deform(undoBlock.getxCoord(), undoBlock.getyCoord(), undoBlock.getzCoord(), undoX, undoY, undoZ);
        }
    }
    public int center()
    {
        return 0;
    }
    public String toString()
    {
        String toRe = "";

        for (int i = 0 ; i < sideLenght; i++)
        {
            for (int j =0; j < sideLenght; j++)
            {
                toRe += '\n';
                for(int k = 0; k < sideLenght; k++)
                {
                    toRe += "["+ cube[i][j][k].getxTransform() +"|"+ cube[i][j][k].getyTransform() +"|"+ cube[i][j][k].getzTransform() +"]";
                }
            }
            toRe += '\n';
            toRe += '\n';
        }

        return toRe;
    }
}
