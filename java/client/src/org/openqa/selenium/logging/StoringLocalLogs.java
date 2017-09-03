// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.openqa.selenium.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LocalLogs instance that has its own storage. This should be used for explicit storing
 * of logs, such as for profiling.
 */
class StoringLocalLogs extends LocalLogs {
  private final Map<String, List<LogEntry>> localLogs = new HashMap<>();
  private final Set<String> logTypesToInclude;

  public StoringLocalLogs(Set<String> logTypesToInclude) {
    this.logTypesToInclude = logTypesToInclude;
  }

  public LogEntries get(String logType) {
    return new LogEntries(getLocalLogs(logType));
  }

  private Iterable<LogEntry> getLocalLogs(String logType) {
    if (localLogs.containsKey(logType)) {
      List<LogEntry> entries = localLogs.get(logType);
      localLogs.put(logType, new ArrayList<>());
      return entries;
    }

    return new ArrayList<>();
  }

  /**
   * Add a new log entry to the local storage.
   *
   * @param logType the log type to store
   * @param entry   the entry to store
   */
  public void addEntry(String logType, LogEntry entry) {
    if (!logTypesToInclude.contains(logType)) {
      return;
    }

    if (!localLogs.containsKey(logType)) {
      localLogs.put(logType, new ArrayList<>());
    } else {
      localLogs.get(logType).add(entry);
    }
  }

  public Set<String> getAvailableLogTypes() {
    return localLogs.keySet();
  }
}
