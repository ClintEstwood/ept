plugins {
    java
}

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

//Configure the dependency resolution and tasks for the project

//block sets up a resolution strategy for all subprojects
subprojects {

    //block runs after the evaluation of the project and is used to configure the resolution strategy
    afterEvaluate {

        // block applies the resolution strategy to all configurations of the subprojects
        configurations.all {

            // block sets up a dependency substitution for each project that can be found in the projects directory of the root project.
            resolutionStrategy {

                //block allows replacing dependencies with other dependencies
                dependencySubstitution {

                    //holds the projects directory
                    val projectDir = File(rootDir, "projects")

                    //holds all subdirectories of projects that are not hidden and not named "yarn-cache"
                    val projectsFolders =
                            if (projectDir.exists()) {
                                projectDir.listFiles { dir, foldername ->
                                    !foldername.startsWith(".") &&
                                            foldername != "yarn-cache"
                                }
                            } else {
                                emptyArray<File>()
                            }

                    // iterates over each subdirectory of projects, and for each subdirectory,
                    // the code checks if there is a project with the same name as the subdirectory.
                    // If there is, it substitutes the dependency with the project
                    projectsFolders.forEach {
                        val projectLocator = ":${it.name}"
                        val foundProject = findProject(projectLocator)

                        if (foundProject != null){
                            val dependencyLocator = "${foundProject.group}:${foundProject.name}"
                            substitute(module(dependencyLocator)).using(project(projectLocator))
                        }
                    }
                }

                //prefer project modules over external modules
                preferProjectModules()
            }
        }
    }
}

tasks {

    //prints out the name and URL of each repository.
    task("showRepositories") {
        doLast {
            repositories.map {
                it as MavenArtifactRepository
            }.forEach {
                println("Repository: ${it.name} ('${it.url}')")
            }
        }
    }

    // block configures the HtmlDependencyReportTask task for generating a report of all dependencies in the project
    withType<HtmlDependencyReportTask> {

        //The 'projects' variable is set to include all projects in the root project.
        projects = project.allprojects
    }
}