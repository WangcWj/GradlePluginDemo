package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        println("====================")
        println("插件搭载成功了啊!")
        println("====================")
    }
}