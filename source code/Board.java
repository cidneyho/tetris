/**
 * Board class is created to control and determine everything on the play board.
 * - we set our play board a 10 x 20 rectangle
 * - for the convenience of rotation, there are one additional column on the left and two additional columns on the right
 */ 

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board {
    private final int width = 13; // (+1) + (+2)
    private final int height = 23; // (+3)
    private int[][] boardArray = new int[height][width];
    private boolean requestNewBrick = false;

    public Board() {}

    public int getHeight() {
        return height;
    }

    public int[][] getBoardArray() {
        return boardArray;
    }

    // in order to store the data, we need an array presenting the board
    // initialize the array for board to all zero
    public void initBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                boardArray[i][j] = 0;
            }
        }
    }

    // change the boardArray data to rectangles in different colors
    public Group display() {
        // the size of squares on the GUI
        int size = 20;
        Group group = new Group();
        // the first 3 rows (0, 1, 2) do not need to convert to rectangle
        for (int i = 3; i < height; i++) {
            // column 0 and the 2 right most columns do not need to convert as well
            for (int j = 1; j < width - 2; j++) {
                // assign different colors according to brick types
                Rectangle rectangle = new Rectangle(size * j, size * i, size, size);
                rectangle.setStroke(Color.WHITE);
                rectangle.setFill(BrickData.color[boardArray[i][j]]);
                // put them into a group and return the group
                group.getChildren().add(rectangle);
            }
        }
        return group;
    }

    public void setBrick(Brick brick) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (brick.getShape()[i][j] != 0) {
                    boardArray[brick.getPosY() + i][brick.getPosX() + j] = brick.getShape()[i][j];
                }
            }
        }
    }

    /**
     * moving logic
     * (1) clean the brick data on the board to avoid interrupting the isFixed (isStuck) checking
     * (2) check the isFixed() condition (when operating vertically) or the isStuck()condition (horizontally)
     * (3) if the brick is vertically not fixed or horizontally not stuck:
     *     (3.1) moveLeft:  copy the brick data on the board one block left, update PosX -1
     *     (3.2) moveRight: copy the brick data on the board one block right, update PosX +1
     *     (3.3) moveDown:  copy the brick data on the board one block down, update PosY +1
     *     (3.4) moveStraightDown: copy the brick data one block down, update PosY +1, loop the isFixed() condition and
     *                             move until the brick is vertically fixed
     * (4) else if the brick is vertically fixed or horizontally stuck:
     *     (4.1) moveLeft/moveRight: copy the brick data to the original position on the board
     *     (4.2) moveDown/moveStraightDown: copy the brick data to the original position on the board and check the isDead() condition
     *
     * @param brick the corresponding brick for operation
     */

    public void moveLeft(Brick brick) {
        clearData(brick);
        if (!(isStuck(brick, "left"))) {
            for (int i = 0; i < 4; i++) {
                for(int j = 0; j < 4; j++) {
                    if (brick.getShape()[i][j] != 0){
                        boardArray[brick.getPosY() + i][brick.getPosX() + j - 1] = brick.getShape()[i][j];
                    }
                }
            }
            brick.addPosX(-1);
        }
        else recoverData(brick);
    }

    public void moveRight(Brick brick) {
        clearData(brick);
        if (!(isStuck(brick,"right"))) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (brick.getShape()[i][j] != 0) {
                        boardArray[brick.getPosY() + i][brick.getPosX() + j + 1] = brick.getShape()[i][j];
                    }
                }
            }
            brick.addPosX(+1);
        }
        else recoverData(brick);
    }

    public void moveDown(Brick brick) {
        clearData(brick);
        if (!(isFixed(brick))) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (brick.getShape()[i][j] != 0) {
                        boardArray[brick.getPosY() + i + 1][brick.getPosX() + j] = brick.getShape()[i][j];
                    }
                }
            }
            brick.addPosY(+1);
        }
        else {
            recoverData(brick);
            if (isDead()) Tetris.setOver();
        }
    }

    public void moveStraightDown(Brick brick) {
        clearData(brick);
        while (!(isFixed(brick))) brick.addPosY(+1);
        setBrick(brick);
        if (isDead()) Tetris.setOver();
    }

    /**
     * 1. clean the data on the board
     * 2. call Brick method "rotate" to change the shape of brick
     * 3. check whether the new shape is stuck or fixed
     *    (1) if new shape is stuck or fixed, then we reverse(undo the rotate) the brick
     *    (2) if it's not stuck or fixed, then set the brick data on the board array
     *
     * @param brick the corresponding brick for operation
     */
    public void rotate(Brick brick) {
        clearData(brick);
        brick.rotate();
        if ((rotateStuck(brick,"left")) || (rotateStuck(brick,"right")) || (rotateFixed(brick))) {
            brick.reverse();
        }
        setBrick(brick);
    }

    // check whether the brick reach the bottom so that it can't further move
    // check if fixed in the y-axis
    public boolean isFixed(Brick brick) {
        int i = brick.getPosY(), j = brick.getPosX();
        int message = -1;
        if (i == height - 4) message = 0; // (-3) + (-1)
        else if ((brick.getShape()[0][0]!=0 && boardArray[i+1][j]!=0) || (brick.getShape()[0][1]!=0 && boardArray[i+1][j+1]!=0) || (brick.getShape()[0][2]!=0 && boardArray[i+1][j+2]!=0) || (brick.getShape()[0][3]!=0 && boardArray[i+1][j+3]!=0)) message = 1;
        else if ((brick.getShape()[1][0]!=0 && boardArray[i+2][j]!=0) || (brick.getShape()[1][1]!=0 && boardArray[i+2][j+1]!=0) || (brick.getShape()[1][2]!=0 && boardArray[i+2][j+2]!=0) || (brick.getShape()[1][3]!=0 && boardArray[i+2][j+3]!=0)) message = 2;
        else if ((brick.getShape()[2][0]!=0 && boardArray[i+3][j]!=0) || (brick.getShape()[2][1]!=0 && boardArray[i+3][j+1]!=0) || (brick.getShape()[2][2]!=0 && boardArray[i+3][j+2]!=0) || (brick.getShape()[2][3]!=0 && boardArray[i+3][j+3]!=0)) message = 3;
        else if ((brick.getShape()[3][0]!=0 && boardArray[i+4][j]!=0) || (brick.getShape()[3][1]!=0 && boardArray[i+4][j+1]!=0) || (brick.getShape()[3][2]!=0 && boardArray[i+4][j+2]!=0) || (brick.getShape()[3][3]!=0 && boardArray[i+4][j+3]!=0)) message = 4;
        if (message >= 0) {
            // message is no longer -1, indicating the brick is fixed
            requestNewBrick = true;
            return true;
        }
        return false;
    }

    // check whether the brick reach the boundary on left or right
    // saying, the brick can still move, just can not further left or right
    public boolean isStuck(Brick brick, String direction) {
        int i = brick.getPosY(), j = brick.getPosX();
        int message = -1;
        if (direction == "right") {
            if (((brick.getShape() == BrickData.shape[1][2]) || (brick.getShape() == BrickData.shape[2][0]) || (brick.getShape() == BrickData.shape[0][0])) && (j == 7)) message = 0;
            else if ((brick.getShape() == BrickData.shape[0][1]) && (j==9)) message = 1;
            else if ((brick.getShape() != BrickData.shape[0][1]) && (j==8)) message = 2;
            else {
                if ((brick.getShape()[0][0]!=0 && boardArray[i][j+1]!=0) || (brick.getShape()[1][0]!=0 && boardArray[i+1][j+1]!=0) || (brick.getShape()[2][0]!=0 && boardArray[i+2][j+1]!=0) || (brick.getShape()[3][0]!=0 && boardArray[i+3][j+1]!=0)) message = 3;
                else if ((brick.getShape()[0][1]!=0 && boardArray[i][j+2]!=0) || (brick.getShape()[1][1]!=0 && boardArray[i+1][j+2]!=0) || (brick.getShape()[2][1]!=0 && boardArray[i+2][j+2]!=0) || (brick.getShape()[3][1]!=0 && boardArray[i+3][j+2]!=0)) message = 4;
                else if ((brick.getShape()[0][2]!=0 && boardArray[i][j+3]!=0) || (brick.getShape()[1][2]!=0 && boardArray[i+1][j+3]!=0) || (brick.getShape()[2][2]!=0 && boardArray[i+2][j+3]!=0) || (brick.getShape()[3][2]!=0 && boardArray[i+3][j+3]!=0)) message = 5;
                else if ((brick.getShape()[0][3]!=0 && boardArray[i][j+4]!=0) || (brick.getShape()[1][3]!=0 && boardArray[i+1][j+4]!=0) || (brick.getShape()[2][3]!=0 && boardArray[i+2][j+4]!=0) || (brick.getShape()[3][3]!=0 && boardArray[i+3][j+4]!=0)) message = 6;
            }
        }
        else {
            if (((brick.getShape() == BrickData.shape[0][0]) || (brick.getShape() == BrickData.shape[1][0]) || (brick.getShape() == BrickData.shape[2][2]) ||
                    (brick.getShape() == BrickData.shape[4][0]) || (brick.getShape() == BrickData.shape[5][0]) || (brick.getShape() == BrickData.shape[5][2]) || (brick.getShape() == BrickData.shape[6][0])) && (j == 1))
                message = 10;
            else if (j == 0) message = 11;
            else {
                if ((brick.getShape()[0][3]!=0 && boardArray[i][j+2]!=0) || (brick.getShape()[1][3]!=0 && boardArray[i+1][j+2]!=0) || (brick.getShape()[2][3]!=0 && boardArray[i+2][j+2]!=0) || (brick.getShape()[3][3]!=0 && boardArray[i+3][j+2]!=0)) message = 12;
                else if ((brick.getShape()[0][2]!=0 && boardArray[i][j+1]!=0) || (brick.getShape()[1][2]!=0 && boardArray[i+1][j+1]!=0) || (brick.getShape()[2][2]!=0 && boardArray[i+2][j+1]!=0) || (brick.getShape()[3][2]!=0 && boardArray[i+3][j+1]!=0)) message = 13;
                else if ((brick.getShape()[0][1]!=0 && boardArray[i][j] !=0) || (brick.getShape()[1][1]!=0 && boardArray[i+1][j]!=0) || (brick.getShape()[2][1]!=0 && boardArray[i+2][j]!=0) || (brick.getShape()[3][1]!=0 && boardArray[i+3][j]!=0)) message = 14;
                else if ((brick.getShape()[0][0]!=0 && boardArray[i][j-1] !=0) || (brick.getShape()[1][0]!=0 && boardArray[i+1][j-1]!=0) || (brick.getShape()[2][0]!=0 && boardArray[i+2][j-1]!=0) || (brick.getShape()[3][0]!=0 && boardArray[i+3][j-1]!=0)) message = 15;
            }
        }
        return message >= 0;
    }

    // called when rotating the brick, check if it is fixed on the board after rotation
    public boolean rotateFixed(Brick brick) {
        int i = brick.getPosY(), j = brick.getPosX();
        int message = -1;
        if (i == height - 4) message = 0; // (-3) + (-1)
        else if ((brick.getShape()[0][0]!=0 && boardArray[i][j]!=0) || (brick.getShape()[0][1]!=0 && boardArray[i][j+1]!=0) || (brick.getShape()[0][2]!=0 && boardArray[i][j+2]!=0) || (brick.getShape()[0][3]!=0 && boardArray[i][j+3]!=0)) message = 1;
        else if ((brick.getShape()[1][0]!=0 && boardArray[i+1][j]!=0) || (brick.getShape()[1][1]!=0 && boardArray[i+1][j+1]!=0) || (brick.getShape()[1][2]!=0 && boardArray[i+1][j+2]!=0) || (brick.getShape()[1][3]!=0 && boardArray[i+1][j+3]!=0)) message = 2;
        else if ((brick.getShape()[2][0]!=0 && boardArray[i+2][j]!=0) || (brick.getShape()[2][1]!=0 && boardArray[i+2][j+1]!=0) || (brick.getShape()[2][2]!=0 && boardArray[i+2][j+2]!=0) || (brick.getShape()[2][3]!=0 && boardArray[i+2][j+3]!=0)) message = 3;
        else if ((brick.getShape()[3][0]!=0 && boardArray[i+3][j]!=0) || (brick.getShape()[3][1]!=0 && boardArray[i+3][j+1]!=0) || (brick.getShape()[3][2]!=0 && boardArray[i+3][j+2]!=0) || (brick.getShape()[3][3]!=0 && boardArray[i+3][j+3]!=0)) message = 4;
        if (message >= 0) {
            requestNewBrick = true;
            return true;
        }
        return false;
    }

    // called when rotating the brick, check if the brick can continue to move (not stuck) after rotation
    public boolean rotateStuck(Brick brick, String direction) {
        int i = brick.getPosY(), j = brick.getPosX();
        int message = -1;
        if (direction == "right") {
            if (((brick.getShape() == BrickData.shape[1][2]) || (brick.getShape() == BrickData.shape[2][0]) || (brick.getShape() == BrickData.shape[0][0])) && (j == 8)) message = 0;
            else if ((brick.getShape() == BrickData.shape[0][1]) && (j==10)) message = 1;
            else if ((brick.getShape() != BrickData.shape[0][1]) && (j==9)) message = 2;
            else {
                if ((brick.getShape()[0][0]!=0 && boardArray[i][j]!=0) || (brick.getShape()[1][0]!=0 && boardArray[i+1][j]!=0) || (brick.getShape()[2][0]!=0 && boardArray[i+2][j]!=0) || (brick.getShape()[3][0]!=0 && boardArray[i+3][j]!=0)) message = 3;
                else if ((brick.getShape()[0][1]!=0 && boardArray[i][j+1]!=0) || (brick.getShape()[1][1]!=0 && boardArray[i+1][j+1]!=0) || (brick.getShape()[2][1]!=0 && boardArray[i+2][j+1]!=0) || (brick.getShape()[3][1]!=0 && boardArray[i+3][j+1]!=0)) message = 4;
                else if ((brick.getShape()[0][2]!=0 && boardArray[i][j+2]!=0) || (brick.getShape()[1][2]!=0 && boardArray[i+1][j+2]!=0) || (brick.getShape()[2][2]!=0 && boardArray[i+2][j+2]!=0) || (brick.getShape()[3][2]!=0 && boardArray[i+3][j+2]!=0)) message = 5;
                else if ((brick.getShape()[0][3]!=0 && boardArray[i][j+3]!=0) || (brick.getShape()[1][3]!=0 && boardArray[i+1][j+3]!=0) || (brick.getShape()[2][3]!=0 && boardArray[i+2][j+3]!=0) || (brick.getShape()[3][3]!=0 && boardArray[i+3][j+3]!=0)) message = 6;
            }
        }
        else {
            if (((brick.getShape() == BrickData.shape[0][0]) || (brick.getShape() == BrickData.shape[1][0]) || (brick.getShape() == BrickData.shape[2][2]) ||
                    (brick.getShape() == BrickData.shape[4][0]) || (brick.getShape() == BrickData.shape[5][0]) || (brick.getShape() == BrickData.shape[5][2]) || (brick.getShape() == BrickData.shape[6][0])) && (j == 0))
                message = 10;
            else if (j == -1) message = 11;
            else {
                if ((brick.getShape()[0][3]!=0 && boardArray[i][j+3]!=0) || (brick.getShape()[1][3]!=0 && boardArray[i+1][j+3]!=0) || (brick.getShape()[2][3]!=0 && boardArray[i+2][j+3]!=0) || (brick.getShape()[3][3]!=0 && boardArray[i+3][j+3]!=0)) message = 12;
                else if ((brick.getShape()[0][2]!=0 && boardArray[i][j+2]!=0) || (brick.getShape()[1][2]!=0 && boardArray[i+1][j+2]!=0) || (brick.getShape()[2][2]!=0 && boardArray[i+2][j+2]!=0) || (brick.getShape()[3][2]!=0 && boardArray[i+3][j+2]!=0)) message = 13;
                else if ((brick.getShape()[0][1]!=0 && boardArray[i][j+1] !=0) || (brick.getShape()[1][1]!=0 && boardArray[i+1][j+1]!=0) || (brick.getShape()[2][1]!=0 && boardArray[i+2][j+1]!=0) || (brick.getShape()[3][1]!=0 && boardArray[i+3][j+1]!=0)) message = 14;
                else if ((brick.getShape()[0][0]!=0 && boardArray[i][j] !=0) || (brick.getShape()[1][0]!=0 && boardArray[i+1][j]!=0) || (brick.getShape()[2][0]!=0 && boardArray[i+2][j]!=0) || (brick.getShape()[3][0]!=0 && boardArray[i+3][j]!=0)) message = 15;
            }
        }
        return message >= 0;
    }

    // check if the coming brick is out of bound
    public boolean isDead() {
        for (int i = 0; i < width; i++) {
            if (boardArray[3][i] != 0) return true;
        }
        return false;
    }

    // called when a whole row on the board is filled
    public void deleteLine(int row) {
        for (int i = 0; i < width; i++) boardArray[row][i] = 0;
        for (int i = row; i > 0 ; i--) {
            for (int j = 0; j < width; j++) {
                boardArray[i][j] = boardArray[i - 1][j];
            }
        }
    }

    // clean the data of the current brick on the board before any operations,
    // and check the isFixed (isStuck) condition afterwards
    public void clearData(Brick brick) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (brick.getShape()[i][j] != 0) {
                    boardArray[brick.getPosY() + i][brick.getPosX() + j] = 0;
                }
            }
        }
    }

    // called to rewrite the cleared (original) data to the board upon illegal operations
    public void recoverData(Brick brick) {
        setBrick(brick);
    }

    // return a boolean to respond to whether a new brick is needed upon isFixed (isStuck) condition
    public boolean requestNewBrick() {
        return requestNewBrick;
    }

    public void resetRequest() {
        requestNewBrick = false;
    }
}
