package Spectrums;

import javafx.scene.Group;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.*;

/**
 * Created by Arthur Geneau on 2017-01-31.
 */
public class MagSpectrum implements AudioSpectrumListener {

    private Rectangle[] rectangles;
    private MediaPlayer mediaPlayer;
    private Group root;
    private int numBands;
    private int div;
    private int conf;
    private double Xoffset;
    private double Yoffset;
    private double objWidth;

    public MagSpectrum(MediaPlayer mediaPlayer, double Xoffset, double Yoffset) {
        this.mediaPlayer = mediaPlayer;
        this.root = new Group();
        this.Xoffset = Xoffset;
        this.Yoffset = Yoffset;
        this.objWidth = 1;
        this.conf = 1;
        this.numBands = this.mediaPlayer.getAudioSpectrumNumBands();
        this.div = numBands;
        this.rectangles = new Rectangle[numBands];
        for(int i = 0; i<numBands; i++) {
            Rectangle rectangle = new Rectangle(1,0);
            rectangle.setFill(Color.GREEN);
            rectangle.setTranslateY(this.Yoffset);
            rectangle.setTranslateX(this.Xoffset+i*objWidth);
            rectangles[i] = rectangle;
            this.root.getChildren().add(rectangle);
        }
    }

    public Group getGroup() {
        return this.root;
    }

    public void setObjWidth(double width) {
        if(width >= 1.0) {
            this.objWidth = width;
        }
    }

    public void setDivs(int div) {
        this.div = div;
        makeRectangles();
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        int lim = numBands / div;
        for (int i = 0; i < div; i++) {
            double ave = 0.0;
            for (int j = i * lim; j < i * lim + lim; j++) {
                ave += (magnitudes[j] + 60) * 5;
            }
            ave = ave / lim;
            rectangles[i].setTranslateX(Xoffset + i * objWidth);
            rectangles[i].setTranslateY(Yoffset - ave / 2);
            rectangles[i].setHeight(ave);
            rectangles[i].setWidth(objWidth);
        }
        if(conf == 1) {
            Queue<Rectangle> rectangleQueue = new LinkedList<>();
            Stack<Rectangle> rectangleStack = new Stack<>();
            Rectangle[] rectangles1 = new Rectangle[rectangles.length*2];
            for(int i = 0; i<rectangles.length; i++) {
                rectangleQueue.offer(rectangles[i]);
                rectangleStack.push(rectangles[i]);
            }
            int i = 0;
            while(!rectangleQueue.isEmpty()) {
                Rectangle original = rectangleQueue.poll();
                Rectangle copy = new Rectangle();
                copy.setFill(original.getFill());
                copy.setHeight(original.getHeight());
                copy.setWidth(original.getWidth());
                copy.setTranslateY(original.getTranslateY());
                copy.setTranslateX(-original.getTranslateX());
                rectangles1[i] = copy;
                i++;
            }
            while(!rectangleStack.isEmpty()) {
                Rectangle original = rectangleStack.pop();
                Rectangle copy = new Rectangle();
                copy.setFill(original.getFill());
                copy.setHeight(original.getHeight());
                copy.setWidth(original.getWidth());
                copy.setTranslateY(original.getTranslateY());
                copy.setTranslateX(original.getTranslateX());
                rectangles1[i] = copy;
                i++;
            }
            this.root.getChildren().clear();
            for(Rectangle rectangle : rectangles1) {
                this.root.getChildren().add(rectangle);
            }
        }
    }

    private void makeRectangles() {
        this.rectangles = new Rectangle[this.div];
        for(int i = 0; i<div; i++) {
            Rectangle rectangle = new Rectangle(1,0);
            rectangle.setFill(Color.GREEN);
            rectangle.setTranslateY(this.Yoffset);
            rectangle.setTranslateX(this.Xoffset+i*objWidth);
            rectangles[i] = rectangle;
            this.root.getChildren().add(rectangle);
        }
    }
}
