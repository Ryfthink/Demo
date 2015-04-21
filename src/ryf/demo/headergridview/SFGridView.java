package ryf.demo.headergridview;

import java.util.Stack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.OverScroller;

public class SFGridView extends AdapterView<ListAdapter> implements IFocusable {
	public static final int OVER_SCROLL_ALWAYS = 0;
	public static final int OVER_SCROLL_TOP = 1;
	public static final int OVER_SCROLL_BOTTOM = 2;
	public static final int OVER_SCROLL_NEVER = 3;
	public static final int AUTO_FIT = -1;

	// user set data
	private int mNumColumns = AUTO_FIT;
	private int mColumnWidth;
	private int mRowHeight;
	private int mHorizontalSpacing;
	private int mVerticalSpacing;
	private int mGravity = Gravity.START;
	private int mTopOffset;
	private int mBottomOffset;
	private int mLeftOffset;
	private Animation mEdgeAnimTop;
	private Animation mEdgeAnimBottom;

	// inner data
	private int mFirstPosition;
	private int mSelectedPosition;
	private boolean mDataChanged;
	private int mItemCount;
	private ListAdapter mAdapter;

	// flag
	private boolean mIsBeingDragged;
	private long mLastLongPress;
	private long mLastScroll;

	private int mFocusOffset = 20;// 焦点偏移
	private int mFocusOffsetTop = 0; //焦点在mFocusOffset的基础上的偏移
	private int mFocusOffsetBottom = 0; //焦点在mFocusOffset的基础上的偏移
	private int mFocusOffsetRight = 0; //焦点在mFocusOffset的基础上的偏移
	private int mFocusOffsetLeft = 0; //焦点在mFocusOffset的基础上的偏移

	public boolean mIncludeAnimScale = true;// 是否使用动画后的缩放参数
	
	private OverScroller mScroller;
	private GestureDetector mGestureDetector;

	private SFScrollbar mScrollbar;

	final RecycleBin mRecycler = new RecycleBin();

	private Drawable mFocusDrawable;

	private OnItemMenuClickListener mOnItemMenuClickListener;

	private OnOutOfEdgeListener mOnOutOfEdgeListener;
	
	private boolean isNeedFocusAnimation = true;

	public SFGridView(Context context) {
		super(context);
		initView();
	}

	public SFGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	@SuppressLint("NewApi")
	private void initView() {
		mScroller = new OverScroller(getContext());
		mGestureDetector = new GestureDetector(getContext(), mGestureListener);
		setOnKeyListener(mOnKeyListener);
		super.setOverScrollMode(View.OVER_SCROLL_NEVER);
		if(isInEditMode()){
			EditmodeUtil.testSFGridView(this);
		}
	}

