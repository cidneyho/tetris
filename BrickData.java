/**
 * BrickData interface contains the bricks' colors and shapes
 * - the Color type array indicates the color of the background, brick I, J, L, O, S, T, and Z accordingly
 * - the int type 4-dimensional array indicates
 *      1. the brick index (type)
 *      2. the shape a specific brick refers to
 *      3. 4. the shape data in the format of a 2-dimensional matrix
 */ 

import javafx.scene.paint.Color;

public interface BrickData {
    Color[] color = {Color.WHITESMOKE, Color.SKYBLUE, Color.ROYALBLUE, Color.LIGHTSALMON, Color.KHAKI, Color.YELLOWGREEN, Color.MEDIUMPURPLE, Color.PALEVIOLETRED};
    int[][][][] shape = {
            // Brick I
            {
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {1, 1, 1, 1}
                    },
                    {
                            {0, 1, 0, 0},
                            {0, 1, 0, 0},
                            {0, 1, 0, 0},
                            {0, 1, 0, 0}
                    }
            },

            // Brick J
            {
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {2, 2, 2, 0},
                            {0, 0, 2, 0}
                    },
                    {

                            {0, 0, 0, 0},
                            {0, 0, 2, 0},
                            {0, 0, 2, 0},
                            {0, 2, 2, 0}
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {0, 2, 0, 0},
                            {0, 2, 2, 2}
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 2, 2, 0},
                            {0, 2, 0, 0},
                            {0, 2, 0, 0}
                    }
            },

            // Brick L
            {
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {0, 3, 3, 3},
                            {0, 3, 0, 0},
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 3, 3, 0},
                            {0, 0, 3, 0},
                            {0, 0, 3, 0}
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {0, 0, 3, 0},
                            {3, 3, 3, 0}
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 3, 0, 0},
                            {0, 3, 0, 0},
                            {0, 3, 3, 0}
                    }
            },

            // Brick O
            {
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {0, 4, 4, 0},
                            {0, 4, 4, 0}
                    }
            },

            // Brick S
            {
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {0, 5, 5, 0},
                            {5, 5, 0, 0}
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 5, 0, 0},
                            {0, 5, 5, 0},
                            {0, 0, 5, 0}
                    }
            },

            // Brick T
            {
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {6, 6, 6, 0},
                            {0, 6, 0, 0}
                    },
                    {

                            {0, 0, 0, 0},
                            {0, 0, 6, 0},
                            {0, 6, 6, 0},
                            {0, 0, 6, 0}
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {0, 6, 0, 0},
                            {6, 6, 6, 0}
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 6, 0, 0},
                            {0, 6, 6, 0},
                            {0, 6, 0, 0}
                    }
            },

            // Brick Z
            {
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {7, 7, 0, 0},
                            {0, 7, 7, 0}
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 0, 7, 0},
                            {0, 7, 7, 0},
                            {0, 7, 0, 0}
                    }
            }
    };
}
