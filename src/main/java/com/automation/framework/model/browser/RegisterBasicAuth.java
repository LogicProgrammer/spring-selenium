package com.automation.framework.model.browser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisterBasicAuth {

    @JsonProperty("url")
    public String url;

    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;

}
