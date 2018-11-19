# <a name="top">Spring源码——Singleton Bean 的创建</a>



`Spring Singleton Bean`的相关的一系列操作在接口 `SingletonBeanRegistry` 里定义，该接口定义的方法有：

+ `void registerSingleton(String beanName, Object singletonObject)`—— 将给定名字的单例对象缓存到`Bean`注册表里


+ `Object getSingleton(String beanName)`——根据名字获取已注册的单例对象


+ `boolean containsSingleton(String beanName)`


+ `String[] getSingletonNames()`


+ `int getSingletonCount()`


+ `Object getSingletonMutex()` —— 返回存放单例对象集合的`ConcurrentHashMap`，对该`ConcurrentHashMap`的使用应该是**互斥**的，***即要加锁***



`DefaultSingletonBeanRegistry`类实现了`SingletonBeanRegistry`接口，Spring启动时默认使用该实现进行单例对象的管理。类间关系如下图所示：

![DefaultSingletonBeanRegistry](https://github.com/HurricanGod/Home/blob/master/spring/img/DefaultSingletonBeanRegistry.png)

![BeanFactory](https://github.com/HurricanGod/Home/blob/master/spring/img/BeanFactory.png)





实现类`DefaultSingletonBeanRegistry`中`getSingleton()`主要有两个实现：

+ `Object getSingleton(String beanName, boolean allowEarlyReference)`
  + 访问类型为`protected`
  + 采用*double-check*加锁的方式创建单例对象，<a href="#protectedGetSingleton">**源码**<a>
  + 主要用以创建需要提前实例化的单例对象


+ `Object getSingleton(String beanName, ObjectFactory<?> singletonFactory)`
  + 访问类型为`public`
  + 如果给定**beanName**的单例对象不存在则创建后并注册到`singletonObjects`中，<a  href="#publicGetSingleton">**源码**</a>





<a name="protectedGetSingleton">`protected Object getSingleton(String beanName, boolean allowEarlyReference)`<a>

```java
	/**
	 * Return the (raw) singleton object registered under the given name.
	 * <p>Checks already instantiated singletons and also allows for an early
	 * reference to a currently created singleton (resolving a circular reference).
	 * @param beanName the name of the bean to look for
	 * @param allowEarlyReference whether early references should be created or not
	 * @return the registered singleton object, or {@code null} if none found
	 */
	protected Object getSingleton(String beanName, boolean allowEarlyReference) {
      // 
	/** Cache of singleton objects: bean name --> bean instance */
	// private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);

		Object singletonObject = this.singletonObjects.get(beanName);
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
			synchronized (this.singletonObjects) {
				singletonObject = this.earlySingletonObjects.get(beanName);
				if (singletonObject == null && allowEarlyReference) {
					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
					if (singletonFactory != null) {
						singletonObject = singletonFactory.getObject();
						this.earlySingletonObjects.put(beanName, singletonObject);
						this.singletonFactories.remove(beanName);
					}
				}
			}
		}
		return (singletonObject != NULL_OBJECT ? singletonObject : null);
	}

```

+ `singletonObjects`是有个**缓存单例Bean对象的Map**
+ `earlySingletonObjects`也是一个**缓存单例Bean对象的Map**， Map里存放提前初始化的单例对象，若在这个Map里根据`beanName`找不到单例Bean实例便会去查找创建该Bean的单例工厂
+ 拿到创建Bean的工厂后便创建单例Bean，创建完成后将实例放入`earlySingletonObjects`中，并把创建给Bean的工厂删除





<a  name="publicGetSingleton">`public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory)`</a>

```java

	/**
	 * Return the (raw) singleton object registered under the given name,
	 * creating and registering a new one if none registered yet.
	 * @param beanName the name of the bean
	 * @param singletonFactory the ObjectFactory to lazily create the singleton
	 * with, if necessary
	 * @return the registered singleton object
	 */
	public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
		Assert.notNull(beanName, "'beanName' must not be null");
		synchronized (this.singletonObjects) {
			Object singletonObject = this.singletonObjects.get(beanName);
			if (singletonObject == null) {
				if (this.singletonsCurrentlyInDestruction) {
					throw new BeanCreationNotAllowedException(beanName,
							"Singleton bean creation not allowed while singletons of this factory are in destruction " +
							"(Do not request a bean from a BeanFactory in a destroy method implementation!)");
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
				}
				beforeSingletonCreation(beanName);
				boolean newSingleton = false;
				boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
				if (recordSuppressedExceptions) {
					this.suppressedExceptions = new LinkedHashSet<Exception>();
				}
				try {
					singletonObject = singletonFactory.getObject();
					newSingleton = true;
				}
				catch (IllegalStateException ex) {
					// Has the singleton object implicitly appeared in the meantime ->
					// if yes, proceed with it since the exception indicates that state.
					singletonObject = this.singletonObjects.get(beanName);
					if (singletonObject == null) {
						throw ex;
					}
				}
				catch (BeanCreationException ex) {
					if (recordSuppressedExceptions) {
						for (Exception suppressedException : this.suppressedExceptions) {
							ex.addRelatedCause(suppressedException);
						}
					}
					throw ex;
				}
				finally {
					if (recordSuppressedExceptions) {
						this.suppressedExceptions = null;
					}
					afterSingletonCreation(beanName);
				}
				if (newSingleton) {
					addSingleton(beanName, singletonObject);
				}
			}
			return (singletonObject != NULL_OBJECT ? singletonObject : null);
		}
	}

```

