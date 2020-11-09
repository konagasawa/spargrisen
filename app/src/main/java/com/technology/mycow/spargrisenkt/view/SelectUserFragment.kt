package com.technology.mycow.spargrisenkt.view

//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//import android.widget.Button
//import android.widget.TableLayout
//import android.widget.TableRow
//import androidx.fragment.app.Fragment
//import com.technology.mycow.spargrisenkt.AppExecutors
//import com.technology.mycow.spargrisenkt.R
//import com.technology.mycow.spargrisenkt.databinding.SelectUserFragmentBinding
//import com.technology.mycow.spargrisenkt.db.SpargrisenDB
//import com.technology.mycow.spargrisenkt.model.ConstantCollection
//import com.technology.mycow.spargrisenkt.model.SetArgumentsToFragment
//import com.technology.mycow.spargrisenkt.viewmodel.*
//
////THIS FRAGMENT IS NOT USED NOW
//class SelectUserFragment: Fragment() {
//
//    private var args = Bundle()
//    private var mBinding: SelectUserFragmentBinding? = null
//    private var mSpargrisenDB: SpargrisenDB? = null
//    private var menuItemId: Int? = null
//
//    internal lateinit var mCallback: OnFragmentChangedListener
////    private lateinit var mViewModelInterface: MainViewModelInterface
////    private val mViewModel: MainViewModel by activityViewModels()
//
//    private var btnArrayList = mutableListOf<Button>()
//    private val buttonArrayList = mutableListOf<Button>()
//    //private val drawableArrayList : MutableList<Drawable>? = null
//    private val drawableArrayList = mutableListOf<Drawable>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//
//        mSpargrisenDB = SpargrisenDB.getInstance(requireActivity().applicationContext, AppExecutors())
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        return inflater.inflate(R.layout.select_user_fragment, container, false)
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        btnArrayList = createUserButton()
//        createTableLayoutWithButton(btnArrayList)
//
//    }
//
//    private val argsMenuItemId: Int?
//        get() {
//            return arguments.let { arguments?.getInt(ConstantCollection.MENU_ITEM_ID) }
//        }
//
//
//    private fun createUserButton() : MutableList<Button> {
//
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_pink, null))
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_blue, null))
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_green, null))
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_red, null))
//        drawableArrayList.add(requireActivity().resources.getDrawable(R.drawable.spagrisen_yellow, null))
//
////        val viewId = this.requireView().id //return parent view id
////        val viewId = this.view?.id //return parent view id
////        var fragmentTag = this.view?.getTag() //return this fragment tag, that is "layout/select_user_fragment_0"
////        val viewId = this.id //return this fragment id
//
//        //var fragmentId: Int? = null
//        //fragmentId = mViewModel.getFragmentId() //This is by mainViewModel (from private val mViewModel: MainViewModel by activityViewModels())
//
//        val fragmentTag = this.tag //return this fragment tag, that is "SelectUserFragment"
//        mCallback.fragmentChanged(fragmentTag!!)
//        for (i in drawableArrayList.indices) {
//            val btn: Button = Button(requireActivity().applicationContext)
//            btn.background = drawableArrayList!![i]
//            btn.id = i
//
////            val constraintLayout: ConstraintLayout = requireActivity().findViewById(R.id.selectUserFragment)
////            val constraintSet = ConstraintSet()
////            constraintSet.clone(constraintLayout)
//
//            btn.setOnClickListener { view ->
//                var createUserFragment: Fragment = CreateUserFragment.sInstance
//                createUserFragment = SetArgumentsToFragment(createUserFragment, args, i.toString(), ConstantCollection.USER_BUTTON_TAG).setArguments()
//                createUserFragment = SetArgumentsToFragment(createUserFragment, args, fragmentTag, ConstantCollection.FRAGMENT_TAG).setArguments()
//
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.selectUserFragment, createUserFragment, "CreateUserFragment")
//                    .addToBackStack(null)
//                    .commit()
//            }
//            buttonArrayList!!.add(btn)
//        }
//        return buttonArrayList
//    }
//
//    private fun createTableLayoutWithButton(buttonArrayList: MutableList<Button>){
//
//
//        val tableLayout = requireActivity().findViewById<TableLayout>(R.id.createUserTableLayout)
//        val row = TableRow(requireActivity().application?.applicationContext)
//        row.layoutParams = TableRow.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//
//        val iterator = buttonArrayList.iterator();
//        for((index, value) in iterator.withIndex()!!){
//            tableLayout?.setColumnShrinkable(index, true)
//            row.addView(value as View)
//        }
//        tableLayout?.addView(row, TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.MATCH_PARENT))
//
//    }
//
//    fun setOnFragmentIdChangedListener(callback: OnFragmentChangedListener) {
//        mCallback = callback
//    }
//
//    companion object {
//        private const val LOG_MSG = "SELECT_USER_FRAG"
//
//        val sInstance: Fragment
//            get() = SelectUserFragment()
//    }
//}