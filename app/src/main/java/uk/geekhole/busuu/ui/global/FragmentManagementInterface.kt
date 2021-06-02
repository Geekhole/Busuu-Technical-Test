package uk.geekhole.busuu.ui.global

import androidx.fragment.app.Fragment

interface FragmentManagementInterface {

    /**
     *
     * Show the passed in fragment.
     * @param fragment the fragment to be shown
     * @param isRoot if true this will remove all fragments from the backstack before setting the newly passed in fragment - the new fragment will not be added to the backstack.
     * */
    fun showFragment(fragment: Fragment, isRoot: Boolean)
    fun removeAllBackstack()
}