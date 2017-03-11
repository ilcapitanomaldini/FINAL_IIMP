package com.liveproject.ycce.iimp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tiger on 26-10-2016.
 */
public class MemberAddress implements Parcelable {
    private String addrline, street, locality, city, state, country, pincode;
    public MemberAddress()
    {
        this.addrline=null;
        this.street=null;
        this.locality=null;
        this.city=null;
        this.state=null;
        this.country=null;
        this.pincode=null;
    }
    public MemberAddress(String addrline, String street, String locality, String city, String state, String country, String pincode)
    {
        this.addrline=addrline;
        this.street=street;
        this.locality=locality;
        this.city=city;
        this.state=state;
        this.country=country;
        this.pincode=pincode;
    }
    public MemberAddress(MemberAddress ma)
    {
        this.addrline = ma.getAddrline();
        this.street = ma.getStreet();
        this.locality = ma.getLocality();
        this.city = ma.getCity();
        this.state = ma.getState();
        this.country = ma.getCountry();
        this.pincode = ma.getPincode();
    }

    protected MemberAddress(Parcel in) {
        addrline = in.readString();
        street = in.readString();
        locality = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        pincode = in.readString();
    }

    public static final Creator<MemberAddress> CREATOR = new Creator<MemberAddress>() {
        @Override
        public MemberAddress createFromParcel(Parcel in) {
            return new MemberAddress(in);
        }

        @Override
        public MemberAddress[] newArray(int size) {
            return new MemberAddress[size];
        }
    };

    public String getAddrline() {
        return addrline;
    }

    public void setAddrline(String addrline) {
        this.addrline = addrline;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addrline);
        dest.writeString(street);
        dest.writeString(locality);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(pincode);
    }
}
