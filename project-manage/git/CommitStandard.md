# <a name="top">git commit message规范</a>



`commit message`格式

```
<类型> [可选作用域]: <描述>
[可选正文]
[可选脚注]
```



**类型type** 主要有2个主要类型，5个特殊类型

+ 主要类型
  + feat —— 增加新功能
  + fix —— 修复bug
+ 特殊类型
  + docs —— 只改动了文档相关的内容
  + style —— 不影响代码含义的改动，如去掉空格，改变缩进
  + build —— 构建工具或外部依赖改动
  + refactor —— 代码重构
  + revert —— 执行 `git revert` 打印的message

**可选作用域** ：描述改动的范围，格式为 `项目名/模块名`，一次commit修改多个模块，建议拆分成多次commit，以便更好追踪和维护



**描述**： 主要描述**改动之前的情况** 和 **动机**

+ 对涉及**破坏性修改** 或 **新特新** 需要提交说明
+ 修复bug类型的提交可以在 `commit message` 中填写影响的缺陷id，格式可以为`fix #bugId`





