package ru.sbt.mth;

public class Test {
    static final int CALC_COUNT = 100000;

    public static void main( String... args ) throws InterruptedException {
        ThreadPool threadPool = new ThreadPoolImpl();
        threadPool.start();

        int cores = 4 * Runtime.getRuntime().availableProcessors();
        for ( int i = 0; i < cores; i++ ) {
            final int numberTask = i;
            threadPool.execute(() -> heavyCalc(numberTask));
        }
        Thread.sleep(3000);
        threadPool.shutdown();
    }

    public static void heavyCalc( int numberTask ) {
        double a = 0;
        for ( int k = 0; k < CALC_COUNT; k++ ) {
            a += Math.tan( k );
        }

        System.out.println( "task: " + numberTask + " a=" + a +
                " from thread:" + Thread.currentThread().getName() );
    }
}