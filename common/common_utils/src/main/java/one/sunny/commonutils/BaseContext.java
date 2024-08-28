package one.sunny.commonutils;

/**
 * ThreadLocal 是Thread的局部变量
 * 实现线程隔离，为每个线程提供单独一份存储空间，只有在线程内才能获取到对应的值，线程外不能访问
 */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
