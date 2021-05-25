package com.oint.poc.auth;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "scopes", "rsid", "rsname" })
public class Authorization {

    @JsonProperty("scopes")
    private List<String> scopes = null;
    @JsonProperty("rsid")
    private String rsid;
    @JsonProperty("rsname")
    private String rsname;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scopes")
    public List<String> getScopes() {
        return scopes;
    }

    @JsonProperty("scopes")
    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    @JsonProperty("rsid")
    public String getRsid() {
        return rsid;
    }

    @JsonProperty("rsid")
    public void setRsid(String rsid) {
        this.rsid = rsid;
    }

    @JsonProperty("rsname")
    public String getRsname() {
        return rsname;
    }

    @JsonProperty("rsname")
    public void setRsname(String rsname) {
        this.rsname = rsname;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
