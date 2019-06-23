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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.logging.Log;

/**
 * Logs mkdocs output into the correct maven log level.
 */
public class MkdocsLogger {
    private static final Pattern LOG_PATTERN = Pattern.compile("^(DEBUG|INFO|WARNING|ERROR)\\s+-\\s+(.*)$");

    private final Log log;
    private String lastLevel;

    /**
     * Creates a new {@link MkdocsLogger}.
     *
     * @param log
     *         {@link Log} target
     */
    public MkdocsLogger(Log log) {
        this.log = log;
    }

    /**
     * Logs a line.
     *
     * @param line
     *         Mkdocs line to log
     */
    public void log(String line) {
        String message = line;

        if (line.startsWith("Traceback")) {
            lastLevel = "TRACEBACK";
        } else {
            Matcher m = LOG_PATTERN.matcher(line);
            if (m.matches()) {
                lastLevel = m.group(1);
                message = m.group(2);
            }
        }

        switch (lastLevel) {
            case "DEBUG":
                log.debug(message);
                break;

            case "INFO":
                log.info(message);
                break;

            case "WARNING":
                log.warn(message);
                break;

            default:
                log.error(message);
        }
    }

}
