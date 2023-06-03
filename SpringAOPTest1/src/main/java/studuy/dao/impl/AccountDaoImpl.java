package studuy.dao.impl;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import studuy.dao.AccountDao;
import studuy.dao.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.SQLException;

//实现对应方法的接口类，并重写里面的抽象方法
@Repository("AccountDao")//表示spring会创建该类对象并起名为AccountDao存到IOC容器中，在需要用到的地方注入即可
public class AccountDaoImpl implements AccountDao {

    //添加queyRunner对象作为成员变量并添加Autowired标签表示等一下通过注入的方式给querRunner对象赋值完成初始化
    @Autowired
    private QueryRunner queryRunner;
    @Autowired//获取ConnectionUtils的对象,是为了调用该类中的getThreadConnection方法,保证不同方法在执行时使用queryRunner.方法名的时候传递的连接是同一个,从而保证多个方法是同一个线程在执行,从而也就保证了同一个事务中的多个方法在执行时属于同一个事务操作
    private ConnectionUtils connectionUtils;

    //账户转出操作
    @Override
    public void out(String outUser, Double money) {
        try {
            //使用连接工具类获取连接,保证事务操作是在同一个连接下进行(这是第一次执行,这时ThreadLocal集合中并没有连接,所以就会走到if里面,从数据源创建一个连接并将此连接放到ThreadLocal集合中)
            Connection connection = connectionUtils.getThreadConnection();
            String sql = "update account set money=money-? where name=?";
            queryRunner.update(connection,sql, money, outUser);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //账户转入操作
    @Override
    public void in(String inUser, Double money) {
        try {
            //使用连接工具类获取连接,保证事务操作是在同一个连接下进行(这是第二次执行获取连接方法,此时ThreadLocal集合中已经有一个连接了(第一次调用即out方法执行时创建的连接),所以这时就不会走if分支了,直接返回已有的连接,所以保证了执行业务操作的线程即连接是同一个)
            Connection connection = connectionUtils.getThreadConnection();
            String sql = "update account set money=money+? where name=?";
            queryRunner.update(connection,sql, money, inUser);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
