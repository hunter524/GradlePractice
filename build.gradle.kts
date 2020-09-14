

println("load buile.gradle.kts")
/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 */
/**
 * gradle assemble 命令在kotlin java 项目中可以构建所有产品 zip tar jar xxx.sh xxx.bat
 * 其中tar zip 文件解压 既可以通过运行 其bin 目录中的 .bat .sh 脚本即可以运行该项目
 * IV scan report :https://gradle.com/s/cuy3ka3cnt5oq  https://gradle.com/s/g5jjmmj4yjg5s
 */
plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.41"
//    scan 组件不需要显示声明 scan 依赖
//    id("com.gradle.build-scan").version("3.3.1")
    // Apply the application plugin to add support for building a CLI application.
    application
    `maven-publish`
    maven
    idea
//    buildSrc 项目中定义的插件定义 id,其他项目通过id 引用该插件
//    同一个插件可以定义两个不同的id 只需要使用 gradlePlugin { plugins {} } 声明两次不同的 id 即可
//    id("build_src")
    id("buildSrc.greeting")
    id("com.github.hunter524.gradle.plugin.BarPlugin")
}


// 插件和脚本的多种添加方式
// 下述三种方式均可以添加插件和脚本。
// apply 三种应用：
// from: 从当前脚本引用其他脚本
// plugin:向当前项目应用指定插件
// TODO:// to:目前认为to是指定当前待配置的对象
apply {
//    type(BuildSrcPlugin::class)
//    from("applied.gradle.kts")

}

//apply (from = "applied.gradle.kts")
apply(mapOf("from" to "applied.gradle.kts"))
base {
    libsDirName = "libselfDef"
    distsDirName = "distributionSeflDef"
    archivesBaseName = "ArchiveBaseName"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    sourceSets {
        main {
            java {
//                只使用 java_src 目录作为 java 根目录 此时 main/java 目录的源码文件不会被编译
//               setSrcDirs(listOf("java_src"))
//                该方法是向 java src 中 添加 java_src 目录 原有的 main 目录依旧在 src 目录中
//                srcDir("java_src")
            }
        }
//        创建一个独立的 SourceSet 用于配置单独的依赖
        create("just")
    }
}

// JavaBasePlugin 直接在Project 内部添加了 sourceSets 扩展
sourceSets {
    main {
        java {
            srcDir("java_src")
        }
    }
}

tasks.register<Jar>("jarJust"){
    this.from(sourceSets.getByName("just").output)
}

tasks.getByName("jar"){
    (this as Jar).from(sourceSets.getByName("just").output)
}

application {
    // Define the main class for the application
    mainClassName = "com.github.hunter524.forlove.WritePropertiesKt"
    version = "0.0.1"
    group = "com.github.hunter524"
}

// JavaPluginConvention 无法在 Project 中获取到
project.convention.getPlugin(JavaPluginConvention::class.java).apply {
    println("Java Base Plugin Convention")
//    JavaPluginConvention 中的 manifest 只是提供了一种创建 manifest 的方法，并不是将 manifest 合并到其他 jar 包中
    manifest {
        attributes(mapOf("Attr" to "Add from JavaPluginConvention"))
    }
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()

//    //    以名字推测 falt_repo 下面的依赖，优先级最低如果同 maven 等仓库重名会被覆盖
//    implementation("org.hunter:javassistht:3.27.0-GA") <group_name>-<version_name>.jar
    flatDir {
        dirs("flat_repo")
    }
}

configurations{
    create("scm")
//    create("justImplementation")

}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.alibaba:easyexcel:2.2.3")

    implementation("com.squareup.okio:okio:1.11.0")
//    为 just 的 SourceSet 添加依赖
    "justImplementation"("com.squareup.okio:okio:1.11.0")
    implementation("com.google.guava:guava:19.0")
//    以名字推测 falt_repo 下面的依赖，优先级最低如果同 maven 等仓库重名会被覆盖
    implementation("org.hunter:javassistht:3.27.0-GA")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
// 自定义 scm Configuration 为 Scm 添加对应类型的依赖
// 可以使用 gradle dependenices --configuration <cfg_name> 查看制定 cfg 的依赖配置
    "scm" ("org.eclipse.jgit:org.eclipse.jgit:4.9.2.201712150930-r")
