/**
 * Copyright 2017 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.raigad.defaultimpl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.netflix.raigad.configuration.IConfiguration;
import com.netflix.raigad.utils.Sleeper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ESProcessManager implements IElasticsearchProcess {
    private static final Logger logger = LoggerFactory.getLogger(ESProcessManager.class);
    private static final String SUDO_STRING = "/usr/bin/sudo";
    private static final int SCRIPT_EXECUTE_WAIT_TIME_MS = 5000;
    private final IConfiguration config;
    private final Sleeper sleeper;

    @Inject
    public ESProcessManager(IConfiguration config, Sleeper sleeper) {
        this.config = config;
        this.sleeper = sleeper;
    }

    public void start(boolean join_ring) throws IOException {
        logger.info("Starting elasticsearch server");

        List<String> command = Lists.newArrayList();
        if (!"root".equals(System.getProperty("user.name"))) {
            command.add(SUDO_STRING);
            command.add("-n");
            command.add("-E");
        }
        command.addAll(getStartCommand());

        ProcessBuilder startEs = new ProcessBuilder(command);
        Map<String, String> env = startEs.environment();

        env.put("DATA_DIR", config.getDataFileLocation());

        startEs.directory(new File("/"));
        startEs.redirectErrorStream(true);
        Process starter = startEs.start();
        logger.info("Starting Elasticsearch server ....");
        try {
            sleeper.sleepQuietly(SCRIPT_EXECUTE_WAIT_TIME_MS);
            int code = starter.exitValue();
            if (code == 0)
                logger.info("Elasticsearch server has been started");
            else
                logger.error("Unable to start Elasticsearch server. Error code: {}", code);

            logProcessOutput(starter);
        } catch (Exception e) {
            logger.warn("Starting Elasticsearch has an error", e.getMessage());
        }
    }

    protected List<String> getStartCommand() {
        List<String> startCmd = new LinkedList<String>();
        for (String param : config.getElasticsearchStartupScript().split(" ")) {
            if (StringUtils.isNotBlank(param))
                startCmd.add(param);
        }
        return startCmd;
    }

    void logProcessOutput(Process p) {
        try {
            final String stdOut = readProcessStream(p.getInputStream());
            final String stdErr = readProcessStream(p.getErrorStream());
            logger.info("std_out: {}", stdOut);
            logger.info("std_err: {}", stdErr);
        } catch (IOException ioe) {
            logger.warn("Failed to read the std out/err streams", ioe);
        }
    }

    String readProcessStream(InputStream inputStream) throws IOException {
        final byte[] buffer = new byte[512];
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(buffer.length);
        int cnt;
        while ((cnt = inputStream.read(buffer)) != -1)
            baos.write(buffer, 0, cnt);
        return baos.toString();
    }


    public void stop() throws IOException {
        logger.info("Stopping Elasticsearch server ....");
        List<String> command = Lists.newArrayList();
        if (!"root".equals(System.getProperty("user.name"))) {
            command.add(SUDO_STRING);
            command.add("-n");
            command.add("-E");
        }
        for (String param : config.getElasticsearchStopScript().split(" ")) {
            if (StringUtils.isNotBlank(param))
                command.add(param);
        }
        ProcessBuilder stopCass = new ProcessBuilder(command);
        stopCass.directory(new File("/"));
        stopCass.redirectErrorStream(true);
        Process stopper = stopCass.start();

        sleeper.sleepQuietly(SCRIPT_EXECUTE_WAIT_TIME_MS);
        try {
            int code = stopper.exitValue();
            if (code == 0)
                logger.info("Elasticsearch server has been stopped");
            else {
                logger.error("Unable to stop Elasticsearch server. Error code: {}", code);
                logProcessOutput(stopper);
            }
        } catch (Exception e) {
            logger.warn("couldn't shut down Elasticsearch correctly", e);
        }
    }
}
