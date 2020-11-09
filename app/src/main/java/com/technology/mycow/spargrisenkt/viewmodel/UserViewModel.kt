package com.technology.mycow.spargrisenkt.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.technology.mycow.spargrisenkt.DataRepository
import com.technology.mycow.spargrisenkt.Spargrisen
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.db.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application, dataRepository: DataRepository)
    : AndroidViewModel(application) {

    private val mDataRepository: DataRepository
    private val mUser: LiveData<List<User>>
    private var mCurrentFragmentId = MutableLiveData<Int>()
    private var mUserByUserId: LiveData<User>

    val mUserToDelete = MutableLiveData<User>()

    init{
        mDataRepository = dataRepository
        mUser = mDataRepository.user

        mUserByUserId = mDataRepository.userByUserId
    }

    val mUserValue = user.value

    val userIdByUseIcon : MutableLiveData<Long> by lazy { MutableLiveData<Long>() }

    val imageViewIconTagLiveData : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val imageViewPlaceHolderIconUserNameLiveData : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val countActiveUsers: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val user: LiveData<List<User>>
        get() = mDataRepository.user

    fun setUserByUserId(userId: Long){
        mDataRepository.setUserByUserId(userId)
    }

    val userByUserId: LiveData<User>
        get() = mDataRepository.userByUserId

    fun setCurrentFragmentId(id: MutableLiveData<Int>){
        mCurrentFragmentId = id
    }

    val currentFragmentId: LiveData<Int>
        get() = mCurrentFragmentId

    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO){
        mDataRepository.insertUser(user)
    }

    fun deleteUser(userId: Long) = viewModelScope.launch(Dispatchers.IO){
        mDataRepository.deleteUser(userId)
    }


    class Factory(private val mApplication: Application): ViewModelProvider.NewInstanceFactory(){
        private val mDataRepository: DataRepository

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(mApplication, mDataRepository) as T
        }

        init {
            mDataRepository = (mApplication as Spargrisen).getDataRepository()!!
            Log.d(LOG_MSG, "USER_VIEWMODEL INITIALIZED")
        }
    }

    companion object {
        private const val LOG_MSG = "USER_VIEWMODEL"
    }

}