	private OnKeyListener mOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode) {
					case KeyEvent.KEYCODE_MENU:
						if (mOnItemMenuClickListener != null) {
							mOnItemMenuClickListener.onItemMenuClick(SFGridView.this, getSelectedView(), getSelectedItemPosition(), getSelectedItemId());
						}
						return true;
				}
			}
			return false;
		}
	};

	public void setHorizontalSpacing(int horizontalSpacing) {
		if (horizontalSpacing != mHorizontalSpacing) {
			mHorizontalSpacing = horizontalSpacing;
			requestLayout();
		}
	}

	public int getHorizontalSpacing() {
		return mHorizontalSpacing;
	}

	public void setVerticalSpacing(int verticalSpacing) {
		if (verticalSpacing != mVerticalSpacing) {
			mVerticalSpacing = verticalSpacing;
			requestLayout();
		}
	}

	public int getVerticalSpacing() {
		return mVerticalSpacing;
	}

	public void setColumnWidth(int columnWidth) {
		if (columnWidth != mColumnWidth) {
			mColumnWidth = columnWidth;
			requestLayout();
		}
	}

	public int getColumnWidth() {
		return mColumnWidth;
	}

	public void setRowHeight(int rowHeight) {
		if (rowHeight != mRowHeight) {
			mRowHeight = rowHeight;
			requestLayout();
		}
	}

	public int getRowHeight() {
		return mRowHeight;
	}

	public void setNumColumns(int numColumns) {
		if (numColumns != mNumColumns) {
			mNumColumns = numColumns;
			requestLayout();
		}
	}

	public int getNumColumns() {
		return mNumColumns;
	}

	public void setGravity(int gravity) {
		if (mGravity != gravity) {
			mGravity = gravity;
			requestLayout();
		}
	}

	public int getGravity() {
		return mGravity;
	}

	public void setOffset(int top, int bottom) {
		if (mTopOffset != top || mBottomOffset != bottom) {
			mTopOffset = top;
			mBottomOffset = bottom;
			requestLayout();
		}
	}

	@Override
	public void setOverScrollMode(int overScrollMode) {
		switch (overScrollMode) {
			case OVER_SCROLL_ALWAYS:
				mEdgeAnimTop = AnimUtil.newEdgeAnimation(KeyEvent.KEYCODE_DPAD_UP);
				mEdgeAnimBottom = AnimUtil.newEdgeAnimation(KeyEvent.KEYCODE_DPAD_DOWN);
				break;
			case OVER_SCROLL_TOP:
				mEdgeAnimTop = AnimUtil.newEdgeAnimation(KeyEvent.KEYCODE_DPAD_UP);
				mEdgeAnimBottom = null;
				break;
			case OVER_SCROLL_BOTTOM:
				mEdgeAnimTop = null;
				mEdgeAnimBottom = AnimUtil.newEdgeAnimation(KeyEvent.KEYCODE_DPAD_DOWN);
				break;
			default:
				mEdgeAnimTop = null;
				mEdgeAnimBottom = null;
				break;
		}
	}

	public void setScrollbar(SFScrollbar scrollbar) {
		mScrollbar = scrollbar;
		if (mScrollbar != null) {
			mScrollbar.setTrackLength(getRealHeight() - getPaddingTop() - getPaddingBottom());
			mScrollbar.setThumbLength(getHeight() - getPaddingTop() - getPaddingBottom());
			mScrollbar.scrollTo(getScrollY());
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (mNumColumns <= 0) {
			int specWidth = MeasureSpec.getSize(widthMeasureSpec);
			try {
				if (specWidth > 0) {
					mNumColumns = (specWidth - getPaddingLeft() - getPaddingRight() + mHorizontalSpacing) / (mColumnWidth + mHorizontalSpacing);
				} else {
					mNumColumns = 2;
				}
			} catch (ArithmeticException e) {
				e.printStackTrace();
			}

		}

		int width = mColumnWidth * mNumColumns + mHorizontalSpacing * (mNumColumns - 1) + getPaddingLeft() + getPaddingRight();
		int height = MeasureSpec.getSize(heightMeasureSpec);

		setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
	}

	@SuppressLint("NewApi")
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			scrollTo(x, y);
			awakenScrollBars();
		}
	}

	private int getRealHeight() {
		int rowCount = (mItemCount - 1) / mNumColumns + 1;
		int realH = getPaddingTop() + getPaddingBottom() + mTopOffset + mBottomOffset + mRowHeight * rowCount + mVerticalSpacing * (rowCount - 1);

		if (realH < getHeight()) {
			realH = getHeight();
		}

		return realH;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed || mDataChanged) {
			mDataChanged = false;

			removeAllViewsInLayout();

			int leftOffset = 0;

			// final int layoutDirection = getLayoutDirection();
			final int absoluteGravity = Gravity.getAbsoluteGravity(mGravity, /* layoutDirection */0);
			switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
				case Gravity.LEFT:
					leftOffset = 0;
					break;
				case Gravity.CENTER_HORIZONTAL:
					leftOffset = (r - l - getPaddingLeft() - getPaddingRight() - mColumnWidth * mNumColumns - mHorizontalSpacing * (mNumColumns - 1)) / 2;
					break;
				case Gravity.RIGHT:
					leftOffset = r - l - getPaddingLeft() - getPaddingRight() - mColumnWidth * mNumColumns - mHorizontalSpacing * (mNumColumns - 1);
					break;
				default:
					leftOffset = 0;
					break;
			}

			mLeftOffset = leftOffset;

			scrollTo(getScrollX(), getScrollY());

			if (isFocused() && mSelectedPosition < 0) {
				setSelection(0);
			} else if (isFocused() && mSelectedPosition >= mItemCount) {
				setSelection(mItemCount - 1);
			} else if (isFocused()) {
				setSelection(mSelectedPosition);
			}

			if (mScrollbar != null) {
				mScrollbar.setTrackLength(getRealHeight() - getPaddingTop() - getPaddingBottom());
				mScrollbar.setThumbLength(getHeight() - getPaddingTop() - getPaddingBottom());
			}
		}
	}

	private void layoutChildren(int firstPosition, int count) {
		final int oldCount = getChildCount();
		if (firstPosition == mFirstPosition && oldCount == count) {
			return;
		}
		final int start = Math.min(mFirstPosition, firstPosition);
		final int end = Math.max(mFirstPosition + oldCount, firstPosition + count);

		Stack<View> removed = new Stack<View>();

		for (int i = start; i < end && i < mItemCount; i++) {
			boolean isNew = i >= firstPosition && i < firstPosition + count;
			boolean isOld = i >= mFirstPosition && i < mFirstPosition + oldCount;
			if (isOld && !isNew) {
				View child = getChildAt(i - mFirstPosition);
				removed.push(child);
			}
		}

		while (!removed.empty()) {
			View child = removed.pop();
			if (child != null) {
				child.setSelected(false);
				child.clearAnimation();
				removeViewInLayout(child);
				mRecycler.addScrapView(child);
			}
		}

		for (int i = start; i < end && i < mItemCount; i++) {
			boolean isNew = i >= firstPosition && i < firstPosition + count;
			boolean isOld = i >= mFirstPosition && i < mFirstPosition + oldCount;
			if (!isOld && isNew) {
				int row = i / mNumColumns;
				int column = i % mNumColumns;

				int left = getPaddingLeft() + mLeftOffset + mColumnWidth * column + mHorizontalSpacing * column;
				int top = getPaddingTop() + mTopOffset + mRowHeight * row + mVerticalSpacing * row;

				View child = mAdapter.getView(i, mRecycler.getScrapView(), this);
				if (child != null) {
					child.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, mColumnWidth), MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, mRowHeight));
					addViewInLayout(child, i < mFirstPosition ? i - firstPosition : -1, null, true);
					child.layout(left, top, left + mColumnWidth, top + mRowHeight);
				}
			}
		}

		mFirstPosition = firstPosition;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mIsBeingDragged = true;
				break;
			case MotionEvent.ACTION_UP:
				mIsBeingDragged = false;
				break;
		}

		return mGestureDetector.onTouchEvent(event);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		boolean handled = false;

		if ((event.getSource() & InputDevice.SOURCE_CLASS_POINTER) != 0) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_SCROLL: {
					if (!mIsBeingDragged) {
						final float vscroll = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
						if (vscroll != 0) {
							final int delta = (int) (vscroll * 20/*
																 * getVerticalScrollFactor
																 * ()
																 */);
							final int range = getRealHeight() - getHeight();
							int oldScrollY = getScrollY();
							int newScrollY = oldScrollY - delta;
							if (newScrollY < 0) {
								newScrollY = 0;
							} else if (newScrollY > range) {
								newScrollY = range;
							}
							if (newScrollY != oldScrollY) {
								scrollTo(getScrollX(), newScrollY);
								handled = true;
							}
						}
					}
					break;
				}
			}
		}

		return handled || super.onGenericMotionEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean handled = false;

		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (mSelectedPosition > 0) {
					setSelection(mSelectedPosition - 1);
					handled = true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				if (mSelectedPosition >= mNumColumns) {
					setSelection(mSelectedPosition - mNumColumns);
					handled = true;
				} else {
					if (mOnOutOfEdgeListener != null) {
						mOnOutOfEdgeListener.onOutOfEdge(OnOutOfEdgeListener.DIRECTION_UP);
						handled = true;
					}
					if (mEdgeAnimTop != null) {
						startAnimation(AnimUtil.newEdgeAnimation(event.getKeyCode()));
						handled = true;
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (mSelectedPosition + 1 < mItemCount) {
					setSelection(mSelectedPosition + 1);
					handled = true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (mSelectedPosition / mNumColumns < (mItemCount - 1) / mNumColumns) {
					setSelection(mSelectedPosition + mNumColumns < mItemCount ? mSelectedPosition + mNumColumns : mItemCount - 1);
					handled = true;
				} else {
					if (mOnOutOfEdgeListener != null) {
						mOnOutOfEdgeListener.onOutOfEdge(OnOutOfEdgeListener.DIRECTION_DOWN);
					}
					if (mEdgeAnimBottom != null) {
						startAnimation(AnimUtil.newEdgeAnimation(event.getKeyCode()));
						handled = true;
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_NUMPAD_ENTER:
			case KeyEvent.KEYCODE_ENTER:
				event.startTracking();
				handled = true;
				break;
		}

		return handled || super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean handled = false;

		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_NUMPAD_ENTER:
			case KeyEvent.KEYCODE_ENTER:
				long duration = event.getEventTime() - event.getDownTime();
				if (event.isTracking() && event.getDownTime() > mLastLongPress && duration < ViewConfiguration.getLongPressTimeout()) {
					performItemClick(getSelectedView(), getSelectedItemPosition(), getSelectedItemId());
				}
				handled = true;
				break;
		}

		return handled || super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		boolean handled = false;

		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_NUMPAD_ENTER:
			case KeyEvent.KEYCODE_ENTER:
				mLastLongPress = event.getEventTime();
				performItemLongClick(getSelectedView(), getSelectedItemPosition(), getSelectedItemId());
				handled = true;
				break;
		}

		return handled || super.onKeyLongPress(keyCode, event);
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		if (view != null) {
			return super.performItemClick(view, position, id);
		}

		return false;
	}

	public boolean performItemLongClick(View view, int position, long id) {
		OnItemLongClickListener l = getOnItemLongClickListener();
		if (l != null) {
			playSoundEffect(SoundEffectConstants.CLICK);
			if (view != null) {
				view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
				l.onItemLongClick(this, view, position, id);
			}
			return true;
		}

		return false;
	}

	@Override
	public ListAdapter getAdapter() {
		return mAdapter;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
		}

		reset();
		mRecycler.clear();
		mAdapter = adapter;

		if (mAdapter != null) {
			mDataChanged = true;
			mItemCount = getAdapter().getCount();
			mAdapter.registerDataSetObserver(mDataSetObserver);
		}

		checkFocus();
		requestLayout();
	}

	protected void reset() {
		removeAllViewsInLayout();
		mFirstPosition = 0;
		mDataChanged = false;
		mSelectedPosition = 0;
		super.scrollTo(0, 0);
		invalidate();
	}

	protected void checkFocus() {
		final Adapter adapter = getAdapter();
		final boolean empty = adapter == null || adapter.getCount() == 0;
		final boolean focusable = !empty;
		super.setFocusable(focusable);
	}

	private DataSetObserver mDataSetObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			mDataChanged = true;
			mItemCount = getAdapter().getCount();
			checkFocus();
			requestLayout();

			// if (isFocused()) {
			// if (mSelectedPosition >= mItemCount) {
			// mSelectedPosition = mItemCount - 1;
			// } else if (mSelectedPosition < 0) {
			// mSelectedPosition = 0;
			// }
			// getViewTreeObserver().addOnGlobalLayoutListener(new
			// OnGlobalLayoutListener() {
			// @Override
			// public void onGlobalLayout() {
			// getViewTreeObserver().removeGlobalOnLayoutListener(this);
			// if (getChildCount() > 0) {
			// setSelection(mSelectedPosition);
			// }
			// }
			// });
			// }
		}

		@Override
		public void onInvalidated() {
			mDataChanged = true;
			mItemCount = 0;
			requestLayout();
		}
	};

	@Override
	public View getSelectedView() {
		if (mItemCount > 0 && mSelectedPosition >= 0) {
			return getChildAt(mSelectedPosition - mFirstPosition);
		} else {
			return null;
		}
	}

	@Override
	public int getSelectedItemPosition() {
		return mSelectedPosition;
	}

	@Override
	public long getSelectedItemId() {
		return 0;
	}

	@Override
	public void setSelection(int position) {
		onFocusChanged(false, 0, 0);
		mSelectedPosition = position;

		View selected = getSelectedView();
		if (selected != null) {
			int dy = 0;
			int top = selected.getTop();
			int bottom = selected.getBottom();
			int scrollY = getScrollY();
			if (top < scrollY + getPaddingTop() + mTopOffset) {
				dy = top - scrollY - getPaddingTop() - mTopOffset;
			} else if (bottom > scrollY + getHeight() - getPaddingBottom() - mBottomOffset) {
				dy = getPaddingBottom() + bottom + mBottomOffset - scrollY - getHeight();
			}

			onFocusChanged(true, 0, -dy);
		} else {
			scrollTo(getScrollX(), getScrollYBySelection(position));
			onFocusChanged(true, 0, 0);
		}

		OnItemSelectedListener l = getOnItemSelectedListener();
		if (l != null && selected != null) {
			l.onItemSelected(this, selected, mSelectedPosition, 0);
		} else if (l != null) {
			l.onNothingSelected(this);
		}
	}

	private int getScrollYBySelection(int selection) {
		int topScrollY = (selection / mNumColumns) * (mRowHeight + mVerticalSpacing);
		int bottomScrollY = topScrollY + mRowHeight - getHeight() + getPaddingTop() + getPaddingBottom() + mTopOffset + mBottomOffset;
		if (bottomScrollY < 0) {
			bottomScrollY = 0;
		}

		int min = Math.min(topScrollY, bottomScrollY);
		int max = Math.max(topScrollY, bottomScrollY);

		final int scrollY = getScrollY();
		if (scrollY < min) {
			return min;
		} else if (scrollY > max) {
			return max;
		} else {
			return scrollY;
		}
	}

	public void scrollTo(int x, int y) {
		int firstPosition = 0;
		if (y > mTopOffset + mRowHeight / 2) {
			int firstRow = (y - mTopOffset - mRowHeight / 2) / (mRowHeight + mVerticalSpacing);
			firstPosition = firstRow * mNumColumns;
		}

		int lastRow;
		try {
			lastRow = (y - mTopOffset + getHeight() - getPaddingTop() - getPaddingBottom() + mRowHeight / 2 + mVerticalSpacing) / (mRowHeight + mVerticalSpacing);
		} catch (ArithmeticException e) {
			return;
		}
		int count = (lastRow + 1) * mNumColumns > mItemCount ? mItemCount - firstPosition : (lastRow + 1) * mNumColumns - firstPosition;
		layoutChildren(firstPosition, count);

		super.scrollTo(x, y);
		if (mScrollbar != null) {
			mScrollbar.scrollTo(y);
		}
	};

	@SuppressLint("NewApi")
	public final void smoothScrollBy(int dx, int dy) {
		if (getChildCount() == 0) {
			return;
		}

		final int scrollY = getScrollY();

		int minY = 0;
		int maxY = getRealHeight() - getHeight() + minY;
		if (scrollY + dy < minY) {
			dy = minY - scrollY;
		} else if (scrollY + dy > maxY) {
			dy = maxY - scrollY;
		}

		long duration = AnimationUtils.currentAnimationTimeMillis() - mLastScroll;
		if (duration > 250) {
			mScroller.startScroll(getScrollX(), scrollY, 0, dy, 250);
			invalidate();
		} else {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			scrollBy(dx, dy);
		}
		mLastScroll = AnimationUtils.currentAnimationTimeMillis();
	}

	public final void smoothScrollTo(int x, int y) {
		smoothScrollBy(x - getScrollX(), y - getScrollY());
	}

	@SuppressLint("NewApi")
	public void fling(int velocityY) {
		if (getChildCount() > 0) {
			int minY = 0;
			int maxY = getRealHeight() - getHeight() + minY;
			int height = getHeight() - getPaddingTop() - getPaddingBottom();

			mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, minY, maxY, 0, height / 4);
			invalidate();
		}
	}

	private Rect getLastFocusRect() {
		ViewParent parent = this;
		do {
			parent = parent.getParent();
		} while (!(parent instanceof IParent));

		return ((IParent) parent).getCurrentRect();
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if (gainFocus) {
			int pos = ((getScrollY() + mVerticalSpacing) / (mRowHeight + mVerticalSpacing)) * mNumColumns;
			setSelection(mSelectedPosition);
		} else {
			onFocusChanged(false, 0, 0);
		}
	}

	@Override
	public void onFocusChanged(boolean focused, int dx, int dy) {
		int scrollY = getScrollY();
		int minY = 0;
		int maxY = getRealHeight() - getHeight() + minY;
		if (scrollY - dy < minY) {
			dy = scrollY - minY;
		} else if (scrollY - dy > maxY) {
			dy = scrollY - maxY;
		}

		Rect targetRect = getSelectedRect();
		if (targetRect != null) {
			targetRect.offset(dx, dy);
		}

		// scroll
		if (focused) {
			smoothScrollBy(0, -dy);
		}

		// frame
		IParent p = FocusUtil.getParent(this);
		if (p != null) {
			p.notifyFocusChange(focused, this, targetRect);
		}

		View child = getSelectedView();
		if (null != child) {
			child.setSelected(focused);

			// scale
			if(isNeedFocusAnimation){
				if (focused) {
					child.startAnimation(AnimUtil.newScaleAnimation(1.1f));
				} else {
					child.clearAnimation();
				}
			}
		}
	}

	public void setFocusOffest(int offset) {
		mFocusOffset = offset;
	}
	
	public void setFocusOffsetOther(int offsetTop , int offsetBottom , int offsetRight , int offsetLeft){
		mFocusOffsetTop = offsetTop; //焦点在mFocusOffset的基础上的偏移
		mFocusOffsetBottom = offsetBottom; //焦点在mFocusOffset的基础上的偏移
		mFocusOffsetRight = offsetRight; //焦点在mFocusOffset的基础上的偏移
		mFocusOffsetLeft = offsetLeft; //焦点在mFocusOffset的基础上的偏移

	}

	public void setIncludeAnimScale(boolean include) {
		mIncludeAnimScale = include;
	}
	
	public void setNeedFocusAnimation(boolean isNeedFocusAnimation){
		this.isNeedFocusAnimation = isNeedFocusAnimation;
	}

	@Override
	public Rect getSelectedRect() {
		Rect rect = FocusUtil.getFocusedRectByView(getSelectedView());

		if (rect != null && !rect.isEmpty()) {
			if (mIncludeAnimScale) {
				FocusUtil.scaleRect(rect, 1.1f);
			}
			rect.left -= (mFocusOffset + mFocusOffsetLeft);
			rect.top -= (mFocusOffset + mFocusOffsetTop);
			rect.right += (mFocusOffset + mFocusOffsetRight);
			rect.bottom += (mFocusOffset + mFocusOffsetBottom);
		}

		return rect;
	}

	@Override
	public Drawable getFocusedBackground() {
		return mFocusDrawable == null ? FocusUtil.getDefaultFocusedBackground(getResources()) : mFocusDrawable;
	}

	public void setFocusDrawable(Drawable drawable) {
		this.mFocusDrawable = drawable;
	}

	private OnGestureListener mGestureListener = new SimpleOnGestureListener() {

		public boolean onDown(MotionEvent e) {
			return true;
		};

		public boolean onSingleTapConfirmed(MotionEvent e) {
			int position = getPositionByXY((int) e.getX(), (int) e.getY());
			if (position >= 0 && position < mItemCount) {
				performItemClick(getChildAt(position - mFirstPosition), position, 0);
			}
			return true;
		};

		public void onLongPress(MotionEvent e) {
			int position = getPositionByXY((int) e.getX(), (int) e.getY());
			if (position >= 0 && position < mItemCount) {
				performItemLongClick(getChildAt(position - mFirstPosition), position, 0);
			}
		};

		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			smoothScrollBy(0, (int) distanceY);
			return true;
		};

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			fling((int) -velocityY);
			return true;
		};

		private int getPositionByXY(int x, int y) {
			int position = -1;

			x = x + getPaddingLeft() - mLeftOffset;
			y = y + getPaddingTop() - mTopOffset + getScrollY();

			if (x > 0 && y > 0 && x % (mColumnWidth + mHorizontalSpacing) <= mColumnWidth && y % (mRowHeight + mVerticalSpacing) <= mRowHeight) {
				int row = y / (mRowHeight + mVerticalSpacing);
				int column = x / (mColumnWidth + mHorizontalSpacing);

				position = row * mNumColumns + column;
			}

			return position;
		}

	};

	private static class RecycleBin {

		private Stack<View> mScrapViews;

		public RecycleBin() {
			mScrapViews = new Stack<View>();
		}

		public View getScrapView() {
			View ret = null;

			if (!mScrapViews.empty()) {
				ret = mScrapViews.pop();
			}

			return ret;
		}

		public void addScrapView(View v) {
			mScrapViews.push(v);
		}

		public void clear() {
			mScrapViews.clear();
		}

	}

	public void setOnItemMenuClickListener(OnItemMenuClickListener onItemMenuClickListener) {
		mOnItemMenuClickListener = onItemMenuClickListener;
	}

	public interface OnItemMenuClickListener {
		void onItemMenuClick(AdapterView<?> parent, View view, int position, long id);
	}

	public void setOnOutOfEdgeListenerr(OnOutOfEdgeListener listener) {
		mOnOutOfEdgeListener = listener;
	}

	public interface OnOutOfEdgeListener {
		public final int DIRECTION_DOWN = 0;
		public final int DIRECTION_UP = 1;

		void onOutOfEdge(int direction);
	}

}
