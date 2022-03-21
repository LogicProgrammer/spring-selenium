
package com.automation.framework.model.query;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Query {

    @JsonProperty("name")
    private String name;

    @JsonProperty("database")
    private Database database;

    @JsonProperty("type")
    private String type;

    @JsonProperty("file")
    private String file;

    @JsonProperty("table")
    private Table table;

    @JsonProperty("orientation")
    private String orientation;

    @JsonProperty("fetch")
    private List<Integer> fetch = new ArrayList<>();

    @JsonProperty("range")
    private Range range;

}
