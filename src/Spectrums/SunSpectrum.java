package Spectrums;

import View.SeekBar;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

/**
 * Created by Arthur Geneau on 2017-02-19.
 */
public class SunSpectrum extends Spectrum {

    private MediaPlayer audioMediaPlayer;
    private SeekBar seekBar;
    private Group group;
    private Line[] lines;
    private Circle innerCircle;
    private Circle outerCircle;
    private String name;

    /**
     * Constructor for the SunSpectrum Object
     */
    public SunSpectrum() {
        name = "SunSpectrum";
        group = new Group();
        innerCircle = new Circle();
        innerCircle.setRadius(100);
        innerCircle.setStroke(Color.YELLOW);
        innerCircle.setFill(Color.TRANSPARENT);
        innerCircle.setCenterX(0);
        group.getChildren().add(innerCircle);
        outerCircle = new Circle();
        outerCircle.setCenterX(0);
        outerCircle.setRadius(281);
        outerCircle.setStroke(Color.TRANSPARENT);
        outerCircle.setFill(Color.TRANSPARENT);
        group.getChildren().add(outerCircle);
        lines = new Line[2160];
        for(int i = 0; i<2160; i++) {
            Line line = new Line(100,0,101,0);
            line.setStroke(Color.WHITE);
            line.getTransforms().add(new Rotate(i/6.0));
            lines[i] = line;
            group.getChildren().add(lines[i]);
        }
    }

    /**
     * Getter method for the group containing the audio spectrum
     * @return the group containing the audio spectrum
     */
    @Override
    public Group getGroup() {
        return group;
    }

    /**
     * Setter method for the color of the spectrum
     * @param color the new color of the spectrum
     */
    @Override
    public void setColor(Color color) {
        for(Line line : lines) {
            line.setStroke(color);
        }
    }

    /**
     * Getter method for the name of the spectrum object
     * @return the name of the spectrum object
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Setter Method for the Media Player
     * @param mediaPlayer the Media Player
     */
    @Override
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        // Set the audioMediaPlayer to the mediaPlayer
        audioMediaPlayer = mediaPlayer;
        // Sets number of audio bands (After testing, I found out you cannot set it to more than 1024, else the magnitudes will all be minimum)
        audioMediaPlayer.setAudioSpectrumNumBands(1024);
    }

    /**
     * Setter method for the corresponding seek bar
     * @param seekBar the corresponding seek bar
     */
    @Override
    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    /**
     * Method called every "duration", used to update the group representing the audio spectrum and the seek bar
     * @param timestamp time ellapsed for the audio
     * @param duration time between each call to the method
     * @param magnitudes array containing the magnitude of each band of the audio spectrum
     * @param phases array containing the phase difference of each band of the audio spectrum
     */
    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        for(int i = 0; i<360; i++) {
            lines[i].setEndX((101+magnitudes[i+100]*3 + 180));
            lines[i+360].setEndX((101+magnitudes[i+100]*3 + 180));
            lines[i+720].setEndX((101+magnitudes[i+100]*3 + 180));
            lines[i+1080].setEndX((101+magnitudes[i+100]*3 + 180));
            lines[i+1440].setEndX((101+magnitudes[i+100]*3 + 180));
            lines[i+1800].setEndX((101+magnitudes[i+100]*3 + 180));
        }

        // Updates the seek bar
        seekBar.setPosition(audioMediaPlayer.getCurrentTime().toMillis());
    }
}
