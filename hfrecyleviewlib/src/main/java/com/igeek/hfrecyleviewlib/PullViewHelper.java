package com.igeek.hfrecyleviewlib;

public class PullViewHelper {

    //下拉视图的高度
    private int pullViewHeight;
    //下拉的最大高度
    private int pullMaxHegiht;
    //下拉视图的最小高度
    private int pullMinHegiht;
    //刷新的临界高度
    private int pullRefreshHeight;
    //阻尼因子
    private float SCROLL_RATIO = 0.5f;
    private boolean mIsInTouch = false;
    private float mScroll = 0;
    private int mMaxScroll = 0;
    private int mMinScroll = 0;
    private float refreshPercentage;

    public PullViewHelper(int height, int maxHeight, int minHeight,int refreshHeight) {
        pullViewHeight = Math.max(0, height);
        pullMinHegiht = Math.max(0, minHeight);
        pullMaxHegiht = Math.max(pullViewHeight, maxHeight);
        pullRefreshHeight=Math.max(pullViewHeight, refreshHeight);

        mScroll = 0;
        mMaxScroll = 0;
        mMinScroll = -pullMaxHegiht;

        refreshPercentage=pullRefreshHeight*(1.0F)/pullMaxHegiht;
    }

    public int getMaxHeight() {
        return pullMaxHegiht;
    }

    public int getMinHeight() {
        return pullMinHegiht;
    }

    public int getHeight() {
        return pullViewHeight;
    }

    public int getScroll() {
        return (int) mScroll;
    }

    public int getMaxScroll() {
        return mMaxScroll;
    }

    public int getMinScroll() {
        return mMinScroll;
    }

    public int getCurPosition() {
        return (int) (pullViewHeight - mScroll);
    }

    public boolean isInTouch() {
        return mIsInTouch;
    }

    public boolean canScrollDown() {
        return mScroll > mMinScroll;
    }

    public boolean canScrollUp() {
        return mScroll < mMaxScroll;
    }

    public int checkUpdateScroll(int deltaY) {

        float willTo;

        float consumed = deltaY;

        if(deltaY>0){
            willTo=mScroll+deltaY;
            if(willTo>mMaxScroll){
                willTo=mMaxScroll;
                consumed=willTo-mMaxScroll;
            }
        }else{
            willTo = mScroll + deltaY * SCROLL_RATIO;
            final float pullHeight=Math.abs(willTo);

            float dragPercent = Math.min(1f, pullHeight / pullViewHeight);
            float tensionSlingshotPercent = Math.max(0, Math.min(pullHeight - pullViewHeight,pullMaxHegiht-pullViewHeight) / pullViewHeight);
            float tensionPercent = (float) ((tensionSlingshotPercent / 4) -Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
            float extraMove = (pullViewHeight) * tensionPercent * 2;
            willTo = -((pullViewHeight * dragPercent) + extraMove);

            if(willTo>mScroll) willTo=mScroll;

            consumed=willTo-mScroll;

//            Logger.i("willTo=" + willTo+"\nmScroll="+mScroll+"\nconsumed="+consumed);

        }

        mScroll = willTo;

        return (int) consumed;
    }

    public boolean isOverHeight() {
        return mScroll < 0;
    }

    public float getMovePercentage() {
        return -mScroll / pullMaxHegiht;
    }

    public boolean canSendRefresh() {
        return getMovePercentage() > refreshPercentage;
    }

    public float getRefreshPercentage() {
        return refreshPercentage;
    }

    public int getPullRefreshHeight() {
        return pullRefreshHeight;
    }
}