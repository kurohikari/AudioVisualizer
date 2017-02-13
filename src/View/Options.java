package View;

import Spectrums.LaurentSpectrum;
import Spectrums.MagSpectrum;
import Spectrums.Spectrum;
import Spectrums.WSpectrum;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Created by Arthur Geneau on 2017-02-10.
 */
public class Options {

    private ImageView cog;
    private Spectrum[] spectrums;
    private Spectrum current;
    private MediaPlayer mediaPlayer;
    private Color nextColor;
    private Spectrum nextSpectrum;
    private Group visualizerGroup;
    private Group group;
    private Stage stage;
    private int elems;

    public Options() {

        elems = 3;
        spectrums = new Spectrum[elems];
        spectrums[0] = new WSpectrum();
        spectrums[1] = new MagSpectrum();
        spectrums[2] = new LaurentSpectrum();

        current = spectrums[0];

        group = new Group();

        createStage();
        initializeCog();

        mediaPlayer = null;
        visualizerGroup = null;
    }

    void createStage() {
        stage = new Stage();
        stage.setTitle("Options");

        TabPane pane = new TabPane();
        Tab OtherTab = new Tab("Other");
        OtherTab.setContent(new Rectangle(200,200,Color.AQUA));
        OtherTab.setClosable(false);
        pane.getTabs().addAll(getSpectrumTab(), OtherTab);

        Scene scene = new Scene(pane, 300, 300);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    private void initializeCog() {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("Resources/Images/cog.png"));
        cog = new ImageView(image);
        cog.setPreserveRatio(false);
        cog.setTranslateY(10);
        cog.setFitWidth(50);
        cog.setFitHeight(50);

        cog.setOnMouseEntered(event -> {
            DropShadow d = new DropShadow();
            d.setSpread(0.5);
            d.setRadius(10);
            d.setColor(Color.WHITE);

            cog.setEffect(d);
        });

        cog.setOnMouseExited(event -> cog.setEffect(null));

        cog.setOnMouseClicked(event -> stage.show());

        group.getChildren().add(cog);
    }

    private Tab getSpectrumTab() {
        Tab SpectrumTab = new Tab("Spectrum");
        SpectrumTab.setClosable(false);

        GridPane grid = new GridPane();
        Label SpectrumLabel = new Label("Spectrum");
        grid.add(SpectrumLabel, 0,0);
        Label ColorLabel = new Label("Color");
        grid.add(ColorLabel, 1, 0);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(100, 200);
        VBox vBox = new VBox();
        for(int i = 0; i<elems; i++) {
            Spectrum s = spectrums[i];
            Button b = new Button(spectrums[i].getName());
            vBox.getChildren().add(b);
            b.setOnAction(event -> nextSpectrum = s);
        }
        scrollPane.setContent(vBox);
        grid.add(scrollPane, 0,1);

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> nextColor = colorPicker.getValue());
        grid.add(colorPicker, 1, 1);

        Button applyButton = new Button("Apply");
        applyButton.setOnAction(event -> {
            mediaPlayer.setAudioSpectrumNumBands(128);
            if(mediaPlayer != null) {
                if(nextSpectrum != null) {
                    current = nextSpectrum;
                    changeListener();
                    nextSpectrum = null;
                }
                if(nextColor != null) {
                    for(Spectrum s : spectrums) {
                        s.setColor(nextColor);
                    }
                    nextColor = null;
                }
                if(visualizerGroup != null) {
                    visualizerGroup.getChildren().clear();
                    visualizerGroup.getChildren().add(current.getGroup());
                }
            }
            stage.close();
        });
        grid.add(applyButton, 1,2);
        grid.setHalignment(applyButton, HPos.RIGHT);

        SpectrumTab.setContent(grid);
        return SpectrumTab;
    }

    public void setVisualizerGroup(Group group) {
        visualizerGroup = group;
    }

    public Spectrum getSpectrum() {
        return current;
    }

    private void changeListener() {
        mediaPlayer.setAudioSpectrumListener(current);
        current.setMediaPlayer(mediaPlayer);
    }

    public void setSeekBar(SeekBar s) {
        for(Spectrum spectrum : spectrums) {
            spectrum.setSeekBar(s);
        }
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        changeListener();
    }

    public Group getGroup() {
        return group;
    }

}
