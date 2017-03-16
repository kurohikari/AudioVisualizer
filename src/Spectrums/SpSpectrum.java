package Spectrums;

import View.SeekBar;
import javafx.scene.Group;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

/**
 * Created by arthur on 3/5/2017.
 */
public class SpSpectrum extends Spectrum {

    private MediaPlayer audioMediaPlayer;
    private SeekBar seekBar;
    private Line[] spiral;
    private Color color;
    private Group group;
    private String name;

    public SpSpectrum() {
        //System.out.println(System.getProperty("user.home"));
        name = "Spiral";
        spiral = new Line[1024*2];
        color = color.WHITE;
        float radius = 0.25f;
        group = new Group();
        for(int i = 0; i<spiral.length/2; i++) {
            Line l = new Line(radius,0,radius+1, 0);
            l.setStroke(color);
            l.setStrokeWidth(2);
            l.getTransforms().add(new Rotate(i));
            spiral[i] = l;
            group.getChildren().add(spiral[i]);
            radius += 0.25f;
            Line l2 = new Line(radius,0,radius+1, 0);
            l2.setStroke(color);
            l2.setStrokeWidth(2);
            l2.getTransforms().add(new Rotate(i+180));
            spiral[i+1024] = l2;
            group.getChildren().add(l2);
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

    @Override
    public void setColor(Color color) {
        this.color = color;
        for(Line l : spiral) {
            l.setStroke(color);
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

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        for(int i = 0; i<512; i++) {
            for(int j = 0; j<4; j++) {
                updateLine(spiral[i + j*512], magnitudes[i]);
            }
        }

        seekBar.setPosition(audioMediaPlayer.getCurrentTime().toMillis());
    }

    private void updateLine(Line l, float mag) {
        double begin = l.getStartX() + 1;
        l.setEndX(begin + mag + 60);
    }
}
