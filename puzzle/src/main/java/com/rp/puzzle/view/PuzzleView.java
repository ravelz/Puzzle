package com.rp.puzzle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rp.puzzle.R;
import com.rp.puzzle.bean.Block;
import com.rp.puzzle.util.BitmapUtil;

import java.util.Random;

/**
 * Created by Rahul Pareta on 14/5/18.
 */

public class PuzzleView extends RelativeLayout {

    public interface OnCompleteListener {
        void onComplete();
    }

    /*Constant*/
    public static final int MODE_CRATE = 1;
    public static final int MODE_PLAY = 2;

    private int puzzleViewHeight, puzzleViewWidth;
    private int blockViewHeight, blockViewWidth;
    private int puzzleSize;
    private Bitmap mBitmap;
    private Block[][] mBlocks;
    private boolean isParentInflated = false;
    private boolean isDataRecieved = false;
    private OnCompleteListener mOnCompleteListener = null;
    private int mode;

    /*Custom Attributes*/
    private int puzzleBorderSize = 0;
    private int puzzleBorderColor = Color.WHITE;
    private boolean puzzleEnabled = true;


    public PuzzleView(Context context) {
        super(context);
        getCustomAttributes(null);
        init();
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getCustomAttributes(attrs);
        init();
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getCustomAttributes(attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getCustomAttributes(attrs);
        init();
    }


    private void getCustomAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = getContext().getTheme().obtainStyledAttributes(
                    attrs, R.styleable.PuzzleView, 0, 0);
            try {
                puzzleBorderSize = mTypedArray.getInt(R.styleable.PuzzleView_pv_borderSize, 0);
                puzzleBorderColor = mTypedArray.getColor(R.styleable.PuzzleView_pv_borderColor, Color.WHITE);
                puzzleEnabled = mTypedArray.getBoolean(R.styleable.PuzzleView_pv_enabled, true);

            } finally {
                mTypedArray.recycle();
            }
        }
    }

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isParentInflated) {
                    puzzleViewWidth = getWidth();
                    puzzleViewHeight = getHeight();
                    isParentInflated = true;
                    if (isDataRecieved) {
                        if (mode == MODE_CRATE)
                            mBlocks = getRandomBlock();
                        showPuzzle();
                    }
                }
            }
        });
    }

    public void createPuzzle(int resId, int puzzleSize) throws Exception {
        mode = MODE_CRATE;
        this.mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        this.puzzleSize = puzzleSize;
        if (mBitmap == null) {
            throw new Exception("Invalid resource Id!");
        } else if (puzzleSize < 2) {
            throw new Exception("puzzle size should be greater than 1");
        } else {
            mBlocks = getRandomBlock();
            isDataRecieved = true;
            if (isParentInflated)
                showPuzzle();
        }
    }

    public void createPuzzle(Bitmap mBitmap, int puzzleSize) throws Exception {
        mode = MODE_CRATE;
        this.mBitmap = mBitmap;
        this.puzzleSize = puzzleSize;
        mBlocks = getRandomBlock();
        if (mBitmap == null) {
            throw new Exception("Bitmap can't be null!");
        } else if (puzzleSize < 2) {
            throw new Exception("puzzle size should be greater than 1");
        } else {
            mBlocks = getRandomBlock();
            isDataRecieved = true;
            if (isParentInflated) {
                showPuzzle();
            }
        }
    }

    public void playPuzzle(Bitmap mBitmap, Block[][] mBlocks) throws Exception {
        mode = MODE_PLAY;
        this.mBitmap = mBitmap;
        this.mBlocks = mBlocks;
        if (mBitmap == null) {
            throw new Exception("Bitmap can't be null!");
        } else if (mBlocks == null) {
            throw new Exception("Bloc[][] can't be null!");
        } else {
            int nosCols = mBlocks.length;
            int nosRows = mBlocks[0].length;
            if (nosCols == nosRows) {
                puzzleSize = nosCols;
                if (puzzleSize < 2) {
                    throw new Exception("Bloc[][] should be n*m where n==m & n>1!");
                } else {
                    isDataRecieved = true;
                    if (isParentInflated) {
                        showPuzzle();
                    }
                }
            } else {
                throw new Exception("Bloc[][] should be n*m where n==m!");
            }
        }

    }

    public void playPuzzle(int resId, Block[][] mBlocks) throws Exception {
        mode = MODE_PLAY;
        this.mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        this.mBlocks = mBlocks;
        if (mBitmap == null) {
            throw new Exception("Invalid resource Id!");
        } else if (mBlocks == null) {
            throw new Exception("Bloc[][] can't be null!");
        } else {
            int nosCols = mBlocks.length;
            int nosRows = mBlocks[0].length;
            if (nosCols == nosRows) {
                puzzleSize = nosCols;
                if (puzzleSize < 2) {
                    throw new Exception("Bloc[][] should be n*m where n==m & n>1!");
                } else {
                    isDataRecieved = true;
                    if (isParentInflated) {
                        showPuzzle();
                    }
                }
            } else {
                throw new Exception("Bloc[][] should be n*m where n==m & n>2!");
            }
        }

    }

    private void showPuzzle() {

        removeAllViews();
        blockViewHeight = puzzleViewHeight / puzzleSize;
        blockViewWidth = puzzleViewWidth / puzzleSize;
        if (blockViewHeight != 0 && blockViewWidth != 0) {
            mBitmap = BitmapUtil.scaleBitmap(mBitmap, puzzleViewWidth, puzzleViewHeight);
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    showBlock(mBlocks[i][j]);
                }
            }
        }

    }

    private void setTouchEvents(final View mView, final Block mBlock) {
        mView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (!puzzleEnabled)
                    return true;
                resetZIndex();
                ViewCompat.setZ(mView, 1);
                int maxLeftMargin = puzzleViewWidth - blockViewWidth;
                int maxTopMargin = puzzleViewHeight - blockViewHeight;
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        mBlock.set_xDelta(X - mLayoutParams.leftMargin);
                        mBlock.set_yDelta(Y - mLayoutParams.topMargin);
                        break;
                    case MotionEvent.ACTION_UP:
                        int currentLeftMargin = mLayoutParams.leftMargin;
                        int currentTopMargin = mLayoutParams.topMargin;
                        int blockPositionX = 0;
                        int blockPositionY = 0;
                        while (true && blockPositionX < puzzleSize) {
                            int blockMaxLeftMargn = (blockViewWidth * blockPositionX) + (blockViewWidth / 2);
                            if (blockPositionX == 0) {
                                if (currentLeftMargin < blockMaxLeftMargn)
                                    break;
                                else
                                    blockPositionX++;
                            } else {
                                if (currentLeftMargin > blockMaxLeftMargn)
                                    blockPositionX++;
                                else
                                    break;
                            }
                        }
                        while (true && blockPositionY < puzzleSize) {
                            int blockMaxTopMargn = (blockViewHeight * blockPositionY) + (blockViewHeight / 2);
                            if (blockPositionY == 0) {
                                if (currentTopMargin < blockMaxTopMargn)
                                    break;
                                else
                                    blockPositionY++;
                            } else {
                                if (currentTopMargin > blockMaxTopMargn)
                                    blockPositionY++;
                                else
                                    break;
                            }
                        }
                        swapBlock(mBlock, mBlocks[blockPositionX][blockPositionY]);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int leftMargin = X - mBlock.get_xDelta();
                        int topMargin = Y - mBlock.get_yDelta();
                        if (leftMargin >= 0 && leftMargin <= maxLeftMargin)
                            mLayoutParams.leftMargin = X - mBlock.get_xDelta();
                        if (topMargin >= 0 && topMargin <= maxTopMargin)
                            mLayoutParams.topMargin = Y - mBlock.get_yDelta();
                        view.setLayoutParams(mLayoutParams);
                        break;
                }
                return true;
            }
        });
    }

    private void showBlock(Block mBlock) {
        ImageView mImageView = new ImageView(getContext());
        mImageView.setImageBitmap(BitmapUtil.cropBitmap(
                mBitmap,
                mBlock.getRealLeftMargin(puzzleSize, puzzleViewWidth),
                mBlock.getRealTopMargin(puzzleSize, puzzleViewHeight),
                blockViewWidth,
                blockViewHeight,
                puzzleBorderSize,
                puzzleBorderColor));
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mBlock.setmImageView(mImageView);
        setTouchEvents(mImageView, mBlock);
        LayoutParams params = new LayoutParams(
                blockViewWidth, blockViewHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.leftMargin = mBlock.getCurrentLeftMargin(puzzleSize, puzzleViewWidth);
        params.topMargin = mBlock.getCurrentTopMargin(puzzleSize, puzzleViewHeight);
        addView(mImageView, params);
    }

    private void repositionBlock(Block mBlock) {
        LayoutParams layoutParams1 = (LayoutParams) mBlock.getmImageView().getLayoutParams();
        layoutParams1.leftMargin = mBlock.getCurrentLeftMargin(puzzleSize, puzzleViewWidth);
        layoutParams1.topMargin = mBlock.getCurrentTopMargin(puzzleSize, puzzleViewHeight);
        mBlock.getmImageView().setLayoutParams(layoutParams1);
    }

    private void swapBlock(Block mBlock1, Block mBlock2) {
        Block temBlock = mBlock1.getCopy();
        mBlock1.changePosition(mBlock2);
        mBlock2.changePosition(temBlock);
        repositionBlock(mBlock1);
        repositionBlock(mBlock2);
        mBlocks[mBlock1.getCurrentX()][mBlock1.getCurrentY()] = mBlock1;
        mBlocks[mBlock2.getCurrentX()][mBlock2.getCurrentY()] = mBlock2;
        if (isPuzzleCompleted()) {
            if (mOnCompleteListener != null) {
                mOnCompleteListener.onComplete();
            }
        }
    }

    /**
     * This method check puzzle completion status
     *
     * @return
     */
    private boolean isPuzzleCompleted() {
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                Block mBlock = mBlocks[i][j];
                if (mBlock.getRealX() != i || mBlock.getRealY() != j) {
                    return false;
                }
            }
        }
        return true;
    }

    /***
     * returns random block[][]
     * @return
     */
    private Block[][] getRandomBlock() {
        Block[][] mBlocksArray = new Block[puzzleSize][puzzleSize];
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                mBlocksArray[i][j] = new Block(i, j, i, j);
            }
        }
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                Random rn = new Random();
                int randomI = 0 + rn.nextInt(puzzleSize);
                int randomJ = 0 + rn.nextInt(puzzleSize);
                Block b1 = mBlocksArray[i][j];
                Block b2 = mBlocksArray[randomI][randomJ];
                Block temBlock = b1.getCopy();
                b1.changePosition(b2);
                b2.changePosition(temBlock);
                mBlocksArray[randomI][randomJ] = b1;
                mBlocksArray[i][j] = b2;

            }
        }
        return mBlocksArray;
    }

    public void autoSolve() {
        if (isDataRecieved && isParentInflated) {
            Block[][] tem = new Block[puzzleSize][puzzleSize];
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    Block mBlocktem = mBlocks[i][j];
                    mBlocktem.setCurrentX(mBlocktem.getRealX());
                    mBlocktem.setCurrentY(mBlocktem.getRealY());
                    tem[mBlocktem.getRealX()][mBlocktem.getRealY()] = mBlocktem;
                }
            }
            mBlocks = tem;
            showPuzzle();
        }

    }

    /*Getter & Setter*/

    public void setOnCompleteListener(OnCompleteListener mOnCompleteListener) {
        this.mOnCompleteListener = mOnCompleteListener;
    }

    public OnCompleteListener getmOnCompleteListener() {
        return mOnCompleteListener;
    }

    public int getPuzzleBorderSize() {
        return puzzleBorderSize;
    }

    public void setPuzzleBorderSize(int puzzleBorderSize) {
        this.puzzleBorderSize = puzzleBorderSize;
        showPuzzle();
    }

    public int getPuzzleBorderColor() {
        return puzzleBorderColor;
    }

    public void setPuzzleBorderColor(int puzzleBorderColor) {
        this.puzzleBorderColor = puzzleBorderColor;
        showPuzzle();
    }

    public boolean isPuzzleEnabled() {
        return puzzleEnabled;
    }

    public void setPuzzleEnabled(boolean puzzleEnabled) {
        this.puzzleEnabled = puzzleEnabled;
    }

    public int getPuzzleSize() {
        return puzzleSize;
    }

    public Block[][] getPuzzleBlockState() {
        return mBlocks;
    }


    private void resetZIndex() {
        for (int i = 0; i < mBlocks.length; i++) {
            for (int j = 0; j < mBlocks.length; j++) {
                ViewCompat.setZ(mBlocks[i][j].getmImageView(), 0);
            }
        }
    }


}
