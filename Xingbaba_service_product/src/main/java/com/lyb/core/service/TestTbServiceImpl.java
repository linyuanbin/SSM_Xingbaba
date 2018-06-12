package com.lyb.core.service;


import com.lyb.core.bean.TestTb;
import com.lyb.core.dao.TestTbDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lin on 2018/3/13.
 */
@Service("testTbService") //testTbService表示TestTbServiceImpl在实例化是起的一个别名
@Transactional   //@Transactional标签打字类名上，类中的所有函数都默认会开启事务，个别函数不行要事务时，只需要在函数名上加上 @Transactional(readOnly=true)
public class TestTbServiceImpl implements TestTbService{

    @Autowired
    private TestTbDao testTbDao;

    public void insertTetstTb(TestTb testTb){
        testTbDao.insertTestTb(testTb);
       // throw new RuntimeException();
    }
}
