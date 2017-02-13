package View;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * Created by Arthur Geneau on 2017-02-11.
 */
public class SeekBar {

    private Line inviLine;
    private Line bar;
    private Circle nowCircle;
    private MediaPlayer mediaPlayer;
    private Group group;
    private double width;
    private double start;
    private double end;
    private double now;

    public SeekBar() {
        start = 0;
        end = 1;
        now = 0;
        mediaPlayer = null;

        nowCircle = new Circle(5);
        nowCircle.setFill(Color.WHITE);
        nowCircle.setCenterX(30 + now/(end-start)*(width-30));

        width = 200;
        inviLine = new Line(0,0,width, 0);
        bar = new Line(30, 0, width-30, 0);
        bar.setStrokeWidth(3);
        bar.setOnMouseClicked(event -> {
            if(mediaPlayer != null) {
                double value = event.getSceneX() - 30;
                value = value / (width - 60) * end;
                mediaPlayer.seek(new Duration(value));
            }
        });
        inviLine.setStroke(Color.TRANSPARENT);
        bar.setStroke(Color.BLUE);
        DropShadow d = new DropShadow();
        d.setColor(Color.WHITE);
        d.setRadius(20);
        d.setSpread(0.5);
        bar.setEffect(d);

        group = new Group();
        group.getChildren().addAll(inviLine, bar, nowCircle);
    }

    public void resize(double screenWidth) {
        width = screenWidth;
        inviLine.setEndX(width);
        bar.setEndX(width-30);
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public void setPosition(double p) {
        now = p;
        nowCircle.setCenterX(30 + now*(width-60)/(end-start));
    }

    public Group getGroup() {
        return group;
    }
}
