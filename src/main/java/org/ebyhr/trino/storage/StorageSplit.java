/*
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
package org.ebyhr.trino.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import io.airlift.slice.SizeOf;
import io.trino.spi.HostAddress;
import io.trino.spi.connector.ConnectorSplit;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class StorageSplit
        implements ConnectorSplit
{
    private static final int INSTANCE_SIZE = SizeOf.instanceSize(StorageSplit.class);
    private static final int MODE_SIZE = SizeOf.instanceSize(Mode.class);

    private final Mode mode;
    private final String schemaName;
    private final String tableName;

    @JsonCreator
    public StorageSplit(
            @JsonProperty("mode") Mode mode,
            @JsonProperty("schemaName") String schemaName,
            @JsonProperty("tableName") String tableName)
    {
        this.schemaName = requireNonNull(schemaName, "schema name is null");
        this.mode = requireNonNull(mode, "mode is null");
        this.tableName = requireNonNull(tableName, "table name is null");
    }

    @JsonProperty
    public Mode getMode()
    {
        return mode;
    }

    @JsonProperty
    public String getSchemaName()
    {
        return schemaName;
    }

    @JsonProperty
    public String getTableName()
    {
        return tableName;
    }

    @Override
    public List<HostAddress> getAddresses()
    {
        return List.of();
    }

    @Override
    public Map<String, String> getSplitInfo()
    {
        return ImmutableMap.<String, String>builder()
                .put("mode", mode.name())
                .put("schemaName", schemaName)
                .put("tableName", tableName)
                .buildOrThrow();
    }

    public long getRetainedSizeInBytes()
    {
        return INSTANCE_SIZE
                + MODE_SIZE
                + SizeOf.estimatedSizeOf(schemaName)
                + SizeOf.estimatedSizeOf(tableName);
    }

    public enum Mode
    {
        TABLE,
        LIST,
        /**/;
    }
}
