package Spectrums;

import View.SeekBar;
import javafx.scene.Group;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by arthur on 2/25/2017.
 */
public class SSpectrum extends Spectrum {

    private MediaPlayer audioMediaPlayer;
    private Line[] lines;
    private Line upperLine, middleLine, lowerLine;
    private Group group;
    private Color color;
    private SeekBar seekBar;
    private double[] lasts;
    private String name;

    /**
     * Constructor for the SSpectrum Object
     */
    public SSpectrum() {
        // Initializes variables
        name = "SSpectrum";
        color = Color.WHITE;
        lines = new Line[600];
        lasts = new double[] {0,0,0,0,0,0,0,0};
        upperLine = new Line(-50, 120, 650, 120);
        middleLine = new Line(-50,0,650,0);
        lowerLine = new Line(-50, -120, 650, -120);
        upperLine.setStroke(Color.WHITE);
        middleLine.setStroke(Color.WHITE);
        lowerLine.setStroke(Color.WHITE);
        group = new Group();

        // Adds the surrounding lines to the group
        group.getChildren().addAll(upperLine, middleLine, lowerLine);

        // Initializes all lines for the spectrum and adds them to the group
        for(int i = 0; i<600; i++) {
            Line l = new Line((i%150)*4, 0, (i%150)*4+4, 0);
            l.setStrokeWidth(3);
            l.setStroke(Color.TRANSPARENT);
            lines[i] = l;
            group.getChildren().add(lines[i]);
        }

        // Sets the colors for the first lines
        lines[0].setStroke(Color.GREEN);
        lines[150].setStroke(Color.RED);
        lines[300].setStroke(Color.YELLOW);
        lines[450].setStroke(Color.BLUE);
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
        this.color = color;
        upperLine.setStroke(color);
        lowerLine.setStroke(color);
        middleLine.setStroke(color);
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
        audioMediaPlayer.setAudioSpectrumNumBands(8);
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
        for(int i = 599; i>0; i--) {
            lines[i].setStartY(lines[i-1].getStartY());
            lines[i].setEndY(lines[i-1].getEndY());
            if(lines[i].getStroke() == Color.TRANSPARENT) {
                lines[i].setStroke(lines[i-1].getStroke());
            }
        }
        for(int i = 2; i<6; i++) {
            Line l = lines[(i-2)*150];
            if(timestamp == 0) {
                l.setEndY((magnitudes[i] + 60) * Math.cos((timestamp - duration) * 22050 / 8 * (i+1) + phases[i]));
            } else {
                l.setEndY(lasts[i]);
            }
            lasts[i] = (magnitudes[i]+60)*2*Math.cos(timestamp*22050/8*(i+1) + phases[i]);
            l.setStartY(lasts[i]);
        }

        seekBar.setPosition(audioMediaPlayer.getCurrentTime().toMillis());
    }
}
