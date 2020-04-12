package com.edou.juc;

/**
 * @ClassName VolatileTest
 * @Description 解决多个线程操作共享数据的内存可见性问题
 * @Author 中森明菜
 * @Date 2020/4/10 12:30
 * @Version 1.0
 */
public class VolatileTest {
    // 多个线程操作共享变量时,会在自己的线程内将主存中的数据拷贝一份作为缓存,以提高操作效率
    // 但是这样就造成了一个问题,共享数据在内存的不可见,就如以下案例
    // 如果让改变共享数据的线程执行比主线程慢,那么在主线程中操作的数据是缓存数据,无法感知主存中数据的改变
    // 解决办法:1.使用synchronized给变量加锁,这样每次访问共享数据会在主存中读取,刷新缓存,但是synchronized是重量级锁,加锁释放锁以及会造成线程阻塞
    //         2.使用volatile 关键字对共享变量修饰,这样每次操作这个变量都会在主存中,防止了内存的不可见,
    //              第二个作用就是 可以防止指令的重排序,比如i++在内存中分为三次操作,读取,自增,赋值,
    //              指令重排序是jvm为了执行效率,可能这三个操作的顺序会更改,
    //              但是有时候我们为了防止jvm的指令重排序 可以使用volatile关键字
    // volatile不具备互斥性,不能保证变量的原子性
    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();
        while(true){
            /*synchronized (td){
                if(td.isFlag()){
                    System.out.println("------------");
                    break;
                }
            }*/
            if(td.isFlag()){
                System.out.println("------------");
                break;
            }
        }
    }
}
class ThreadDemo implements Runnable{
    private volatile boolean flag = false;
    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
        System.out.println("flag is:"+flag);
    }
    public boolean isFlag(){
        return flag;
    }
}
