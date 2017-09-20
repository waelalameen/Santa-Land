package com.wael.alameen.santaland;


public class Menu {

    private String name;
    private String image;
    private String desc;
    private String facebookLink;
    private String instaLink;
    private String phone;

    Menu(String name, String desc, String image, String face, String insta, String phone) {
        setName(name);
        setDesc(desc);
        setImage(image);
        setFacebookLink(face);
        setInstaLink(insta);
        setPhone(phone);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    private void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    private void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getInstaLink() {
        return instaLink;
    }

    private void setInstaLink(String instaLink) {
        this.instaLink = instaLink;
    }

    public String getPhone() {
        return phone;
    }

    private void setPhone(String phone) {
        this.phone = phone;
    }
}
