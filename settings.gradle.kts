pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()

        // You're company repo:

        //insecure protocol:
        //maven {
        //    url = uri("http://my.company.repo.com/nexus/content/groups/releases")
        //    isAllowInsecureProtocol = true;
        //}

        //secure protocol:
        //maven {
        //    url = uri("https://my.company.repo.com/nexus/content/groups/releases")
        //}

    }
}

buildscript {
    repositories {
        mavenLocal()

        // You're company repo:

        //insecure protocol:
        //maven {
        //    url = uri("http://my.company.repo.com/nexus/content/groups/releases")
        //    isAllowInsecureProtocol = true;
        //}

        //secure protocol:
        //maven {
        //    url = uri("https://my.company.repo.com/nexus/content/groups/releases")
        //}
    }
}

rootProject.name = "ept"

val projectsDir = File(settingsDir, "projects")

listOf<File>()
        .plus(
                if (projectsDir.exists()) {
                    projectsDir.listFiles { dir, folderName ->
                        !folderName.startsWith(".") && folderName != "yarn-cache"
                    }
                } else {
                    emptyArray<File>()
                })
        .forEach {
            println("Including project ${it.parentFile.name}/${it.name}")

            val path = "./${it.parentFile.name}/${it.name}"

            include(path)

            project(":$path").name = it.name
        }
