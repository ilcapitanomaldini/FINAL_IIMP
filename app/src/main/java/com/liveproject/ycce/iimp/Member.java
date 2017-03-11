package com.liveproject.ycce.iimp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tiger on 06-10-2016.
 */
public class Member implements Parcelable {

    private MemberPersonalInfo mpi;
    private MemberAddress memaddr;

    public void setMpi(MemberPersonalInfo mpi) {
        this.mpi = mpi;
    }

    public void setMemaddr(MemberAddress memaddr) {
        this.memaddr = memaddr;
    }

    public MemberPersonalInfo getMemberPersonalInfo(){
        return mpi;
    }

    public MemberAddress getMemberAddress(){
        return memaddr;
    }


    public Member() {
        this.mpi=new MemberPersonalInfo();
        this.memaddr=new MemberAddress();
    }

    public Member(MemberPersonalInfo mpi, MemberAddress ma) {
        this.mpi=new MemberPersonalInfo(mpi);
        this.memaddr=new MemberAddress(ma);
    }

    public Member(Member m){
        this.mpi=new MemberPersonalInfo(m.mpi);
        this.memaddr=new MemberAddress(m.memaddr);
    }

    public MemberAddress getMemaddr() {
        return memaddr;
    }

    public MemberPersonalInfo getMpi() {
        return mpi;
    }

    protected Member(Parcel in) {
        mpi = in.readParcelable(MemberPersonalInfo.class.getClassLoader());
        memaddr = in.readParcelable(MemberPersonalInfo.class.getClassLoader());
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mpi,flags);
        dest.writeParcelable(memaddr,flags);
    }
}
