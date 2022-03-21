package com.automation.framework.model.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIncludeProperties()
public class Database {

    @JsonProperty("database_name")
    public String databaseName;

    @JsonProperty("database_server")
    public String databaseServer;

    @JsonProperty("")
    public String databaseType;

    @JsonProperty("")
    public String databaseCredentials;

    @JsonProperty("query")
    public Object query;

}