//    过滤相同功能的依赖
//    制造相同功能的两个库的依赖 通过 LoggingCapability 告诉 gradle 这两个依赖具有相同的功能 此时 gradle 即会报错提示
//    该功能 gradle 4.7 引入
    implementation("apache-log4j:log4j:1.2.15")
    implementation("org.slf4j:log4j-over-slf4j:1.7.10")
//    components.all{
//        println("All Component: ${this.id.toString()}")
//        this.allVariants{
//            println("All Component VariantMetaData: ${this.attributes}")
//        }
//    }
//    components.all(buildSrc.LoggingCapability::class.java){
////        该 Action 是用来配置 LoggingCapability 的构造参数
//        params("excel_op")
//    }
// 添加动态版本依赖 用于测试 dependency locking
//    动态版本大于 1.0.0 包含 小于 2.0.0 不包含 gradle 自动选择了最高版本 2.0 实际上google 的 gson 库最新版本为 2.8.6
    implementation("com.google.code.gson:gson:2+")
//    implementation("com.google.code.gson:gson:2.8.6")

//    使用 modules 的替换规则 只有当 module 和 replacedBy 的依赖均存在时才会执行替换
//    该处的替换主要是用于解决两个依赖的冲突，因此并不能替代 resolve 和 substitution 的功能
//    添加 fastjson 依赖使其与 gson冲突
    implementation("com.alibaba:fastjson:1.2.9")
    modules{
        module("com.google.code.gson:gson"){
            println("Call Module Conflict Gson And fastJson use FastJson")
            replacedBy("com.alibaba:fastjson")
        }
    }

    "scm"("apache-log4j:log4j:1.2.15")
    "scm"("org.slf4j:log4j-over-slf4j:1.7.10")

//    添加 apt 项目
    annotationProcessor(project(":apt_proj"))
    implementation(project(":apt_proj"))
}

//    通过 Configuration#ResolutionStrategy#capabilitiesResolution 解决冲突
//    解决了 log4j capability 的冲突 添加了 org.slf4j:log4j-over-slf4j:1.7.10 的依赖
configurations.all {
    this.resolutionStrategy.capabilitiesResolution.withCapability("log4j","log4j"){
        var candidates = this.candidates
        candidates.forEach {
            println("log4j capabilitiey candidate: ${it.displayName} candidate class:${it::class.java.simpleName}")
            if(it.displayName.contains("slf4j")){
                select(it)
            }
        }
    }
}
// lock 注释掉关闭/即lockfile 与 build.gradle 中的文件不一致也可以编译通过
// 打开注释 lockfile 文件的版本号与 build.gradle 不一致即不能编译通过
dependencyLocking {
    lockAllConfigurations()
}
// 运行该任务解析所有 Configuration 添加 --write-locks 命令即会生成对所有 Configuration 的 xxx.lock 文件
// 通过 lock 文件锁定 Configuration 的依赖版本
tasks.register("lockAndResolveAll"){
    doFirst{
        require(gradle.startParameter.isWriteDependencyLocks)
    }
    doLast{
        configurations.filter { it.isCanBeResolved }.forEach { it.resolve() }
    }
}




