package com.myapplication.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("xml")
public class AppConfig {

    @XStreamAlias("id")
    private String id;
    @XStreamAlias("config_hash")
    private String config_hash;
    @XStreamAlias("state")
    private String state;
    @XStreamAlias("client_name")
    private String client_name;
    @XStreamAlias("api_root")
    private String api_root;
    @XStreamAlias("api_prefix")
    private String api_prefix;
    @XStreamAlias("copyright")
    private String copyright;
    @XStreamAlias("platform")
    private String platform;
    @XStreamAlias("message")
    private String message;
    @XStreamAlias("os")
    private String os;
    @XStreamAlias("version")
    private String version;
    @XStreamAlias("versioncode")
    private String versioncode;
    @XStreamAlias("versionlogs")
    private String versionlogs;
    @XStreamAlias("versionpath")
    private String versionpath;
    @XStreamAlias("force_update")
    private String force_update;
    @XStreamAlias("modules")
    private Modules listModules;
    @XStreamAlias("index-ads")
    private Indexads listIndexads;
    @XStreamAlias("content-ads")
    private contentads contentads;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfig_hash() {
        return config_hash;
    }

    public void setConfig_hash(String config_hash) {
        this.config_hash = config_hash;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getApi_root() {
        return api_root;
    }

    public void setApi_root(String api_root) {
        this.api_root = api_root;
    }

    public String getApi_prefix() {
        return api_prefix;
    }

    public void setApi_prefix(String api_prefix) {
        this.api_prefix = api_prefix;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionlogs() {
        return versionlogs;
    }

    public void setVersionlogs(String versionlogs) {
        this.versionlogs = versionlogs;
    }

    public String getVersionpath() {
        return versionpath;
    }

    public void setVersionpath(String versionpath) {
        this.versionpath = versionpath;
    }

    public String getForce_update() {
        return force_update;
    }

    public void setForce_update(String force_update) {
        this.force_update = force_update;
    }

    public Modules getListModules() {
        return listModules;
    }

    public void setListModules(Modules listModules) {
        this.listModules = listModules;
    }

    public Indexads getListIndexads() {
        return listIndexads;
    }

    public void setListIndexads(Indexads listIndexads) {
        this.listIndexads = listIndexads;
    }

    public AppConfig.contentads getContentads() {
        return contentads;
    }

    public void setContentads(AppConfig.contentads contentads) {
        this.contentads = contentads;
    }

    public class Modules {

        @XStreamImplicit(itemFieldName = "item")
        private List<Item> listItem;

        public List<Item> getListItem() {
            return listItem;
        }

        public void setListItem(List<Item> listItem) {
            this.listItem = listItem;
        }

        public class Item {

            @XStreamAlias("module-key")
            private String modulekey;
            @XStreamAlias("class")
            private String classname;
            @XStreamAlias("list-api")
            private String list_api;
            @XStreamAlias("content-api")
            private String content_api;
            @XStreamAlias("title")
            private String title;
            @XStreamAlias("icon")
            private String icon;
            @XStreamAlias("logo")
            private String logo;
            @XStreamAlias("indexpic")
            private String indexpic;
            @XStreamAlias("plusdata")
            private String plusdata;
            @XStreamAlias("channel")
            private Channel channel;

            public class Channel {

                @XStreamImplicit(itemFieldName = "item")
                private List<channelItem> listItem;

                public List<channelItem> getListItem() {
                    return listItem;
                }

                public void setListItem(List<channelItem> listItem) {
                    this.listItem = listItem;
                }

                public class channelItem {
                    @XStreamAlias("key")
                    private String key;
                    @XStreamAlias("name")
                    private String name;

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

            }


            public String getModulekey() {
                return modulekey;
            }

            public void setModulekey(String modulekey) {
                this.modulekey = modulekey;
            }

            public Channel getChannel() {
                return channel;
            }

            public void setChannel(Channel channel) {
                this.channel = channel;
            }

            public String getClassname() {
                return classname;
            }

            public void setClassname(String classname) {
                this.classname = classname;
            }

            public String getList_api() {
                return list_api;
            }

            public void setList_api(String list_api) {
                this.list_api = list_api;
            }

            public String getContent_api() {
                return content_api;
            }

            public void setContent_api(String content_api) {
                this.content_api = content_api;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getIndexpic() {
                return indexpic;
            }

            public void setIndexpic(String indexpic) {
                this.indexpic = indexpic;
            }

            public String getPlusdata() {
                return plusdata;
            }

            public void setPlusdata(String plusdata) {
                this.plusdata = plusdata;
            }

        }

    }

    public class Indexads {
        @XStreamAlias("url")
        private String url;
        @XStreamAlias("item")
        private String item;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }

    public class contentads {
        @XStreamAlias("url")
        private String url;
        @XStreamAlias("item")
        private String item;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }

}
