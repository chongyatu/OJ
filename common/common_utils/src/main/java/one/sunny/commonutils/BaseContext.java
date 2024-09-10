//package one.sunny.commonutils;
//
//import one.sunny.commonutils.pojo.bo.LoginUserBo;
//
///**
// * ThreadLocal 是Thread的局部变量
// * 实现线程隔离，为每个线程提供单独一份存储空间，只有在线程内才能获取到对应的值，线程外不能访问
// */
//public class BaseContext {
//
//    public static ThreadLocal<LoginUserBo> threadLocal = new ThreadLocal<>();
//
//    public static void setCurrentLoginUserBo(LoginUserBo loginUserBo) {
//        threadLocal.set(loginUserBo);
//    }
//
//    public static one.sunny.ttoj.pojo.bo.LoginUserBo getCurrentLoginUserBo() {
//        return threadLocal.get();
//    }
//
//    public static void removeCurrentLoginUserBo() {
//        threadLocal.remove();
//    }
//
//}
