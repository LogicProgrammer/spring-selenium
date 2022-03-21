package com.automation.framework.model.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Table {

    @JsonProperty("sheet")
    private String sheet;

    @JsonProperty("table_name")
    private String tableName;

    @JsonProperty("keys")
    private List<String> keys = new ArrayList<>();

}
