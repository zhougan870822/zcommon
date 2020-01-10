package com.zhoug.common.adapters.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView的数据适配器
 *
 * @param <T> 数据类型
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = ">>>RecyclerViewAdapter";
    /**
     * 数据集合
     */
    private List<T> data;

    /**
     * 储存头部组件
     */
    private SparseArray<View> headers;
    /**
     * 储存尾部组件
     */
    private SparseArray<View> footers;

    /**
     * 普通item项的ViewType(除了头部,尾部,上拉刷新item)
     */
    protected static final int VIEW_TYPE_NORMAL = 0;
    /**
     * 第一个头部组件的ViewType和key
     */
    protected static final int VIEW_TYPE_HEADER = Integer.MIN_VALUE;
    /**
     * 最后一个尾部组件的ViewType和key
     */
    protected static final int VIEW_TYPE_FOOTER = Integer.MAX_VALUE;

    /**
     * 加载更多的ViewType
     */
    private static final int VIEW_TYPE_LOAD_MORE = 1;

    /**
     * item单击监听器
     */
    private OnItemClickListener onItemClickListener;
    /**
     * item长按监听器
     */
    private OnItemLongClickListener onItemLongClickListener;
    /**
     * item中的child的单击监听器
     */
    private OnItemChildClickListener onItemChildClickListener;
    /**
     * item中的child的长按监听器
     */
    private OnItemChildLongClickListener onItemChildLongClickListener;

    /**
     * 默认header独自占一行
     */
    private boolean headerFullLine = true;
    /**
     * 默认footer独自占一行
     */
    private boolean footerFullLine = true;

    /**
     * 是否开启加载更多
     */
    private boolean mLoadMoreEnable = false;

    /**
     * 加载更多监听器
     */
    private OnLoadMoreListener onLoadMoreListener;

    /**
     * 是否正在加载
     */
    private boolean loading = false;

    /**
     * 是否没有更多数据了
     */
    private boolean loadEnd = false;


    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();

    /**
     * 关联的RecyclerView
     */
    private RecyclerView mRecyclerView;




    public BaseRecyclerViewAdapter() {

    }

    public BaseRecyclerViewAdapter(List<T> data) {
        this.data = data;
    }

    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }



    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 获取header数目
     *
     * @return
     */
    public int getHeaderCount() {
        if (headers != null) {
            return headers.size();
        }

        return 0;
    }

    /**
     * 获取footer数目
     *
     * @return
     */
    public int getFooterCount() {
        if (footers != null) {
            return footers.size();
        }
        return 0;
    }


    /**
     * 获取数据的数目
     *
     * @return data==null ? 0 : data.size();
     */
    public int getDataCount() {
        return data == null ? 0 : data.size();
    }


    /**
     * 获取item总数(包含所有项)
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return getDataCount() + getHeaderCount() + getFooterCount() + (mLoadMoreEnable ? 1 : 0);
    }

    /**
     * 数据在所有item(包括header,footer)中的开始位置position
     * @return
     */
    public int getStartDataPosition(){
        return  getHeaderCount();
    }

    /**
     * 数据在所有item(包括header,footer)中的结束位置position
     * @return
     */
    public int getEndDataPosition(){
        int i = getHeaderCount() + getDataCount() - 1;
        return  i>=0 ? i :0;
    }

    /**
     * 获取指定位置的数据
     *
     * @param position position-getItemCount();
     * @return
     */
    public T getItemData(int position) {
        if (data == null) {
            return null;
        }
        //数据项
        if (position >= getStartDataPosition() && position <= getEndDataPosition()) {
            return data.get(position - getStartDataPosition());

        } else {
            return null;
        }
    }



    /**
     * 添加header
     *
     * @param header
     */
    public void addHeader(View header) {
        if (headers == null) {
            headers = new SparseArray<>();
        }
        headers.put(headers.size() + VIEW_TYPE_HEADER, header);
    }

    /**
     * 添加footer
     *
     * @param footer
     */
    public void addFooter(View footer) {
        if (footers == null) {
            footers = new SparseArray<>();
        }
        footers.put(VIEW_TYPE_FOOTER - footers.size(), footer);
    }

    /**
     * 移除指定位置的header
     *
     * @param index 0-headers.size()
     */
    public void removeHeader(int index) {
        if (headers != null && index >= 0 && index < headers.size()) {
            headers.removeAt(index);
        }
    }

    /**
     * 移除所有header
     */
    public void removeAllHeader() {
        if (headers != null) {
            headers.clear();
        }
    }

    /**
     * 移除指定位置的footer
     *
     * @param index 0-footers.size()
     */
    public void removeFooter(int index) {
        if (footers != null && index >= 0 && index < footers.size()) {
            footers.removeAt(index);
        }
    }

    /**
     * 移除所有footer
     */
    public void removeAllFooter() {
        if (footers != null) {
            footers.clear();
        }
    }

    /**
     * Gets to load more locations
     *
     * @return
     */
    public int getLoadMoreViewPosition() {
        return getItemCount() - 1;
    }

    /**
     * 加载更多View
     *
     * @param parent
     * @return
     */
    private BaseViewHolder getLoadMoreView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLoadMoreView.getLayoutId(), parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);

        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(v -> {
            if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                startLoadMore();
            }

        });

        return holder;
    }

    /**
     * 加载更多状态改变 刷新列表
     */
    private void notifyLoadMoreStatusChange() {
        switch (mLoadMoreView.getLoadMoreStatus()) {
            case LoadMoreView.STATUS_LOADING://开始加载
                if (onLoadMoreListener != null &&  !loading) {
                    loading = true;
                    onLoadMoreListener.requestLoadMore();
                }
                notifyItemChanged(getLoadMoreViewPosition());
                break;
            case LoadMoreView.STATUS_COMPLETE://加载完成
                notifyDataSetChanged();
                break;
            case LoadMoreView.STATUS_GONE://隐藏
            case LoadMoreView.STATUS_DEFAULT://拉上
            case LoadMoreView.STATUS_END://没有更多
            case LoadMoreView.STATUS_FAIL://加载失败
                notifyItemChanged(getLoadMoreViewPosition());

                break;

        }

    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.mLoadMoreEnable = loadMoreEnable;
        this.loadEnd=false;
        if(mLoadMoreView.getLoadMoreStatus()!=LoadMoreView.STATUS_GONE){
            Log.d(TAG, "初始化loadMoreView状态");
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_GONE);
            notifyItemChanged(getLoadMoreViewPosition());
        }

    }


    /**
     * 隐藏
     */
    private void loadMoreGone() {
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_GONE) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_GONE);
            loadEnd=false;
            Log.d(TAG, "隐藏上拉加载");
            notifyLoadMoreStatusChange();
        }


    }

    /**
     * 显示默认提示
     */
    private void loadMoreDefault() {
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_GONE||
                mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_COMPLETE) {
            Log.d(TAG, "显示上拉加载");
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
            loadEnd=false;
            notifyLoadMoreStatusChange();
        }


    }

    /**
     * 开始加载更多
     */
    private void startLoadMore() {
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_LOADING) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
            loadEnd=false;
            Log.d(TAG, "开始加载更多");
            notifyLoadMoreStatusChange();
        }


    }

    /**
     * 加载更多 成功
     */
    public void loadMoreComplete() {
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_COMPLETE) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_COMPLETE);
            loading = false;
            loadEnd=false;
            Log.d(TAG, "加载成功");
            notifyLoadMoreStatusChange();
        }

    }



    /**
     * 加载更多 没有更多了 会关闭加载功能
     * 刷新数据后,需要调用{@link #setLoadMoreEnable(boolean)}方法重启
     */
    public void loadMoreEnd() {
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_END) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            loading = false;
            loadEnd=true;
            Log.d(TAG, "没有更多了");
            notifyLoadMoreStatusChange();
        }


    }

    /**
     * 加载更多 失败
     */
    public void loadMoreFail() {
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_FAIL) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
            loading = false;
            loadEnd=false;
            Log.d(TAG, "加载失败");
            notifyLoadMoreStatusChange();
        }
    }

    /**
     * 初始化加载更多滑动到底部监听
     */
    private void initLoadMore() {
        if (!mLoadMoreEnable) return;
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean isSlidingUpward = false;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //没有开启自动加载或者没有更多数据了
                    if (!mLoadMoreEnable || loadEnd) return;
                    //1触摸滚动,2放手惯性滚动,3停止滚动
//                RecyclerView.SCROLL_STATE_IDLE; 不滑动时
                    if (newState != RecyclerView.SCROLL_STATE_IDLE) return;

                    int offset = recyclerView.computeVerticalScrollOffset();
                    int extent = recyclerView.computeVerticalScrollExtent();
                    int range = recyclerView.computeVerticalScrollRange();
                    /*Log.d(TAG,"offset="+offset );
                    Log.d(TAG,"extent="+extent );
                    Log.d(TAG,"range="+range );*/
                    if(extent==range){
                        Log.d(TAG,"不能滚动" );
                    }else if(offset==0){
                       Log.d(TAG,"滚到顶部" );
                   }else if(offset+extent>=range && isSlidingUpward){
                       Log.d(TAG,"滚到底部" );
                        startLoadMore();
                   }

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                    if (mLoadMoreEnable) {
                        isSlidingUpward = dy > 0;
                    } else {
                        isSlidingUpward = false;
                    }
                }
            });

            //可以滚动则显示默认 不能滚动则隐藏
            mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(()->{
                if(!mLoadMoreEnable) return;
                if(mRecyclerView.computeVerticalScrollExtent()==mRecyclerView.computeVerticalScrollRange()){
//                    Log.d(TAG,"addOnGlobalLayoutListener 不能滚动" );
                    loadMoreGone();
                }else{
//                    Log.d(TAG,"addOnGlobalLayoutListener 可以滚动" );
                    loadMoreDefault();
                }


            });


        }
    }


    

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderCount()) {
            //是头部
            return headers.keyAt(position);
        } else if (position >= getHeaderCount() + getDataCount() && position < getHeaderCount() + getDataCount() + getFooterCount()) {
            //是尾部,position-(getHeaderCount()+getDataCount()))是尾部的位置
            //由于存入的数据是按照key从小到大存储的,刚好和我们添加的footer顺序相反
            return footers.keyAt(getFooterCount() - 1 - (position - (getHeaderCount() + getDataCount())));
        } else if (mLoadMoreEnable && position == getItemCount() - 1) {
            //加载更多
            return VIEW_TYPE_LOAD_MORE;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
//            LogUtils.d(TAG, "context:"+parent.getContext());
            return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false), this);
        } else if (viewType == VIEW_TYPE_LOAD_MORE) {
            //加载更多
            return getLoadMoreView(parent);
        } else {
            if (headers != null) {
                View header = headers.get(viewType);
                if (header != null) {
                    return new BaseViewHolder(header);
                }
            }
            if (footers != null) {
                View footer = footers.get(viewType);
                if (footer != null) {
                    return new BaseViewHolder(footer);
                }
            }
        }
        return null;

    }

    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_NORMAL:
                //绑定监听
                bindOnItemClickListener(holder, position);
                bindItemLongClickListener(holder, position);

                onBindData(holder, getItemData(position), position, getItemViewType(position));
                break;
            case VIEW_TYPE_LOAD_MORE:
                //加载更多
                mLoadMoreView.convert(holder);
                break;
        }

    }

    /**
     * 绑定单击事件到指定的holder
     *
     * @param holder
     * @param position
     */
    protected void bindOnItemClickListener(BaseViewHolder holder, int position) {
        if (onItemClickListener != null) {
            if (!holder.itemView.isClickable()) {
                holder.itemView.setClickable(true);
            }
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(BaseRecyclerViewAdapter.this, v, (Integer) v.getTag()));
        }
    }

    /**
     * *绑定长按事件到指定的holder
     *
     * @param holder
     * @param position
     */
    protected void bindItemLongClickListener(BaseViewHolder holder, int position) {
        if (onItemLongClickListener != null) {
            if (!holder.itemView.isClickable()) {
                holder.itemView.setLongClickable(true);
            }
            holder.itemView.setTag(position);
            holder.itemView.setOnLongClickListener(v -> onItemLongClickListener.onItemLongClick(BaseRecyclerViewAdapter.this, v, (Integer) v.getTag()));
        }
    }

    /**
     * 网格布局设置头部尾部独自占一行
     *
     * @param recyclerView
     */
    private void setSpanSizeInGrid(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //网格布局设置头部尾部独自占一行
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            //设置每个item站的span
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //头部
                    if (position < getHeaderCount()) {
                        if (isHeaderFullLine()) {
                            return gridLayoutManager.getSpanCount();
                        } else {
                            return 1;
                        }
                    } else if (position < getHeaderCount() + getDataCount()) {
                        //内容
                        return 1;
                    } else if (position < getHeaderCount() + getDataCount() + getFooterCount()) {
                        //尾部
                        if (isFooterFullLine()) {
                            return gridLayoutManager.getSpanCount();
                        } else {
                            return 1;
                        }
                    } else if (position == getHeaderCount() + getDataCount() + getFooterCount()) {
                        //加载更多
                        return gridLayoutManager.getSpanCount();
                    }

                    return 1;
                }
            });
        }
    }

    /**
     * 瀑布流布局设置头部尾部独自占一行
     *
     * @param holder
     */
    private void setSpanSizeInStaggeredGrid(BaseViewHolder holder) {
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (!(layoutManager instanceof StaggeredGridLayoutManager)) {
                return;
            }
        }

        int position = holder.getLayoutPosition();
