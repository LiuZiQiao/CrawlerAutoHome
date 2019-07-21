## 开发流程分析

### 流程图

![流程图](流程图.png)

### 数据库设计

​	爬取的汽车信息的属性

## 定时任务

### Quartz对任务调度的时序大致如下:

0.调度器线程run()
1.获取待触发trigger 
1.1读取JobDetail信息 
1.2读取trigger表中触发器信息并标记为”已获取”
2.触发trigger 
2.1确认trigger的状态 
2.2读取trigger的JobDetail信息 
2.3读取trigger的Calendar信息 
2.4更新trigger信息
3实例化并执行Job 
3.1从线程池获取线程执行JobRunShell的run方法

## 爬取

###  测试

用Jsoup下载一个页面并写入test.html中，然后找到title字段并打印出来

```java
    public static void main(String[] args) throws Exception {
        Document document = Jsoup.parse(new URL("https://www.autohome.com.cn/bestauto/1"), 10000);
        FileUtils.writeStringToFile(new File("G:/test.html"),document.html(),"UTF-8");
        String title = document.getElementsByTag("title").first().text();
        System.out.println(title);
    }
```

### 布隆过滤器

​         刚开始设计的是使用Map，Set集合来去重，最后看文章发现数据量大的时候存在很多问题，极大的占用内存和系统资源，导致爬虫系统崩溃。

​        哈希表也能用于判断元素是否在集合中，但是布隆过滤器只需要哈希表的1/8或1/4的空间复杂度就能完成同样的问题。布隆过滤器可以插入元素，但不可以删除已有元素。其中的元素越多，误报率越大，但是漏报是不可能的。

**过滤器原理**

布隆过滤器需要的是一个位数组(和位图类似)和K个映射函数(和Hash表类似)，在初始状态时，对于长度为m的位数组array，它的所有位被置0。

![](F:\项目\CrawlerAutoHome\01.png)

对于有n个元素的集合S={S1,S2...Sn},通过k个映射函数{f1,f2,......fk}，将集合S中的每个元素Sj(1<=j<=n)映射为K个值{g1,g2...gk}，然后再将位数组array中相对应的array[g1],array[g2]......array[gk]置为1：

![](F:\项目\CrawlerAutoHome\02.png)

​     如果要查找某个元素item是否在S中，则通过映射函数{f1,f2,...fk}得到k个值{g1,g2...gk}，然后再判断array[g1],array[g2]...array[gk]是否都为1，若全为1，则item在S中，否则item不在S中。

​      布隆过滤器会造成一定的误判，因为集合中的若干个元素通过映射之后得到的数值恰巧包括g1,g2,...gk，在这种情况下可能会造成误判，但是概率很小。

**过滤器算法**

```java
public class TitleFilter {
    /* BitSet初始分配2^24个bit */
    private static final int DEFAULT_SIZE = 1 << 24;

    /* 不同哈希函数的种子，一般应取质数 */
    private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37 };

    private BitSet bits = new BitSet(DEFAULT_SIZE);

    /* 哈希函数对象 */
    private SimpleHash[] func = new SimpleHash[seeds.length];

    public TitleFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }

    // 将url标记到bits中
    public void add(String str) {
        for (SimpleHash f : func) {
            bits.set(f.hash(str), true);
        }
    }

    // 判断是否已经被bits标记
    public boolean contains(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }

        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(str));
        }

        return ret;
    }

    /* 哈希函数类 */
    public static class SimpleHash {
        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        // hash函数，采用简单的加权和hash
        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }
}
```

**写入数据库时的过滤**

```java
public class TitleFilter {
    /* BitSet初始分配2^24个bit */
    private static final int DEFAULT_SIZE = 1 << 24;
    /* 不同哈希函数的种子，一般应取质数 */
    private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37 };
    private BitSet bits = new BitSet(DEFAULT_SIZE);
    /* 哈希函数对象 */
    private SimpleHash[] func = new SimpleHash[seeds.length];
    public TitleFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }
    // 将url标记到bits中
    public void add(String str) {
        for (SimpleHash f : func) {
            bits.set(f.hash(str), true);
        }
    }
    // 判断是否已经被bits标记
    public boolean contains(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(str));
        }
        return ret;
    }
    /* 哈希函数类 */
    public static class SimpleHash {
        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }
        // hash函数，采用简单的加权和hash
        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }
}
```



```java
@Configuration
public class TitleFilterCfg {
    @Autowired
    private CarTestService carTestService;
    @Bean
    public TitleFilter titleFilter(){
        //创建汽车标题的去重过滤器
        TitleFilter titleFilter = new TitleFilter();
        int page = 1,pageSzie = 0;
        do {
            //查询数据库中title数据，防止数据过大，分页查询
            List<String> titles = carTestService.queryTitleByPage(page,500);
            for (String title: titles) {
                titleFilter.add(title);
            }
            page++;
            pageSzie = titles.size();
        }while(pageSzie == 500);
        return titleFilter;
    }
}
```

