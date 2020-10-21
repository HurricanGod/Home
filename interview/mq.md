# <a name="top">消息队列常见面试题</a>





### <a name="message_lose">如何保证消息不丢失</a>

消息队列系统的引入主要包括三个阶段：

+ <a href="#produce"> **生产消息** </a>
+ <a href="#store">**存储消息**</a>
+ <a href="#consume">**消费消息**</a>



<a name="produce"> **生产消息** </a> ：生产者发送消息到 `broker`，需要 `broker` 响应了消息发送流程才算完成。往 `broker`发送消息一般有同步和异步发送两种方式。在异步发送消息情况下如果在`broker`返回写入失败未做重试，那么就会存在消息丢失的问题。因此，在异步发送消息需要引入重试机制保障消息的不丢失。

<a name="store">**存储消息**</a> ：`broker` 需要在消息刷盘之后再给生产者响应，如果`broker`是集群部署，需要同时写入`broker`和其副本才给生产者返回响应。如果再消息未刷盘前就给生产者返回响应，`broker`宕机时则会丢失消息







### <a name="message_duplicate">如何处理重复消息</a>





### <a name="message_order">如何保证消息的有序性</a>







### <a name="message_heap_up">如何处理消息堆积</a>



