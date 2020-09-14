/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/5.6.4/userguide/multi_project_builds.html
 */
println("load setting script")
rootProject.name = "excel-op"
// 自定义配置 --build-cache 的缓存位置
buildCache{
    local<DirectoryBuildCache> {
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}
include("other_sub")

//includeBuild("../jvmLang")
include("sub_prj:sub_sub_prj")
//includeFlat("jvmLang")
//includeBuild("sub_prj/sub_sub_prj")
settings.gradle.allprojects{
    println("iterator all Project Name:${this.name}")
}

include("apt_proj")