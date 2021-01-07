package com.example.kolkoikrzyzyk

import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.kolkoikrzyzyk.database.DbUser
import com.example.kolkoikrzyzyk.database.UserDao
import com.example.kolkoikrzyzyk.model.User
import kotlin.concurrent.thread

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

//    suspend fun logoutUser(user: User) {
//        userDao.update(user.toDbUser(false))
//    }

    fun logoutUser(user: User) {
        thread {
            userDao.update(user.toDbUser(false))
        }
    }

//    suspend fun updateUser(user: User) {
//        userDao.update(user.toDbUser(true))
//    }

    fun updateUser(user: User) {
        thread {
            userDao.update(user.toDbUser(true))
        }
    }

    fun getAllUsers() =
        Transformations.map(userDao.getAllUsers()) { it.map { user -> user.toUser() } }

}