package com.technology.mycow.spargrisenkt.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.*
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.db.entity.User

class UserListAdapter internal constructor(context: Context): Adapter<UserListAdapter.UserListViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    //private var mUser: List<User>
    private var mUser = emptyList<User>()

    private var mUserPositionAt: User? = null
    lateinit var mHolderUserItemTouchedListener: HolderUserItemTouchedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val view = mLayoutInflater.inflate(R.layout.user_list_item, parent, false)
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        var user = mUser[position]
        if(user != null){
            holder.mUserName.text = user.userName
            holder.mRateOfPoint.text = user.rateOfPoint.toString()
            holder.userImageMap.forEach { userImageName ->
                if(userImageName.key == user.iconTag){
                    holder.mUserListItemIcon.setImageDrawable(userImageName.value)
                }
            }
        } else {
            holder.mUserName.text = "NO ENTRY"
        }
    }

    fun setUser(user: List<User>) {
        mUser = user
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        var userSize = 0
        if(mUser != null){
            userSize = mUser.size
        }
        return userSize
    }

    fun getUserPositionAt(): User? {
        return mUserPositionAt
    }

    fun setUserPositionAt(position: Int){
        mUserPositionAt = mUser[position]
    }

    fun removeUserCanceled(){
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        Log.d(LOG_MSG, "TOUCH: " + position)
        setUserPositionAt(position)
        mHolderUserItemTouchedListener.onHolderUserItemTouchedListener(mUserPositionAt!!)
    }

    fun setOnHolderUserItemTouchedListener(holderItemTouchedListener: HolderUserItemTouchedListener){
        mHolderUserItemTouchedListener = holderItemTouchedListener
    }

    inner class UserListViewHolder(itemView: View) : ViewHolder(itemView) {
        var mUserName: TextView
        var mRateOfPoint: TextView
        var mUserListItemIcon : ImageView
        var userImageMap = mutableMapOf<String, Drawable>()

        init {
            mUserName = itemView.findViewById(R.id.userListUserNameTv)
            mRateOfPoint = itemView.findViewById(R.id.userListRateTv)
            mUserListItemIcon = itemView.findViewById(R.id.userListItemIcon)

            userImageMap[itemView.resources.getString(R.string.userImage_1)] = itemView.resources.getDrawable(R.drawable.spagrisen_pink, null)
            userImageMap[itemView.resources.getString(R.string.userImage_2)] = itemView.resources.getDrawable(R.drawable.spagrisen_blue, null)
            userImageMap[itemView.resources.getString(R.string.userImage_3)] = itemView.resources.getDrawable(R.drawable.spagrisen_green, null)
            userImageMap[itemView.resources.getString(R.string.userImage_4)] = itemView.resources.getDrawable(R.drawable.spagrisen_red, null)
            userImageMap[itemView.resources.getString(R.string.userImage_5)] = itemView.resources.getDrawable(R.drawable.spagrisen_yellow, null)

//            itemView.setOnClickListener(this)
        }

//        override fun onClick(v: View?) {
//            Log.d(LOG_MSG, "TOUCH: " + adapterPosition)
//            setUserPositionAt(adapterPosition)
//            mHolderUserItemTouchedListener.onHolderUserItemTouchedListener(mUserPositionAt!!)
//        }

    }

    interface HolderUserItemTouchedListener {
        fun onHolderUserItemTouchedListener(userPositionAt: User)
    }

    companion object {
        private const val LOG_MSG = "User_List_Adapter_Log"
    }


}
