package com.yjs.relationshipview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yangjingsong on 16/12/2.
 */
public class RelationshipView extends View {

    int x, y;
    int width;
    int height;

    Paint middleCirclePaint;
    Paint otherCircleOuterPaint;
    Paint otherCircleInnerPaint;
    Paint linePaint;
    Paint centerWordsPaint;
    Paint otherWordsPaint;
    Rect textRect;
    Context mContext;
    float animatorValue = 0;
    boolean isReverse = false;
    List<MyPath> paths = new ArrayList<MyPath>();
    List<PathMeasure> pathMeasures = new ArrayList<PathMeasure>();
    List<Integer> radiusArray = new ArrayList<Integer>();

    List<DataBean> stories;
    DataBean centerStory;
    OnClickStoryListener onClickStoryListener;
    int clickItem = -1;

    int count = 0;

    public RelationshipView(Context context) {
        this(context, null);
    }

    public RelationshipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelationshipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        middleCirclePaint = new Paint();
        middleCirclePaint.setAntiAlias(true);
        middleCirclePaint.setColor(Color.parseColor("#F8D46F"));
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#C5B170"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dip2px(1));
        otherCircleOuterPaint = new Paint();
        otherCircleOuterPaint.setAntiAlias(true);
        otherCircleOuterPaint.setColor(Color.parseColor("#C5B170"));
        otherCircleOuterPaint.setStyle(Paint.Style.STROKE);
        otherCircleOuterPaint.setStrokeWidth(dip2px(1));
        otherCircleInnerPaint = new Paint();
        otherCircleInnerPaint.setAntiAlias(true);
        otherCircleInnerPaint.setColor(Color.parseColor("#F3EDD6"));
        otherCircleInnerPaint.setStyle(Paint.Style.FILL);
        centerWordsPaint = new Paint();
        centerWordsPaint.setStyle(Paint.Style.STROKE);
        centerWordsPaint.setAntiAlias(true);
        centerWordsPaint.setColor(Color.parseColor("#ffffff"));
        centerWordsPaint.setTextSize(50);

