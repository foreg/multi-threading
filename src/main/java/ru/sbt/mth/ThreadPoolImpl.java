package ru.sbt.mth;

import java.util.LinkedList;

public class ThreadPoolImpl implements ThreadPool{
    private final int THREAD_COUNT = 4;

    private volatile LinkedList<Runnable> queue = new LinkedList<>();
    private Thread[] workers = new Thread[THREAD_COUNT];
    private Thread thread;
    @Override
    public void start() {
        thread = new Thread(() -> doWork());
        thread.start();
    }

    @Override
    public void execute(Runnable runnable) {
        queue.add(runnable);
    }

    @Override
    public void shutdown() {
        thread.interrupt();
    }

    public void doWork() {
        while (!thread.isInterrupted()) {
            if (!queue.isEmpty()) {
                final Runnable temp = queue.peek();
                boolean allBuzy = true;
                for (int i = 0; i < workers.length; i++) {
                    if (workers[i] == null || workers[i].getState() != Thread.State.RUNNABLE) {
                        workers[i] = new Thread(temp);
                        workers[i].start();
                        allBuzy = false;
                        break;
                    }
                }
                if (!allBuzy) {
                    queue.remove();
                }
            }
        }
    }
}
