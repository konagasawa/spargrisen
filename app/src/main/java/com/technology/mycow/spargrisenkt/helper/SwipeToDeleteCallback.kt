package com.technology.mycow.spargrisenkt.helper

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.adapter.TaskListAdapter
import com.technology.mycow.spargrisenkt.adapter.UserListAdapter

class SwipeToDeleteCallback constructor(context: Context, adapter: Any, dragDirs: Int = 0, swipeDirs: Int = (ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT )) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private var mUserListAdapter: UserListAdapter? = null
    private var mTaskListAdapter: TaskListAdapter? = null
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)
    private val colors = intArrayOf(Color.parseColor("#FFB33448"), Color.parseColor("#FFB33448"))
    private val background = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)
    private val baseIconWidth = deleteIcon?.intrinsicWidth
    private val baseIconHeight = deleteIcon?.intrinsicHeight
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


    //TODO
//    private val deleteLeftIcon =  ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)
//    private val baseLeftIconWidth = deleteLeftIcon?.intrinsicWidth
//    private val baseLeftIconHeight = deleteLeftIcon?.intrinsicHeight

    init {
        if(adapter is UserListAdapter ){
            mUserListAdapter = adapter
        } else if (adapter is TaskListAdapter){
            mTaskListAdapter = adapter
        }
    }

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        Log.d(LOG_MSG, "MOVE MAY NEED TO IMPLEMENT")
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var position = viewHolder.adapterPosition

        mUserListAdapter?.let {
            (mUserListAdapter as UserListAdapter).deleteItem(position) }
        mTaskListAdapter?.let {
            (mTaskListAdapter as TaskListAdapter).deleteItem(position)
        }
    }


    override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if(isCanceled){
            //clearCanvas(c, itemView.left + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            clearCanvas(c, itemView.left.toFloat(), itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        background.cornerRadius = 20F
        //background.setBounds(itemView.left + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
        background.draw(c)


        if(dX > 1f){
            //SWIPE TO RIGHT, SHOW ICON LEFT
            val deleteLeftIconTop = itemView.top + (itemHeight - baseIconHeight!!) / 2
            val deleteLeftIconMargin = (itemView.height - baseIconHeight) / 2
            val deleteLeftIconRight = itemView.left + deleteLeftIconMargin + baseIconWidth!!
            val deleteLeftIconLeft = itemView.left + deleteLeftIconMargin
            val deleteLeftIconBottom = deleteLeftIconTop + baseIconHeight
            deleteIcon?.setBounds(deleteLeftIconLeft, deleteLeftIconTop, deleteLeftIconRight, deleteLeftIconBottom)
            deleteIcon?.draw(c)
        } else {
            //SWIPE TO LEFT, SHOW ICON RIGHT
            val deleteIconTop = itemView.top + (itemHeight - baseIconHeight!!) / 2
            val deleteIconMargin = (itemView.height - baseIconHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - baseIconWidth!!
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + baseIconHeight
            deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon?.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float){
        c.drawRect(left, top, right, bottom, clearPaint)
    }

    companion object {
        private const val LOG_MSG = "Swipe_To_Delete_Callback_Helper"
    }


}