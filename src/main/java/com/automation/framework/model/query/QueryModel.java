
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
public class QueryModel {

    @JsonProperty("queries")
    private List<Query> queries = new ArrayList<>();

}
