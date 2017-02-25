package View;

/*
 * Created by Arthur Geneau on 2017-01-31.
 */

import Spectrums.Spectrum;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/*
 * Main View Class
 * TODO Cleanup and reorganize
 */
public class AudioVisualizer extends Application {

    private Options options;
    private SeekBar seekBar;
    private Spectrum spectrum;
    private MediaPlayer audioMediaPlayer;
    private ImageView backgroundView;
    private GridPane buttonsGridPane;
    private GridPane viewGridPane;
    private Group ASLGroup;
    private Stage stage;
    private boolean looping;
    private double sceneWidth;
    private double sceneHeight;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        initialize();
        primaryStage.setTitle("AudioVisualizer");

        Group group = new Group();
        group.getChildren().add(backgroundView);
        group.getChildren().add(viewGridPane);
        group.getChildren().add(options.getGroup());
        Scene scene = new Scene(group, sceneWidth,sceneHeight);
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            sceneWidth = (double) newValue;
            updateSize();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            sceneHeight = (double) newValue;
            updateSize();
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initialize() {
        sceneWidth = 1280;
        sceneHeight = 720;

        looping = false;

        ASLGroup = new Group();

        initBackground();
        initOptions();
        initButtonsGridPane();
        initViewGridPane();
        initSeekBar();

        updateSize();
    }

    private void initBackground() {
        Image backgroundImage = new Image(getClass().getClassLoader().getResourceAsStream("Resources/Images/background3.jpg"));
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
    }

    private void initOptions() {
        options = new Options();
        options.setVisualizerGroup(ASLGroup);

    }

    private void updateSize() {
        backgroundView.setFitWidth(sceneWidth);
        backgroundView.setFitHeight(sceneHeight);

        seekBar.resize(sceneWidth);

        buttonsGridPane.getColumnConstraints().clear();
        ColumnConstraints BcolumnConstraints = new ColumnConstraints(sceneWidth/2);
        buttonsGridPane.getColumnConstraints().addAll(BcolumnConstraints, BcolumnConstraints);

        viewGridPane.getRowConstraints().clear();
        viewGridPane.getColumnConstraints().clear();
        RowConstraints VrowConstraints1 = new RowConstraints(sceneHeight-100);
        ColumnConstraints VcolumnConstraints = new ColumnConstraints(sceneWidth);
        viewGridPane.getRowConstraints().addAll(VrowConstraints1);
        viewGridPane.getColumnConstraints().add(VcolumnConstraints);
    }

    void initSeekBar() {
        seekBar = new SeekBar();
        options.setSeekBar(seekBar);
        viewGridPane.add(seekBar.getGroup(), 0,1);
    }

