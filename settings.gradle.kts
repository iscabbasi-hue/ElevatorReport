pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/google")
        maven("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
        gradlePluginPortal()
        mavenCentral()
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        mavenCentral()
    }
}

rootProject.name = "ElevatorRportMahestan"
include(":app")
