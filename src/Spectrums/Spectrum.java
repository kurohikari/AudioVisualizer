package Spectrums;

import View.SeekBar;
import javafx.scene.Group;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

/**
 * Created by Arthur Geneau on 2017-02-03.
 */
public abstract class Spectrum implements AudioSpectrumListener {

    public abstract Group getGroup();
    public abstract void setColor(Color color);
    public abstract String getName();
    public abstract void setMediaPlayer(MediaPlayer mediaPlayer);
    public abstract void setSeekBar(SeekBar seekBar);

}
