package com.william.garcia;

import com.genexus.ModelContext;
import com.genexus.xml.GXXMLSerializable;

public class NotificationMessage extends GXXMLSerializable {

    public String Status;
    public String Detail;

    public NotificationMessage(ModelContext context) {
        super(context, "NotificationMessage");
    }

    @Override
    public String getJsonMap(String var1) {
        return null;
    }

    @Override
    public void initialize() {
        Status = "";
        Detail = "";
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public void setDetail(String detail) {
        this.Detail = detail;
    }

    @Override
    public void tojson() {
        this.AddObjectProperty("Status", this.Status);
        this.AddObjectProperty("Detail", this.Detail);
    }
}
