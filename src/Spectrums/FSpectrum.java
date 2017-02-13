package Spectrums;

import View.SeekBar;
import javafx.scene.Group;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

/**
 * Created by Arthur Geneau on 2017-02-07.
 */
public class FSpectrum extends Spectrum {

    private MediaPlayer audioMediaPlayer;
    private SeekBar seekBar;

    public FSpectrum() {
        audioMediaPlayer = null;
    }

    @Override
    public Group getGroup() {
        return null;
    }

    @Override
    public void setColor(Color color) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        audioMediaPlayer = mediaPlayer;
        mediaPlayer.setAudioSpectrumNumBands(11025);
    }

    @Override
    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {

    }
}
