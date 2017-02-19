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

        Image image1 = new Image(getClass().getClassLoader().getResourceAsStream("Resources/Images/play.png"));
        ImageView playButton = new ImageView(image1);
        playButton.setTranslateX(40);
        playButton.setFitWidth(30);
        playButton.setFitHeight(30);
        DropShadow playShadow = new DropShadow(10, Color.WHITE);
        playShadow.setSpread(0.5);
        playButton.setEffect(playShadow);
        playButton.setOnMouseEntered(event-> playShadow.setColor(Color.GRAY));
        playButton.setOnMouseExited(event -> playShadow.setColor(Color.WHITE));
        playButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer != null) {
                audioMediaPlayer.play();
            }
        });

        Image image2 = new Image(getClass().getClassLoader().getResourceAsStream("Resources/Images/pause.png"));
        ImageView pauseButton = new ImageView(image2);
        pauseButton.setTranslateX(80);
        pauseButton.setFitWidth(30);
        pauseButton.setFitHeight(30);
        DropShadow pauseShadow = new DropShadow(10, Color.WHITE);
        pauseShadow.setSpread(0.5);
        pauseButton.setEffect(pauseShadow);
        pauseButton.setOnMouseEntered(event -> pauseShadow.setColor(Color.GRAY));
        pauseButton.setOnMouseExited(event -> pauseShadow.setColor(Color.WHITE));
        pauseButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer!= null) {
                audioMediaPlayer.pause();
            }
        });

        Image image3 = new Image(getClass().getClassLoader().getResourceAsStream("Resources/Images/stop.png"));
        ImageView stopButton = new ImageView(image3);
        stopButton.setTranslateX(120);
        stopButton.setFitWidth(30);
        stopButton.setFitHeight(30);
        DropShadow stopShadow = new DropShadow(10, Color.WHITE);
        stopShadow.setSpread(0.5);
        stopButton.setEffect(stopShadow);
        stopButton.setOnMouseEntered(event -> stopShadow.setColor(Color.GRAY));
        stopButton.setOnMouseExited(event -> stopShadow.setColor(Color.WHITE));
        stopButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer!= null) {
                audioMediaPlayer.stop();
            }
        });

        Image image4 = new Image(getClass().getClassLoader().getResourceAsStream("Resources/Images/loop.png"));
        ImageView loopButton = new ImageView(image4);
        loopButton.setTranslateX(160);
        loopButton.setFitWidth(30);
        loopButton.setFitHeight(30);
        DropShadow loopShadow = new DropShadow(10,Color.WHITE);
        loopShadow.setSpread(0.5);
        loopButton.setEffect(loopShadow);
        loopButton.setOnMouseEntered(event -> loopShadow.setColor(Color.GRAY));
        loopButton.setOnMouseExited(event -> loopShadow.setColor(Color.WHITE));
        loopButton.setOnMouseClicked(event -> {
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
