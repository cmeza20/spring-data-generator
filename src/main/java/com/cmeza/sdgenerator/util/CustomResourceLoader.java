package com.cmeza.sdgenerator.util;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Created by carlos on 22/04/17.
 */
public class CustomResourceLoader implements ResourceLoader {

    private URLClassLoader urlClassLoader;

    public CustomResourceLoader(MavenProject project){

        try {
            List<String> runtimeClasspathElements = project.getRuntimeClasspathElements();
            URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
            for (int i = 0; i < runtimeClasspathElements.size(); i++) {
                String element = runtimeClasspathElements.get(i);
                runtimeUrls[i] = new File(element).toURI().toURL();
            }
            urlClassLoader = new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
        } catch (DependencyResolutionRequiredException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource getResource(String s) {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.urlClassLoader;
    }

    public URLClassLoader getUrlClassLoader(){
        return this.urlClassLoader;
    }

}
