package com.automation.framework.model.browser;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Options {

    @JsonProperty("headless")
    private Boolean headless = null;

    @JsonProperty("binary")
    private String binary;

    @JsonProperty("accept_insecure_certs")
    private Boolean acceptInsecureCerts = null;

    @JsonProperty("arguments")
    private List<String> args = null;

    @JsonProperty("include_switches")
    private List<String> includeSwitches = null;

    @JsonProperty("exclude_switches")
    private List<String> excludeSwitches = null;

    @JsonProperty("unhandled_prompt_behaviour")
    private String unhandledPromptBehaviour;

    @JsonProperty("experimental_options")
    private Map<String,Object> experimentalOptions;

    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
