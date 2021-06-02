package uk.geekhole.busuu.ui.global

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment(layoutId) {

    lateinit var fragmentManager: FragmentManagementInterface
    val disposables = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is FragmentManagementInterface) throw IllegalArgumentException("Nuh-uh you need to attach this to an instance of FragmentManagementInterface!")

        fragmentManager = context
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.dispose()
        disposables.clear()
    }

}