package com.xiaopo.flying.photolayout;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.SquarePuzzleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snowbean on 16-8-17.
 */
public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.PuzzleViewHolder> {

    private List<PuzzleLayout> mLayoutData = new ArrayList<>();
    private List<Bitmap> mBitmapData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    private boolean mNeedDrawBorder = false;
    private boolean mNeedDrawOuterBorder = false;

    @Override
    public PuzzleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle, parent, false);
        return new PuzzleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PuzzleViewHolder holder, int position) {
        final PuzzleLayout puzzleLayout = mLayoutData.get(position);

        holder.mPuzzleView.setNeedDrawBorder(mNeedDrawBorder);
        holder.mPuzzleView.setNeedDrawOuterBorder(mNeedDrawOuterBorder);
        holder.mPuzzleView.setMoveLineEnable(false);

        holder.mPuzzleView.setPuzzleLayout(puzzleLayout);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(puzzleLayout, puzzleLayout.getTheme());
                }
            }
        });
        

        if (mBitmapData == null) return;

        final int bitmapSize = mBitmapData.size();

        if (puzzleLayout.getBorderSize() > bitmapSize) {
            for (int i = 0; i < puzzleLayout.getBorderSize(); i++) {
                holder.mPuzzleView.addPiece(mBitmapData.get(i % bitmapSize));
            }
        } else {
            holder.mPuzzleView.addPieces(mBitmapData);
        }


    }

    @Override
    public int getItemCount() {
        return mLayoutData == null ? 0 : mLayoutData.size();
    }

    public void refreshData(List<PuzzleLayout> layoutData, List<Bitmap> bitmapData) {
        mLayoutData = layoutData;
        mBitmapData = bitmapData;

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public boolean isNeedDrawBorder() {
        return mNeedDrawBorder;
    }

    public void setNeedDrawBorder(boolean needDrawBorder) {
        mNeedDrawBorder = needDrawBorder;
    }

    public boolean isNeedDrawOuterBorder() {
        return mNeedDrawOuterBorder;
    }

    public void setNeedDrawOuterBorder(boolean needDrawOuterBorder) {
        mNeedDrawOuterBorder = needDrawOuterBorder;
    }

    public static class PuzzleViewHolder extends RecyclerView.ViewHolder {

        SquarePuzzleView mPuzzleView;

        public PuzzleViewHolder(View itemView) {
            super(itemView);
            mPuzzleView = (SquarePuzzleView) itemView.findViewById(R.id.puzzle);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PuzzleLayout puzzleLayout, int themeId);
    }
}