    private void initButtonsGridPane() {
        GridPane gridPane = new GridPane();

        // Instantiates play button
        ImageView playButton = makeAudioButton("Resources/Images/play.png", 40);
        playButton.setOnMouseEntered(event-> {
            DropShadow playShadow = (DropShadow) playButton.getEffect();
            // Sets shadow to gray when entering the play button
            playShadow.setColor(Color.GRAY);
        });
        playButton.setOnMouseExited(event -> {
            DropShadow playShadow = (DropShadow) playButton.getEffect();
            // Sets shadow to white when leaving the play button
            playShadow.setColor(Color.WHITE);
        });
        playButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer != null) {
                audioMediaPlayer.play();
            }
        });

        // Instantiates pause button
        ImageView pauseButton = makeAudioButton("Resources/Images/pause.png", 80);
        pauseButton.setOnMouseEntered(event -> {
            DropShadow pauseShadow = (DropShadow) pauseButton.getEffect();
            // Sets shadow to gray when entering the pause button
            pauseShadow.setColor(Color.GRAY);
        });
        pauseButton.setOnMouseExited(event -> {
            DropShadow pauseShadow = (DropShadow) pauseButton.getEffect();
            // Sets shadow to white when leaving the pause button
            pauseShadow.setColor(Color.WHITE);
        });
        pauseButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer!= null) {
                audioMediaPlayer.pause();
            }
        });

        // Instantiates stop button
        ImageView stopButton = makeAudioButton("Resources/Images/stop.png", 120);
        stopButton.setOnMouseEntered(event -> {
            DropShadow stopShadow = (DropShadow) stopButton.getEffect();
            // Sets shadow to gray when entering the stop button
            stopShadow.setColor(Color.GRAY);
        });
        stopButton.setOnMouseExited(event -> {
            DropShadow stopShadow = (DropShadow) stopButton.getEffect();
            // Sets shadow to white when leaving the stop button
            stopShadow.setColor(Color.WHITE);
        });
        stopButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer!= null) {
                audioMediaPlayer.stop();
            }
        });

        // Instantiates loop button
        ImageView loopButton = makeAudioButton("Resources/Images/loop.png", 160);
        loopButton.setOnMouseEntered(event -> {
            DropShadow loopShadow = (DropShadow) loopButton.getEffect();
            // Sets the shadow of the button to gray when entering the button and looping is disables, else sets it to dark blue when looping is enabled
            if(!looping) {
                loopShadow.setColor(Color.GRAY);
            } else {
                loopShadow.setColor(Color.DARKBLUE);
            }
        });
        loopButton.setOnMouseExited(event -> {
            DropShadow loopShadow = (DropShadow) loopButton.getEffect();
            // Sets the shadow of the button to white when leaving the button and looping is disables, else sets it to blue when looping is enabled
            if(!looping) {
                loopShadow.setColor(Color.WHITE);
            } else {
                loopShadow.setColor(Color.BLUE);
            }
        });
        loopButton.setOnMouseClicked(event -> {
            DropShadow loopShadow = (DropShadow) loopButton.getEffect();
            if(audioMediaPlayer!= null) {
                if(!looping) {
                    // Sets cycle count to infinite if looping was false
                    audioMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    looping = true;
                    loopShadow.setColor(Color.BLUE);
                } else {
                    // Sets cycle count to a value such that the song does not repeat
                    audioMediaPlayer.setCycleCount(1);
                    looping = false;
                    loopShadow.setColor(Color.GRAY);
                }
            }
        });

        Group audioGroup = new Group();
        Rectangle container = new Rectangle(150,100,Color.TRANSPARENT);
        audioGroup.getChildren().addAll(container, playButton, pauseButton, stopButton, loopButton);
        gridPane.add(audioGroup, 0,0);
        gridPane.setHalignment(stopButton, HPos.CENTER);
        gridPane.setValignment(stopButton, VPos.BOTTOM);


        Button musicButton = new Button("Choose Audio");
        musicButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a music file.");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                    new FileChooser.ExtensionFilter("WAV", "*.wav")
            );
            try {
                File file = fileChooser.showOpenDialog(stage);
                Media media = new Media(file.toURI().toASCIIString());
                if(audioMediaPlayer != null) {
                    audioMediaPlayer.stop();
                    audioMediaPlayer.dispose();
                }
                audioMediaPlayer = new MediaPlayer(media);
                audioMediaPlayer.setAudioSpectrumInterval(0.05);
                if(spectrum == null) {
                    spectrum = options.getSpectrum();
                    ASLGroup.getChildren().clear();
                    ASLGroup.getChildren().add(spectrum.getGroup());
                }

                options.setMediaPlayer(audioMediaPlayer);
                seekBar.setMediaPlayer(audioMediaPlayer);
                audioMediaPlayer.setOnReady(() -> seekBar.setEnd(audioMediaPlayer.getMedia().getDuration().toMillis()));
                audioMediaPlayer.play();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        });
        gridPane.add(musicButton, 1,0);
        gridPane.setHalignment(musicButton, HPos.CENTER);
        gridPane.setValignment(musicButton, VPos.TOP);

        buttonsGridPane = gridPane;
    }

    /**
     * Method called to create the play, pause, stop and loop button
     * @return an ImageView with the right format to be a button
     */
    private ImageView makeAudioButton(String relPath, double xOffset) {
        // Creates the ImageView
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(relPath));
        ImageView button = new ImageView(image);

        // Sets parameters for the ImageView
        button.setTranslateX(xOffset);
        button.setFitWidth(30);
        button.setFitHeight(30);

        // Adds shadow effect to the ImageView
        DropShadow shadow = new DropShadow(10,Color.WHITE);
        shadow.setSpread(0.5);
        button.setEffect(shadow);

        return button;
    }

    private void initViewGridPane() {
        GridPane gridPane = new GridPane();

        gridPane.add(ASLGroup, 0,0);
        gridPane.setHalignment(ASLGroup, HPos.CENTER);
        gridPane.setValignment(ASLGroup, VPos.CENTER);

        gridPane.add(buttonsGridPane, 0,2);
        gridPane.setHalignment(buttonsGridPane, HPos.CENTER);
        gridPane.setValignment(buttonsGridPane, VPos.BOTTOM);

        viewGridPane = gridPane;
    }
}
