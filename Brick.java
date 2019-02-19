/**
 * Brick class contains methods and fields that controls the brick of the Tetris game.
 * 
 * the class
 * - generates brick objects and gets specific shape data from the BrickData class
 * - rotates (reverses) the brick posture
 */ 

public class Brick {
    private int index;
    private char type;
    private int[][] shape;
    private int posX = 4, posY = 0;

    // generate bricks by random,
    // calling the overload constructor for further operation
    public Brick() {
        this((int)(Math.random()*7));
    }

    // 1. be called by the non-arg constructor
    // 2. generate specific bricks by directly indicating
    public Brick(int n) {
        switch(n) {
            case 0:
                index = 0;
                type = 'I';
                shape = BrickData.shape[0][0];
                break;
            case 1:
                index = 1;
                type = 'J';
                shape = BrickData.shape[1][0];
                break;
            case 2:
                index = 2;
                type = 'L';
                shape = BrickData.shape[2][0];
                break;
            case 3:
                index = 3;
                type = 'O';
                shape = BrickData.shape[3][0];
                break;
            case 4:
                index = 4;
                type = 'S';
                shape = BrickData.shape[4][0];
                break;
            case 5:
                index = 5;
                type = 'T';
                shape = BrickData.shape[5][0];
                break;
            case 6:
                index = 6;
                type = 'Z';
                shape = BrickData.shape[6][0];
                break;
            default:
                break;
        }
    }

    public int[][] getShape() {
        return shape;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void addPosX(int i) {
        this.posX += i;
    }

    public void addPosY(int i) {
        this.posY += i;
    }

    // rotate the bricks to different postures
    // - check how many postures can it be rotate as
    //   if there are 2 postures for the brick, switch between posture 0 and posture 1
    //   if there are 3 postures, turn 0 to 1, 1 to 2, and 2 back to 0, etc.
    public void rotate() {
        switch(BrickData.shape[index].length){
            case 2:
                if (shape == BrickData.shape[index][0]) shape = BrickData.shape[index][1];
                else shape = BrickData.shape[index][0];
                break;
            case 3:
                if (shape == BrickData.shape[index][0]) shape = BrickData.shape[index][1];
                else if (shape == BrickData.shape[index][1]) shape = BrickData.shape[index][2];
                else shape = BrickData.shape[index][0];
                break;
            case 4:
                if (shape == BrickData.shape[index][0]) shape = BrickData.shape[index][1];
                else if (shape == BrickData.shape[index][1]) shape = BrickData.shape[index][2];
                else if (shape == BrickData.shape[index][2]) shape = BrickData.shape[index][3];
                else shape = BrickData.shape[index][0];
                break;
            default:
                break;
        }
    }

    // reverse the rotation (same concept as the rotate function)
    public void reverse() {
        switch(BrickData.shape[index].length){
            case 2:
                if (shape == BrickData.shape[index][0]) shape = BrickData.shape[index][1];
                else shape = BrickData.shape[index][0];
                break;
            case 3:
                if (shape == BrickData.shape[index][0]) shape = BrickData.shape[index][2];
                else if (shape == BrickData.shape[index][2]) shape = BrickData.shape[index][1];
                else shape = BrickData.shape[index][0];
                break;
            case 4:
                if (shape == BrickData.shape[index][0]) shape = BrickData.shape[index][3];
                else if (shape == BrickData.shape[index][3]) shape = BrickData.shape[index][2];
                else if (shape == BrickData.shape[index][2]) shape = BrickData.shape[index][1];
                else shape = BrickData.shape[index][0];
                break;
            default:
                break;
        }
    }
}
