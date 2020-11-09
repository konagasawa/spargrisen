package com.technology.mycow.spargrisenkt.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.technology.mycow.spargrisenkt.db.entity.Task
import com.technology.mycow.spargrisenkt.DataRepository
import com.technology.mycow.spargrisenkt.Spargrisen
import com.technology.mycow.spargrisenkt.db.entity.TaskAssignUpdate
import com.technology.mycow.spargrisenkt.db.entity.TaskCompleteUpdate
import com.technology.mycow.spargrisenkt.db.entity.TaskWithPointAndStatusAndUser
import kotlinx.coroutines.*
//import com.technology.mycow.spargrisenkt.db.entity.TaskList
//import com.technology.mycow.spargrisenkt.db.entity.TaskWithUser

class TaskViewModel(application: Application, dataRepository: DataRepository)
    : AndroidViewModel(application) {

    private val mDataRepository : DataRepository
    private val mTask : LiveData<List<Task>>
//    private val mTaskWithUser : LiveData<List<TaskWithUser>>

//    val buttonId : MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    init {
        mDataRepository = dataRepository
        mTask = mDataRepository.task
//        mTaskWithUser = mDataRepository.taskWithUserByIconId(100)

    }

    var mMonthButtonValue = MutableLiveData<Int>()
    var mMonthConstant = MutableLiveData<Int>()
    var totalPriceLiveData = MutableLiveData<Int>()
    var totalPriceToShowDelay = MutableLiveData<Int>()

    val isTaskCompleted = MutableLiveData<Boolean>()

    val mTaskStatus = MutableLiveData<String>()

    val mTaskPositionAt = MutableLiveData<Task>()

    val mIsTaskDeleteCanceled = MutableLiveData<Boolean>()
    val mTaskToDelete = MutableLiveData<Task>()


    val task: LiveData<List<Task>>
        get() = mDataRepository.task

    fun taskWithSortedByStatus(taskStatus: String): LiveData<List<Task>>{
        return mDataRepository.taskWithSortedByStatus(taskStatus)
    }

//    val taskWithUnassigned: LiveData<List<Task>>
//        get() = mDataRepository.taskWithUnassigned
//
//    fun setTaskWithUnassigned(taskStatus: String){
//        mDataRepository.setTaskWithUnassigned(taskStatus)
//    }

    fun getTaskByUserId(userId: Long): LiveData<List<Task>> {
        return mDataRepository.getTaskByUser(userId)
    }

    fun getTaskByUserSortedByStatus(userId: Long, taskStatus: String): LiveData<List<Task>>{
        return mDataRepository.getTaskByUserSortedByStatus(userId, taskStatus)
    }

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        mDataRepository.insertTask(task)
    }

    fun updateTaskToAssign(taskAssignUpdate: TaskAssignUpdate) = viewModelScope.launch(Dispatchers.IO){
        mDataRepository.updateTaskToAssign(taskAssignUpdate)
    }

    fun updateTaskToComplete(taskCompleteUpdate: TaskCompleteUpdate) = viewModelScope.launch(Dispatchers.IO){
        mDataRepository.updateTaskToComplete(taskCompleteUpdate)
    }

    suspend fun getPointOfTask(userId: Long, startDate: Long, endDate: Long): List<TaskWithPointAndStatusAndUser> = withContext(Dispatchers.IO){
            mDataRepository.getPointOfTask(userId, startDate, endDate)
        }

    fun setMonthButtonState(month: Int){
        mMonthButtonValue.postValue(month)
    }

    val monthButtonValue: MutableLiveData<Int>
        get() = mMonthButtonValue

    fun setMonthConstant(monthConstant: Int){
        mMonthConstant.postValue(monthConstant)
    }

    val monthConstant: MutableLiveData<Int>
        get() = mMonthConstant

    fun deleteTask(taskId: Long) = viewModelScope.launch(Dispatchers.IO){
        mDataRepository.deleteTask(taskId)
    }

    fun deleteAllTaskByUser(userId: Long) = viewModelScope.launch(Dispatchers.IO){
        mDataRepository.deleteAllTaskByUser(userId)
    }

        //var point = viewModelScope.launch(Dispatchers.IO) { mDataRepository.getPointOfTask(iconId, startDate, endDate)}
        //return point
//    }
//    fun getPointOfTask(iconId: Long, startDate: Long, endDate: Long): List<Task> {
//        return mDataRepository.getPointOfTask(iconId, startDate, endDate)
//    }
//    fun getPointOfTask(iconId: Int, startDate: Long, endDate: Long): List<Int> {
//        var pointByUser: List<Int> = viewModelScope.launch(Dispatchers.IO){ mDataRepository.getPointOfTask(iconId, startDate, endDate) }
//        return pointByUser
//    }



//    fun taskWithIconId(iconId: Int): LiveData<List<TaskWithUser>> {
//        return mDataRepository.taskWithUserByIconId(iconId)
//    }

//    fun getObjTaskWithIconId() : LiveData<List<TaskWithUser>> {
//        return mTaskWithUser
//    }

//    fun insertTaskList(taskList: TaskList) = viewModelScope.launch(Dispatchers.IO){
//        mDataRepository.insertTaskList(taskList)
//    }

    class Factory(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory(){
        private val mDataRepository: DataRepository

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TaskViewModel(mApplication, mDataRepository) as T
        }

        init {
            mDataRepository = (mApplication as Spargrisen).getDataRepository()!!
            Log.d(LOG_MSG, "TASK VIEWMODEL INITIALIZED")
        }
    }

    companion object {
        private const val LOG_MSG = "TASK_VIEWMODEL"
    }

}

