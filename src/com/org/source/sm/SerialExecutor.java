package com.org.source.sm;


import java.util.ArrayDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerialExecutor {
        final ArrayDeque<RunnableWrapper> mTasks = new ArrayDeque<RunnableWrapper>();
        RunnableWrapper mActive;
        private ExecutorService mSingleThreadExecutor = null;

        public void execute(final Runnable r) {
            execute(r, false);
        }

        public synchronized void execute(final Runnable r,  boolean immediately) {

            if (null == r) {
                throw new RuntimeException("Invalid param");
            }

            RunnableWrapper runnable = new RunnableWrapper(r);
            if (runnable.equals(mActive)) {
                return ;
            }
            
            if (mTasks.contains(runnable)) {
                if (!immediately) {
                    return ;
                }
                mTasks.remove(runnable);
            }

            if (immediately) {
                mTasks.push(runnable);
            } else{
                mTasks.offer(runnable);
            }

            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                if (null == mSingleThreadExecutor || mSingleThreadExecutor.isShutdown() || mSingleThreadExecutor.isTerminated()) {
                    mSingleThreadExecutor = Executors.newSingleThreadExecutor();
                }
                mSingleThreadExecutor.execute(mActive);
            }
        }
        
        private class RunnableWrapper implements Runnable {
            private final Runnable mActualRunnable;
            
            public RunnableWrapper(Runnable r) {
                if (null == r) {
                    throw new RuntimeException("Invalid params");
                }
                mActualRunnable = r;
            }
            
            public Runnable getActualRunnable() {
                return mActualRunnable;
            }

            @Override
            public void run() {
               try {
                   mActualRunnable.run();
               } finally {
                   scheduleNext();
               }
            }
            
            @Override
            public boolean equals(Object o) {
                if (null == o || !(o instanceof RunnableWrapper)){
                    return false;
                }
                return mActualRunnable.equals(((RunnableWrapper)o).getActualRunnable());
            }
        }
    }