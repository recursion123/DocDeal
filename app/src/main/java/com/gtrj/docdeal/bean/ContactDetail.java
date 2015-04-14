package com.gtrj.docdeal.bean;

public class ContactDetail {
    private String id;
    private String phone;
    private String phoneForWork;
    private String name;
    private String job;
    private String fax;
    private String mail;
    private String housePhone;
    private String outsidePhone;
    private String cornet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneForWork() {
        return phoneForWork;
    }

    public void setPhoneForWork(String phoneForWork) {
        this.phoneForWork = phoneForWork;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHousePhone() {
        return housePhone;
    }

    public void setHousePhone(String housePhone) {
        this.housePhone = housePhone;
    }

    public String getOutsidePhone() {
        return outsidePhone;
    }

    public void setOutsidePhone(String outsidePhone) {
        this.outsidePhone = outsidePhone;
    }

    public String getCornet() {
        return cornet;
    }

    public void setCornet(String cornet) {
        this.cornet = cornet;
    }

    public ContactDetail(String id, String phone, String name) {
        super();
        this.id = id;
        this.phone = phone;
        this.name = name;
    }

}