// fat-jar/uber-jar
// 相同意思 均为包含被依赖的 jar 一个 jar
// uber 原为德语 意为 above over 超过 simple 只包含当前项目 jar 的 jar
// https://docs.gradle.org/current/userguide/working_with_files.html#sec:creating_uber_jar_example
// 通过 jar 任务 解压被依赖的 jar 文件，将 jar 中的内容拷贝到当前生成的 jar 文件中
tasks.register<Jar>("fatJar") {

    manifest {
        attributes("Main-Class" to "com.github.hunter524.forlove.AppKt")
    }

    archiveClassifier.set("fat")

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

// 分析task 被多次执行的原因 A->C B->C
// 同时执行 A B 任务 C 只被执行了一次
tasks.create("A") {
    doFirst {
        println("A")
    }
}

tasks.create("B") {
    doFirst {
        println("B")
    }
}

tasks.create("C") {
    doFirst {
        println("C")
    }
}

tasks.create("D") {
    doFirst {
        println("D")
    }
}
// 同时运行 A B C 任务时 Task 的运行顺序生效
// 只运行 C 任务时 A B 不运行
// 建立下述 依赖关系之后 运行 A 任务 B C 任务也均会被运行 (B->C->A)
tasks["C"].mustRunAfter(tasks["B"])
afterEvaluate {
    tasks.getByName("A").dependsOn("B")
    tasks.getByName("A").dependsOn("C")
    tasks.getByName("C").dependsOn("D")
    tasks.getByName("B").dependsOn("D")

}
tasks.create("print") {
    doFirst {
        var sysProperties = System.getProperties()
        println(sysProperties.keys)

        var projectProperties = project.extensions.extraProperties.properties
        println(projectProperties.keys)

        println("bar: ${project.extensions.extraProperties["org.gradle.project.bar"]} " +
                "bar from project#properties:${project.properties["org.gradle.project.bar"]}")

        println("foo: ${project.extensions.extraProperties["ORG_GRADLE_PROJECT_foo"]} " +
                "foo from project#properties:${project.properties["ORG_GRADLE_PROJECT_foo"]}")

    }
}

//扩展方法结合编译器魔法,实现 provideDelegate 委托属性的创建
val taskCreateByBy by tasks.registering {
    doFirst {
        println("taskCreate By By!")
    }
    doLast {
        println("Property from task: ${this.extra["p"]}")
    }
}
// task#extensions#extraProperties (扩展方法委托至该字段)
// 该属性每个 Task 均有自己的 Extensions
tasks["taskCreateByBy"].extra["p"] = "v"

//创建指定类型的task并且返回的数据为 TaskProvider
//并且指定创建的 Task 类型为 Copy 类型
//创建该 task 的闭包内部使用的 from into 还是处于配置该类型的task阶段
//外层 into 指定根目录,内层目录使用 /开始 为相对外层根目录的子目录
// 使用相对目录则是转化为绝对目录，再使用绝对目录相对于 into 的根目录进行复制操作

var taskProvider = tasks.register<Copy>("copySub") {

//    该种 from 传入闭包的写法称之为 ChildSpec 则与 Copy 任务的主 Spec 是相互隔离的
    from(file("/home/hunter/IdeaProjects/ForLove/src/main/kotlin/com/github/hunter524/forlove/App.kt")) {
//        into(file("src"))
        into(file("/src"))
    }

    from(file("src/test")) {
//        into(file("test2"))
        into(file("/test2"))
    }
    into(file("/home/hunter/kl/"))
}

tasks.register<Zip>("zipSub") {
//    basename-appedix-version-class.ext
    archiveBaseName.set("basename")
    archiveAppendix.set("appedix")
    archiveVersion.set("0.0.1")
    archiveClassifier.set("class")
    archiveExtension.set("ext")
    destinationDirectory.set(file("ziptest/zip"))

    from("ziptest/src")
}

//ExtraPropertiesExtension 扩展了 invoke 方法实现为该属性提供一个初始值
val archivesDirPath by extra { "$buildDir/archives" }

//解压zip 指定目录下的文件进入 ziptest/uzip 目录
tasks.register<Copy>("unZipSub") {
    from(zipTree("ziptest/zip/basename-appedix-0.0.1-class.ext")) {
        include("libs/**")
        eachFile {
            relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
        }
        includeEmptyDirs = false
    }
    into("ziptest/uzip")
}

// jar 包实际上就是一个class文件和资源文件的压缩包
// TODO:// 但是重新压缩文件生成 jar 包无法运行
tasks.register<Copy>("unZipJar") {
    from(zipTree("ziptest/jar/javassist-3.27.0-GA.jar"))
    into("ziptest/uzip")
}

//TODO:// Any 扩展的 withGroovyBuilder 方法，为 kotlin 提供了 groovy 语法编写脚本的支持
tasks.register("antTask") {
    doFirst {
        ant.withGroovyBuilder {
            "move"("file" to "${buildDir}/reports", "todir" to "${buildDir}/toArchive")
        }
    }
}

//使用 kotlin 委托属性获取 task (虽然该处的 tasks#getting 不是 Map)
project.afterEvaluate {
//    val copySub by tasks.existing
//    println("copySub Name:${copySub.name}")
}

// 验证 Project#file 相关的 api
tasks {
    register("file") {
        doFirst {
            var gitignore = file(".gitignore")
            println("git ignore path: ${gitignore.absolutePath}")
            println("git ignore contents: ${gitignore.readLines()}")
        }
    }
}

// 通过 Annotation 注解定义的 uptodate 任务
// 执行完一次 该任务后更改 .gitignore 会导致 up-to-date 失效,该任务重新运行
tasks.register("uptodate", UptodateTask::class, File("/home/hunter/IdeaProjects/ForLove/.gitignore"), File("/home/hunter/IdeaProjects/ForLove/.gitignore"))
open class UptodateTask @javax.inject.Inject constructor(@get:org.gradle.api.tasks.InputFile val input: File, @get:org.gradle.api.tasks.OutputFile val outPut: File) : DefaultTask() {

    @org.gradle.api.tasks.TaskAction
    fun printExe() {
        println("UptodateTask printExe")
    }
}

// 通过运行时 api 定义 增量构建任务
tasks.register("rtUptodate", DefaultTask::class) {
    doFirst {
        println("rtUptodate executed")
    }
    outputs.file("/home/hunter/IdeaProjects/ForLove/.gitignore")
    inputs.file("/home/hunter/IdeaProjects/ForLove/.gitignore")
}

// 自行进行 up-to-date 检测，有该文件即可认为up-to-date
tasks.register("selfCheckUptoDate") {
    outputs.upToDateWhen {
        file("uptodate.cfg").exists()
    }
    doFirst {
        println("selfCheckUptoDate Executed")
    }
}

//批量的根据 Task 名称的规则执行任务，该任务在运行该task之前并不存在，是被根据规则动态创建的
//如该处的域名则可以根据执行的任务名称动态的获取和更改
// 该处的 addRule 实际使用的也是 NamedDomainObjectCollection 的
tasks.addRule("Pattern: Ping<ID>") {
    val taskName = this;
    if (startsWith("ping")) {
        task(taskName) {
            doLast {
//                exec{
//                    commandLine("wget","https://guyuesh2.online:8443")
//                }
                println("ping ${this.name.substring(4)}")
            }
        }
    }
}
// 依赖的任务也可以根据 Tasks#Rule 动态进行生成
tasks.register("groupPing") {
    dependsOn("pingServer1", "pingServer2")
}

//build.gradle script 内部调用的方法会被委托到 Project 对象上进行调用
//内部的this 指针指向的是 Script 引用
//(kotlin 为生成的子类继承了 KotlinBuildScript 其通过继承 ProjectDelegate 通过其扩展方法实现了脚本内部方法调用委托到 Project 对象上)


println("This From Script type is ${this::class.java} super Class is ${this::class.java.superclass}")

// FileCollection FileTree Api

// 验证一个 FileCollection 可以被惰性求值两次
// 即 srcDir 改变了 再次迭代 FileCollection 会获得不同的结果
tasks.register("fileCollection") {
    doFirst {
        var srcDir: File? = null
//        kotlin 该处传递的 Closure 代表一个 Provider
        val filecl = layout.files({
            srcDir?.listFiles()
        })
        srcDir = file("src")
        filecl.map { relativePath(it) }.sorted().forEach { println("lazy dir 1: ${it}") }

        srcDir = file("ziptest")
        filecl.map { /*relativePath(it)*/ it }.sorted().forEach { println("lazy dir 2: ${it}") }


        println("files:${filecl.files} \n toList:${filecl.toList()} \n asPath:${filecl.asPath}")
    }
}

tasks.register("fcft") {
    doLast {
        var fileCollection = project.files("./")
        var fileTree = project.fileTree("./").apply {
            include("**/*.kts")
        }
        println("file tree files${fileTree.asPath}")
        println("file collection files${fileCollection.asPath}")

        fileTree.visit {
            if (!this.isDirectory) {
                println("File: ${this.name} mode :${Integer.toHexString(this.mode)}")
            }
        }

    }
}

tasks.register<Copy>("cpfilter") {
    project.delete("copied/Copy_template.txt")
    from("copy_temp")
//    groovy 模板模式 语法参见 SimpleTemplateEngine
//    expand("year" to "2009","month" to "06","day" to true)
//    ant 模式  @var@
//    filter(org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to mapOf("year" to "2009","month" to "06","day" to "day"))
//    直接按行 Transformer 模式（最灵活，但是编码最复杂) 按行模式加上行号
    var i = 0
    filter {
        "==${i++}->${it}"
    }
    into("copied")
}

tasks.register("printCfg") {
    doFirst {
        var configurations = project.configurations
        configurations.asMap.forEach { k, v ->
            println("Key: $k -> Value: $v")
        }
    }
}

// 自定义的 task 使用 分组 和 Description 便于 gradle tasks 命令归纳统一分组的task 在一起，desc 便于使用者知道该 Task 执行的任务
tasks.register("taskDefExcelOp1"){
    group="excel_def"
    description = "Defined By excel_op project"
    doLast {
        println("taskDefExcelOp1 exe")
    }
}

tasks.register("taskDefExcelOp2"){
    group="excel_def"
    description = "Defined By excel_op project"
    doLast {
        println("taskDefExcelOp2 exe")
    }
}

//向所有Project 添加同名的task (包括 RootProject 添加同名的task)

allprojects {
    this.tasks.register("allProjTask") {
        doFirst {
            println("Task in ${this.project.name}")
        }
    }
}

// 从服务断下载zip 用于更新 init.d 中的init.gradle.kts 脚本和 gradle.properties 配置文件
// 需要自己编写下载任务与上传任务
//tasks.register<DownloadGradle>("downDloadCfg"){
//
//}


// gradle build task
tasks.register<org.gradle.api.tasks.GradleBuild>("gradleBuild"){
    dir = File("/home/hunter/IdeaProjects/jvmLang")
    tasks = listOf("tasks")
}

// configuration resolve 阶段替换指定库
// 每一个依赖的库均会在解析阶段被解析
//configurations.all{
////    resolve 阶段
//    resolutionStrategy.eachDependency {
//        println(" Resolve:${this.requested.toString()} Module:${this.requested.module}")
//    }
////    substitution 阶段
//    resolutionStrategy.dependencySubstitution {
//        all {
//            println("Substitution:${this.requested.toString()}")
//        }
//    }
////
//    resolutionStrategy.componentSelection {
//        all{
//            var candidate = this.candidate
//            println("Selection : $candidate")
//            if(candidate.version.equals("2.8.6")){
//                reject("just want to reject 2.8.6 version.usually this version is gson")
//            }
//        }
//    }
//}

//configurations{
//    getByName("implementation").withDependencies {
//        this.forEach {
//            println("Configuration#withDependencies: ${it.toString()}")
//        }
//    }
//}

// 迭代 Configuration 中依赖的文件(task 执行中的依赖可以获得级联依赖，因为task 依赖的 Configuration 在 task 执行之前已经 resolve 解析过了
// 文件路径指向了
tasks.register("iteratorFileFromCfg"){
    var scm = configurations.getByName("scm")
    dependsOn(scm)
    doLast {
        var files = scm.files
        files.forEach {
            println("Iterator Cfg: ${it.absolutePath}")
        }
    }
}

tasks.register("getPublishCfg"){
    var defaultCfg = configurations.getByName("runtime")
    dependsOn(defaultCfg)
    doLast {
        configurations.forEach {
            println("Iteratort All Cfg: ${it.name}")
        }
        var publications = defaultCfg.outgoing
        publications.artifacts.forEach {
           println("Iterator Publications: ${it.toString()}")
        }
    }
}

//dependencies {
//    attributesSchema {
//        attribute(nothing)                      // (1)
//    }
//    artifactTypes.getByName("jar") {
//        attributes.attribute(nothing, false)    // (2)
//    }
//}
//
//configurations.all {
//    afterEvaluate {
//        if (isCanBeResolved) {
//            attributes.attribute(nothing, true) // (3)
//        }
//    }
//}
//
//dependencies {
//    registerTransform(buildSrc.DoNothingTransformer::class) {
//        from.attribute(nothing, false).attribute(artifactType, "jar")
//        to.attribute(nothing, true).attribute(artifactType, "jar")
//    }
//}

// 自定义 Aritifact 不使用 Component

var ktFile = file("settings.gradle.kts")

// 只发布这一个文件和 maven 文件
var ktArtifact = artifacts.add("archives", ktFile) {
    type = "kt"
}
// 定义 maven publish 发布到指定的本地 maven 残酷
publishing {
    publications {
        create<MavenPublication>("myLibrary") {
            from(components["java"])
//            自定义一个文件发布到 repo 中
            artifact(mapOf("source" to file("dep.txt"),"classifier" to "classifier","extension" to "ext"))
        }

        create<MavenPublication>("maven") {
            artifact(ktArtifact)
        }
    }

    repositories {
        maven {
            name = "myRepo"
            url = uri("file:${rootProject.projectDir.absolutePath}/repo")
        }
    }
}

// 测试 publis<publicatin_name>PublicationTo<Repo——Name>Repository 的任务是在 Project Evaluated 阶段创建，因此无法在 build.gradle
// 脚本中引用该 task 进行修改
project.afterEvaluate {
    var tasks = project.tasks.toList()
    println("AfterEvaluated Tasks: ${(tasks.toTypedArray().toString())}")
}
tasks.withType(PublishToMavenRepository::class.java).configureEach {
    println("config Each")
    "".apply {  }
}

//在 processResource 任务中添加额外的处理任务
tasks.withType(ProcessResources::class.java).configureEach {
//    在执行 processResources 的任务中过滤掉指定文件
//    this.exclude("**/exclude*")
//   将其他文件包含进入 也会导致 processResource 不会执行既定的任务
    this.include("/home/hunter/IdeaProjects/ForLove/gradlew")
//    不能向该 task 添加 Action 添加 Action 则会使该 Task 不再执行
//    this.doLast {
//        var copy = this as Copy
//        var destinationDir = copy.destinationDir
//        println("destinationDir is ${destinationDir.absolutePath}")
//    }
}

// kotlin 提供的常用的扩展方法访问
tasks.compileJava{

}

tasks.register("writeProperties",WriteProperties::class.java){
    property("key","value")
    outputFile = File("/home/hunter/IdeaProjects/ForLove/taskwrite.properties")
}

//压缩源码进入 jar包

tasks.register("sourceJar",Jar::class.java){
    archiveClassifier.set("source")
    from(sourceSets.main.get().allJava)
}
// 让 assemble 依赖于 sourceJar 即可以使用 assemble 一次运行既编译生成运行包 jar 也可以生成 source.jar
//tasks.getByName("assemble").dependsOn(tasks.getByName("sourceJar"))

//
val asciidoclet by configurations.creating

dependencies {
    asciidoclet("org.asciidoctor:asciidoclet:1.+")
}

tasks.register("configureJavadoc") {
    doLast {
        tasks.javadoc {
            options.doclet = "org.asciidoctor.Asciidoclet"
            options.docletpath = asciidoclet.files.toList()
//            一定要使用大写的UTF-8 才可以解决乱码问题
            options.encoding = "UTF-8"
        }
    }
}

tasks.javadoc {
    dependsOn("configureJavadoc")
}

//添加predefined Task
tasks.register<com.github.hunter524.gradle.task.LatestArtifactTask>("latestArtifact"){
    url = "https://blog.guyuesh2.online"
    artifact = "latest blog"
}

//可以使用该方式进行 extension 配置 但是在 build.gradle.kts 脚本中这种设置方式会爆红，但是可以正常使用
// 自定义的 Extension 推荐使用该方式进行设置
//barExt{
//    home = "config home by user"
//}

// 自定义属性推荐使用这种方式进行设置
configure<com.github.hunter524.gradle.plugin.BarExtension>(){
    home = "user def home"
    site = "user def site"
    content = content.copy("user def title","user def boady")
}

tasks.register<com.github.hunter524.gradle.task.TaskProperty>("taskProperty","https://google.com",this.objects.property(String::class).apply {
    set("https://blog.guyuesh.online")
})

//tasks.register<com.github.hunter524.gradle.task.ReadOnlyTaskProperty>("readOnlyTaskProperty"){
//    getNameProperty().set("name forlove")
//}

//测试ext中属性的获取规则 Task 有自己的ext属性存储空间，且优先级高于 Project
// todo:// kotlin 脚本中无法实现自动委托？需要使用者指定使用哪一个层级的 ext
this.ext.set("extName","extValue Project")
tasks.register("extTask"){
    this.extra.set("extName","extValue Task")
    doLast {
        println("this name ${this.extra.get("extName")}")
    }
}

// 测试 Provider Property 在Task属性中的使用，并且使用 flatmap 方法实现 Property 的转换
tasks.register<com.github.hunter524.gradle.task.ProviderPropertyTask>("pptask"){
    this.nameProperty.set("Hunter")
}
val producer1 by tasks.registering(com.github.hunter524.gradle.task.relation.ProducerTask::class)
val producer2 by tasks.registering(com.github.hunter524.gradle.task.relation.ProducerTask::class)
val consumer by tasks.registering(com.github.hunter524.gradle.task.relation.ConsumerTask::class)

producer1.configure{
    outPutFile.set(layout.buildDirectory.file("out1.txt"))
}

producer2.configure{
    outPutFile.set(layout.buildDirectory.file("out2.txt"))
}

// 通过 Provider/Property 即可以建立 任务之间的关联关系，不一定需要是 RegularFileProperty 和 DirectoryProperty
consumer.configure {
    inputFiles.add(producer1.flatMap { it.outPutFile })
    inputFiles.add(producer2.flatMap { it.outPutFile })
}