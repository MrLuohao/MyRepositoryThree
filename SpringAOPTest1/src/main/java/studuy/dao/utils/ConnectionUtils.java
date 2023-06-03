package studuy.dao.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/*
连接工具类:从数据源中获取到一个连接,并且将获取到的连接于线程进行绑定
ThreadLocal:线程内部的存储类,可以在指定的线程内存储数据  key: threadLocal(当前线程)  value :任意类型的值(此处为Connection)
 */
@Component
public class ConnectionUtils {
    //我们已经在核心配置文件中创建了dataSource对象存放在SpringIOC容器中,我们直接注入即可使用
    @Autowired
    private DataSource dataSource;
    //创建ThreadLocal集合对象用于存储线程
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    /*
    获取当前线程上的连接
     */
    public Connection getThreadConnection() {
        //1.获取threadLocal该集合对象中的线程
        Connection connection = threadLocal.get();
        //3.判断是否为空(判断threadLocal集合中是否存在连接)
        if (connection == null) {
            try {
                //4.如果threadLocal为空(没有连接),就从数据源中获取连接
                connection = dataSource.getConnection();
                //5.将获取到的连接放进threadLocal集合中
                threadLocal.set(connection);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return connection;
    }

    /*
    完善工具类:解除线程绑定
     */
    public void RemoveThreadLocal() {
        threadLocal.remove();
    }
}
