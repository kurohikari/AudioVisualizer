package Spectrums;

import View.SeekBar;
import javafx.scene.Group;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by arthur on 2/27/2017.
 */
public class PSpectrum extends Spectrum {

    private MediaPlayer audioMediaPlayer;
    private SeekBar seekBar;
    private Group group;
    private Color color;
    private String name;
    private Rectangle[] rects;

    public PSpectrum() {
        // Initializes variables
        name = "PSpectrum";
        color = Color.RED;
        group = new Group();
        rects = new Rectangle[81];
        double offset = 70;
        for(int i = 0; i<rects.length; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(60);
            rectangle.setY(20);
            rectangle.setStroke(Color.BLACK);
            switch (i%12) {
                case 0:
                    rectangle.setX(i/12*offset);
                    rectangle.setWidth(9);
                    rectangle.setFill(Color.WHITE);
                    break;
                case 1:
                    rectangle.setX(8+i/12*offset);
                    rectangle.setWidth(4);
                    rectangle.setHeight(45);
                    rectangle.setFill(Color.GRAY);
                    break;
                case 2:
                    rectangle.setX(10+i/12*offset);
                    rectangle.setWidth(9);
                    rectangle.setFill(Color.WHITE);
                    break;
                case 3:
                    rectangle.setX(18+i/12*offset);
                    rectangle.setWidth(4);
                    rectangle.setHeight(45);
                    rectangle.setFill(Color.GRAY);
                    break;
                case 4:
                    rectangle.setX(20+i/12*offset);
                    rectangle.setWidth(9);
                    rectangle.toBack();
                    rectangle.setFill(Color.WHITE);
                    break;
                case 5:
                    rectangle.setX(30+i/12*offset);
                    rectangle.setWidth(9);
                    rectangle.setFill(Color.WHITE);
                    break;
                case 6:
                    rectangle.setX(38+i/12*offset);
                    rectangle.setWidth(4);
                    rectangle.setHeight(45);
                    rectangle.setFill(Color.GRAY);
                    break;
                case 7:
                    rectangle.setX(40+i/12*offset);
                    rectangle.setWidth(9);
                    rectangle.setFill(Color.WHITE);
                    break;
                case 8:
                    rectangle.setX(48+i/12*offset);
                    rectangle.setWidth(4);
                    rectangle.setHeight(45);
                    rectangle.setFill(Color.GRAY);
                    break;
                case 9:
                    rectangle.setX(50+i/12*offset);
                    rectangle.setWidth(9);
                    rectangle.setFill(Color.WHITE);
                    break;
                case 10:
                    rectangle.setX(58+i/12*offset);
                    rectangle.setWidth(4);
                    rectangle.setHeight(45);
                    rectangle.setFill(Color.GRAY);
                    break;
                default:
                    rectangle.setX(60+i/12*offset);
                    rectangle.setWidth(9);
                    rectangle.setFill(Color.WHITE);
                    break;
            }
            rects[i] = rectangle;
            group.getChildren().add(rects[i]);
        }
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        audioMediaPlayer = mediaPlayer;

        // 1 band = 21.5 frequencies
        audioMediaPlayer.setAudioSpectrumNumBands(1024);
        audioMediaPlayer.setAudioSpectrumInterval(0.1);
    }

    @Override
    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        //440 / 21.5 = 20.4
        //8000 / 21.5 = 372.09

        Notes n = new Notes();
        Note[] notes = {Note.C, Note.CSharp, Note.D, Note.DSharp, Note.E, Note.F, Note.FSharp, Note.G, Note.GSharp, Note.A, Note.ASharp, Note.B};
        int[] octaves = {2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,6,6,6,6,6,6,6,7,7,7,7,7,7,7,7,7,7,7,7,8,8,8,8,8,8,8,8,8,8,8,8};

        for(int i = 0; i<rects.length; i++) {
            float freq = n.getFrequency(notes[i%12], octaves[i]);
            int index = (int) (freq/(22050f/1024f));
            float prevharmonic = magnitudes[index/2] + 60;
            float harmonic = magnitudes[index] + 60;
            float subharmonic = magnitudes[index*2] + 60;
            if(subharmonic < harmonic/2 && harmonic > prevharmonic/2 && harmonic > 24-octaves[i]*2.5) {
                rects[i].setFill(color);
            } else {
                switch (i%12) {
                    case 0:case 2:case 4:case 5:case 7: case 9:case 11:
                        rects[i].setFill(Color.WHITE);
                        break;
                    default:
                        rects[i].setFill(Color.GRAY);
                        rects[i].toFront();
                        break;
                }
            }
        }

        seekBar.setPosition(audioMediaPlayer.getCurrentTime().toMillis());
    }
}