//        Log.d(TAG, "setHeaderAndFooterSpanSizeInStaggeredGrid:position="+position);
        boolean b = false;
        //头部
        if (position < getHeaderCount()) {
            if (isHeaderFullLine()) {
                b = true;
            }
        } else if (position < getHeaderCount() + getDataCount()) {
            //内容
            b = false;
        } else if (position < getHeaderCount() + getDataCount() + getFooterCount()) {
            //尾部
            if (isFooterFullLine()) {
                b = true;
            }
        } else if (position == getHeaderCount() + getDataCount() + getFooterCount()) {
            //加载更多
            b = true;
        }
        if (b) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }


    }

    /**
     * adapter绑定RecyclerView时调用
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
//        Log.d(TAG, "onAttachedToRecyclerView");
        if (mRecyclerView == null) {
            mRecyclerView = recyclerView;
        }

        setSpanSizeInGrid(recyclerView);
        initLoadMore();

    }


    /**
     * 每个item显示到屏幕上调用
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        setSpanSizeInStaggeredGrid(holder);


    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }


    public boolean isHeaderFullLine() {
        return headerFullLine;
    }

    public void setHeaderFullLine(boolean headerFullLine) {
        this.headerFullLine = headerFullLine;
    }



    public boolean isFooterFullLine() {
        return footerFullLine;
    }

    public void setFooterFullLine(boolean footerFullLine) {
        this.footerFullLine = footerFullLine;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isLoadEnd() {
        return loadEnd;
    }


    public LoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    public void setLoadMoreView(LoadMoreView mLoadMoreView) {
        this.mLoadMoreView = mLoadMoreView;
    }

    /**
     * 返回布局文件的id
     *
     * @param viewType 用来判断返回不同的布局文件
     * @return
     */
    public abstract @LayoutRes int getLayoutId(int viewType);

    /**
     * header和footer不会回掉onBindData方法
     * 绑定数据到item
     * @param holder   BaseViewHolder
     * @param data     List<T>
     * @param position 0-getItemCount()
     * @param viewType viewType
     */
    public abstract void onBindData(BaseViewHolder holder, T data, int position, int viewType);

    /**
     * 设置item单击监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按监听
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 设置item中的子组件单击监听
     *
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    /**
     * 设置item中的子组件长按监听
     *
     * @param onItemChildLongClickListener
     */
    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.onItemChildLongClickListener = onItemChildLongClickListener;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public OnItemChildClickListener getOnItemChildClickListener() {
        return onItemChildClickListener;
    }

    public OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return onItemChildLongClickListener;
    }

    /**
     * item单击回掉接口
     */
    public interface OnItemClickListener {
        /**
         * @param adapter  BaseRecyclerViewAdapter
         * @param view     触发事件的item
         * @param position item位子
         */
        void onItemClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

    /**
     * item长按回掉接口
     */
    public interface OnItemLongClickListener {
        /**
         * @param adapter  BaseRecyclerViewAdapter
         * @param view     触发事件的item
         * @param position item位子
         * @return
         */
        boolean onItemLongClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

    /**
     * item中的子组件单击回掉接口
     */
    public interface OnItemChildClickListener {
        /**
         * @param adapter  BaseRecyclerViewAdapter
         * @param view     触发事件的item中的子组件
         * @param position item位子
         */
        void onItemChildClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

    /**
     * item中的子组件长按回掉接口
     */
    public interface OnItemChildLongClickListener {
        /**
         * @param adapter  BaseRecyclerViewAdapter
         * @param view     触发事件的item中的子组件
         * @param position item位子
         * @return 是否消费时间
         */
        boolean onItemChildLongClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

    /**
     * 加载更多监听
     */
    public interface OnLoadMoreListener {
        void requestLoadMore();
    }

}
