package View;

/*
 * Created by Arthur Geneau on 2017-01-31.
 */

import Spectrums.MagSpectrum;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AudioVisualizer extends Application {

    private MagSpectrum ASL;
    private Media audioMedia;
    private MediaPlayer audioMediaPlayer;
    private ImageView backgroundView;
    private GridPane buttonsGridPane;
    private GridPane viewGridPane;
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
        sceneWidth = 680;
        sceneHeight = 360;

        initBackground();
        initAudio();
        initButtonsGridPane();
        initViewGridPane();

        updateSize();
    }

    private void initBackground() {
        String path = this.getClass().getResource("../Resources/Images/background2.jpg").toString().replace("file:/", "");
        File file1 = new File(path);
        Image backgroundImage = new Image(file1.toURI().toString());
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
    }

    private void initAudio() {
        String path = this.getClass().getResource("../Resources/Music/Ahxello-Frisbee.mp3").toString().replace("file:/", "");
        File file = new File(path);
        audioMedia = new Media(file.toURI().toASCIIString());
        audioMediaPlayer = new MediaPlayer(audioMedia);
        audioMediaPlayer.setAudioSpectrumInterval(0.05);
        ASL = new MagSpectrum(audioMediaPlayer, 0, 300);
        ASL.setObjWidth(2);

        try {
            audioMediaPlayer.setAudioSpectrumListener(ASL);
            audioMediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSize() {
        backgroundView.setFitWidth(sceneWidth);
        backgroundView.setFitHeight(sceneHeight);

        buttonsGridPane.getColumnConstraints().clear();
        ColumnConstraints BcolumnConstraints = new ColumnConstraints(sceneWidth/4);
        buttonsGridPane.getColumnConstraints().addAll(BcolumnConstraints, BcolumnConstraints, BcolumnConstraints, BcolumnConstraints);

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

        Button playButton = new Button("play");
        playButton.setOnAction(event -> audioMediaPlayer.play());
        gridPane.add(playButton, 0,0);
        GridPane.setHalignment(playButton, HPos.CENTER);

        Button pauseButton = new Button("pause");
        pauseButton.setOnAction(event -> audioMediaPlayer.pause());
        gridPane.add(pauseButton, 1,0);
        GridPane.setHalignment(pauseButton, HPos.CENTER);

        Button stopButton = new Button("stop");
        stopButton.setOnAction(event -> audioMediaPlayer.stop());
        gridPane.add(stopButton, 2,0);
        GridPane.setHalignment(stopButton, HPos.CENTER);

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
                audioMediaPlayer.stop();
                audioMediaPlayer.dispose();
                audioMediaPlayer = new MediaPlayer(media);
                ASL.setMediaPlayer(audioMediaPlayer);
                audioMediaPlayer.setAudioSpectrumListener(ASL);
                audioMediaPlayer.play();
            } catch (Exception e) {

            }
        });
        gridPane.add(musicButton, 3,0);
        GridPane.setHalignment(musicButton, HPos.CENTER);

        buttonsGridPane = gridPane;
    }

    private void initViewGridPane() {
        GridPane gridPane = new GridPane();

        Group ASLGroup = ASL.getGroup();
        gridPane.add(ASLGroup, 0,0);
        GridPane.setHalignment(ASLGroup, HPos.CENTER);

        gridPane.add(buttonsGridPane, 0,1);
        GridPane.setHalignment(buttonsGridPane, HPos.CENTER);

        viewGridPane = gridPane;
    }
}
