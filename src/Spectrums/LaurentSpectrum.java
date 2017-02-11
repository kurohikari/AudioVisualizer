package Spectrums;

import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;

/**
 * Created by Arthur Geneau on 2017-02-04.
 */
public class LaurentSpectrum extends Spectrum {

    private Group group;
    private Color color;
    private Text[] texts;
    private DropShadow[] shadows;
    private String name;

    public LaurentSpectrum() {
        name = "LSpectrum";
        group = new Group();
        color = Color.WHITE;
        String message = "Joyeux Anniversaire papa!!";
        texts = new Text[message.length()];
        shadows = new DropShadow[message.length()];
        for(int i = 0; i<message.length(); i++) {
            DropShadow d = new DropShadow();
            d.setRadius(40);
            d.setColor(Color.WHITE);
            d.setBlurType(BlurType.GAUSSIAN);
            d.setSpread(0.95);
            shadows[i] = d;

            Text t = new Text();
            t.setFill(Color.WHITE);
            t.setText("" + message.charAt(i));
            t.setFont(new Font("Arial", 30.0));
            t.setTranslateX(i*30);
            t.setEffect(d);
            texts[i] = t;
        }

        for(Text t : texts) {
            group.getChildren().add(t);
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

    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        int k = 0;
        int l = 0;
        for(int i = 0; i<96; i+=8) {
            double ave = 0;
            for(int j = 0; j<8; j++) {
                ave += magnitudes[j+i] + 60;
            }
            ave = ave/8;
            double radius  = 40 + ave*10;
            int red =(int) (ave/60 * 255);
            if(texts[i/8+k].getText().compareTo(" ")== 0) {
                k++;
            }
            if(texts[25-(i/8+l)].getText().compareTo(" ")== 0) {
                l++;
            }
            shadows[i/8+k].setRadius(radius);
            shadows[i/8+k].setColor(Color.rgb(255-red, 255-red, 255));
            shadows[25-(i/8+k)].setRadius(radius);
            shadows[25-(i/8+k)].setColor(Color.rgb(255, 255-red, 255-red));
        }
    }
}
