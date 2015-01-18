package com.org.source.window;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import com.org.source.eventbus.EventBus;
import com.org.source.nineoldandroids.animation.Animator;
import com.org.source.nineoldandroids.animation.ObjectAnimator;
import com.org.source.window.WindowManager.WindowEvent;

@SuppressLint("NewApi")
public class DefaultWindowSwiper extends AbstractWindowSwiper
{
    private ObjectAnimator mAnimatorX;
    private ObjectAnimator mAnimatorY;
    private final View mTargetView;

    private boolean mCanDrag = false;
    
    public DefaultWindowSwiper(View target)
    {
        mTargetView = target;
        mAnimatorX = ObjectAnimator.ofFloat(mTargetView, "TranslationX", 0);
        mAnimatorY = ObjectAnimator.ofFloat(mTargetView, "TranslationY", 0);
        setWindowSwipeListener(mListener);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return mCanDrag;
    }
    
    @Override
    public Animator getPopupAnimator()
    {
        return mAnimatorX;
    }
    
    @Override
    public Animator getPushAnimator()
    {
        mAnimatorX.setFloatValues(1000, 0);
        return mAnimatorX;
    }
    
    @Override
    public boolean isAnimation()
    {
        return mAnimatorX.isRunning() || mAnimatorY.isRunning();
    }
    
    private void popupWindow()
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.POPUPWINDOW;
        EventBus.getDefault().post(windowEvent);
    }
    
    private WindowSwipeListener mListener = new WindowSwipeListener()
    {
        @Override
        public void onInit() 
        {
            mCanDrag =  (mDownPointX < 200 || mDownPointX > (mTargetView.getWidth() - 200));
        };
        
        @Override
        public void onStart()
        {
            switch (mDirection)
            {
                case LEFT:
                    mAnimatorX.setFloatValues(0, -mTargetView.getWidth());
                    break;

                case RIGHT:
                    mAnimatorX.setFloatValues(0, mTargetView.getWidth());
                    break;

                case UP:
                    mAnimatorY.setFloatValues(0, -mTargetView.getHeight());
                    break;

                case DOWN:
                    mAnimatorY.setFloatValues(0, mTargetView.getHeight());
                    break;

                default:
                    break;
            }
        }
        
        @Override
        public void onMove(float dx, float dy)
        {
            if ((Direction.LEFT == mDirection && dx < 0) || (Direction.RIGHT == mDirection && dx > 0))
            {
                mAnimatorX.setCurrentPlayTime((long) (mAnimatorX.getDuration() * Math.abs(dx) / (mTargetView.getWidth() + 0.5f)));
            }

            if ((Direction.UP == mDirection && dy < 0) || (Direction.DOWN == mDirection && dy > 0))
            {
                mAnimatorY.setCurrentPlayTime((long) (mAnimatorY.getDuration() * Math.abs(dy) / (mTargetView.getHeight() + 0.5f)));
            }
        }
        
        @Override
        public void onEnd()
        {
            switch (mDirection)
            {
                case LEFT:
                    handleLeftOrRight(true);
                    break;

                case RIGHT:
                    handleLeftOrRight(false);
                    break;

                case UP:
                    handleUpOrDown(true);
                    break;
                    
                case DOWN:
                    handleUpOrDown(false);
                    break;

                default:
                    break;
            }
        }
        
        private void handleLeftOrRight(boolean isLeft)
        {
            float translationX = mTargetView.getTranslationX();
            float width = mTargetView.getWidth() / (3 + 0.5f);
            if (Math.abs(translationX) < width)
            {
                mAnimatorX.setFloatValues(mTargetView.getTranslationX(), 0);
                mAnimatorX.start();
            }
            else 
            {
                int toValue = isLeft ? - mTargetView.getWidth() : mTargetView.getWidth();
                mAnimatorX.setFloatValues(mTargetView.getTranslationX(), (float)toValue);                        
                popupWindow();
            }
        }
        
        private void handleUpOrDown(boolean isUp)
        {
            float translationY = mTargetView.getTranslationY();
            float height = mTargetView.getHeight() / (3 + 0.5f);
            
            if (Math.abs(translationY) < height)
            {
                mAnimatorY.setFloatValues(mTargetView.getTranslationY(), 0);
                mAnimatorY.start();
            }
            else 
            {
                int toValue = isUp ? - mTargetView.getHeight() : mTargetView.getHeight();
                mAnimatorY.setFloatValues(mTargetView.getTranslationY(), (float)toValue);                        
                popupWindow();
            }
        }
    };
}
