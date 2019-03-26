package com.sh3h.dataprovider.data.entity.base;

import org.json.JSONArray;

/**
 * 消息推送
 */
public class DUReceive {
    private String type;
    private JSONArray content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONArray getContent() {
        return content;
    }

    public void setContent(JSONArray content) {
        this.content = content;
    }
}
