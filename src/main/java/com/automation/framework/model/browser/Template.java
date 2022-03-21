package com.automation.framework.model.browser;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Template {

    @JsonProperty("name")
    private String name;

    @JsonProperty("browser_name")
    private String browserName;

    @JsonProperty("remote_browser")
    private String remoteBrowserType;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("port")
    private int port;

    @JsonProperty("driver")
    private String driver;

    @JsonProperty("options")
    private Options options;

    @JsonProperty("proxy")
    private ProxyModel proxy;

    @JsonProperty("authentication")
    private List<RegisterBasicAuth> basicAuths;

    @JsonProperty("mobile_emulation")
    private MobileEmulation mobileEmulation;

    @JsonProperty("geolocation")
    private GeoLocation geoLocation;

    @JsonProperty("capture_network_logs")
    private Boolean captureNetworkLogs;

    @JsonProperty("capture_console_logs")
    private Boolean captureLog = null;

    @JsonProperty("capture_js_exceptions")
    private Boolean captureJsExceptions = null;

    @JsonProperty("capture_performance_metrics")
    private Boolean capturePerformanceMetrics = null;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
