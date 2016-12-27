package com.yjs.relationshipview;

import android.graphics.Point;

/**
 * Created by yangjingsong on 16/12/3.
 */
public class DataBean {

    private int storyId;
    private String name;
    private int score;
    private Point point;

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
