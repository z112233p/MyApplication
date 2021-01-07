package com.illa.joliveapp.tools;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.illa.joliveapp.interface_class.ItemMoveSwipeListener;

public class RZItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemMoveSwipeListener itemMoveSwipeListener;
    private int listSize;
    // 設定1個帶 ItemMoveSwipeListener 的參數建構式
    public RZItemTouchHelperCallback(ItemMoveSwipeListener itemMoveSwipeListener, int listSize) {
        this.itemMoveSwipeListener = itemMoveSwipeListener;
        this.listSize = listSize;
    }

    public void setListSize(int listSize){
        this.listSize = listSize;

    }

    /**
     * 這個方法決定RecyclerView Item可以移動&滑動的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        // 先律定可「移動」的方向，這邊限制只能上、下移動
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        // 在律定可「滑動」的方向，這邊限制只能左、右滑動
        int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        if(viewHolder.getAdapterPosition()+1 > listSize){
            dragFlags = 0;
            swipeFlags = 0;
        }
            // 如果是GridLayoutManager，那麼就不需要滑動所以可以這樣設置(左、上、右、下)
        // int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN;

        // 如果想讓「移動」或是「滑動」，其中1個無效用則參數設為0即可
        // int dragFlags = 0;
        // int swipeFlags = 0;

        // 再透過 makeMovementFlags()方法去設置
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 移動完成後，要做甚麼事
     *
     * @param recyclerView
     * @param viewHolder   當前的手指正在移動的item
     * @param target       要被交換的item
     * @return 決定當次的移動是否要執行，true 執行 ; false 不執行
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 透過itemMoveSwipeListener的onItemMove，讓adapter實作該方法
//        Log.e("Peter","itemMOVE PAR  onMove  viewHolder.getAdapterPosition()  "+viewHolder.getAdapterPosition());
//        Log.e("Peter","itemMOVE PAR  onMove  listSize  "+listSize);
        if(viewHolder.getAdapterPosition()+1 <= listSize){

            int targetPosition = target.getAdapterPosition();
            if (targetPosition >= listSize){
                targetPosition = listSize-1;
            }
            return itemMoveSwipeListener.onItemMove(viewHolder.getAdapterPosition(), targetPosition);
        }
        return false;
//        return itemMoveSwipeListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    /**
     * 滑動完成後，要做甚麼事
     *
     * @param viewHolder
     * @param direction  當前滑動的方向
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // 透過itemMoveSwipeListener的onItemSwipe，讓adapter實作該方法
        Log.e("Peter","itemMOVE PAR  onSwiped  viewHolder.getAdapterPosition()  "+viewHolder.getAdapterPosition());
//
//        if(viewHolder.getAdapterPosition()+1 <= listSize){
//            itemMoveSwipeListener.onItemSwipe(viewHolder.getAdapterPosition());
//
//        }
//        itemMoveSwipeListener.onItemSwipe(viewHolder.getAdapterPosition());

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.e("Peter","itemMOVE PAR  onSelectedChanged  viewHolder.getAdapterPosition()  "+actionState);
        if(actionState == 0){
            itemMoveSwipeListener.onFinish();

        }
        super.onSelectedChanged(viewHolder, actionState);
    }
}
