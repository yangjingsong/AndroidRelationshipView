package com.yjs.relationshipview;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

/**
 * Created by yangjingsong on 16/12/2.
 */
public class MyPath extends Path {

    public enum EDotDirection {
        LEFT(180), TOP(270), RIGHT(0), BOTTOM(90);
        private int value;
        private EDotDirection(int value) {
            this.value = value;
        }
    }

    private Context mContext;
    public EDotDirection endDirection;
    public Point endPoint;

    public MyPath(Context context) {
        mContext = context;

    }

    public android.graphics.Point forward(int start, int pointOne, boolean isHorizontal) {
        int distance = (int) Math.sqrt(Math.pow(34, 2) - Math.pow(start, 2));
        distance = dip2px(distance);
        if (pointOne < 0) {
            distance = -distance;
        }
        int startX, startY, pointOneX, pointOneY;
        start = dip2px(start);
        pointOne = dip2px(pointOne);

        if (isHorizontal) {
            startX = distance;
            startY = start;
            pointOneX = pointOne;
            pointOneY = startY;
        } else {
            startX = start;
            startY = distance;
            pointOneX = startX;
            pointOneY = pointOne;
        }
        this.moveTo(startX, startY);
        this.lineTo(pointOneX, pointOneY);
        endPoint = new Point(pointOneX, pointOneY);
        return endPoint;
    }

    public Point forwardWithCircle(Point startPoint, Point endPoint, boolean isHorizontal) {
        EDotDirection dotDirectionIn = EDotDirection.LEFT;
        EDotDirection dotDirectionOut = EDotDirection.LEFT;
        if (isHorizontal) {
            if (startPoint.x > endPoint.x && startPoint.y < endPoint.y) {
                dotDirectionIn = EDotDirection.TOP;
                dotDirectionOut = EDotDirection.LEFT;
            } else if (startPoint.x < endPoint.x && startPoint.y < endPoint.y) {
                dotDirectionIn = EDotDirection.TOP;
                dotDirectionOut = EDotDirection.RIGHT;
            } else if (startPoint.x > endPoint.x && startPoint.y > endPoint.y) {
                dotDirectionIn = EDotDirection.BOTTOM;
                dotDirectionOut = EDotDirection.LEFT;
            } else if (startPoint.x < endPoint.x && startPoint.y > endPoint.y) {
                dotDirectionIn = EDotDirection.BOTTOM;
                dotDirectionOut = EDotDirection.RIGHT;
            }
        } else {
            if (startPoint.x > endPoint.x && startPoint.y < endPoint.y) {
                dotDirectionIn = EDotDirection.RIGHT;
                dotDirectionOut = EDotDirection.BOTTOM;
            } else if (startPoint.x < endPoint.x && startPoint.y < endPoint.y) {
                dotDirectionIn = EDotDirection.LEFT;
                dotDirectionOut = EDotDirection.BOTTOM;
            } else if (startPoint.x > endPoint.x && startPoint.y > endPoint.y) {
                dotDirectionIn = EDotDirection.RIGHT;
                dotDirectionOut = EDotDirection.TOP;
            } else if (startPoint.x < endPoint.x && startPoint.y > endPoint.y) {
                dotDirectionIn = EDotDirection.LEFT;
                dotDirectionOut = EDotDirection.TOP;
            }
        }

        drawCircle(startPoint, endPoint, dotDirectionIn, dotDirectionOut);
        this.endPoint = endPoint;
        return endPoint;
    }


    public void drawCircle(Point startPoint, Point endPoint, EDotDirection dotDirectionIn, EDotDirection dotDirectionOut) {
        Point centerPoint;
        if (dotDirectionIn == EDotDirection.TOP || dotDirectionIn == EDotDirection.BOTTOM) {
            centerPoint = new Point(startPoint.x, endPoint.y);

        } else {
            centerPoint = new Point(endPoint.x, startPoint.y);

        }

//        this.moveTo(startPoint.x, startPoint.y);
        RectF rectF = new RectF(centerPoint.x - dip2px(2), centerPoint.y - dip2px(2), centerPoint.x + dip2px(2), centerPoint.y + dip2px(2));
        this.arcTo(rectF, dotDirectionIn.value, dotDirectionOut.value - dotDirectionIn.value);
        this.arcTo(rectF, dotDirectionOut.value, 359.9f);


    }

    public void drawLine(Point startPoint,Point endPoint){
        if(startPoint!=null){
            this.moveTo(startPoint.x,startPoint.y);
        }
        this.lineTo(endPoint.x,endPoint.y);
    }

    public int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
