package com.oneself.swagger.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "oneself.swagger")
public class OneselfSwaggerProperties {

    private boolean enabled = true;
    private boolean groupEnabled = true;
    private String title = "Oneself API";
    private String description;
    private String version = "v1";
    private String termsOfService;
    private String groupName = "default";
    private ContactProperties contact = new ContactProperties();
    private LicenseProperties license = new LicenseProperties();
    private List<String> basePackages = new ArrayList<>();
    private List<String> pathsToMatch = new ArrayList<>();
    private List<String> pathsToExclude = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isGroupEnabled() {
        return groupEnabled;
    }

    public void setGroupEnabled(boolean groupEnabled) {
        this.groupEnabled = groupEnabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ContactProperties getContact() {
        return contact;
    }

    public void setContact(ContactProperties contact) {
        this.contact = contact;
    }

    public LicenseProperties getLicense() {
        return license;
    }

    public void setLicense(LicenseProperties license) {
        this.license = license;
    }

    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public List<String> getPathsToMatch() {
        return pathsToMatch;
    }

    public void setPathsToMatch(List<String> pathsToMatch) {
        this.pathsToMatch = pathsToMatch;
    }

    public List<String> getPathsToExclude() {
        return pathsToExclude;
    }

    public void setPathsToExclude(List<String> pathsToExclude) {
        this.pathsToExclude = pathsToExclude;
    }

    public static class ContactProperties {

        private String name;
        private String url;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class LicenseProperties {

        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
