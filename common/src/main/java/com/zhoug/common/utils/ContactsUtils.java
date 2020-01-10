package com.zhoug.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import com.zhoug.common.beans.ZContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机通讯录工具
 */
public class ContactsUtils {
    private static final String TAG = "ContactsUtils";
    /**
     * 主表(ZContacts) 储存联系人信息
     */
    private static final Uri CONTACTS_URI=ContactsContract.Contacts.CONTENT_URI;
    //姓名列
    private static final String CONTACTS_NAME=ContactsContract.Contacts.DISPLAY_NAME;
    //id列
    private static final String CONTACTS_ID=ContactsContract.Contacts._ID;

    /**
     * Phone表：(一个用户可以有多个号码)
     */
    private static final Uri PHONE_URI=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    //关联主表(ZContacts)的列名：
    private static final String PHONE_CONTACT_ID=ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    //号码列名：
    private static final String PHONE_NUMBER=ContactsContract.CommonDataKinds.Phone.NUMBER;

    /**
     * 查询出联系人 姓名和电话 需要权限
     * <uses-permission android:name="android.permission.READ_CONTACTS"/>
     * @param context
     * @return
     */
    public static List<ZContacts> getContacts(Context context){
        List<ZContacts> lists=new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        ZContacts contacts;
        StringBuilder telPhone;
        if(contentResolver!=null){
            //主表中查询姓名和id
            Cursor query = contentResolver.query(CONTACTS_URI,
                    new String[]{CONTACTS_NAME, CONTACTS_ID}, null, null, null);
            if(query!=null){
                while (query.moveToNext()){
                    String id = query.getString(query.getColumnIndex(CONTACTS_ID));
//                    Log.d(TAG, "getContacts:id="+id);
                    String name = query.getString(query.getColumnIndex(CONTACTS_NAME));
//                    Log.d(TAG, "getContacts:name="+name);
                    contacts=new ZContacts();
                    lists.add(contacts);
                    contacts.setName(name);
                    telPhone=new StringBuilder();
                    //获取联系人电话号码
                    Cursor phoneQuery = contentResolver.query(PHONE_URI, new String[]{PHONE_NUMBER, PHONE_CONTACT_ID}, PHONE_CONTACT_ID + "=" + id, null, null);
                    if(phoneQuery!=null){
                        while (phoneQuery.moveToNext()){
                            String number = phoneQuery.getString(phoneQuery.getColumnIndex(PHONE_NUMBER));
//                            Log.d(TAG, "getContacts:number="+number);
                            telPhone.append(ZContacts.SEPARATOR);
                            telPhone.append(number);
                        }
                        //第一个分隔符去掉
                        if(telPhone.length()>1 && ZContacts.SEPARATOR.equals(telPhone.substring(0,1))){
                            telPhone.delete(0,1 );
                        }
//                        Log.d(TAG, "getContacts:telPhone="+telPhone.toString());
                        contacts.setTelPhone(telPhone.toString());
                        phoneQuery.close();
                    }
//                    Log.d(TAG, "getContacts:>>>>>>>");

                }
                query.close();
            }
        }

        return lists;
    }



}
