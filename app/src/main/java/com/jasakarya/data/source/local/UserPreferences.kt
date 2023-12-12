package com.jasakarya.data.source.local

import android.content.Context

class UserPreferences(context: Context){
    private val preferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE)

    fun setUserId(userid:String){
        val edit = preferences.edit()
        edit.putString("userid",userid)
        edit.apply()
    }

    fun setToken(token:String){
        val edit = preferences.edit()
        edit.putString("token",token)
        edit.apply()
    }

    fun getUserId():String?{
        return preferences.getString("userid",null)
    }

    fun getToken():String?{
        return preferences.getString("token",null)
    }

    fun clearData(){
        preferences.edit().clear().apply()
    }


}