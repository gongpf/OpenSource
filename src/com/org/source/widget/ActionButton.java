package com.org.source.widget;

import android.widget.FrameLayout;

import com.org.source.base.ContextManager;

public class ActionButton extends FrameLayout {
    
    public static class ActionInfo {
        public enum ViewStyle {IMAGE, BUTTON, TEXT};
        public ViewStyle mViewStyle = ViewStyle.BUTTON;
    }
    public ActionButton() {
        super(ContextManager.getContext());
    }

}
