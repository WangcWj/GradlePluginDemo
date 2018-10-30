# GradlePluginDemo

####  Android Gradle插件开发-插件搭建

#### 第一 构建基础插件。

Gradle插件需要开发者具备Groovy语法的基础，要不然很难懂插件的原理跟写插件的脚本，如果照葫芦画瓢写出来一个插件，到最后还是一脸懵逼什么都不懂。这里用Module单独开发插件。

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

**第八步：** 引用插件。在根目录的*build.gradle*里面配置我们本地仓库的*maven*地址以及*classpath*。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-ten.png)

之后再需要用到插件的地方引用*apply plugin: 'com.demo.plugin'*就可以，这里的插件名称就是我们定义的那个

*com.demo.plugin.properties*文件的前缀。引入之后*build*一下会在*Gradle Console*里面看到我们写的插件脚本的打印信息。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-nine.png)

到此我们完成了一个简单Gradle插件,并发到了我们本地的maven仓库.

#### 第二：自定义插件像applicatipn插件一样接受属性传值。

通过以上的学习我们搭建起来了自定义插件，也让插件正常运行起来，那么我们如何才能想*application*插件那种接收属性值呢？以下的*demo*是接着上面的写下去的。先来看下*Android  applicatipn*插件定义的属性：

```groovy
android {
    compileSdkVersion 26
    defaultConfig {
        applicationId "plugin.gradle.com.gradleplugindemo"
        minSdkVersion 15
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
```

那么自定义的插件再完成某些需求的时候也需要接受参数。接下来看如何像*application*一样去接收参数。

1.属性直接赋值.

```groovy
pluginVersion 26
pluginName "pluginOne"
//我们要接收使用插件的build.gradle文件里面写的这些属性
```

第一步:：首先先创建一个接收自定义属性的类。可以再跟我们的插件同一个包里面直接创建一个*groovy*文件，记得一定要是*.groovy*后缀啊。这里呢我们建立一个*CusProperties*的文件.

```groovy
package com.plugin

class CusProperties {
    def pluginVersion
    def pluginName

    @Override
    String toString() {
        return "pluginVersion is  " + pluginVersion +"     pluginName  is   " +pluginName
    }
}
```

第二步:将我们的自定属性添加到目标对象上,这是官方解释.先看下面的代码:

```groovy
project.extensions.create("cusProperties",CusProperties)
```

代码不多但是很陌生吧，碰到不熟悉的类方法属性呢我们最好先去看下[API](https://docs.gradle.org/current/javadoc/org/gradle/api/plugins/ExtensionContainer.html#findByName-java.lang.String-)的解释，这里*project.extensions*调用的是*Project*内部的*ExtensionContainer*类，这个类的作用就是*Allows adding 'namespaced' DSL extensions to a target object*，也就是将自定义的命名空间添加到调用者*project*里面。然后调用*ExtensionContainer*的*create*方法，该方法有好几个重载的方法，具体请看*api*，调用*create*方法就可以把我们自定义的类跟我们*build.gradle*里面的定义的属性相关联了。也就是直接赋值给我们自定义的类里面，先看下完整的代码吧：

```groovy
package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project


class DemoPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        CusProperties cus = project.extensions.create("cusProperties",CusProperties)
        project.afterEvaluate {
            println("=================   "+cus.toString())
        }
    }
}
```

这是插件代码书写完成的样子：

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin_pro1.png)

第三步 ：当我们每次改变插件文件的内容的时候记得要重新打包进本地的仓库中。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin-eight.png)

点击*uploadArchives*重新把插件里面的代码打包到本地的仓库，要不然项目引用的插件还是之前的代码。

第三步：再引用该插件的地方去写自定义的属性。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin_pro2.png)



第四步：重新*build*一下我们就就会看到我们再插件里面打印的信息。

![](https://raw.githubusercontent.com/WangcWj/image-folder/master/plugin_pro3.png)

这就说明我们的接收到了自定义命名空间的值。

注意点：

1.我们的插件是在*apply plugin: 'com.demo.plugin'*的时候就会执行apply方法里面的代码，那么自定义属性都是定义再引入插件之后，那么我们如果不采取措施的话是接收不到属性值的。请看下面的代码：

```groovy
class DemoPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        CusProperties cus = project.extensions.create("cusProperties",CusProperties)
        println("=================   打印1  "+cus.toString())
        //该方法是Project建立好任务的有向图之后再执行,一般需要再某个系统任务之后或者之前做一些事情的时候一定要在这个方法里面去写
        project.afterEvaluate {
            println("=================   打印2  "+cus.toString())
        }
    }
}

结果:
    =================   打印1  pluginVersion is  null     pluginName  is   null
=================   打印2  pluginVersion is  26     pluginName  is   pluginOne
```

一定要让我们定义的自定义属性代码走过之后再去获取值。

2.就是当我们的插件文件有改动的时候一定要记得重新生成本地仓库。当升级插件的版本的时候要把之前的依赖都注释掉再生成新版本的仓库。

