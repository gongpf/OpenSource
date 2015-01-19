package com.org.source.widget.menudrawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class MenuDrawer extends ViewGroup {

    /**
     * Callback interface for changing state of the drawer.
     */
    public interface OnDrawerStateChangeListener {

        /**
         * Called when the drawer state changes.
         *
         * @param oldState The old drawer state.
         * @param newState The new drawer state.
         */
        void onDrawerStateChange(int oldState, int newState);

        /**
         * Called when the drawer slides.
         *
         * @param openRatio    Ratio for how open the menu is.
         * @param offsetPixels Current offset of the menu in pixels.
         */
        void onDrawerSlide(float openRatio, int offsetPixels);
    }

    /**
     * Callback that is invoked when the drawer is in the process of deciding whether it should intercept the touch
     * event. This lets the listener decide if the pointer is on a view that would disallow dragging of the drawer.
     * This is only called when the touch mode is {@link #TOUCH_MODE_FULLSCREEN}.
     */
    public interface OnInterceptMoveEventListener {

        /**
         * Called for each child the pointer i on when the drawer is deciding whether to intercept the touch event.
         *
         * @param v     View to test for draggability
         * @param delta Delta drag in pixels
         * @param x     X coordinate of the active touch point
         * @param y     Y coordinate of the active touch point
         * @return true if view is draggable by delta dx.
         */
        boolean isViewDraggable(View v, int delta, int x, int y);
    }

    public enum Type {
        /**
         * Positions the drawer behind the content.
         */
        BEHIND,

        /**
         * Positions the drawer on top of the content.
         */
        OVERLAY,
    }

    /**
     * Tag used when logging.
     */
    private static final String TAG = "MenuDrawer";

    /**
     * Indicates whether debug code should be enabled.
     */
    private static final boolean DEBUG = false;

    /**
     * The time between each frame when animating the drawer.
     */
    protected static final int ANIMATION_DELAY = 1000 / 60;

    /**
     * The default touch bezel size of the drawer in dp.
     */
    private static final int DEFAULT_DRAG_BEZEL_DP = 24;

    /**
     * The default drop shadow size in dp.
     */
    private static final int DEFAULT_DROP_SHADOW_DP = 6;

    /**
     * Disallow opening the drawer by dragging the screen.
     */
    public static final int TOUCH_MODE_NONE = 0;

    /**
     * Allow opening drawer only by dragging on the edge of the screen.
     */
    public static final int TOUCH_MODE_BEZEL = 1;

    /**
     * Allow opening drawer by dragging anywhere on the screen.
     */
    public static final int TOUCH_MODE_FULLSCREEN = 2;

    /**
     * Indicates that the drawer is currently closed.
     */
    public static final int STATE_CLOSED = 0;

    /**
     * Indicates that the drawer is currently closing.
     */
    public static final int STATE_CLOSING = 1;

    /**
     * Indicates that the drawer is currently being dragged by the user.
     */
    public static final int STATE_DRAGGING = 2;

    /**
     * Indicates that the drawer is currently opening.
     */
    public static final int STATE_OPENING = 4;

    /**
     * Indicates that the drawer is currently open.
     */
    public static final int STATE_OPEN = 8;

    /**
     * Indicates whether to use {@link View#setTranslationX(float)} when positioning views.
     */
    static final boolean USE_TRANSLATIONS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;

    /**
     * Time to animate the indicator to the new active view.
     */
    static final int INDICATOR_ANIM_DURATION = 800;

    /**
     * The maximum animation duration.
     */
    private static final int DEFAULT_ANIMATION_DURATION = 600;

    /**
     * Interpolator used when animating the drawer open/closed.
     */
    protected static final Interpolator SMOOTH_INTERPOLATOR = new SmoothInterpolator();

    /**
     * Interpolator used for stretching/retracting the active indicator.
     */
    protected static final Interpolator INDICATOR_INTERPOLATOR = new AccelerateInterpolator();

    /**
     * Drawable used as menu overlay.
     */
    protected Drawable mMenuOverlay;

    /**
     * Defines whether the drop shadow is enabled.
     */
    protected boolean mDropShadowEnabled = true;

    /**
     * The color of the drop shadow.
     */
    protected int mDropShadowColor;

    /**
     * Drawable used as content drop shadow onto the menu.
     */
    protected Drawable mDropShadowDrawable;

    private boolean mCustomDropShadow;

    /**
     * The size of the content drop shadow.
     */
    protected int mDropShadowSize = dpToPx(DEFAULT_DROP_SHADOW_DP);

    /**
     * The custom menu view set by the user.
     */
    private View mMenuView;

    /**
     * The parent of the menu view.
     */
    protected BuildLayerFrameLayout mMenuContainer;

    /**
     * The parent of the content view.
     */
    protected BuildLayerFrameLayout mContentContainer;

    /**
     * The size of the menu (width or height depending on the gravity).
     */
    protected int mMenuSize = dpToPx(240);

    /**
     * Indicates whether the menu is currently visible.
     */
    protected boolean mMenuVisible;

    /**
     * The current drawer state.
     *
     * @see #STATE_CLOSED
     * @see #STATE_CLOSING
     * @see #STATE_DRAGGING
     * @see #STATE_OPENING
     * @see #STATE_OPEN
     */
    protected int mDrawerState = STATE_CLOSED;

    /**
     * The touch bezel size of the drawer in px.
     */
    protected int mTouchBezelSize = dpToPx(DEFAULT_DRAG_BEZEL_DP);

    /**
     * The touch area size of the drawer in px.
     */
    protected int mTouchSize;

    /**
     * Listener used to dispatch state change events.
     */
    private OnDrawerStateChangeListener mOnDrawerStateChangeListener;

    /**
     * Touch mode for the Drawer.
     * Possible values are {@link #TOUCH_MODE_NONE}, {@link #TOUCH_MODE_BEZEL} or {@link #TOUCH_MODE_FULLSCREEN}
     * Default: {@link #TOUCH_MODE_BEZEL}
     */
    protected int mTouchMode = TOUCH_MODE_BEZEL;

    /**
     * Indicates whether to use {@link View#LAYER_TYPE_HARDWARE} when animating the drawer.
     */
    protected boolean mHardwareLayersEnabled = true;

    /**
     * Bundle used to hold the drawers state.
     */
    protected Bundle mState;

    /**
     * The maximum duration of open/close animations.
     */
    protected int mMaxAnimationDuration = DEFAULT_ANIMATION_DURATION;

    /**
     * Callback that lets the listener override intercepting of touch events.
     */
    protected OnInterceptMoveEventListener mOnInterceptMoveEventListener;

    protected SlideDrawable mSlideDrawable;

    /**
     * The position of the drawer.
     */
    private Position mPosition;

    private Position mResolvedPosition;

    protected boolean mIsStatic;

    protected final Rect mDropShadowRect = new Rect();

    /**
     * Current offset.
     */
    protected float mOffsetPixels;

    /**
     * Whether an overlay should be drawn as the drawer is opened and closed.
     */
    protected boolean mDrawOverlay = true;

    /**
     * Constructs the appropriate MenuDrawer based on the position.
     */
    public static MenuDrawer createMenuDrawer(Context context, Position position, Type type) {

        MenuDrawer drawer;

        if (type == Type.OVERLAY) {
            drawer = new OverlayDrawer(context);
        } else {
            drawer = new SlidingDrawer(context);
        }

        drawer.setPosition(position);
        return drawer;
    }

    public MenuDrawer(Context context) {
        super(context);
        initDrawer(context);
    }

    protected void initDrawer(Context context) {
        setWillNotDraw(false);
        setFocusable(false);

        if (mDropShadowDrawable == null) {
            mDropShadowColor = Color.GRAY;
        } else {
            mCustomDropShadow = true;
        }

        setPosition(Position.fromValue(0));

        mMenuContainer = new NoClickThroughFrameLayout(context);
        mMenuContainer.setBackgroundColor(Color.BLACK);

        mContentContainer = new NoClickThroughFrameLayout(context);
        mContentContainer.setBackgroundColor(Color.WHITE);

        mMenuOverlay = new ColorDrawable(0xFF000000);
    }

    protected int dpToPx(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    protected boolean isViewDescendant(View v) {
        ViewParent parent = v.getParent();
        while (parent != null) {
            if (parent == this) {
                return true;
            }

            parent = parent.getParent();
        }

        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        final int offsetPixels = (int) mOffsetPixels;

        if (mDrawOverlay && offsetPixels != 0) {
            drawOverlay(canvas);
        }
        if (mDropShadowEnabled && (offsetPixels != 0 || mIsStatic)) {
            drawDropShadow(canvas);
        }
    }

    protected abstract void drawOverlay(Canvas canvas);

    private void drawDropShadow(Canvas canvas) {
        // Can't pass the position to the constructor, so wait with loading the drawable until the drop shadow is
        // actually drawn.
        if (mDropShadowDrawable == null) {
            setDropShadowColor(mDropShadowColor);
        }

        updateDropShadowRect();
        mDropShadowDrawable.setBounds(mDropShadowRect);
        mDropShadowDrawable.draw(canvas);
    }

    protected void updateDropShadowRect() {
        // This updates the rect for the static and sliding drawer. The overlay drawer has its own implementation.
        switch (getPosition()) {
            case LEFT:
                mDropShadowRect.top = 0;
                mDropShadowRect.bottom = getHeight();
                mDropShadowRect.right = ViewHelper.getLeft(mContentContainer);
                mDropShadowRect.left = mDropShadowRect.right - mDropShadowSize;
                break;

            case TOP:
                mDropShadowRect.left = 0;
                mDropShadowRect.right = getWidth();
                mDropShadowRect.bottom = ViewHelper.getTop(mContentContainer);
                mDropShadowRect.top = mDropShadowRect.bottom - mDropShadowSize;
                break;

            case RIGHT:
                mDropShadowRect.top = 0;
                mDropShadowRect.bottom = getHeight();
                mDropShadowRect.left = ViewHelper.getRight(mContentContainer);
                mDropShadowRect.right = mDropShadowRect.left + mDropShadowSize;
                break;

            case BOTTOM:
                mDropShadowRect.left = 0;
                mDropShadowRect.right = getWidth();
                mDropShadowRect.top = ViewHelper.getBottom(mContentContainer);
                mDropShadowRect.bottom = mDropShadowRect.top + mDropShadowSize;
                break;
                
            default:
                break;
        }
    }

    private void setPosition(Position position) {
        mPosition = position;
        mResolvedPosition = getPosition();
    }

    protected Position getPosition() {
        final int layoutDirection = ViewHelper.getLayoutDirection(this);

        switch (mPosition) {
            case START:
                if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                    return Position.RIGHT;
                } else {
                    return Position.LEFT;
                }

            case END:
                if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                    return Position.LEFT;
                } else {
                    return Position.RIGHT;
                }
                
            default:
                break;
        }

        return mPosition;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);

        if (!mCustomDropShadow) setDropShadowColor(mDropShadowColor);

        if (getPosition() != mResolvedPosition) {
            mResolvedPosition = getPosition();
            setOffsetPixels(mOffsetPixels * -1);
        }

        if (mSlideDrawable != null) mSlideDrawable.setIsRtl(layoutDirection == LAYOUT_DIRECTION_RTL);

        requestLayout();
        invalidate();
    }

    /**
     * Sets the number of pixels the content should be offset.
     *
     * @param offsetPixels The number of pixels to offset the content by.
     */
    protected void setOffsetPixels(float offsetPixels) {
        final int oldOffset = (int) mOffsetPixels;
        final int newOffset = (int) offsetPixels;

        mOffsetPixels = offsetPixels;

        if (mSlideDrawable != null) {
            final float offset = Math.abs(mOffsetPixels) / mMenuSize;
            mSlideDrawable.setOffset(offset);
        }

        if (newOffset != oldOffset) {
            onOffsetPixelsChanged(newOffset);
            mMenuVisible = newOffset != 0;

            // Notify any attached listeners of the current open ratio
            final float openRatio = ((float) Math.abs(newOffset)) / mMenuSize;
            dispatchOnDrawerSlide(openRatio, newOffset);
        }
    }

    /**
     * Called when the number of pixels the content should be offset by has changed.
     *
     * @param offsetPixels The number of pixels to offset the content by.
     */
    protected abstract void onOffsetPixelsChanged(int offsetPixels);

    /**
     * Toggles the menu open and close with animation.
     */
    public void toggleMenu() {
        toggleMenu(true);
    }

    /**
     * Toggles the menu open and close.
     *
     * @param animate Whether open/close should be animated.
     */
    public abstract void toggleMenu(boolean animate);

    /**
     * Animates the menu open.
     */
    public void openMenu() {
        openMenu(true);
    }

    /**
     * Opens the menu.
     *
     * @param animate Whether open/close should be animated.
     */
    public abstract void openMenu(boolean animate);

    /**
     * Animates the menu closed.
     */
    public void closeMenu() {
        closeMenu(true);
    }

    /**
     * Closes the menu.
     *
     * @param animate Whether open/close should be animated.
     */
    public abstract void closeMenu(boolean animate);

    /**
     * Indicates whether the menu is currently visible.
     *
     * @return True if the menu is open, false otherwise.
     */
    public abstract boolean isMenuVisible();

    /**
     * Set the size of the menu drawer when open.
     *
     * @param size The size of the menu.
     */
    public abstract void setMenuSize(int size);

    /**
     * Returns the size of the menu.
     *
     * @return The size of the menu.
     */
    public int getMenuSize() {
        return mMenuSize;
    }

    /**
     * Compute the touch area based on the touch mode.
     */
    protected void updateTouchAreaSize() {
        if (mTouchMode == TOUCH_MODE_BEZEL) {
            mTouchSize = mTouchBezelSize;
        } else if (mTouchMode == TOUCH_MODE_FULLSCREEN) {
            mTouchSize = getMeasuredWidth();
        } else {
            mTouchSize = 0;
        }
    }

    /**
     * Enables or disables offsetting the menu when dragging the drawer.
     *
     * @param offsetMenu True to offset the menu, false otherwise.
     */
    public abstract void setOffsetMenuEnabled(boolean offsetMenu);

    /**
     * Indicates whether the menu is being offset when dragging the drawer.
     *
     * @return True if the menu is being offset, false otherwise.
     */
    public abstract boolean getOffsetMenuEnabled();

    /**
     * Get the current state of the drawer.
     *
     * @return The state of the drawer.
     */
    public int getDrawerState() {
        return mDrawerState;
    }

    /**
     * Register a callback to be invoked when the drawer state changes.
     *
     * @param listener The callback that will run.
     */
    public void setOnDrawerStateChangeListener(OnDrawerStateChangeListener listener) {
        mOnDrawerStateChangeListener = listener;
    }

    /**
     * Register a callback that will be invoked when the drawer is about to intercept touch events.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnInterceptMoveEventListener(OnInterceptMoveEventListener listener) {
        mOnInterceptMoveEventListener = listener;
    }

    /**
     * Defines whether the drop shadow is enabled.
     *
     * @param enabled Whether the drop shadow is enabled.
     */
    public void setDropShadowEnabled(boolean enabled) {
        mDropShadowEnabled = enabled;
        invalidate();
    }

    protected GradientDrawable.Orientation getDropShadowOrientation() {
        // Gets the orientation for the static and sliding drawer. The overlay drawer provides its own implementation.
        switch (getPosition()) {
            case TOP:
                return GradientDrawable.Orientation.BOTTOM_TOP;

            case RIGHT:
                return GradientDrawable.Orientation.LEFT_RIGHT;

            case BOTTOM:
                return GradientDrawable.Orientation.TOP_BOTTOM;

            default:
                return GradientDrawable.Orientation.RIGHT_LEFT;
        }
    }

    /**
     * Sets the color of the drop shadow.
     *
     * @param color The color of the drop shadow.
     */
    public void setDropShadowColor(int color) {
        GradientDrawable.Orientation orientation = getDropShadowOrientation();

        final int endColor = color & 0x00FFFFFF;
        mDropShadowDrawable = new GradientDrawable(orientation,
                new int[] {
                        color,
                        endColor,
                });
        invalidate();
    }

    /**
     * Sets the drawable of the drop shadow.
     *
     * @param drawable The drawable of the drop shadow.
     */
    public void setDropShadow(Drawable drawable) {
        mDropShadowDrawable = drawable;
        mCustomDropShadow = drawable != null;
        invalidate();
    }

    /**
     * Sets the drawable of the drop shadow.
     *
     * @param resId The resource identifier of the the drawable.
     */
    public void setDropShadow(int resId) {
        setDropShadow(getResources().getDrawable(resId));
    }

    /**
     * Returns the drawable of the drop shadow.
     */
    public Drawable getDropShadow() {
        return mDropShadowDrawable;
    }

    /**
     * Sets the size of the drop shadow.
     *
     * @param size The size of the drop shadow in px.
     */
    public void setDropShadowSize(int size) {
        mDropShadowSize = size;
        invalidate();
    }

    /**
     * Animates the drawer slightly open until the user opens the drawer.
     */
    public abstract void peekDrawer();

    /**
     * Animates the drawer slightly open. If delay is larger than 0, this happens until the user opens the drawer.
     *
     * @param delay The delay (in milliseconds) between each run of the animation. If 0, this animation is only run
     *              once.
     */
    public abstract void peekDrawer(long delay);

    /**
     * Animates the drawer slightly open. If delay is larger than 0, this happens until the user opens the drawer.
     *
     * @param startDelay The delay (in milliseconds) until the animation is first run.
     * @param delay      The delay (in milliseconds) between each run of the animation. If 0, this animation is only run
     *                   once.
     */
    public abstract void peekDrawer(long startDelay, long delay);

    /**
     * Enables or disables the user of {@link View#LAYER_TYPE_HARDWARE} when animations views.
     *
     * @param enabled Whether hardware layers are enabled.
     */
    public abstract void setHardwareLayerEnabled(boolean enabled);

    /**
     * Sets the maximum duration of open/close animations.
     *
     * @param duration The maximum duration in milliseconds.
     */
    public void setMaxAnimationDuration(int duration) {
        mMaxAnimationDuration = duration;
    }

    /**
     * Sets whether an overlay should be drawn when sliding the drawer.
     *
     * @param drawOverlay Whether an overlay should be drawn when sliding the drawer.
     */
    public void setDrawOverlay(boolean drawOverlay) {
        mDrawOverlay = drawOverlay;
    }

    /**
     * Gets whether an overlay is drawn when sliding the drawer.
     *
     * @return Whether an overlay is drawn when sliding the drawer.
     */
    public boolean getDrawOverlay() {
        return mDrawOverlay;
    }

    /**
     * Returns the ViewGroup used as a parent for the menu view.
     *
     * @return The menu view's parent.
     */
    public ViewGroup getMenuContainer() {
        return mMenuContainer;
    }

    /**
     * Returns the ViewGroup used as a parent for the content view.
     *
     * @return The content view's parent.
     */
    public ViewGroup getContentContainer() {
            return mContentContainer;
    }

    /**
     * Set the menu view from a layout resource.
     *
     * @param layoutResId Resource ID to be inflated.
     */
    public void setMenuView(int layoutResId) {
        mMenuContainer.removeAllViews();
        mMenuView = LayoutInflater.from(getContext()).inflate(layoutResId, mMenuContainer, false);
        mMenuContainer.addView(mMenuView);
    }

    /**
     * Set the menu view to an explicit view.
     *
     * @param view The menu view.
     */
    public void setMenuView(View view) {
        setMenuView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * Set the menu view to an explicit view.
     *
     * @param view   The menu view.
     * @param params Layout parameters for the view.
     */
    public void setMenuView(View view, LayoutParams params) {
        mMenuView = view;
        mMenuContainer.removeAllViews();
        mMenuContainer.addView(view, params);
    }

    /**
     * Returns the menu view.
     *
     * @return The menu view.
     */
    public View getMenuView() {
        return mMenuView;
    }

    /**
     * Set the content from a layout resource.
     *
     * @param layoutResId Resource ID to be inflated.
     */
    public void setContentView(int layoutResId) {
        mContentContainer.removeAllViews();
        LayoutInflater.from(getContext()).inflate(layoutResId, mContentContainer, true);
    }

    /**
     * Set the content to an explicit view.
     *
     * @param view The desired content to display.
     */
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * Set the content to an explicit view.
     *
     * @param view   The desired content to display.
     * @param params Layout parameters for the view.
     */
    public void setContentView(View view, LayoutParams params) {
        mContentContainer.removeAllViews();
        mContentContainer.addView(view, params);
    }

    protected void setDrawerState(int state) {
        if (state != mDrawerState) {
            final int oldState = mDrawerState;
            mDrawerState = state;
            if (mOnDrawerStateChangeListener != null) mOnDrawerStateChangeListener.onDrawerStateChange(oldState, state);
            if (DEBUG) logDrawerState(state);
        }
    }

    protected void logDrawerState(int state) {
        switch (state) {
            case STATE_CLOSED:
                Log.d(TAG, "[DrawerState] STATE_CLOSED");
                break;

            case STATE_CLOSING:
                Log.d(TAG, "[DrawerState] STATE_CLOSING");
                break;

            case STATE_DRAGGING:
                Log.d(TAG, "[DrawerState] STATE_DRAGGING");
                break;

            case STATE_OPENING:
                Log.d(TAG, "[DrawerState] STATE_OPENING");
                break;

            case STATE_OPEN:
                Log.d(TAG, "[DrawerState] STATE_OPEN");
                break;

            default:
                Log.d(TAG, "[DrawerState] Unknown: " + state);
        }
    }

    /**
     * Returns the touch mode.
     */
    public abstract int getTouchMode();

    /**
     * Sets the drawer touch mode. Possible values are {@link #TOUCH_MODE_NONE}, {@link #TOUCH_MODE_BEZEL} or
     * {@link #TOUCH_MODE_FULLSCREEN}.
     *
     * @param mode The touch mode.
     */
    public abstract void setTouchMode(int mode);

    /**
     * Sets the size of the touch bezel.
     *
     * @param size The touch bezel size in px.
     */
    public abstract void setTouchBezelSize(int size);

    /**
     * Returns the size of the touch bezel in px.
     */
    public abstract int getTouchBezelSize();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void postOnAnimation(Runnable action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.postOnAnimation(action);
        } else {
            postDelayed(action, ANIMATION_DELAY);
        }
    }

    protected void dispatchOnDrawerSlide(float openRatio, int offsetPixels) {
        if (mOnDrawerStateChangeListener != null) {
            mOnDrawerStateChangeListener.onDrawerSlide(openRatio, offsetPixels);
        }
    }

    /**
     * Saves the state of the drawer.
     *
     * @return Returns a Parcelable containing the drawer state.
     */
    public final Parcelable saveState() {
        if (mState == null) mState = new Bundle();
        saveState(mState);
        return mState;
    }

    void saveState(Bundle state) {
        // State saving isn't required for subclasses.
    }

    /**
     * Restores the state of the drawer.
     *
     * @param in A parcelable containing the drawer state.
     */
    public void restoreState(Parcelable in) {
        mState = (Bundle) in;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState state = new SavedState(superState);

        if (mState == null) mState = new Bundle();
        saveState(mState);

        state.mState = mState;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        restoreState(savedState.mState);
    }

    static class SavedState extends BaseSavedState {

        Bundle mState;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel in) {
            super(in);
            mState = in.readBundle();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(mState);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
