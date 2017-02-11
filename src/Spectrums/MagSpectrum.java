package Spectrums;

import javafx.scene.Group;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

/**
 * Created by Arthur Geneau on 2017-01-31.
 */
public class MagSpectrum extends Spectrum {

    private Rectangle[] rectangles;
    private Color color;
    private Group root;
    private String name;
    private int numBands;
    private int div;
    private int conf;
    private double Xoffset;
    private double Yoffset;
    private double objWidth;

    public MagSpectrum() {
        name = "MSpectrum";
        root = new Group();
        Xoffset = 10;
        Yoffset = 0;
        objWidth = 3;
        conf = 1;
        numBands = 128;
        div = numBands;
        color = Color.RED;
        rectangles = new Rectangle[numBands];
        for(int i = 0; i<numBands; i++) {
            Rectangle rectangle = new Rectangle(1,0);
            rectangle.setFill(color);
            rectangle.setTranslateY(Yoffset);
            rectangle.setTranslateX(Xoffset+i*objWidth);
            rectangles[i] = rectangle;
            root.getChildren().add(rectangle);
        }
    }

    @Override
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
            rectangles[i].setFill(color);
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
        rectangles = new Rectangle[div];
        for(int i = 0; i<div; i++) {
            Rectangle rectangle = new Rectangle(1,0);
            rectangle.setFill(Color.GREEN);
            rectangle.setTranslateY(Yoffset);
            rectangle.setTranslateX(Xoffset+i*objWidth);
            rectangles[i] = rectangle;
            root.getChildren().add(rectangle);
        }
    }
}
