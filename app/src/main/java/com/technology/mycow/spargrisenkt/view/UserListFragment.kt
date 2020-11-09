package com.technology.mycow.spargrisenkt.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.adapter.UserListAdapter
import com.technology.mycow.spargrisenkt.db.entity.User
import com.technology.mycow.spargrisenkt.helper.SwipeToDeleteCallback
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import com.technology.mycow.spargrisenkt.viewmodel.TaskViewModel
import com.technology.mycow.spargrisenkt.viewmodel.UserViewModel

class UserListFragment : Fragment(), ActionPromptDialogFragment.ActionPromptDialogListener {

    //private var mUserListAdapter: UserListAdapter? = null
    private var mUserViewModel: UserViewModel? = null
    private var mTaskViewModel: TaskViewModel? = null
    private var mUser = mutableListOf<User>()

    private var mIsUserDeleteCanceled = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(mUserViewModel == null){
            val factory = UserViewModel.Factory(requireActivity().application)
            mUserViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        }
        if(mTaskViewModel == null){
            val factory = TaskViewModel.Factory(requireActivity().application)
            mTaskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)
        }

        //subscribeToViewModel()

    }

    private fun subscribeToViewModel() {
        val mUserListAdapter = UserListAdapter(requireContext())
        val mUserListRecyclerView = ((activity) as MainActivity).findViewById<RecyclerView>(R.id.listRecyclerView)
        mUserListRecyclerView.adapter = mUserListAdapter
        mUserListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(requireContext(), mUserListAdapter))
        itemTouchHelper.attachToRecyclerView(mUserListRecyclerView)

        if(mUserViewModel != null){
            mUserViewModel!!.user.observe(viewLifecycleOwner, Observer { createdUser ->
                Log.d(LOG_MSG, "USER CREATED")
                mUserListAdapter!!.setUser(createdUser)
                mUserListAdapter.setOnHolderUserItemTouchedListener(holderUserItemTouchListener())
            })
        }

        if(mIsUserDeleteCanceled != null){
            mIsUserDeleteCanceled.observe(viewLifecycleOwner, Observer { newValue ->
                mUserListAdapter.removeUserCanceled()
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToViewModel()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val LOG_MSG = "USER_LIST_FRAGMENT_LOG"

        val sInstance : Fragment
            get() = UserListFragment()
    }

    fun holderUserItemTouchListener(): UserListAdapter.HolderUserItemTouchedListener{
        return object: UserListAdapter.HolderUserItemTouchedListener {
            override fun onHolderUserItemTouchedListener(userPositionAt: User) {
                mUserViewModel?.mUserToDelete?.postValue(userPositionAt)
                showActionPromptDialog()
            }

        }
    }

    fun showActionPromptDialog() {
        // Create an instance of the dialog fragment and show it

        var mArgs = Bundle()
        mArgs.putInt(ConstantCollection.DIALOG_TITLE_ID, R.string.action_prompt_delete_user_title)
        mArgs.putInt(ConstantCollection.DIALOG_MESSAGE_ID, R.string.action_prompt_delete_user_message)
        val dialog = ActionPromptDialogFragment()
        dialog.arguments = mArgs
        dialog.setTargetFragment(this, 100)
        dialog.show(requireActivity().supportFragmentManager, "ActionPromptDialogFragmentByUserList")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        if(mUserViewModel?.mUserToDelete != null){
            mUserViewModel?.deleteUser(mUserViewModel!!.mUserToDelete!!.value!!.userId)
            mTaskViewModel?.deleteAllTaskByUser(mUserViewModel!!.mUserToDelete!!.value!!.userId)
            mIsUserDeleteCanceled.postValue(false)

        }
        Log.d(LOG_MSG, "POSITIVE")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.d(LOG_MSG, "NEGATIVE")
        mIsUserDeleteCanceled.postValue(true)
    }


}