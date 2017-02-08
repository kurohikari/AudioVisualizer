package View;

/*
 * Created by Arthur Geneau on 2017-01-31.
 */

import Spectrums.LaurentSpectrum;
import Spectrums.MagSpectrum;
import Spectrums.Spectrum;
import Spectrums.WSpectrum;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AudioVisualizer extends Application {

    private SpectrumOptions spectrumOptions;
    private Spectrum spectrum;
    private MediaPlayer audioMediaPlayer;
    private ImageView backgroundView;
    private GridPane buttonsGridPane;
    private GridPane viewGridPane;
    private Group ASLGroup;
    private Stage stage;
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
        group.getChildren().add(spectrumOptions.getGroup());
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

        ASLGroup = new Group();

        initBackground();
        initOptions();
        initButtonsGridPane();
        initViewGridPane();

        updateSize();
    }

    private void initBackground() {
        String path = this.getClass().getResource("../Resources/Images/background3.jpg").toString().replace("file:/", "");
        File file1 = new File(path);
        Image backgroundImage = new Image(file1.toURI().toString());
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
    }

    private void initOptions() {
        spectrumOptions = new SpectrumOptions();
        spectrumOptions.setVisualizerGroup(ASLGroup);

    }

    private void updateSize() {
        backgroundView.setFitWidth(sceneWidth);
        backgroundView.setFitHeight(sceneHeight);

        spectrumOptions.updateSizes(sceneWidth, sceneHeight);

        buttonsGridPane.getColumnConstraints().clear();
        ColumnConstraints BcolumnConstraints = new ColumnConstraints(sceneWidth/2);
        buttonsGridPane.getColumnConstraints().addAll(BcolumnConstraints, BcolumnConstraints);

        viewGridPane.getRowConstraints().clear();
        viewGridPane.getColumnConstraints().clear();
        RowConstraints VrowConstraints1 = new RowConstraints(sceneHeight-100);
        RowConstraints VrowConstraints2 = new RowConstraints(100);
        ColumnConstraints VcolumnConstraints = new ColumnConstraints(sceneWidth);
        viewGridPane.getRowConstraints().addAll(VrowConstraints1, VrowConstraints2);
        viewGridPane.getColumnConstraints().add(VcolumnConstraints);
    }

    private void initButtonsGridPane() {
        GridPane gridPane = new GridPane();

        String path = this.getClass().getResource("../Resources/Images/play.png").toString().replace("file:/", "");
        File file1 = new File(path);
        Image image1 = new Image(file1.toURI().toString());
        ImageView playButton = new ImageView(image1);
        playButton.setFitWidth(30);
        playButton.setFitHeight(30);
        DropShadow playShadow = new DropShadow();
        playShadow.setColor(Color.WHITE);
        playShadow.setSpread(0.5);
        playShadow.setRadius(10);
        playButton.setEffect(playShadow);
        playButton.setOnMouseEntered(event-> playShadow.setRadius(20));
        playButton.setOnMouseExited(event -> playShadow.setRadius(10));
        playButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer != null) {
                audioMediaPlayer.play();
            }
        });

        String path2 = this.getClass().getResource("../Resources/Images/pause.png").toString().replace("file:/", "");
        File file2 = new File(path2);
        Image image2 = new Image(file2.toURI().toString());
        ImageView pauseButton = new ImageView(image2);
        pauseButton.setTranslateX(40);
        pauseButton.setFitWidth(30);
        pauseButton.setFitHeight(30);
        DropShadow pauseShadow = new DropShadow();
        pauseShadow.setColor(Color.WHITE);
        pauseShadow.setSpread(0.5);
        pauseShadow.setRadius(10);
        pauseButton.setEffect(pauseShadow);
        pauseButton.setOnMouseEntered(event -> pauseShadow.setRadius(20));
        pauseButton.setOnMouseExited(event -> pauseShadow.setRadius(10));
        pauseButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer!= null) {
                audioMediaPlayer.pause();
            }
        });

        String path3 = this.getClass().getResource("../Resources/Images/stop.png").toString().replace("file:/", "");
        File file3 = new File(path3);
        Image image3 = new Image(file3.toURI().toString());
        ImageView stopButton = new ImageView(image3);
        stopButton.setTranslateX(80);
        stopButton.setFitWidth(30);
        stopButton.setFitHeight(30);
        DropShadow stopShadow = new DropShadow();
        stopShadow.setColor(Color.WHITE);
        stopShadow.setSpread(0.5);
        stopShadow.setRadius(10);
        stopButton.setEffect(stopShadow);
        stopButton.setOnMouseEntered(event -> stopShadow.setRadius(20));
        stopButton.setOnMouseExited(event -> stopShadow.setRadius(10));
        stopButton.setOnMouseClicked(event -> {
            if(audioMediaPlayer!= null) {
                audioMediaPlayer.stop();
            }
        });

        Group audioGroup = new Group();
        audioGroup.getChildren().addAll(playButton, pauseButton, stopButton);
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
                    spectrum = spectrumOptions.getSpectrum();
                    ASLGroup.getChildren().clear();
                    ASLGroup.getChildren().add(spectrum.getGroup());
                }

                spectrumOptions.setMediaPlayer(audioMediaPlayer);
                audioMediaPlayer.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        gridPane.add(musicButton, 1,0);
        gridPane.setHalignment(musicButton, HPos.CENTER);
        gridPane.setValignment(musicButton, VPos.BOTTOM);

        buttonsGridPane = gridPane;
    }

    private void initViewGridPane() {
        GridPane gridPane = new GridPane();

        gridPane.add(ASLGroup, 0,0);
        gridPane.setHalignment(ASLGroup, HPos.LEFT);
        gridPane.setValignment(ASLGroup, VPos.CENTER);

        gridPane.add(buttonsGridPane, 0,1);
        gridPane.setHalignment(buttonsGridPane, HPos.CENTER);
        gridPane.setValignment(buttonsGridPane, VPos.BOTTOM);

        viewGridPane = gridPane;
    }
}
