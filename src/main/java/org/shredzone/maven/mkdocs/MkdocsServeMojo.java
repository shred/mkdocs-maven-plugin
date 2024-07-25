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
 * This is the mkdocs serve mojo.
 */
@Mojo(name = "serve", requiresDirectInvocation = true)
public class MkdocsServeMojo extends AbstractMkdocsMojo {

    /**
     * Server host name.
     */
    @Parameter(property = "mkdocs.serve.host")
    private String host;

    /**
     * Server port.
     */
    @Parameter(property = "mkdocs.serve.port")
    private Integer port;

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
     * Enables live reload mode.
     */
    @Parameter(property = "mkdocs.serve.liveReload", defaultValue = "true")
    private boolean liveReload;

    @Override
    protected void perform() throws IOException {
        final List<String> args = new ArrayList<>();

        args.add("serve");

        if (getLog().isDebugEnabled()) {
            args.add("--verbose");
        }

        if (strict) {
            getLog().info("Strict mode is enabled");
            args.add("--strict");
        }

        if (configFile != null) {
            args.add("--config-file");
            args.add(configFile.toString());
        }

        if (host != null || port != null) {
            final String addr = (host != null ? host : "localhost")
                    + ":"
                    + (port != null ? port : 8000);

            args.add("--dev-addr");
            args.add(addr);
        }

        if (theme != null) {
            args.add("--theme");
            args.add(theme);
        }

        if (themeDirectory != null) {
            args.add("--theme-dir");
            args.add(themeDirectory.toString());
        }

        if (liveReload) {
            args.add("--livereload");
        } else {
            args.add("--no-livereload");
        }

        invokeMkdocs(args, directory);
    }

}
