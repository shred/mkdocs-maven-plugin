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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This is the mkdocs gh-deploy mojo.
 */
@Mojo(name = "gh-deploy", threadSafe = true, defaultPhase = LifecyclePhase.DEPLOY)
public class MkdocsDeployMojo extends AbstractMkdocsMojo {

    /**
     * Remove old files from the target directory before building. Defaults to {@code
     * true}.
     */
    @Parameter(property = "mkdocs.clean", defaultValue = "true")
    private boolean clean;

    /**
     * A commit message to use when committing to the Github Pages remote branch. Commit
     * {sha} and MkDocs {version} are available as expansions.
     */
    @Parameter(property = "mkdocs.deploy.message")
    private String message;

    /**
     * The remote branch to commit to for Github Pages.
     */
    @Parameter(property = "mkdocs.deploy.remoteBranch")
    private String remoteBranch;

    /**
     * The remote name to commit to for Github Pages.
     */
    @Parameter(property = "mkdocs.deploy.remoteName")
    private String remoteName;

    /**
     * Force the push to the repository.
     */
    @Parameter(property = "mkdocs.deploy.force")
    private boolean force;

    @Override
    protected void perform() throws IOException {
        final List<String> args = new ArrayList<>();

        args.add("gh-deploy");

        if (getLog().isDebugEnabled()) {
            args.add("--verbose");
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

        if (message != null) {
            args.add("--message");
            args.add(message);
        }

        if (remoteBranch != null) {
            args.add("--remote-branch");
            args.add(remoteBranch);
        }

        if (remoteName != null) {
            args.add("--remote-name");
            args.add(remoteName);
        }

        if (force) {
            args.add("--force");
        }

        invokeMkdocs(args, directory);
    }

}
