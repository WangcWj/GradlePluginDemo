# GradlePluginDemo

####  Android Gradle插件开发-插件搭建

Gradle插件需要开发者具备Groovy语法的基础，要不然很难懂插件的原理跟写插件的脚本，如果照葫芦画瓢写出来一个插件，到最后还是一脸懵逼什么都不懂。

**第一步：** 新建一个*module*，*demo*使用的*module*的名字为*gradleplugin*，然后删除*src*目录下面的除*main*目录的其它的文件夹。删除后如图：

![图一](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-one.png)

**第二步：** 接着删除*main*文件夹下面的所有文件夹以及文件，删除后如图：

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-two.png)

**第三步：** 删除*build.gradle*里面的文件内容，并且引入*groovy*插件，因为我们需要用到*groovy*语法，如图所示：

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/groovy-three.png)

```groovy
apply plugin: 'groovy'
apply plugin: 'maven'

dependencies {
    compile gradleApi()
    compile localGroovy()
    //这里面你想用什么就去引入什么
    compile 'com.android.tools.build:gradle:3.0.0'
}

repositories {
    jcenter()
}

```

**第四步：** 在*main*文件下面建立*groovy*文件夹，一定不要拼写错误哦。然后在*main/groovy*里面创建*groovy*文件用来写脚本，注意文件的后缀要是***.groovy***。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-four.png)



**第五步：** 写插件,我们需要实现*Plugin*接口泛型为*Project(Gradle的Project)*，然后复写*apply*方法，在apply方法里面写脚本。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-five.png)



**第六步：** 在*main*目录下建立*resources/META-INF/gradle-plugins*文件夹，然后建立*com.demo.plugin.properties*文件，文件的名称随意定义，之后引入插件的时候会根据该名称引入，如*apply plugin: 'com.demo.plugin'*，文件内容为*key = value*，*key*写死为*implementation-class* ，*value*为你写定义的那个*groovy*文件的路径，一般是选择你定义好的*package+文件名*。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-six.png)

**第七步：** 打包发到本地的*maven*仓库里面。先在该*module*的*build.gradle*文件里面写下仓库的配置文件。

```groovy
uploadArchives {
    repositories.mavenDeployer {
        repository(url: uri('../gradleplugin/src/main/repertory'))
        pom.groupId = 'com.wang'
        pom.artifactId = 'demo-plugin'
        pom.version = "1.0.0"
    }
}
```

然后同步一下，之后去点击*Android  Studio* 右侧的*Gradle* 。如图所示：

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-eight.png)

点击 *uploadArchives*  之后开始执行*task*结束之后就会再我们定义的仓库的路径中找到如下文件：

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-seven.png)

这样我们的本地仓库创建完毕,可以再根目录的build.gradle里面引用了。

**第八步：**引用插件。在根目录的*build.gradle*里面配置我们本地仓库的*maven*地址以及*classpath*。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-ten.png)

之后再需要用到插件的地方引用*apply plugin: 'com.demo.plugin'*就可以，这里的插件名称就是我们定义的那个

*com.demo.plugin.properties*文件的前缀。引入之后*build*一下会在*Gradle Console*里面看到我们写的插件脚本的打印信息。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-nine.png)

到此我们完成了一个简单Gradle插件,并发到了我们本地的maven仓库.

