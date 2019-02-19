/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.lib.eventhubs;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.Target;
import com.streamsets.pipeline.api.credential.CredentialValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventHubCommon {

  private static final Logger LOG = LoggerFactory.getLogger(EventHubCommon.class);
  public final static String CONF_NAME_SPACE = "commonConf.namespaceName";
  private final EventHubConfigBean commonConf;

  public EventHubCommon(EventHubConfigBean commonConf) {
    this.commonConf = commonConf;
  }

  public EventHubClient createEventHubClient(String threadNamePattern) throws IOException, EventHubException, StageException {
    final ConnectionStringBuilder connStr = new ConnectionStringBuilder()
        .setNamespaceName(commonConf.namespaceName.get())
        .setEventHubName(commonConf.eventHubName.get())
        .setSasKey(commonConf.sasKey.get())
        .setSasKeyName(commonConf.sasKeyName.get());

    final Executor executor = Executors.newCachedThreadPool(
        new ThreadFactoryBuilder().setNameFormat(threadNamePattern).build()
    );

    return EventHubClient.createSync(connStr.toString(), executor);
  }

}
