package com.example.barapp.data

import android.content.Context
import com.example.barapp.data.local.SharedPreferencesUtil
import com.example.barapp.domain.models.User
import com.example.barapp.domain.repositories.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {
    override fun getLoggedInUser(): User? {
        val id = SharedPreferencesUtil.getSharedIntData(context, USER_ID, 0)
        val roleId = SharedPreferencesUtil.getSharedIntData(context, USER_ROLE_ID, 0)
        if(id!=0){
            return User(
                id = id,
                roleId = roleId
            )
        }
        else{
            return null
        }
    }

    override fun setLoggedInUser(user: User?) {
        val _user = user?:User(id=0, roleId = 0)
        SharedPreferencesUtil.setSharedData(context, USER_ID, _user.id)
        SharedPreferencesUtil.setSharedData(context, USER_ROLE_ID, _user.roleId)
    }
    companion object{
        const val USER_ID = "user_id"
        const val USER_ROLE_ID = "user_role_id"
    }
}