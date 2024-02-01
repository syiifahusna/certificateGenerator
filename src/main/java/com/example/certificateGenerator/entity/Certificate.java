package com.example.certificateGenerator.entity;

import java.io.File;

public class Certificate {

    private String certOf;
    private String desc1;
    private String desc2;
    private String eventName;
    private String organizer;
    private String desc3;
    private String desc4;
    private File signImage;
    private String issuerName;
    private String issuerTitle;

    public Certificate(String certOf, String desc1, String desc2, String eventName, String organizer, String desc3, String desc4, File signImage, String issuerName, String issuerTitle) {
        this.certOf = certOf;
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.eventName = eventName;
        this.organizer = organizer;
        this.desc3 = desc3;
        this.desc4 = desc4;
        this.signImage = signImage;
        this.issuerName = issuerName;
        this.issuerTitle = issuerTitle;
    }

    public String getCertOf() {
        return certOf.toUpperCase();
    }

    public String getDesc1() {
        return desc1.toUpperCase();
    }

    public String getDesc2() {
        return desc2.toUpperCase();
    }

    public String getEventName() {
        return eventName.toUpperCase();
    }

    public String getOrganizer() {
        return organizer.toUpperCase();
    }

    public String getDesc3() {
        return desc3.toUpperCase();
    }

    public String getDesc4() {
        return desc4.toUpperCase();
    }

    public File getSignImage() {
        return signImage;
    }

    public String getIssuerName() {
        return issuerName.toUpperCase();
    }

    public String getIssuerTitle() {
        return issuerTitle.toUpperCase();
    }
}
