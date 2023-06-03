package studuy.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;
import studuy.dao.utils.TransactionManager;
import studuy.serivce.AccountSerivce;

import java.lang.reflect.Method;

@Component//添加该类对象存到SpringIOC容器中
public class CglibProxyFactory {
    @Autowired
    private AccountSerivce accountSerivce;
    @Autowired
    private TransactionManager transactionManager;

    public AccountSerivce creatAccountSerivceCglibProxy() {
        AccountSerivce accountSerivceCglibProxy = (AccountSerivce) Enhancer.create(accountSerivce.getClass(), new MethodInterceptor() {
            /*
            采用Cglib动态代理返回被代理对象
            参数一:目标类(指被代理类)的字节码对象
            参数二:动作类,当代理对象调用调用目标对象中原方法时,都会先执行intercept方法
             */
            @Override//o:代表生成的代理对象 method:调用目标方法的引用 object:方法入参 MethodProxy:代理方法
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                try {
                    //1.开启事务
                    transactionManager.beginTransaction();
                    //2.执行事务,执行的是AccountSerivceImpl类中的transfer方法
                    method.invoke(accountSerivce, objects);
                    //3.提交事务
                    transactionManager.CommitTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                    //4.如果出现异常,就回滚事务
                    transactionManager.RollbackTransaction();
                } finally {
                    //5.无论提交还是回滚,都要执行资源的释放
                    transactionManager.release();
                }
                return null;
            }
        });
        return accountSerivceCglibProxy;
    }
}
