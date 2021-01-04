package com.example.kolkoikrzyzyk

import com.example.kolkoikrzyzyk.database.DbUser
import com.example.kolkoikrzyzyk.database.UserDao
import com.example.kolkoikrzyzyk.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun registerNewUser(name: String, password: String) =
        userDao.insert(DbUser(name = name, password = password))

    suspend fun loginUser(name: String, password: String): User? {
        val user = userDao.findByName(name)
        return user?.run {
            if (user.password == password) return@run this.toUser()
            null
        }
    }

    suspend fun logoutUser(user: User) {
        userDao.updateLoginStatus(user.uid, false)
    }
}