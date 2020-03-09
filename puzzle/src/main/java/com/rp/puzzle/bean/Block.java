package com.rp.puzzle.bean;

import android.widget.ImageView;

import org.json.JSONObject;



public class Block {

    private ImageView mImageView;
    private int realX;
    private int realY;
    private int currentX;
    private int currentY;
    private int _xDelta;
    private int _yDelta;

    public Block(int realX, int realY, int currentX, int currentY) {
        this.realX = realX;
        this.realY = realY;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    /***
     * change only current block position with mBlock
     * @param
     */

    public void changePosition(Block mBlock) {
        setCurrentX(mBlock.getCurrentX());
        setCurrentY(mBlock.getCurrentY());
        set_xDelta(mBlock.get_xDelta());
        set_yDelta(mBlock.get_yDelta());
    }

    /***
     *
     * @return Returns the changePosition of current object
     */
    public Block getCopy() {
        Block mBlock = new Block(realX, realY, currentX, currentY);
        mBlock.setmImageView(mImageView);
        mBlock.set_xDelta(_xDelta);
        mBlock.set_yDelta(_yDelta);
        return mBlock;
    }

    public int getCurrentLeftMargin(int puzzleSize, int parentWidth) {
        int blockWidth = parentWidth / puzzleSize;
        return blockWidth * (currentX);
    }

    public int getCurrentTopMargin(int puzzleSize, int parentHeight) {
        int blockHeight = parentHeight / puzzleSize;
        return blockHeight * (currentY);
    }

    public int getRealLeftMargin(int puzzleSize, int parentWidth) {
        int blockWidth = parentWidth / puzzleSize;
        return blockWidth * (realX);
    }

    public int getRealTopMargin(int puzzleSize, int parentHeight) {
        int blockHeight = parentHeight / puzzleSize;
        return blockHeight * (realY);
    }


    /*setter & Getter*/

    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    public ImageView getmImageView() {
        return mImageView;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getCurrentX() {
        return currentX;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void set_xDelta(int _xDelta) {
        this._xDelta = _xDelta;
    }

    public int get_xDelta() {
        return _xDelta;
    }

    public int get_yDelta() {
        return _yDelta;
    }

    public void set_yDelta(int _yDelta) {
        this._yDelta = _yDelta;
    }

    public void setRealX(int realX) {
        this.realX = realX;
    }

    public int getRealX() {
        return realX;
    }

    public void setRealY(int realY) {
        this.realY = realY;
    }

    public int getRealY() {
        return realY;
    }



}
