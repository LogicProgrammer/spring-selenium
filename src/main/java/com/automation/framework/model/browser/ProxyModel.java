package com.automation.framework.model.browser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProxyModel {

    @JsonProperty("ftp")
    public String ftpProxy;

    @JsonProperty("ssl")
    public String sslProxy;

    @JsonProperty("http")
    public String httpProxy;

    @JsonProperty("pac")
    public String pac;

    @JsonProperty("type")
    public String proxyType;

    @JsonProperty("sock")
    public Sock sock;

    @Data
    public static class Sock{
        @JsonProperty("sock_proxy")
        String sockProxy;

        @JsonProperty("sock_version")
        int sockVersion;

        @JsonProperty("sock_username")
        String sockUsername;

        @JsonProperty("sock_password")
        String sockPassword;
    }

}
