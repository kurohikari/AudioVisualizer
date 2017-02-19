package Spectrums;

import View.SeekBar;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created by Arthur Geneau on 2017-02-07.
 */
public class FSpectrum extends Spectrum {

    private MediaPlayer audioMediaPlayer;
    private SeekBar seekBar;
    private Circle[] bubbles;
    private Group group;
    private String name;

    /**
     * Constructor for the FSpectrum Object
     */
    public FSpectrum() {
        name="FSpectrum";
        audioMediaPlayer = null;
        bubbles = new Circle[20];

        // sets each circle with radius 5, color yellow and a shadow
        for(int i = 0; i<20; i++) {
            Circle circle = new Circle();
            circle.setCenterX(i*40 + 100);
            circle.setRadius(5);
            circle.setFill(Color.YELLOW);
            DropShadow d = new DropShadow();
            d.setRadius(30);
            d.setSpread(0.8);
            d.setColor(Color.YELLOW);
            circle.setEffect(d);
            bubbles[i] = circle;
        }
        group = new Group();
        Line line = new Line(0,0,100,0);
        line.setStroke(Color.TRANSPARENT);
        group.getChildren().add(line);
        for(Circle c : bubbles) {
            group.getChildren().add(c);
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
        for(Circle c : bubbles) {
            c.setFill(color);
            DropShadow d = (DropShadow) c.getEffect();
            d.setColor(color);
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
        int k = 0;
        int i = 0;

        // keep looping while all the circles have not been updated
        while(k<20) {
            // sets how many frequencies are used to evaluate each circle
            int l = (int) (1+k*1.73);
            double max = -60;
            for(int j = 0; j<l; j++) {
                // updates max when a new max has been found
                if(magnitudes[i+j] > max) {
                    max = magnitudes[i+j] + 60;
                }
            }
            i += l;
            max = max*20/60;
            if(max > -20) {
                // sets the radius of the circles if the band magnitude was not -60DB
                bubbles[k].setRadius(5 + max);
            }
            k++;
        }

        // Updates the seek bar
        seekBar.setPosition(audioMediaPlayer.getCurrentTime().toMillis());
    }
}
