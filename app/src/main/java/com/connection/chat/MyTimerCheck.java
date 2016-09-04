/*
 *@author Dawin,2015-3-5
 *
 *
 *
 */
package com.connection.chat;

public abstract class MyTimerCheck {
    private int mCount = 0;
    private int mTimeOutCount = 1;
    private int mSleepTime = 1000; //
    private boolean mExitFlag = false;
    private Thread mThread = null;

    /**
     * Do not process UI work in this.
     */
    public abstract void doTimerCheckWork();

    public abstract void doTimeOutWork();

    public MyTimerCheck() {
        mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!mExitFlag) {
                    mCount++;
                    if (mCount < mTimeOutCount) {
                        doTimerCheckWork();
                        try {
                            mThread.sleep(mSleepTime);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            exit();
                        }
                    } else {
                        doTimeOutWork();
                    }
                }
            }
        });
    }

    /**
     * start
     *
     * @param timeOutCount How many times will check?
     * @param sleepTime    ms, Every check sleep time.
     */
    public void start(int timeOutCount, int sleepTime) {
        mTimeOutCount = timeOutCount;
        mSleepTime = sleepTime;

        mThread.start();
    }

    public void exit() {
        mExitFlag = true;
    }

}