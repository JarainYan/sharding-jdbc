import com.example.sharding.ShardingApplication;
import com.example.sharding.dao.MOrderDao;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zyan
 * @Date: 2021/8/17 11:02
 * @Description: MOrderTest
 * @Version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingApplication.class)
public class MOrderTest {


    @Autowired
    private MOrderDao mOrderDao;

    @Test
    public  void findByuserIdSellerIdTime(){
        mOrderDao.getMOrderDoByuserIdSellerIdTime(632940421651103744L,7L,
                "2021-03-07 09:56:29","2021-03-22 09:56:29");
    }


}
