package com.esper.game;

/**
 * Created by jorge on 5/11/2014.
 */
import com.esper.game.Block;
import com.esper.game.Cube;
public class ModelLayerTestSuite {
    public static void main(String [] args)
    {
        System.out.println("Enter block test");
        testBlock();
        System.out.println("Exit block test");
        System.out.println("===============");
        System.out.println("Enter cube test");
        testCube();
        System.out.println("Exit cube test");
    }
    public static void testBlock()
    {
        Block b1 = new Block(0,0,0,0,0,0, null);
        System.out.println(b1.toString());
        b1.push(1,1,1);
        System.out.println(b1.toString());
        b1.push(-1,-1,-1);
        System.out.println(b1.toString());
        b1.push(2,4,6);
        System.out.println(b1.toString());

        //missing test for faces, this is coming up
    }
    public static void testCube()
    {
        Cube c1 =  new Cube(3);
        c1.deform(0,0,0,1,0,0);
        System.out.println(c1.toString());
        c1.deform(0,0,0,1,0,0);
        System.out.println(c1.toString());

        System.out.println(c1.toString());
        if(c1.isProper())
            System.out.println("Proper");
        else
            System.out.println("Non-Proper");
        c1.cube[1][1][1].push(3,3,3);
        if(c1.isProper())
            System.out.println("Proper");
        else
            System.out.println("Non-Proper");
        c1.cube[1][1][1].push(-3,-3,-3);
        if(c1.isProper())
            System.out.println("Proper");
        else
            System.out.println("Non-Proper");

        for (int i = 0 ; i < c1.sideLenght; i++) {
            for (int j = 0; j < c1.sideLenght; j++) {
                for (int k = 0; k < c1.sideLenght; k++) {
                    c1.cube[i][j][k].push(1,0,0);
                }

            }
        }
        if(c1.isProper())
            System.out.println("Proper");
        else
            System.out.println("Non-Proper");


    }
}
