package studuy.dao;

public interface AccountDao {
    /*
    定义转账方法
     */

    //转出操作
    public void out(String outUser, Double money);

    //转入操作
    public void in(String inUser, Double money);
}
