package com.automation.framework.model.browser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeoLocation {

    @JsonProperty("longitude")
    public double longitude;

    @JsonProperty("latitude")
    public double latitude;

    @JsonProperty("accuracy")
    public double accuracy;

}
