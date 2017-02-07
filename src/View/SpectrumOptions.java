package View;

import Spectrums.LaurentSpectrum;
import Spectrums.MagSpectrum;
import Spectrums.Spectrum;
import Spectrums.WSpectrum;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.io.File;

/*
 * Created by Arthur Geneau on 2017-02-07.
 */

public class SpectrumOptions {

    private ImageView cog;
    private VBox menu;
    private Spectrum[] spectrums;
    private Spectrum current;
    private MediaPlayer mediaPlayer;
    private Group visualizerGroup;
    private Group group;
    private int elems;
    private boolean visible;

    public SpectrumOptions () {
        visible = false;
        elems = 3;
        spectrums = new Spectrum[elems];
        spectrums[0] = new WSpectrum();
        spectrums[1] = new MagSpectrum();
        spectrums[2] = new LaurentSpectrum();

        current = spectrums[0];

        group = new Group();

        initializeCog();
        initializeVBox();

        mediaPlayer = null;
        visualizerGroup = null;
    }

    private void initializeCog() {
        String path = this.getClass().getResource("../Resources/Images/cog.png").toString().replace("file:/", "");
        File file1 = new File(path);
        Image image = new Image(file1.toURI().toString());
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

        cog.setOnMouseClicked(event -> {
            visible = !visible;
            menu.setVisible(visible);
        });

        group.getChildren().add(cog);
    }

    private void initializeVBox() {
        menu = new VBox();

        for(int i = 0; i<elems; i++) {
            Spectrum s = spectrums[i];
            Button b = new Button(spectrums[i].getName());

            b.setOnAction(event -> {
                current = s;
                if(mediaPlayer != null) {
                    changeListener();
                }
                if(visualizerGroup != null) {
                    visualizerGroup.getChildren().clear();
                    visualizerGroup.getChildren().add(current.getGroup());
                }
                visible = !visible;
                menu.setVisible(visible);
            });

            menu.getChildren().add(b);
        }

        menu.setPrefSize(70,100);
        menu.setTranslateY(63);

        menu.setVisible(visible);

        group.getChildren().add(menu);
    }

    public void setVisualizerGroup(Group group) {
        visualizerGroup = group;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        changeListener();
    }

    private void changeListener() {
        mediaPlayer.setAudioSpectrumListener(current);
    }

    public Spectrum getSpectrum() {
        return current;
    }

    public void updateSizes(double width, double height) {
        cog.setTranslateX(width-60);
        menu.setTranslateX(width-80);
    }

    public Group getGroup() {
        return group;
    }

}
