# <a name="top">Git常用命令</a>



-----

+ 每次 `git push` 都要输入账号和密码

  ```shell
  git remote rm origin

  git remote add origin git@github.com:HurricanGod/home.git
  ```




+  查看配置

  ```shell
  git config --list
  ```



+ 查看远程地址

  ```shell
  git remote -v
  ```

  ​


+ 对某个**未加入缓存区**文件放弃本地修改

  ```shell
  # 未进行 git add filename 的本地修改（即未加入缓存区的修改）可以使用以下命令放弃本地修改
  git checkout -- filename

  # "." 表示放弃本地所有修改（未加入缓存区的修改）
  git checkout .
  ```

  ​


+ 放弃**已加入缓存区但未提交**文件的修改

  ```shell
  git reset HEAD filename

  # 放弃未提交的缓存区的所有修改
  git reset HEAD .
  ```

  ​


+ 回退到某个版本（通常使用`git commit`提交了代码）

  ```shell
  # commit_id 是指每次执行 git commit 后产生的id
  # 可以使用 git log 查看 commit_id
  git reset --hard commit_id

  # 回退到上一次 commit 的状态
  git reset --hard HEAD^
  ```

  ![commit_id](https://github.com/HurricanGod/Home/blob/master/project-manage/git/img/commit_id.png)

---

## 调整 Tab 字符所代表的空格数

+ 在URL后面添加 `?ts=4`




----


## 查看用户的所有Commit

+ 在URL后面添加 `?author={user}`






-----

## 整行高亮

+ 在**代码文件**后面添加`#L{start}`或`#L{start}-{end}`
