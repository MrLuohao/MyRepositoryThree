package studuy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import studuy.Proxy.CglibProxyFactory;
import studuy.Proxy.JdkProxyFactory;
import studuy.serivce.AccountSerivce;

//测试类
@RunWith(SpringJUnit4ClassRunner.class)//指定测试运行环境为Spring运行环境
@ContextConfiguration("classpath:applicationContext.xml")//加载指定路径的核心配置文件
public class SpringAOPTest {
    @Autowired//注入JDK代理工厂类用于测试
    private JdkProxyFactory jdkProxyFactory;
    @Autowired//注入Cglib代理工厂类用于测试
    private CglibProxyFactory cglibProxyFactory;

    @Autowired
    @Qualifier("AccountSerivce")//使用指定类型指定名称的IOC中的已经创建好的对象
    private AccountSerivce accountSerivce;

    @Test
    public void testTransfer() {
        accountSerivce.transfer("xiaob", "kkk", 200);
    }

    @Test
    public void testTransferJdkFoctory() {
        //这里的accountServiceJdkProxy对象并不是AccountSerivce真正的实现类对象,而是jdk动态代理生成的代理对象proxy
        AccountSerivce accountServiceJdkProxy = jdkProxyFactory.creatAccountServiceJdkProxy();
        //在执行transfer方法之前,会先执行creatAccountServiceJdkProxy方法里匿名内部类里的invoke方法
        accountServiceJdkProxy.transfer("xiaob", "kkk", 200);
    }

    @Test
    public void testTransferCglibFactory() {
        //这里的accountSerivceCglibProxy对象不是真正的实现类对象,而是Cglib动态代理生成的代理对象proxy
        AccountSerivce accountSerivceCglibProxy = cglibProxyFactory.creatAccountSerivceCglibProxy();
        //在执行transfer方法之前,会先执行CglibProxyFactory类中creatAccountSerivceCglibProxy方法里匿名内部类里的intercept方法
        accountSerivceCglibProxy.transfer("xiaob", "kkk", 200);
    }
}
