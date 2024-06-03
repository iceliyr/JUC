package com.liyiru.juc.cf;

import java.util.concurrent.*;

/**
 * @auther zzyy
 * @create 2022-01-17 15:20
 */
public class CompletableFutureAPIDemo
{
//    public static void main(String[] args) throws ExecutionException, InterruptedException
//    {
//
//    }

    /**
     * ● 对计算结果进行处理
     *   ○ thenApply --->计算结果存在依赖关系，这两个线程串行化---->由于存在依赖关系（当前步错，不走下一步），当前步骤有异常的话就叫停
     *   ○ handle --->计算结果存在依赖关系，这两个线程串行化---->有异常也可以往下走一步
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }, threadPool).thenApply(f -> {
            System.out.println("222");
            return f + 2;
        }).handle((f, e) -> {
            System.out.println("3333");
            int i=10/0;
            return f + 2;

//             thenApply(f -> {
//            System.out.println("3333");
//            return f + 2;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("----计算结果" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getCause());
            return null;
        });
        System.out.println(Thread.currentThread().getName() + "------主线程先去做其他事情");
    }

    /**
     * 获得结果和触发计算
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void group1() throws InterruptedException, ExecutionException
    {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "abc";
        });

        //System.out.println(completableFuture.get());
        //System.out.println(completableFuture.get(2L,TimeUnit.SECONDS));
        //System.out.println(completableFuture.join());

        //暂停几秒钟线程
        //try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        //System.out.println(completableFuture.getNow("xxx"));
        System.out.println(completableFuture.complete("completeValue")+"\t"+completableFuture.get());
    }
}
