package cn.yang.elasticsearch.test;

import cn.yang.elasticsearch.Repository.ItemRepository;
import cn.yang.elasticsearch.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepository itemRepository;
    @Test
    public void testIndex() {
        //创建索引，会根据Item类的@Document注解信息来创建
        this.elasticsearchTemplate.createIndex(Item.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        this.elasticsearchTemplate.putMapping(Item.class);
    }

    @Test
    public void testCreate() {
        Item item = new Item(1L, "小米手机8", " 手机",
                "小米", 3699.00, "http://image.leyou.com/13123.jpg");
        this.itemRepository.save(item);
    }

    @Test
    public void testCreateall() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.leyou.com/3.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }


    @Test
    public void testFind() {
        Optional<Item> item = this.itemRepository.findById(1L);
        System.out.println(item.get());
    }

    @Test
    public void testFind1() {
        Iterable<Item> items= this.itemRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        items.forEach(item->{
            System.out.println(item);
        });

    }
}
