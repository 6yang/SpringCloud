1.引入组件的启动器
2.覆盖默认配置
3.在引导类上添加注解，开发相关组件
Map<serviceId， Map<服务实例名，实例对象(instance)>>

### 1.架构的演变

传统架构-->水平拆分-->垂直拆分（最早的分布式）-->soa(dubbo)-->微服务(springCloud)

### 2.远程调用技术：rpc http

rpc协议：自定义数据格式，限定技术，传输速度快，效率高 tcp，dubbo
http协议：统一的数据格式，不限定技术 rest接口  tcp协议 springCloud

### 3.什么是springCloud

微服务架构的解决方案，是很多组件的集合
eureka：注册中心，服务的注册与发现
zull：网关协议，路由请求，过滤器（ribbon hystrix）
ribbon：负载均衡组件
hystrix：熔断组件
feign：远程调用组件(ribbon hystrix)

#### eureka

注册中心：itcast-eureka（1.引入启动器， 2.配置spring.application.name=itcast-eureka 3.在引导类上@EnableEurekaServer）
客户端：itcast-service-provider itcast-service-consumer
	（1.引入启动器 2.配置spring.application.name eureka.client.service-url.defaultZone=http://localhost:10086/eureka 3.@EnableDiscoveryClient(启用eure客户端)）

1.引入启动器
2.覆盖默认配置
3.在引导类上启用组件

1.高可用 itcast-eureka
10086 10087

2.心跳过期 itcast-service-provider
eureka：
	instance:
		lease-renewal-interval-in-seconds: 5 # 心跳时间
		lease-expiration-duration-in-seconds: 15 # 过期时间

3.拉取服务的间隔时间 itcast-service-consumer
eureka:
  client:
    registry-fetch-interval-seconds: 5

4.关闭自我保护，定期清除无效连接
eureka:
  server:
    eviction-interval-timer-in-ms: 5000 
    enable-self-preservation: false

#### ribbon: 负载均衡组件

​	1.eureka集成了
​	2.@LoadBalanced：开启负载均衡
​	3.this.restTemplate.getForObject("http://service-provider/user/" + id, User.class);
​	

#### hystrix：容错组件

​	降级：检查每次请求，是否请求超时，或者连接池已满
​		1.引入hystrix启动器
​		2.熔断时间，默认1s， 6s
​		3.在引导类上添加了一个注解：@EnableCircuitBreaker  @SpringCloudApplication
​		4.定义熔断方法：局部（要和被熔断的方法返回值和参数列表一致）  全局（返回值类型要被熔断的方法一致，参数列表必须为空）
​		5.@HystrixCommand(fallbackMethod="局部熔断方法名")：声明被熔断的方法
​		6.@DefaultProperties(defaultFallback="全局熔断方法名")
​	熔断：不再发送请求
​		1.close：闭合状态，所有请求正常方法
​		2.open：打开状态，所有请求都无法访问。如果在一定时间内容，失败的比例不小于50%或者次数不少于20次
​		3.half open：半开状态，打开状态默认5s休眠期，在休眠期所有请求无法正常访问。过了休眠期会进入半开状态，放部分请求通过
​		

#### feign

​	1.引入openFeign启动器
​	2.feign.hystrix.enable=true,开启feign的熔断功能
​	3.在引导类上 @EnableFeignClients
​	4.创建一个接口，在接口添加@FeignClient(value="服务id", fallback=实现类.class)
​	5.在接口中定义一些方法，这些方法的书写方式跟之前controller类似
​	6.创建了一个熔断类，实现feign接口，实现对应的方法，这些实现方法就是熔断方法
​	

#### zuul

​	1.引入zuul的启动器
​	2.配置：
​		zuul.routes.<路由名称>.path=/service-provider/**
​		zuul.routes.<路由名称>.url=http://localhost:8082

​		zuul.routes.<路由名称>.path=/service-provider/**
​		zuul.routes.<路由名称>.serviceId=service-provider


​		zuul.routes.服务名=/service-provider/**   *******************

​		不用配置，默认就是服务id开头路径
​	3.@EnableZuulProxy
​	过滤器：
​		创建一个类继承ZuulFilter基类
​		重写四个方法
​			filterType：pre route post error
​			filterOrder：返回值越小优先级越高
​			shouldFilter：是否执行run方法。true执行
​			run：具体的拦截逻辑




​	
​	
​	
​	
​	
​	
​	
​	