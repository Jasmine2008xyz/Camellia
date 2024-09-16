package com.tencent.qqnt.kernel.nativeinterface;

/* compiled from: P */
public final class LinkInfo {
    public String desc;
    public String icon;
 //   public RichStatus richStatus;
    public Integer tencentDocType;
    public String title;

    public LinkInfo() {
  //      this.richStatus = RichStatus.values()[0];
    }

    public String getDesc() {
        return this.desc;
    }

    public String getIcon() {
        return this.icon;
    }

    /*public RichStatus getRichStatus() {
        return this.richStatus;
    }*/

    public Integer getTencentDocType() {
        return this.tencentDocType;
    }

    public String getTitle() {
        return this.title;
    }

    public String toString() {
        throw new RuntimeException("stub");
       // return "LinkInfo{title=" + this.title + ",icon=" + this.icon + ",desc=" + this.desc + ",richStatus=" + this.richStatus + ",tencentDocType=" + this.tencentDocType + ",}";
    }

   /* public LinkInfo(String str, String str2, String str3, RichStatus richStatus, Integer num) {
       // this.richStatus = RichStatus.values()[0];
        this.title = str;
        this.icon = str2;
        this.desc = str3;
       // this.richStatus = richStatus;
        this.tencentDocType = num;
    }*/
}