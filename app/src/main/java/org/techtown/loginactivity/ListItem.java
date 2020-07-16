package org.techtown.loginactivity;

public class ListItem {
    private String[] mData;

    public ListItem(String[] data ){

        mData = data;
    }

    public ListItem(String name, String phone, String id, String password1, String password2){

        mData = new String[5];
        mData[0] = name;
        mData[1] = phone;
        mData[2] = id;
        mData[3] = password1;
        mData[4] = password2;

    }

    public String[] getData(){
        return mData;
    }

    public String getData(int index){ return mData[index]; }

    public void setData(String[] data){
        mData = data;
    }


}
