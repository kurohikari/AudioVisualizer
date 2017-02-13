package Spectrums;

import View.SeekBar;
import javafx.scene.Group;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Created by Arthur Geneau on 2017-02-03.
 */
public class WSpectrum extends Spectrum {

    private Rectangle[] rectangles;
    private Group group;
    private Color color;
    private Line bottomLine;
    private MediaPlayer mediaPlayer;
    private double XoffSet;
    private int numBands;
    private double minDB;
    private int waveHalfWidth;
    private SeekBar seekbar;
    private String name;

    public WSpectrum() {
        name = "WSpectrum";
        numBands = 128;
        minDB = -60;
        waveHalfWidth = 4;
        XoffSet = 10;
        rectangles = new Rectangle[2*waveHalfWidth*numBands];
        color = Color.BLUE;
        group = new Group();
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void setSeekBar(SeekBar seekBar) {
        this.seekbar = seekBar;
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        for(int i = 0; i<rectangles.length; i+=2*waveHalfWidth) {
            for(int j = 0; j<waveHalfWidth; j++) {
                rectangles[i+j] = new Rectangle();
                rectangles[i+j].setFill(color);
                rectangles[i+j].setWidth(1);
                rectangles[i+j].setHeight((magnitudes[i/(2*waveHalfWidth)]-minDB)*2/((waveHalfWidth-j)));
                rectangles[i+j].setTranslateX(XoffSet+j+i);
                rectangles[i+j].setTranslateY(200-rectangles[i+j].getHeight());
            }
            for(int j = waveHalfWidth; j<2*waveHalfWidth; j++) {
                rectangles[i+j] = new Rectangle();
                rectangles[i+j].setFill(color);
                rectangles[i+j].setWidth(1);
                rectangles[i+j].setHeight((magnitudes[i/(2*waveHalfWidth)]-minDB)*2/((j-waveHalfWidth+1)));
                rectangles[i+j].setTranslateX(XoffSet+j+i);
                rectangles[i+j].setTranslateY(200-rectangles[i+j].getHeight());
            }
        }
        Line inviLine = new Line(0,-minDB*2, 0,0);
        inviLine.setStroke(Color.TRANSPARENT);
        bottomLine = new Line(0,0,numBands*2*waveHalfWidth,0);
        bottomLine.setStroke(color);
        bottomLine.setTranslateY(200);
        group.getChildren().clear();
        for(Rectangle rectangle : rectangles) {
            group.getChildren().add(rectangle);
        }
        group.getChildren().add(bottomLine);
        group.getChildren().add(inviLine);
        seekbar.setPosition(mediaPlayer.getCurrentTime().toMillis());
        //System.out.println(mediaPlayer.getCurrentTime());
    }
}
