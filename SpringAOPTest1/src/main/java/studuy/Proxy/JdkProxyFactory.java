package studuy.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import studuy.dao.utils.TransactionManager;
import studuy.serivce.AccountSerivce;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
jdk动态代理方式:减少业务层和事务控制的代码耦合
 */
@Component//添加该类对象到springIOC容器中
public class JdkProxyFactory {
    @Autowired//注入事务管理类对象进行invoke方法中事务的控制流程
    private TransactionManager transactionManager;
    @Autowired//注入AccountSerivce对象是为了执行Proxy.newProxyInstance方法调用时作为参数传递
    private AccountSerivce accountSerivce;
    /*
    采用jdk动态代理技术来生成目标类的代理对象
    ClassLoader loader:类加载器:借助被代理对象来获取到类加载器
    Class<?> interfaces:被代理类所需要实现的全部接口
    InvocationHandler :当代理对象调用接口中的任意方法时都会执行InvocationHandler中的invoke方法
     */

    public AccountSerivce creatAccountServiceJdkProxy() {

        AccountSerivce accountSerivceProxyFactory = (AccountSerivce) Proxy.newProxyInstance(accountSerivce.getClass().getClassLoader(), accountSerivce.getClass().getInterfaces(), new InvocationHandler() {
            @Override    //proxy:当前的代理对象引用  method:被调用的目标方法的引用  args:被调用的目标方法所使用到的参数
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    //1.开启事务
                    transactionManager.beginTransaction();
                    //2.执行业务操作
                    method.invoke(accountSerivce, args);
                    //3.提交事务
                    transactionManager.CommitTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                    //回滚事务
                    transactionManager.RollbackTransaction();
                } finally {
                    //4.释放资源
                    transactionManager.release();
                }
                return null;
            }
        });
        return accountSerivceProxyFactory;
    }
}
