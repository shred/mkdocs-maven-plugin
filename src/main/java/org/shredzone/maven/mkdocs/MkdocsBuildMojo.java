/*
 * mkdocs-maven-plugin
 *
 * Copyright (C) 2019 Richard "Shred" Körber
 *   https://github.com/shred/mkdocs-maven-plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.shredzone.maven.mkdocs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This is the mkdocs build mojo.
 */
@Mojo(name = "build", threadSafe = true)
public class MkdocsBuildMojo extends AbstractMkdocsMojo {

    /**
     * Theme name to be used.
     */
    @Parameter(property = "mkdocs.theme")
    private String theme;

    /**
     * Directory of a mkdocs theme to be used.
     */
    @Parameter(property = "mkdocs.themeDirectory")
    private File themeDirectory;

    /**
     * Enable mkdocs strict mode. It will abort the build on any warning.
     */
    @Parameter(property = "mkdocs.strict", defaultValue = "false")
    private boolean strict;

    /**
     * The directory the documentation is generated into.
     */
    @Parameter(property = "mkdocs.outputDirectory", defaultValue = "${project.build.directory}/mkdocs")
    private File outputDirectory;

    /**
     * Remove old files from the target directory before building. Defaults to {@code
     * true}.
     */
    @Parameter(property = "mkdocs.clean", defaultValue = "true")
    private boolean clean;

    @Override
    protected void perform() throws IOException {
        List<String> args = new ArrayList<>();
        args.add(mkdocsPath);
        args.add("build");

        if (getLog().isDebugEnabled()) {
            args.add("--verbose");
        }

        if (strict) {
            getLog().info("Strict mode is enabled");
            args.add("--strict");
        }

        if (clean) {
            args.add("--clean");
        } else {
            args.add("--dirty");
        }

        if (configFile != null) {
            args.add("--config-file");
            args.add(configFile.toString());
        }

        if (theme != null) {
            args.add("--theme");
            args.add(theme);
        }

        if (themeDirectory != null) {
            args.add("--theme-dir");
            args.add(themeDirectory.toString());
        }

        args.add("--site-dir");
        args.add(outputDirectory.toString());

        invokeMkdocs(args, directory);
    }

}