        centerWordsPaint.setTextAlign(Paint.Align.CENTER);
        otherWordsPaint = new Paint();
        otherWordsPaint.setStyle(Paint.Style.STROKE);
        otherWordsPaint.setAntiAlias(true);
        otherWordsPaint.setColor(Color.parseColor("#C5B170"));
        otherWordsPaint.setTextSize(40);
        otherWordsPaint.setTextAlign(Paint.Align.CENTER);
        textRect = new Rect();
        //initPath();
    }

    private void initPath() {

        if (stories.size() >= 1) {
            MyPath path1 = makeLine(-5, new int[]{-52, -60, -9}, true, new int[]{-2, -2});
            path1.endDirection = MyPath.EDotDirection.LEFT;
            paths.add(path1);
        }
        if (stories.size() >= 2) {
            MyPath path2 = makeLine(0, new int[]{-76, 31, -43}, true, new int[]{-2, 2});
            path2.endDirection = MyPath.EDotDirection.LEFT;
            paths.add(path2);
        }
        if (stories.size() >= 3) {
            MyPath path3 = makeLine(0, new int[]{49, -52, 22}, true, new int[]{2, -2});
            path3.endDirection = MyPath.EDotDirection.RIGHT;
            paths.add(path3);
        }
        if (stories.size() >= 4) {
            MyPath path4 = makeLine(-7, new int[]{51, -54, 12}, false, new int[]{2, -2});
            path4.endDirection = MyPath.EDotDirection.BOTTOM;
            paths.add(path4);
        }
        if (stories.size() >= 5) {
            MyPath path5 = makeLine(5, new int[]{60, 37, 34, 38}, false, new int[]{2, 2, 2});
            path5.endDirection = MyPath.EDotDirection.RIGHT;
            paths.add(path5);
        }

        int max = 34;
        int min = 20;
        for (Path path : paths) {
            PathMeasure pathMeasure = new PathMeasure(path, false);
            pathMeasures.add(pathMeasure);
            Random random = new Random();
            int s = random.nextInt(max) % (max - min + 1) + min;
            radiusArray.add(s);
        }

        drawLine(false);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        if (widthSpec == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = 200;
        }
        if (heightSpec == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = 200;
        }
        x = width / 2;
        y = height / 2;
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(x, y);

        for (int i = 0; i < pathMeasures.size(); i++) {
            Path path1 = new Path();
            PathMeasure pathMeasure = pathMeasures.get(i);
            MyPath path = paths.get(i);
            if (!isReverse) {
                pathMeasure.getSegment(0, pathMeasure.getLength() * animatorValue, path1, true);
                canvas.drawPath(path1, linePaint);
            } else {
                pathMeasure.getSegment(0, pathMeasure.getLength() * (1 - animatorValue), path1, true);
                canvas.drawPath(path1, linePaint);
            }
            drawCircles(radiusArray.get(i), canvas, path.endPoint, path.endDirection, i);
        }

        drawCenterCircle(canvas);
//        drawCircles(radiusArray.get(0),canvas, paths.get(0).endPoint, paths.get(0).endDirection);
    }

    private void drawCenterCircle(Canvas canvas) {
        middleCirclePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, dip2px(30), middleCirclePaint);
        middleCirclePaint.setStyle(Paint.Style.STROKE);
        middleCirclePaint.setStrokeWidth(dip2px(3));
        canvas.drawCircle(0, 0, dip2px(34), middleCirclePaint);

        String centerName = centerStory.getName();
        centerName = "人机交互";
        centerWordsPaint.getTextBounds(centerName, 0, centerName.length() - 1, textRect);
        canvas.drawText(centerName, 0, 0 + textRect.height() / 2, centerWordsPaint);

        if (isReverse) {
            middleCirclePaint.setAlpha((int) (255 * (1 - animatorValue)));
            centerWordsPaint.setAlpha((int) (255 * (1 - animatorValue)));
        } else {
            middleCirclePaint.setAlpha((int) (animatorValue * 255));
            centerWordsPaint.setAlpha((int) (255 * animatorValue));
        }
    }

    private void drawCircles(Integer aRadius, Canvas canvas, android.graphics.Point centerPoint, MyPath.EDotDirection endDirection, int i) {
        android.graphics.Point point = new android.graphics.Point(centerPoint.x, centerPoint.y);
        int radius = dip2px(aRadius);
        switch (endDirection) {
            case LEFT:
                point.x -= radius;
                break;
            case RIGHT:
                point.x += radius;
                break;
            case BOTTOM:
                point.y += radius;
                break;
            case TOP:
                point.y -= radius;
                break;
        }
        //周围圆圆心point.x, point.y
        stories.get(i).setPoint(point);
        canvas.drawCircle(point.x, point.y, radius - dip2px(4), otherCircleInnerPaint);
        canvas.drawCircle(point.x, point.y, radius, otherCircleOuterPaint);
        String name = stories.get(i).getName();
        name = "人工智能";
        otherWordsPaint.getTextBounds(name, 0, name.length() - 1, textRect);
        canvas.drawText(name, point.x, point.y + textRect.height() / 2, otherWordsPaint);
        if (isReverse) {
            otherCircleInnerPaint.setAlpha((int) (255 * (1 - animatorValue)));
            otherCircleOuterPaint.setAlpha((int) (255 * (1 - animatorValue)));
            otherWordsPaint.setAlpha((int) (255 * (1 - animatorValue)));
        } else {
            otherCircleInnerPaint.setAlpha((int) (255 * animatorValue));
            otherCircleOuterPaint.setAlpha((int) (255 * animatorValue));
            otherWordsPaint.setAlpha((int) (255 * animatorValue));
        }

    }

    public int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void drawLine(final boolean reverse) {

        isReverse = reverse;
        if (!isReverse) {
            setPaintColor();
        }
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1).setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatorValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (clickItem != -1) {
                    onClickStoryListener.onClickStory(stories.get(clickItem));
                    clickItem = -1;
                }

