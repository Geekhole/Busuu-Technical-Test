package uk.geekhole.busuu.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.plugins.RxJavaPlugins
import uk.geekhole.busuu.R
import uk.geekhole.busuu.global.RxThrowable
import uk.geekhole.busuu.ui.countries.CountriesListFragment
import uk.geekhole.busuu.ui.global.FragmentManagementInterface

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentManagementInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(CountriesListFragment.newInstance(), true)

        RxJavaPlugins.setErrorHandler {
            val message = if (it is RxThrowable) {
                getString(it.messageId)
            } else {
                it.message ?: getString(R.string.generic_error)
            }

            Snackbar.make(findViewById(R.id.fragment_container), message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun showFragment(fragment: Fragment, isRoot: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        if (!isRoot) {
            transaction.add(R.id.fragment_container, fragment, fragment.javaClass.canonicalName)
            transaction.addToBackStack(fragment.javaClass.canonicalName)

        } else {
            transaction.replace(R.id.fragment_container, fragment, fragment.javaClass.canonicalName)
            removeAllBackstack()
        }

        transaction.commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }

    override fun removeAllBackstack() {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}