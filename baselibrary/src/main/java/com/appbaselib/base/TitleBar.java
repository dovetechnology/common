package com.appbaselib.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pangu.appbaselibrary.R;

import java.util.LinkedList;

/**
 * ===============================
 * 描    述：自定义TitleBar
 * ===============================
 */
public class TitleBar extends ViewGroup implements View.OnClickListener {

    private static final int DEFAULT_MAIN_TEXT_SIZE = 18;
    private static final int DEFAULT_SUB_TEXT_SIZE = 12;
    private static final int DEFAULT_ACTION_TEXT_SIZE = 14;
    private static final int DEFAULT_TITLE_BAR_HEIGHT = 48;

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    //    private TextView mLeftText;
    private ImageView mLeftImage;
    private LinearLayout mRightLayout;
    private LinearLayout mCenterLayout;
    private TextView mCenterText;
    private TextView mSubTitleText;
    private View mCustomCenterView;
    private View mDividerView;

    private boolean mImmersive;

    private int mScreenWidth;
    private int mStatusBarHeight;
    private int mActionPadding;
    private int mOutPadding;
    private int mActionTextColor;
    private int mHeight;

    private int DEFAULT_TITLE_COLOR = Color.parseColor("#FF686371");
    private int DEFAULT_DIVIDER_COLOR = Color.parseColor("#FFF1EEF6");

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mImmersive = false;
        } else {
            mImmersive = false;
        }
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        }
        mActionPadding = dip2px(5);
        mOutPadding = dip2px(1);
        mHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_48);
        initView(context);
    }

    private void initView(Context context) {
//        mLeftText = new TextView(context);
        mLeftImage = new ImageView(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mDividerView = new View(context);
        mDividerView.setBackgroundColor(DEFAULT_DIVIDER_COLOR);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

//        mLeftText.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
//        mLeftText.setSingleLine();
//        mLeftText.setGravity(Gravity.CENTER_VERTICAL);
//        mLeftText.setPadding(mOutPadding, 0, mOutPadding, 0);

        mLeftImage.setImageResource(R.drawable.return_icon_1);

        mLeftImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        int padding = getResources().getDimensionPixelSize(R.dimen.dp_12);
        mLeftImage.setPadding(padding, padding, padding, padding);

        mCenterText = new TextView(context);
        mSubTitleText = new TextView(context);
        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);

        mCenterLayout.setGravity(Gravity.CENTER);
        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.sp_17));
        mCenterText.setSingleLine();
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setEllipsize(TextUtils.TruncateAt.END);
        mCenterText.setTextColor(DEFAULT_TITLE_COLOR);

        mSubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);
        mSubTitleText.setTextColor(DEFAULT_TITLE_COLOR);

        mRightLayout.setPadding(mOutPadding, 0, mOutPadding, 0);

//        addView(mLeftText, layoutParams);
        LayoutParams leftImageParams = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.dp_44), LayoutParams.MATCH_PARENT);
        addView(mLeftImage, leftImageParams);
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
    }

    public void setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
    }

    public void setHeight(int height) {
        mHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    public int getTitleBarHeight() {
        return mHeight;
    }

    public void setLeftImageResource(int resId) {
//        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        mLeftImage.setImageResource(resId);
    }

    public void setLeftClickListener(OnClickListener l) {
//        mLeftText.setOnClickListener(l);
        mLeftImage.setOnClickListener(l);
    }

    public ImageView getLeftImageView() {
        return mLeftImage;
    }

    public TextView getTitleTextView() {
        return mCenterText;
    }

//    public void setLeftText(CharSequence title) {
//        mLeftText.setText(title);
//    }
//
//    public void setLeftText(int resid) {
//        mLeftText.setText(resid);
//    }
//
//    public void setLeftTextSize(float size) {
//        mLeftText.setTextSize(size);
//    }
//
//    public void setLeftTextColor(int color) {
//        mLeftText.setTextColor(color);
//    }

    public void setLeftVisible(boolean visible) {
//        mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
        mLeftImage.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitle(CharSequence title) {
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), " " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
    }

    private void setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
    }

    public void setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
    }

    public void setTitle(int resid) {
        setTitle(getResources().getString(resid));
    }

    public void setTitleColor(int resid) {
        mCenterText.setTextColor(resid);
    }

    public void setTitleBackground(int resid) {
        mCenterText.setBackgroundResource(resid);
    }

    public void setSubTitleColor(int resid) {
        mSubTitleText.setTextColor(resid);
    }

    public void setCustomTitle(View titleView) {
//        mCenterText.setVisibility(View.GONE);
//        addView(titleView);

        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
    }

    public void setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
    }

    public void setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
    }

    public void setActionTextColor(int colorResId) {
        mActionTextColor = colorResId;
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public void setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     */
    public void addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public View addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index);
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public View addAction(Action action, int index) {
        LinearLayout.LayoutParams params;
        if (TextUtils.isEmpty(action.getText())) {
            params = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.dp_44), LayoutParams.MATCH_PARENT);
        } else {
            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
        View view = inflateAction(action);
        mRightLayout.addView(view, index, params);
        return view;
    }

    public LinearLayout.LayoutParams addAction(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mRightLayout.addView(view, mRightLayout.getChildCount(), params);
        return params;
    }

    public TextView setRightTitle(String text, Context mActivity) {
        TextView textView = new TextView(mActivity);
        int dp15 = getResources().getDimensionPixelOffset(R.dimen.dp_16);
        textView.setPadding(dp15, 0, dp15, 0);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sp_14));
        LinearLayout.LayoutParams params = this.addAction(textView);
        params.gravity = Gravity.CENTER_VERTICAL;
        return  textView;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    public View getActionView(int index) {
        return mRightLayout.getChildAt(index);
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        View view = null;
        if (TextUtils.isEmpty(action.getText())) {
            ImageView img = new ImageView(getContext());
            img.setImageResource(action.getDrawable());
            view = img;
            view.setPadding(mActionPadding, 0, mActionPadding, 0);
        } else {
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setPadding(dip2px(10), 0, dip2px(10), 0);
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sp_14_5));
            if (mActionTextColor != 0) {
                text.setTextColor(mActionTextColor);
            }
            view = text;
        }

        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    public View getViewByAction(Action action) {
        View view = findViewWithTag(action);
        return view;
    }

    public void setActionPadding(int actionPadding) {
        this.mActionPadding = actionPadding;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }

        measureChild(mLeftImage, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        if (mLeftImage.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftImage.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftImage.layout(0, mStatusBarHeight, mLeftImage.getMeasuredWidth(), mLeftImage.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        if (mLeftImage.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftImage.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftImage.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    public static int dip2px(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public ImageView setRightImageButton(Context mActivity,int res) {
        ImageView textView = new ImageView(mActivity);
        int dp15 = getResources().getDimensionPixelOffset(R.dimen.dp_15);
        textView.setPadding(dp15,dp15,dp15,dp15);
        textView.setImageResource(res);
        LinearLayout.LayoutParams params = this.addAction(textView);
        params.gravity = Gravity.CENTER_VERTICAL;
        return  textView;
    }


    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {
        String getText();

        int getDrawable();

        void performAction(View view);
    }

    public static abstract class ImageAction implements Action {
        private int mDrawable;

        public ImageAction(int drawable) {
            mDrawable = drawable;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;

        public TextAction(String text) {
            mText = text;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }
    }

}