//                if (reverse) {
//                    drawLine(false);
//                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }


    public MyPath makeLine(int start, int[] controlPoint, boolean isHorizontal, int[] additional) {
        MyPath myPath = new MyPath(mContext);

        android.graphics.Point endPoint = myPath.forward(start, controlPoint[0], isHorizontal);
        isHorizontal = !isHorizontal;
        for (int i = 0; i < additional.length; i++) {
            if (isHorizontal) {
                endPoint = myPath.forwardWithCircle(endPoint,
                        new android.graphics.Point(endPoint.x + dip2px(controlPoint[i + 1]), endPoint.y + dip2px(additional[i])),
                        isHorizontal);
            } else {
                endPoint = myPath.forwardWithCircle(endPoint,
                        new android.graphics.Point(endPoint.x + dip2px(additional[i]), endPoint.y + dip2px(controlPoint[i + 1])),
                        isHorizontal);
            }
            myPath.drawLine(null, endPoint);
            isHorizontal = !isHorizontal;
        }
        myPath.endPoint = endPoint;
        return myPath;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                for (int i = 0; i < stories.size(); i++) {
                    int mRadius = radiusArray.get(i) + 20;
                    int x = stories.get(i).getPoint().x;
                    int y = stories.get(i).getPoint().y;
                    int realX, realY;
                    realX = getWidth() / 2 + x;
                    realY = getHeight() / 2 + y;
                    Log.d("realX", realX + "");
                    Log.d("realY", realY + "");
                    Log.d("radius", mRadius + "");
                    if ((upX >= realX - mRadius && upX <= realX + mRadius) && (upY >= realY - mRadius && upY <= realY + mRadius)) {
                        clickItem = i;
                        break;
                    }
                }


                Log.d("upx", event.getX() + "");
                Log.d("upy", event.getY() + "");
                break;
            case MotionEvent.ACTION_UP:
                if (clickItem != -1) {

                    drawLine(true);


                }

                break;
        }
        return true;
    }

    public void setStories(List<DataBean> stories, DataBean centerStory) {
        paths.clear();
        pathMeasures.clear();
        radiusArray.clear();
        this.stories = stories;
        this.centerStory = centerStory;
        initPath();
    }

    public interface OnClickStoryListener {
        void onClickStory(DataBean story);
    }

    public void setOnClickStoryListener(OnClickStoryListener onClickStoryListener) {
        this.onClickStoryListener = onClickStoryListener;
    }

    private void setPaintColor() {
        count++;
        Random random = new Random();
        int index = random.nextInt(3);
        switch (count % 3) {
            case 0:
                middleCirclePaint.setColor(Color.parseColor("#F8D46F"));
                linePaint.setColor(Color.parseColor("#C5B170"));
                otherCircleOuterPaint.setColor(Color.parseColor("#C5B170"));
                otherCircleInnerPaint.setColor(Color.parseColor("#F3EDD6"));
                otherWordsPaint.setColor(Color.parseColor("#C5B170"));
                break;
            case 1:
                middleCirclePaint.setColor(Color.parseColor("#A0A5FB"));
                linePaint.setColor(Color.parseColor("#A0A5FB"));
                otherCircleOuterPaint.setColor(Color.parseColor("#A0A5FB"));
                otherCircleInnerPaint.setColor(Color.parseColor("#D6EAF3"));
                otherWordsPaint.setColor(Color.parseColor("#7F83C1"));
                break;
            case 2:
                middleCirclePaint.setColor(Color.parseColor("#FBB8A0"));
                linePaint.setColor(Color.parseColor("#FBB8A0"));
                otherCircleOuterPaint.setColor(Color.parseColor("#FBB8A0"));
                otherCircleInnerPaint.setColor(Color.parseColor("#FFEBE0"));
                otherWordsPaint.setColor(Color.parseColor("#CD7252"));
                break;
        }
        middleCirclePaint.setAlpha(0);
        otherCircleOuterPaint.setAlpha(0);
        otherCircleInnerPaint.setAlpha(0);
        otherWordsPaint.setAlpha(0);
    }
}
