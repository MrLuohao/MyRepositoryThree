package studuy.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studuy.dao.AccountDao;
import studuy.serivce.AccountSerivce;

@Service("AccountSerivce")//表示Spring会创建该类对象并起名为AccountSerivce存到IOC容器当中
public class AccountSerivceImpl implements AccountSerivce {
    //添加dao层接口类型的方法去实现该接口中方法的调用
    @Autowired//添加上该注解表示spring会将IOC容器中的accountDao类型的对象注入给当前成员变量accountDao
    private AccountDao accountDao;

    @Override
    public void transfer(String outUser, String inUser, double money) {
        accountDao.out(outUser, money);
//              int i = 2 / 0;//此时报错是因为两个方法执行时不是同一个事务,也不是同一个线程在执行,所以为了保证两个方法属于同一个事务,也就需要保证两个方法属于同一个线程在执行,这样的话,两个方法都执行成功从才能提交事务,其中一个方法执行失败就执行回滚操作,这样才能保证事务的完整性,以及数据库数据的完整性
        accountDao.in(inUser, money);
    }
}
