package studuy.serivce;

public interface AccountSerivce {
    /*
    服务层实现业务逻辑的编写
     */

    //定义转账方法(参数转出账户，转入账户，转出金额)
    public void transfer(String outUser, String inUser, double money);
}
