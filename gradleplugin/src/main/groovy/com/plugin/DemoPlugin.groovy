package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project


class DemoPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        CusProperties cus = project.extensions.create("cusProperties",CusProperties)
        println("=================   打印1  "+cus.toString())
        project.afterEvaluate {
            println("=================   打印2  "+cus.toString())
        }
    }
}