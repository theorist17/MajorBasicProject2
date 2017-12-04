package org.androidtown.MajorBasicProject2;

public class ItemAddressView {
    private String id_address;
    private String country;
    private String province;
    private String city;
    private String town;
    private String road;
    private String postcode;
    private String latitude;
    private String longitude;

    public String getId_address() {
        return this.id_address;
    }
    public void setId_address(String id_address){
        this.id_address = id_address;
    }
    public String getAddress() {
        return province + " " + city + " " + town + " " + road;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getTown() {
        return town;
    }
    public void setTown(String town) {
        this.town = town;
    }
    public String getRoad() {
        return road;
    }
    public void setRoad(String road) {
        this.road = road;
    }
    public String getPostcode() {
        return postcode;
    }
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    public String getLatitude() {
        return this.latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return this.longitude;
    }
    public void setLongitude(String desc) {
        this.longitude = desc ;
    }
}