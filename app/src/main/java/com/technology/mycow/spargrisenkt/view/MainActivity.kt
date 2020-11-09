package com.technology.mycow.spargrisenkt.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.technology.mycow.spargrisenkt.R
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.IntentCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.technology.mycow.spargrisenkt.databinding.ActivityMainBinding
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import com.technology.mycow.spargrisenkt.viewmodel.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.create_user_fragment.*

class MainActivity : AppCompatActivity(), OnFragmentChangedListener {

    var mBinding: ActivityMainBinding? = null
    var mSharedViewModel: SharedViewModel? = null
    var args: Bundle? = null

    private lateinit var contentMainImage: ImageView
    private lateinit var contentMainText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.hide();

        hideSystemUI()

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if(mSharedViewModel == null){
            mSharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        }


        createDrawerLayout()
        createNavView()
        createContentMain()
        softKeyboardHide()

    }

    private fun setArgumentsToFragment(fragment: Fragment, menuItemId: Int): Fragment{
        args.let {
            args = Bundle()
            args!!.putInt(ConstantCollection.MENU_ITEM_ID, menuItemId)
            fragment.arguments = args
        }
        return fragment
    }

    private fun createContentMain(){
        contentMainImage = findViewById(R.id.contentMainImage)
        contentMainImage.setImageResource(R.drawable.spargrisen_darkpink_purpleback_icon)
        contentMainImage.alpha = 0.5F

        contentMainText = findViewById(R.id.contentMainTv)
        contentMainText.setTextColor(getColor(R.color.colorContentMainText))
        contentMainText.text = resources.getString(R.string.contentMainText)

    }

    private fun createDrawerLayout(){

        //Font Awesome
        //val contentMainTv: TextView = findViewById(R.id.fontCreateChild)
        //contentMainTv.text = resources.getString(R.string.menuFontCreateUser)

        val toolbar: Toolbar = findViewById(R.id.toolBar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val toogle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.opendrawer, R.string.closedrawer)
        drawerLayout.let { drawerLayout.addDrawerListener(toogle) }
        toogle.syncState()

    }

    private fun createNavView(){

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener { item ->
            val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
            val fragmentManager = supportFragmentManager


            //TO DELETE FRAGMENT IN BACKSTACK
            var count = fragmentManager.backStackEntryCount
            for (num in 0..count){
                fragmentManager.popBackStack()
            }

            when (item.itemId) {
                R.id.menuCreateUser -> {
                    var createUserFragment: Fragment = CreateUserFragment.sInstance
                    createUserFragment = setArgumentsToFragment(createUserFragment, item.itemId) //Button ID
                    Log.d(LOG_MAIN, "SELECT USER TO CREATE")
                    fragmentManager.beginTransaction().replace(R.id.contentMain, createUserFragment, ConstantCollection.FRAGMENT_TAG_CREATE_USER)
                        .addToBackStack(resources.getString(R.string.contentMainTag))
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menuCreateTask -> {
                    var createTaskFragment: Fragment = CreateTaskFragment.sInstance
                    createTaskFragment = setArgumentsToFragment(createTaskFragment, item.itemId) // Button ID
                    Log.d(LOG_MAIN, "SELECT TASK TO CREATE")
                    fragmentManager.beginTransaction().replace(R.id.contentMain, createTaskFragment, ConstantCollection.FRAGMENT_TAG_CREATE_TASK)
                        .addToBackStack(resources.getString(R.string.contentMainTag))
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menuAssignTask -> {
                    var assignTaskFragment: Fragment = AssignTaskFragment.sInstance
                    assignTaskFragment = setArgumentsToFragment(assignTaskFragment, item.itemId)
                    Log.d(LOG_MAIN, "TASK ASSIGN")
                    fragmentManager.beginTransaction().replace(R.id.contentMain, assignTaskFragment, ConstantCollection.FRAGMENT_TAG_ASSIGN_TASK)
                        .addToBackStack(resources.getString(R.string.contentMainTag))
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menuCalculatePoint -> {
                    var calculatePointFragment: Fragment = CalculatePointFragment.sInstance
                    calculatePointFragment = setArgumentsToFragment(calculatePointFragment, item.itemId)
                    Log.d(LOG_MAIN, "CALCULATE POINT")
                    fragmentManager.beginTransaction().replace(R.id.contentMain, calculatePointFragment, ConstantCollection.FRAGMENT_TAG_CALCULATE_POINT)
                        .addToBackStack(resources.getString(R.string.contentMainTag))
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menuUserList -> {
                    var userListFragment: Fragment = UserListFragment.sInstance
                    var fragmentTag = mSharedViewModel?.getFragmentTag()
                    var tempFragment = fragmentManager.findFragmentByTag(fragmentTag)
                    var temp = tempFragment?.id
                    userListFragment = setArgumentsToFragment(userListFragment, item.itemId)
                    Log.d(LOG_MAIN, "USER LIST")
                    fragmentManager.beginTransaction().replace(R.id.contentMain, userListFragment, ConstantCollection.FRAGMENT_TAG_USER_LIST)
                        .addToBackStack(resources.getString(R.string.contentMainTag))
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menuTaskList -> {
                    var taskListFragment : Fragment = TaskListFragment.sInstance
                    taskListFragment = setArgumentsToFragment(taskListFragment, item.itemId)
                    Log.d(LOG_MAIN, "TASK LIST")
                    fragmentManager.beginTransaction().replace(R.id.contentMain, taskListFragment, ConstantCollection.FRAGMENT_TAG_TASK_LIST)
                        .addToBackStack(resources.getString(R.string.contentMainTag))
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menuShowOngoing -> {
                    var taskListByUserFragment: Fragment = TaskListByUserFragment.sInstance
                    taskListByUserFragment = setArgumentsToFragment(taskListByUserFragment, item.itemId)
                    Log.d(LOG_MAIN, "TASK LIST BY USER")
                    fragmentManager.beginTransaction().replace(R.id.contentMain, taskListByUserFragment, ConstantCollection.FRAGMENT_TAG_TASK_LIST_BY_USER)
                        .addToBackStack(resources.getString(R.string.contentMainTag))
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    Log.d(LOG_MAIN, "MISSED MENU" + item.itemId)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
            }
        }
    }

    private fun hideSystemUI(){

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun softKeyboardHide(){

        val navView: View = findViewById<NavigationView>(R.id.nav_view)
        navView?.setOnTouchListener { v: View, event: MotionEvent ->
            val imm = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(navView != null){
                imm.hideSoftInputFromWindow(navView.windowToken, 0)
            }
        true
        }


    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

//    //This is called from SelectUserFragment
//    override fun onAttachFragment(fragment: Fragment){
//        if(fragment is SelectUserFragment){
//            fragment.setOnFragmentIdChangedListener(this)
//        }
//    }

    override fun fragmentChanged(fragmentTag: String) {
        mSharedViewModel?.setFragmentTag(fragmentTag)
    }

    companion object {
        private const val LOG_MAIN = "MAIN_LOG"
    }


}