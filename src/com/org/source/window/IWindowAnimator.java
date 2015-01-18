package com.org.source.window;

import com.org.source.nineoldandroids.animation.Animator;

public interface IWindowAnimator
{
    public boolean isAnimation();
    
    public Animator getPushAnimator();

    public Animator getPopupAnimator();
}
