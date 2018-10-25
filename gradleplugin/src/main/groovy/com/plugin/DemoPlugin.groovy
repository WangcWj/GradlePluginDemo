package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project


class DemoPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.extensions.create("cus",CusExtension)
        project.afterEvaluate {
            println("=================   "+project.cus.name)
        }
    }
}