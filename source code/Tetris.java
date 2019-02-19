import java.net.URL;
import java.util.Arrays;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tetris extends Application {
    private int score = 0;
    private static boolean isOver = false;
    private boolean isPaused = false;

    static void setOver() {
        isOver = true;
    }

    private Board board;
    private StackPane stackPane;
    private MediaPlayer mediaPlayer;
    private Brick current = new Brick();
    private int nextBrick = (int)(Math.random()*7);

    @Override
    public void start(Stage stage) {
        try {
            // game initialization
            board = new Board();
            board.initBoard();
            stackPane = new StackPane();
            Scene scene = new Scene(stackPane, 380, 480);
            scene.setOnKeyPressed(new keyHandler());

            // timeline setup for auto dropping
            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            KeyFrame run = new KeyFrame(Duration.seconds(0.5), event -> {
                if (isOver || isPaused) return;
                board.moveDown(current);
                checkIfNew();
                setInterface();
            });
            timeline.getKeyFrames().add(run);
            timeline.play();

            // background music setup
            URL url = getClass().getResource("soundtrack.mp3");
            if (url == null) {
                System.out.println("media not found.");
                System.exit(0);
            }
            Media media = new Media(url.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setAutoPlay(true);

            stage.setTitle("TETRIS");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private class keyHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent e) {
            if (!isOver && !isPaused) {
                switch (e.getCode()) {
                    case DOWN:
                    case S:
                        board.moveDown(current);
                        checkIfNew();
                        setInterface();
                        break;
                    case LEFT:
                    case A:
                        board.moveLeft(current);
                        checkIfNew();
                        setInterface();
                        break;
                    case RIGHT:
                    case D:
                        board.moveRight(current);
                        checkIfNew();
                        setInterface();
                        break;
                    case UP:
                    case SHIFT:
                    case W:
                        board.rotate(current);
                        checkIfNew();
                        setInterface();
                        break;
                    case SPACE:
                        board.moveStraightDown(current);
                        checkIfNew();
                        setInterface();
                        break;
                    case P:
                        pause();
                        setInterface();
                        break;
                    default:
                        board.setBrick(current);
                        setInterface();
                        break;
                }
            }
            else if (!isOver && e.getCode() == KeyCode.P) pause();
        }
    }

    /**
     * check current game status
     * - pop up an alert window if the game is over
     * - if the game shall continue and the board request a new brick
     *   determine whether there are full lines, increase the score, and create a new brick
     */
    private void checkIfNew() {
        if (isOver) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("GAME OVER");
            alert.setHeaderText(null);
            alert.setContentText("Game over! Your score is: " + score);
            alert.show();
            alert.setOnHidden(e -> Platform.exit());
            mediaPlayer.stop();
        }
        if (board.requestNewBrick() && !isOver) {
            board.resetRequest();
            for (int i = 3; i < board.getHeight(); i++) {
                int counter = 0;
                int[] temp = Arrays.copyOfRange(board.getBoardArray()[i],1,11);
                for (int j = 0; j < temp.length; j++) if (temp[j] != 0) counter++;
                if (counter == 10) {
                    board.deleteLine(i);
                    score += 100;
                }
            }
            current = new Brick(nextBrick);
            nextBrick = (int)(Math.random()*7);
            board.setBrick(current);
        }
    }

    /**
     * set the Graphical User Interface
     */
    private void setInterface() {
        stackPane.getChildren().clear();
        stackPane.setStyle("-fx-background-image: url('background.png'); -fx-background-repeat: no-repeat; -fx-background-size: cover;");
        stackPane.setPadding(new Insets(36));

        // tetris main display
        Group display = board.display();

        stackPane.getChildren().add(display);
        StackPane.setAlignment(display, Pos.TOP_LEFT);

        // next brick preview
        Group preview = new Group();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int data = BrickData.shape[nextBrick][0][i][j];
                if (data != 0) {
                    Rectangle rectangle = new Rectangle(20 * j, 20 * i, 20, 20);
                    rectangle.setStroke(Color.WHITE);
                    rectangle.setFill(BrickData.color[data]);
                    preview.getChildren().add(rectangle);
                }
            }
        }

        // score board display
        Text text = new Text("score");
        text.setTextAlignment(TextAlignment.RIGHT);
        text.setFont(Font.font ("Verdana", 12));
        Text result = new Text(Integer.toString(score));
        result.setTextAlignment(TextAlignment.RIGHT);
        result.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 16));

        // instruction
        Text instruction;
        Text pauseInstruction = new Text("press P\nto pause ");
        Text resumeInstruction = new Text("press P\nto resume");
        instruction = isPaused? resumeInstruction: pauseInstruction;
        instruction.setTextAlignment(TextAlignment.LEFT);
        instruction.setFont(Font.font("monospace", FontWeight.SEMI_BOLD, 11));

        VBox score = new VBox(2);
        score.getChildren().addAll(text, result);
        score.setAlignment(Pos.TOP_RIGHT);
        VBox info = new VBox(40);
        info.getChildren().addAll(score, preview, instruction);
        info.setAlignment(Pos.TOP_RIGHT);
        stackPane.getChildren().add(info);
    }

    /**
     * pause (resume) the game, background music, and keyboard control
     */
    private void pause() {
        if (isPaused) mediaPlayer.play();
        else mediaPlayer.pause();
        // pause toggle
        isPaused = !isPaused;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
