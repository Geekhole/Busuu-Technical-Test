package uk.geekhole.busuu.global

import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

object FragmentArgumentDelegate {

    operator fun <T : Any> getValue(thisRef: Fragment, property: KProperty<*>): T {
        val args = thisRef.arguments
            ?: throw IllegalStateException("Cannot read property ${property.name} if no arguments have been set")
        @Suppress("UNCHECKED_CAST")
        return args.get(property.name) as T? ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    operator fun <T : Any> setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        var args = thisRef.arguments
        if (args == null) {
            args = Bundle()
            thisRef.arguments = args
        }
        val key = property.name

        @Suppress("UNCHECKED_CAST")
        when (value) {
            is String -> args.putString(key, value)
            is ByteArray -> args.putByteArray(key, value)
            is CharArray -> args.putCharArray(key, value)
            is CharSequence -> args.putCharSequence(key, value)
            is Bundle -> args.putBundle(key, value)
            is Binder -> BundleCompat.putBinder(args, key, value)
            is Parcelable -> args.putParcelable(key, value)
            is Array<*> -> when {
                value.isArrayOf<String>() -> args.putStringArray(key, value as Array<String>)
                else -> throw IllegalStateException("Type array ${value.javaClass.canonicalName} of property ${property.name} is not supported")
            }
            is java.io.Serializable -> args.putSerializable(key, value)
            else -> throw IllegalStateException("Type ${value.javaClass.canonicalName} of property ${property.name} is not supported")
        }
    }
}