import com.example.sharding.ShardingApplication;
import com.example.sharding.dao.OrderDao;
import com.example.sharding.entity.OrderDo;
import com.google.common.collect.Lists;
import javafx.application.Application;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingApplication.class)
class MyTestTest {
    @Autowired
    private OrderDao orderDao;

    /**
     * 新增订单,用户  分表
     */
    @Test
    public void testInsertOrder(){
        for (int i = 0 ; i<10; i++){
            /**
             * 保存订单 手写sql
             */
//           orderDao.insertOrder(new BigDecimal((i+1)*5),1L,"WAIT_PAY",String.valueOf(i));
            /**
             * 保存用户 手写sql
             */
//            orderDao.insertUser("名字"+i,i);
            /**
             * 保存订单  jpa
             */
            OrderDo orderDo = new OrderDo();
            orderDo.setCreateTime(new Date());
            orderDo.setPrice(new BigDecimal(8));
            orderDo.setStatus("已付款");
            orderDo.setUser_name("小红");
            orderDo.setUserId(55L);
            orderDao.save(orderDo);
        }
    }

    /**
     * 查询订单  分表查询
     */
    @Test
    public void testSelectOrderbyIds(){
        /**
         * 自定义sql查询
         */
//        List<Long> ids = new ArrayList<>();
//        ids.add(632882620174172160L);
//        ids.add(632882620245475329L);
//        List<OrderDo> maps = orderDao.selectOrderbyIds(ids);
//        System.out.println("maps:"+maps.size()+maps.toString());
        /**
         * jpa查询  查询全部
         */
        List<OrderDo> all = orderDao.findAll();
        System.out.println("all:"+all.size()+all.toString());

    }

    /**
     * 查询商品 不分表
     */
    @Test
    public void selectOrderbyProductId(){
        List<Map> maps = orderDao.selectOrderbyProductId(1L);
        System.out.println("maps:"+maps.size());
        maps.forEach(map -> {
            map.keySet().forEach(k->{
                System.out.println("map:"+map.get(k));
            });
            System.out.println("============================================");
        });
    }

    /**
     * 新增商品 不分表
     */
    @Test
    public void insertProduct(){
        for (int i = 0 ; i<10; i++){
            orderDao.insertProduct(new BigDecimal((i+1)*5),"商品8"+i);
        }
    }

    /**
     * 两个做了分表的表关联查询
     */
    @Test
    public void selectOrderbUserId(){
        //单 user_id 查询  会先判断user_id在哪个表  再去关联
        //比如 632940421609160704再t_user_1表就会关联t_user_1查询，
        // 632940421588189185L再t_user_2表就会关联t_user_2查询
//        List<Map> maps = orderDao.selectOrderbUserId(632940421609160704L);
//        List<Map> maps2 = orderDao.selectOrderbUserId(632940421588189185L);
//
//        ArrayList<Long> list = Lists.newArrayList();
//        list.add(632940421651103744L);
//        list.add(632940421588189185L);
//        List<Map> maps3 = orderDao.selectOrderbUserIds(list);

        List<Map> maps4 = orderDao.selectOrderbUserName("名字5");


        System.out.println("maps:"+maps4.size());
        maps4.forEach(map -> {
            map.keySet().forEach(k->{
                System.out.println("map:"+map.get(k));
            });
            System.out.println("============================================");
        });
    }
}