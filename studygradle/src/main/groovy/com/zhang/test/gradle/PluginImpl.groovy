package com.zhang.test.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create('ourInfo',OurInfo)
        def android = project.extensions.getByType(AppExtension)
        //注册一个Transform
        def classTransform = new MyMakeClassTransform(project)
        android.registerTransform(classTransform)

        String application = project['ourInfo'].id
        project.task('testPlugin') {
            doLast{
                OurInfo extension = project['ourInfo']
                String a = extension.id
                println("ourInfoOne:${a}")
            }
        }
        project.afterEvaluate {
            OurInfo extension = project['ourInfo']
            String a = extension.id
            println("ourInfo:${a}")

        }
    }
}
