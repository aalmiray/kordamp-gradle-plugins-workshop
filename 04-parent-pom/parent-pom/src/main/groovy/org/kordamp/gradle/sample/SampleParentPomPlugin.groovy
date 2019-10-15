/*
 * Copyright 2019 The original author or authors.
 */
package org.kordamp.gradle.sample

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.kordamp.gradle.plugin.base.ProjectConfigurationExtension
import org.kordamp.gradle.plugin.project.ProjectPlugin

class SampleParentPomPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.plugins.apply(ProjectPlugin)

        project.extensions.findByType(ProjectConfigurationExtension).with {
            release = (project.rootProject.findProperty('release') ?: false).toBoolean()

            info {
                vendor = 'Kordamp'

                links {
                    website      = "https://github.com/kordamp/${project.rootProject.name}"
                    issueTracker = "https://github.com/kordamp/${project.rootProject.name}/issues"
                    scm          = "https://github.com/kordamp/${project.rootProject.name}.git"
                }

                people {
                    person {
                        id    = 'anonymous'
                        name  = 'Anonymous'
                        roles = ['developer']
                    }
                }
				
		        repositories {
		            repository {
		                name = 'stagingRelease'
		                url  = "${project.buildDir}/repos/staging/release"
		            }
		            repository {
		                name = 'stagingSnapshot'
		                url  = "${project.buildDir}/repos/staging/snapshot"
		            }
		        }
            }

            licensing {
                licenses {
                    license {
                        id = 'Apache-2.0'
                    }
                }
            }

            javadoc {
                excludes = ['**/*.html', 'META-INF/**']
            }
        }

        project.allprojects {
            repositories {
                jcenter()
                mavenCentral()
				mavenLocal()
            }

            normalization {
                runtimeClasspath {
                    ignore('/META-INF/MANIFEST.MF')
                }
            }
        }

        project.allprojects { Project p ->
            def scompat = project.findProperty('sourceCompatibility')
            def tcompat = project.findProperty('targetCompatibility')

            p.tasks.withType(JavaCompile) { JavaCompile c ->
                if (scompat) c.sourceCompatibility = scompat
                if (tcompat) c.targetCompatibility = tcompat
            }
        }
    }
}
