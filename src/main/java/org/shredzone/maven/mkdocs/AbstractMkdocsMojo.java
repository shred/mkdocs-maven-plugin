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

import static java.nio.charset.Charset.defaultCharset;
import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * An abstract mkdocs superclass with common methods that help to invoke mkdocs.
 */
public abstract class AbstractMkdocsMojo extends AbstractMojo {

    /**
     * Specifies whether all mkdocs goals should be skipped.
     */
    @Parameter(property = "mkdocs.skip", defaultValue = "false")
    protected boolean skip;

    /**
     * The mkdocs base directory.
     */
    @Parameter(property = "mkdocs.docs", defaultValue = "${basedir}/src/doc")
    protected File directory;

    /**
     * Path to the {@code mkdocs.yml} file to be used. If not set, it is expected in the
     * mkdocs base directory.
     */
    @Parameter(property = "mkdocs.mkfile")
    protected File configFile;

    /**
     * The {@code mkdocs} tool to be used. By default, the first {@code mkdocs} on the
     * system's path is used.
     */
    @Parameter(property = "mkdocs.toolPath", defaultValue = "mkdocs")
    protected String mkdocsPath;

    /**
     * Performs the mkdocs goal.
     * <p>
     * Override this method instead of {@link AbstractMojo#execute()}.
     */
    protected abstract void perform() throws MojoExecutionException, MojoFailureException, IOException;

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException{
        if (skip) {
            getLog().info("Skipping mkdocs build...");
            return;
        }

        // Check if there is a mkdocs.yml file. If not, skip the build.
        File usedConfigFile = configFile != null ? configFile : new File(directory, "mkdocs.yml");
        if (!usedConfigFile.isFile()) {
            getLog().info("No mkdocs.yml found, skipping mkdocs...");
            return;
        }

        try {
            perform();
        } catch (IOException ex) {
            throw new MojoExecutionException("mkdocs has failed", ex);
        }
    }

    /**
     * Invokes mkdocs.
     *
     * @param args
     *         Arguments to be used
     * @param basedir
     *         Base directory
     */
    protected void invokeMkdocs(List<String> args, File basedir) throws IOException {
        final String[] mkdocsPathArgs =  mkdocsPath.split("\\s+");
        final List<String> processArgs = new ArrayList<>();

        processArgs.addAll(Arrays.asList(mkdocsPathArgs));
        processArgs.addAll(args);

        if (getLog().isDebugEnabled()) {
            getLog().debug(processArgs.stream().collect(joining("' '", "'", "'")));
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(processArgs)
                    .directory(basedir);

            pb.environment().put("NO_COLOR", "1");
            Process proc = pb.start();

            MkdocsLogger logger = new MkdocsLogger(getLog());

            StreamGobbler outGobbler = new StreamGobbler(proc.getInputStream(), logger);
            outGobbler.start();

            StreamGobbler errGobbler = new StreamGobbler(proc.getErrorStream(), logger);
            errGobbler.start();

            int rc = proc.waitFor();
            outGobbler.join();
            errGobbler.join();
            if (rc != 0) {
                throw new IOException("mkdocs failed with return code: " + rc);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted", ex);
        }
    }

    /**
     * A {@link Thread} that reads the {@link InputStream} and feeds a {@link
     * MkdocsLogger}.
     */
    private static class StreamGobbler extends Thread {
        private final InputStream in;
        private final MkdocsLogger logger;

        StreamGobbler(InputStream in, MkdocsLogger logger) {
            this.in = in;
            this.logger = logger;
        }

        @Override
        public void run() {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(in, defaultCharset()))) {
                r.lines().forEach(logger::log);
            } catch (IOException ex) {
                throw new RuntimeException("Failed reading stream", ex);
            }
        }
    }

}
