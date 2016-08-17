package com.xiaopo.flying.photolayout;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.SquarePuzzleView;

import java.util.List;

/**
 * Created by snowbean on 16-8-17.
 */
public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.PuzzleViewHolder> {

    private List<PuzzleLayout> mLayoutData;
    private List<Bitmap> mBitmapData;

    @Override
    public PuzzleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle, parent, false);
        return new PuzzleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PuzzleViewHolder holder, int position) {
        PuzzleLayout puzzleLayout = mLayoutData.get(position);
        holder.mPuzzleView.setPuzzleLayout(puzzleLayout);
        holder.mPuzzleView.setNeedDrawBorder(true);
        holder.mPuzzleView.setMoveLineEnable(false);

        for (Bitmap bitmap : mBitmapData) {
            holder.mPuzzleView.addPiece(bitmap);
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

    public static class PuzzleViewHolder extends RecyclerView.ViewHolder {

        SquarePuzzleView mPuzzleView;

        public PuzzleViewHolder(View itemView) {
            super(itemView);
            mPuzzleView = (SquarePuzzleView) itemView.findViewById(R.id.puzzle);
        }
    }
}
