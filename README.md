curing
=====

####用途

+  简化ibatis层的dao访问

+  统一的接口调用方式

+  使得ibatis的逻辑分页为物理分页

+  默认统计sql性能

####使用

+	maven依赖
<pre>
&lt;dependency>
	&lt;groupId>com.edwin&lt;/groupId>
	&lt;artifactId>curing&lt;/artifactId>
	&lt;version>0.0.1-SNAPSHOT&lt;/version>
&lt;/dependency>
</pre>

+	公用spring配置，已配置
<!-- 公用spring配置 -->

	<!-- mysql方言，用于分页 -->
	<bean id="mysqlDialect" class="com.edwin.curing.dialect.impl.MySqlDialect" />

	<!-- advice拦截器，实现IntroductionInterceptor用于接口增强 -->
	<bean id="daoAdvice" class="com.edwin.curing.DAOAdvice" />
	
	<bean id="daoAdvisor"
		class="org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor">
		<property name="advice" ref="daoAdvice" />
		<property name="expression" 
			value="execution(* com..dao..*.*(..)) and !execution(* com.edwin.curing..dao..*.*(..))" />
	</bean>
	
	<!-- 抽象父bean，spring不会去实例化 -->
	<bean id="parentDao" class="org.springframework.aop.framework.ProxyFactoryBean"
		abstract="true">
		<property name="interceptorNames">
			<list>
				<value>daoAdvisor</value>
			</list>
		</property>
	</bean>

+ 	业务spring配置
<!-- 业务spring配置-->

	<!-- 数据源 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" />

	<!-- sqlMapClient配置 -->
	<bean id="sqlMapClient" 
		class="com.edwin.curing.ibatis.LimitSqlMapClientFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="configLocation" value="classpath:sqlmap-config.xml" />
		<property name="dialect" ref="mysqlDialect" />
	</bean>

	<!-- 需要被代理的抽象target配置 -->
	<bean id="proxyTarget" class="com.edwin.curing.ibatis.IbatisGenericDaoImpl"
		abstract="true">
		<property name="sqlMapClient" ref="sqlMapClient" />
	</bean>

	<!-- 业务example -->
	<bean id="exampleDao" parent="parentDao">
		<property name="proxyInterfaces" value="com.edwin.dao.ExampleDao" />
		<property name="target">
			<bean parent="proxyTarget">
				<constructor-arg value="Example" />
		</bean>
	</property>
	</bean>
	
+ 	业务Dao Example
<pre>
<code>
	/**
     * 获取某一城市下的用户id
     * 
     * @param userIds
     * @return
     */
    @DAOAction(action = DAOActionType.QUERY)
    List<Integer> findUserIdsByCityId(@DAOParam("cityId")
    int cityId, @DAOParam("userIds")
    List<Integer> userIds);
</code>
</pre>


####实现原理
基于spring的IntroductionInterceptor接口实现自定义接口的代理增强，关键实现类是IbatisGenericDaoImpl
