package cn.yang.elasticsearch.test;

import cn.yang.elasticsearch.Repository.ItemRepository;
import cn.yang.elasticsearch.pojo.Item;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        // items.forEach(item->{
        //    System.out.println(item);
        // });
        items.forEach(System.out::println);
    }

    @Test
    public void findByTitle() {
        List<Item> items = this.itemRepository.findByTitle("手机");
        items.forEach(System.out::println);
    }

    @Test
    public void findByPirce() {
        List<Item> items = this.itemRepository.findByPriceBetween(3699d,4400d);
        items.forEach(System.out::println);

    }

    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    @Test
    public void testSearch() {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "手机");
        Iterable<Item> items = this.itemRepository.search(matchQueryBuilder);
        items.forEach(System.out::println);
    }

    @Test
    public void nativequery() {
        // 构建自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本的查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","手机"));
        //执行查询 并且获取分页结果集
        Page<Item> itempage = this.itemRepository.search(queryBuilder.build());
        System.out.println(itempage.getTotalPages());
        System.out.println(itempage.getTotalElements());
        itempage.getContent().forEach(System.out::println);
    }

    @Test
    public void nativequery1() {
        // 构建自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本的查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("category","手机"));
        //添加分页条件 ,页码是从0开始
        // queryBuilder.withPageable(PageRequest.of(1,2));
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        //执行查询 并且获取分页结果集
        Page<Item> itempage = this.itemRepository.search(queryBuilder.build());
        System.out.println(itempage.getTotalPages());
        System.out.println(itempage.getTotalElements());
        itempage.getContent().forEach(System.out::println);
    }

    @Test
    public void testAggs() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand"));
        //添加结果集过滤  不包括任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));
        // 执行结果集查询
        AggregatedPage<Item> itemAggregatedPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
        //获取聚合结果集,根据聚合的类型和字段类型要进行强转
        StringTerms brandAgg = (StringTerms) itemAggregatedPage.getAggregation("brandAgg");
        // 获取桶的集合
        List<StringTerms.Bucket> buckets = brandAgg.getBuckets();
        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString() +":"+bucket.getDocCount());
        });


    }


    @Test
    public void testSubAggs() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand")
        .subAggregation(AggregationBuilders.avg("price_avg").field("price")));
        //添加结果集过滤  不包括任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));
        // 执行结果集查询
        AggregatedPage<Item> itemAggregatedPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
        //获取聚合结果集,根据聚合的类型和字段类型要进行强转
        StringTerms brandAgg = (StringTerms) itemAggregatedPage.getAggregation("brandAgg");
        // 获取桶的集合
        List<StringTerms.Bucket> buckets = brandAgg.getBuckets();
        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString() +":"+bucket.getDocCount());
            // 获取子聚合的map集合 ， key 聚合名称 value对应的子聚合对象
            Map<String, Aggregation> stringAggregationMap = bucket.getAggregations().asMap();
            InternalAvg price_avg = (InternalAvg) stringAggregationMap.get("price_avg");
            System.out.println(price_avg.getValue());

        });


    }
}
