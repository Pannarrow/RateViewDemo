package com.pan.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * @Description 点赞动画
 * @Author hongpan
 * @Date 2023/10/10 周二 11:48
 */
public class RateView extends View {
    /**
     * 动画间隔时间
     */
    private static final int DURATION = 300;
    /**
     * 动画
     */
    private ValueAnimator animator;

    private int startWidth;

    private int endWidth;

    private int bgColor;

    private int divColor;

    private Bitmap leftBitmap;
    private Bitmap rightBitmap;
    private Paint paint;
    private Rect rect;
    private int padding;
    private int length;
    private int leftBitmapL;
    private int leftBimapR;
    private int rightBitmapL;
    private int rightBitmapR;

    private GestureDetector gestureDetector = null;

    private int rateType = 0; // 0:未评价 1:赞 2:踩

    public void setRateType(int rateType) {
        this.rateType = rateType;
        if (rateType == 0) {
            leftBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_praise);
            rightBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_criticize);
            post(new Runnable() {
                @Override
                public void run() {
                    setLayoutParamsWidth(startWidth);
                }
            });
        } else {
            if (rateType == 1) {
                leftBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_praised);
            } else {
                rightBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_criticized);
            }
            post(new Runnable() {
                @Override
                public void run() {
                    setLayoutParamsWidth(endWidth);
                }
            });
        }

    }

    private OnRateListener onRateListener;

    public void setOnRateListener(OnRateListener onRateListener) {
        this.onRateListener = onRateListener;
    }

    public RateView(Context context) {
        super(context);
        init();
    }

    public RateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        bgColor = Color.WHITE;
        divColor = Color.GRAY;
        leftBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_praise);
        rightBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_criticize);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(4);
        rect = new Rect();
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int x = (int) e.getX();
                if (rateType == 0) {
                    if (x >= leftBitmapL && x <= leftBimapR) {
                        // 点击赞
                        leftBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_praised);
                        rateType = 1;
                        startAnim(true);
                    } else if (x >= rightBitmapL && x <= rightBitmapR) {
                        // 点击踩
                        rightBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_criticized);
                        rateType = 2;
                        startAnim(true);
                    }
                } else {
                    if (rateType == 1) {
                        // 取消赞
                        leftBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_praise);
                    } else if (rateType == 2) {
                        // 取消踩
                        rightBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_criticize);
                    }
                    rateType = 0;
                    startAnim(false);
                }
                return true;
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > startWidth) {
            startWidth = getMeasuredWidth();
            endWidth = getMeasuredHeight();
        }
        padding = getMeasuredHeight() * 3 / 16; // 6 / 32
        length = getMeasuredHeight() * 5 / 8; // 20 / 32
        if (rateType == 0) {
            leftBitmapL = getMeasuredWidth() / 4 - length / 2;
            leftBimapR = getMeasuredWidth() / 4 + length / 2;
            rightBitmapL = getMeasuredWidth() * 3 / 4 - length / 2;
            rightBitmapR = getMeasuredWidth() * 3 / 4 + length / 2;
        } else if (rateType == 1) {
            leftBitmapL = getMeasuredWidth() / 2 - length / 2;
            leftBimapR = getMeasuredWidth() / 2 + length / 2;
            rightBitmapL = 0;
            rightBitmapR = 0;
        } else if (rateType == 2) {
            leftBitmapL = 0;
            leftBimapR = 0;
            rightBitmapL = getMeasuredWidth() / 2 - length / 2;
            rightBitmapR = getMeasuredWidth() / 2 + length / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画背景
        paint.setColor(bgColor);
        canvas.drawRoundRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), getMeasuredHeight() / 2f, getMeasuredHeight() / 2f, paint);

        if (rateType == 0 || rateType == 1) {
            // 画左侧拇指
            rect.set(leftBitmapL, padding, leftBimapR, padding + length);
            canvas.drawBitmap(leftBitmap, null, rect, paint);
        }

        if (rateType == 0) {
            // 画中间间隔
            paint.setColor(divColor);
            canvas.drawLine(getMeasuredWidth() / 2f, padding, getMeasuredWidth() / 2f, padding + length, paint);
        }

        if (rateType == 0 || rateType == 2) {
            // 画右侧拇指
            rect.set(rightBitmapL, padding, rightBitmapR, padding + length);
            canvas.drawBitmap(rightBitmap, null, rect, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 开始动画
     */
    private void startAnim(boolean isRated) {
        if (isRated) {
            animator = ValueAnimator.ofInt(startWidth, endWidth);
        } else {
            animator = ValueAnimator.ofInt(endWidth, startWidth);
        }
        animator.setDuration(DURATION);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            // 改变view宽度
            setLayoutParamsWidth(animatedValue);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (onRateListener != null) {
                    onRateListener.onRate(rateType);
                }
            }
        });
        animator.start();
    }

    /**
     * 改变view宽度
     * @param width
     */
    private void setLayoutParamsWidth(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        setLayoutParams(layoutParams);
    }

    /**
     * 取消动画
     */
    public void cancelAnim() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    public interface OnRateListener {
        /**
         * 点赞状态
         * @param rateType
         */
        void onRate(int rateType);
    }
}
