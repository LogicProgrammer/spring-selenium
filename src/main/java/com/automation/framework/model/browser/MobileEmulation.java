package com.automation.framework.model.browser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MobileEmulation {

    @JsonProperty("pixel_ratio")
    public String pixelRatio;

    @JsonProperty("width")
    public String width;

    @JsonProperty("height")
    public String height;

    @JsonProperty("device_name")
    public String deviceName;

}
