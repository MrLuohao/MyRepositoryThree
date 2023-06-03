package studuy.dao.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component//表示创建该类的对象保存于IOC容器中,需要使用时注入即可
public class TransactionManager {
    @Autowired//注入该类对象
    private ConnectionUtils connectionUtils;

    /*
    开启事务
     */
    public void beginTransaction() {
        try {
            //开启事务,并设置为手动提交事务
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    提交事务
     */
    public void CommitTransaction() {
        try {
            //提交事务
            connectionUtils.getThreadConnection().commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    回滚事务
     */
    public void RollbackTransaction() {
        try {
            //回滚事务
            connectionUtils.getThreadConnection().rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    释放资源
     */
    public void release() {
        try {
            //改回自动提交事务
            connectionUtils.getThreadConnection().setAutoCommit(true);
            //释放资源,归还连接
            connectionUtils.getThreadConnection().close();
            //解除线程绑定
            connectionUtils.RemoveThreadLocal();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
