
package com.myapplication.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.myapplication.R;
import com.myapplication.adapter.base.animation.AlphaInAnimation;
import com.myapplication.adapter.base.animation.BaseAnimation;
import com.myapplication.adapter.base.animation.ScaleInAnimation;
import com.myapplication.adapter.base.animation.SlideInBottomAnimation;
import com.myapplication.adapter.base.animation.SlideInLeftAnimation;
import com.myapplication.adapter.base.animation.SlideInRightAnimation;
import com.myapplication.adapter.base.listener.OnItemDragListener;
import com.myapplication.adapter.base.listener.OnItemSwipedListener;
import com.myapplication.model.list.ListPageInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();

    protected Context mContext;
    protected int mLayoutResId;
    protected LayoutInflater mLayoutInflater;

    private int mLastPosition = -1;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;
    private RequestLoadMoreListener mRequestLoadMoreListener;

    private boolean mNextLoadEnable = false;
    private boolean mLoadingMoreEnable = false;
    private boolean mOpenAnimationEnable = false;
    private boolean mEmptyEnable;         // true will show emptyView
    private boolean mHeadAndEmptyEnable;  // true will show headView
    private boolean mFootAndEmptyEnable;  // true will show footView

    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;
    private View mLoadingView;
    protected static final int HEADER_VIEW = 0x00000111;
    protected static final int LOADING_VIEW = 0x00000222;
    protected static final int FOOTER_VIEW = 0x00000333;
    protected static final int EMPTY_VIEW = 0x00000555;

    @AnimationType
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    public static final int ALPHAIN = 0x00000001;
    public static final int SCALEIN = 0x00000002;
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    public static final int SLIDEIN_LEFT = 0x00000004;
    public static final int SLIDEIN_RIGHT = 0x00000005;

    private static final int NO_TOGGLE_VIEW = 0;
    private int mToggleViewId = NO_TOGGLE_VIEW;
    private ItemTouchHelper mItemTouchHelper;
    private boolean itemDragEnabled = false;
    private boolean itemSwipeEnabled = false;
    private OnItemDragListener mOnItemDragListener;
    private OnItemSwipedListener mOnItemSwipedListener;
    private boolean mDragOnLongPress = true;

    private View.OnTouchListener mOnToggleViewTouchListener;
    private View.OnLongClickListener mOnToggleViewLongClickListener;

    /**
     * data source
     */
    protected ListPageInfo<T> mListPageInfo;

    public void setListPageInfo(ListPageInfo<T> listPageInfo) {
        mListPageInfo = listPageInfo;
    }

    public ListPageInfo<T> getListPageInfo() {
        return mListPageInfo;
    }

    /**
     * down load more callback
     */
    public interface RequestLoadMoreListener {

        void onLoadMoreRequested();
    }

    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, int position);
    }

    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerViewItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    public interface OnRecyclerViewItemLongClickListener {

        boolean onItemLongClick(View view, int position);
    }

    private OnRecyclerViewItemChildClickListener mChildClickListener;

    public void setOnRecyclerViewItemChildClickListener(OnRecyclerViewItemChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    public interface OnRecyclerViewItemChildClickListener {
        void onItemChildClick(BaseQuickAdapter adapter, View view, int position);
    }

    public class OnItemChildClickListener implements View.OnClickListener {
        public RecyclerView.ViewHolder mViewHolder;

        @Override
        public void onClick(View v) {
            if (mChildClickListener != null)
                mChildClickListener.onItemChildClick(BaseQuickAdapter.this, v, mViewHolder.getLayoutPosition() - getHeaderViewsCount());
        }
    }

    public BaseQuickAdapter(int layoutResId) {
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) {
            setFullSpan(holder);
        }
    }

    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * 是否添加尾部
     * @param enable
     */
    public void openLoadMore(boolean enable) {
        mNextLoadEnable = enable;
    }

    private void addLoadMore() {
        if (isLoadMore() && !mLoadingMoreEnable) {
            mLoadingMoreEnable = true;
            mRequestLoadMoreListener.onLoadMoreRequested();
        }
    }

    private boolean isLoadMore() {
        return mNextLoadEnable && mRequestLoadMoreListener != null;
    }

    public void add(int position, T item) {
        mListPageInfo.getDataList().add(position, item);
        notifyItemInserted(position);
    }

    public void addData(List<T> data) {
        mListPageInfo.getDataList().addAll(data);
        if (mListPageInfo.isFirstPage()) {
            if (mRequestLoadMoreListener != null) {
                mNextLoadEnable = true;
                mFooterView = null;
            }
            mLastPosition = -1;
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mListPageInfo.getDataList().remove(position);
        notifyItemRemoved(position + getHeaderViewsCount());
    }

    public List getData() {
        return mListPageInfo.getDataList();
    }

    public T getItem(int position) {
        return mListPageInfo.getDataList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int i = isLoadMore() ? 1 : 0;  //isLoadMore() count is loadMoreView
        int count = mListPageInfo.getDataList().size() + i + getHeaderViewsCount() + getFooterViewsCount();
        if (mListPageInfo.getDataList().size() == 0 && mEmptyView != null) {
            if (count == 0 && (!mHeadAndEmptyEnable || !mFootAndEmptyEnable)) {
                count += getEmptyViewCount();
            } else if (mHeadAndEmptyEnable || mFootAndEmptyEnable) {
                count += getEmptyViewCount();
            }

            if ((mHeadAndEmptyEnable && getHeaderViewsCount() == 1 && count == 1) || count == 0) {
                mEmptyEnable = true;
                count += getEmptyViewCount();
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {

        if (mHeaderView != null && position == 0) {
            return HEADER_VIEW;
        }
        if (mListPageInfo.getDataList().size() == 0 && mEmptyEnable && mEmptyView != null && position <= 2) {

            if ((mHeadAndEmptyEnable || mFootAndEmptyEnable) && position == 1) {

                if (mHeaderView == null && mEmptyView != null && mFooterView != null) {
                    return FOOTER_VIEW;
                } else if (mHeaderView != null && mEmptyView != null) {
                    return EMPTY_VIEW;
                }
            } else if (position == 0) {
                if (mHeaderView == null) {
                    return EMPTY_VIEW;
                } else if (mFooterView != null)
                    return EMPTY_VIEW;
            } else if (position == 2 && (mFootAndEmptyEnable || mHeadAndEmptyEnable) && mHeaderView != null && mEmptyView != null) {
                return FOOTER_VIEW;
            } else if ((!mFootAndEmptyEnable || !mHeadAndEmptyEnable) && position == 1 && mFooterView != null) {
                return FOOTER_VIEW;
            }
        } else if (mListPageInfo.getDataList().size() == 0 && mEmptyView != null && getItemCount() == (mHeadAndEmptyEnable ? 2 : 1) && mEmptyEnable) {
            return EMPTY_VIEW;
        } else if (position == mListPageInfo.getDataList().size() + getHeaderViewsCount()) {
            if (mNextLoadEnable)
                return LOADING_VIEW;
            else
                return FOOTER_VIEW;
        }
        return getDefItemViewType(position - getHeaderViewsCount());
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder;
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case LOADING_VIEW:
                baseViewHolder = getLoadingView(parent); //choose default loading view  if setLoadingView is not
                break;
            case HEADER_VIEW:
                baseViewHolder = new BaseViewHolder(mHeaderView);
                break;
            case EMPTY_VIEW:
                baseViewHolder = new BaseViewHolder(mEmptyView);
                break;
            case FOOTER_VIEW:
                baseViewHolder = new BaseViewHolder(mFooterView);
                break;
            default:
                baseViewHolder = onCreateDefViewHolder(parent, viewType);
                initItemClickListener(baseViewHolder);
        }
        return baseViewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int positions) {
        int viewType = holder.getItemViewType();

        switch (viewType) {
            case 0:
                convert((BaseViewHolder) holder, mListPageInfo.getDataList().get(holder.getLayoutPosition() - getHeaderViewsCount()));
                addAnimation(holder);
                break;
            case LOADING_VIEW:
                addLoadMore();
                break;
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                convert((BaseViewHolder) holder, mListPageInfo.getDataList().get(holder.getLayoutPosition() - getHeaderViewsCount()));
                onBindDefViewHolder((BaseViewHolder) holder, mListPageInfo.getDataList().get(holder.getLayoutPosition() - getHeaderViewsCount()));
                break;
        }

        if (mItemTouchHelper != null && itemDragEnabled && viewType != LOADING_VIEW && viewType != HEADER_VIEW
                && viewType != EMPTY_VIEW && viewType != FOOTER_VIEW) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                View toggleView = ((BaseViewHolder) holder).getView(mToggleViewId);
                if (toggleView != null) {
                    toggleView.setTag(holder);
                    if (mDragOnLongPress) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener);
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener);
                    }
                }
            } else {
                holder.itemView.setTag(holder);
                holder.itemView.setOnLongClickListener(mOnToggleViewLongClickListener);
            }
        }
    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return new BaseViewHolder(getItemView(layoutResId, parent));
    }

    public void setLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
    }

    private BaseViewHolder getLoadingView(ViewGroup parent) {
        if (mLoadingView == null) {
            return createBaseViewHolder(parent, R.layout.def_loading);
        }
        return new BaseViewHolder(mLoadingView);
    }

    public void addHeaderView(View header) {
        this.mHeaderView = header;
        this.notifyDataSetChanged();
    }

    public void addFooterView(View footer) {
        mNextLoadEnable = false;
        this.mFooterView = footer;
        this.notifyDataSetChanged();
    }

    public void setEmptyView(View emptyView) {
        setEmptyView(false, false, emptyView);
    }

    public void setEmptyView(boolean isHeadAndEmpty, View emptyView) {
        setEmptyView(isHeadAndEmpty, false, emptyView);
    }

    public void setEmptyView(boolean isHeadAndEmpty, boolean isFootAndEmpty, View emptyView) {
        mHeadAndEmptyEnable = isHeadAndEmpty;
        mFootAndEmptyEnable = isFootAndEmpty;
        mEmptyView = emptyView;
        mEmptyEnable = true;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public int getHeaderViewsCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewsCount() {
        return mFooterView == null ? 0 : 1;
    }

    public int getEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }

    public void notifyDataChangedAfterLoadMore(boolean isNextLoad) {
        mNextLoadEnable = isNextLoad;
        mLoadingMoreEnable = false;
        notifyDataSetChanged();
    }

    public void notifyDataChangedAfterLoadMore(List<T> data, boolean isNextLoad) {
        mListPageInfo.getDataList().addAll(data);
        notifyDataChangedAfterLoadMore(isNextLoad);
    }

    private void initItemClickListener(final BaseViewHolder baseViewHolder) {
        if (onRecyclerViewItemClickListener != null) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition() - getHeaderViewsCount());
                }
            });
        }
        if (onRecyclerViewItemLongClickListener != null) {
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onRecyclerViewItemLongClickListener.onItemLongClick(v, baseViewHolder.getLayoutPosition() - getHeaderViewsCount());
                }
            });
        }
    }

    protected void onBindDefViewHolder(BaseViewHolder holder, T item) {
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    public void openLoadAnimation(@AnimationType int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
    }

    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    protected abstract void convert(BaseViewHolder helper, T item);

    public void setToggleViewId(int toggleViewId) {
        mToggleViewId = toggleViewId;
    }

    public void setToggleDragOnLongPress(boolean longPress) {
        mDragOnLongPress = longPress;
        if (mDragOnLongPress) {
            mOnToggleViewTouchListener = null;
            mOnToggleViewLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemTouchHelper != null && itemDragEnabled) {
                        mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag());
                    }
                    return true;
                }
            };
        } else {
            mOnToggleViewTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN
                            && !mDragOnLongPress) {
                        if (mItemTouchHelper != null && itemDragEnabled) {
                            mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag());
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            mOnToggleViewLongClickListener = null;
        }
    }

    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper) {
        enableDragItem(itemTouchHelper, NO_TOGGLE_VIEW, true);
    }

    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper, int toggleViewId, boolean dragOnLongPress) {
        itemDragEnabled = true;
        mItemTouchHelper = itemTouchHelper;
        setToggleViewId(toggleViewId);
        setToggleDragOnLongPress(dragOnLongPress);
    }

    public void disableDragItem() {
        itemDragEnabled = false;
        mItemTouchHelper = null;
    }

    public boolean isItemDraggable() {
        return itemDragEnabled;
    }

    public void enableSwipeItem() {
        itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        itemSwipeEnabled = false;
    }

    public boolean isItemSwipeEnable() {
        return itemSwipeEnabled;
    }

    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        mOnItemDragListener = onItemDragListener;
    }

    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() - getHeaderViewsCount();
    }

    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);

        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mListPageInfo.getDataList(), i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mListPageInfo.getDataList(), i, i - 1);
            }
        }
        notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());

        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragMoving(source, from, target, to);
        }
    }

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void setOnItemSwipedListener(OnItemSwipedListener listener) {
        mOnItemSwipedListener = listener;
    }

    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipedListener != null && itemSwipeEnabled) {
            mOnItemSwipedListener.onItemSwipedStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwipeClear(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipedListener != null && itemSwipeEnabled) {
            mOnItemSwipedListener.clearView(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwiped(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipedListener != null && itemSwipeEnabled) {
            mOnItemSwipedListener.onItemSwiped(viewHolder, getViewHolderPosition(viewHolder));
        }
        int pos = getViewHolderPosition(viewHolder);
        mListPageInfo.getDataList().remove(pos);
        notifyItemRemoved(viewHolder.getAdapterPosition());
    }

}
