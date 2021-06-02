package uk.geekhole.busuu.ui.global

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.ImageView
import com.caverock.androidsvg.SVG
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import uk.geekhole.busuu.R
import uk.geekhole.busuu.models.database.Country
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.ceil


object ImageHelper {

    private val downloading = mutableListOf<String>()

    fun loadImage(country: Country, view: ImageView) {
        val imageDir = view.context.filesDir.absolutePath + "/flags/"

        // Make sure the directory exists for saving images
        File(imageDir).mkdirs()

        view.setImageResource(R.drawable.icn_img_placeholder)

        val image = File(imageDir + country.name)
        if (image.exists()) {
            val bitmap = BitmapFactory.decodeFile(image.absolutePath)
            view.setImageBitmap(bitmap)
        } else if (!downloading.contains(country.name)) {
            // Don't try downloading if we are already trying.

            Completable.fromAction {
                downloadImage(country.name, country.flag, view)
            }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe()
        }
    }

    private fun downloadImage(id: String, url: String, view: ImageView) {
        val imageDir = view.context.filesDir.absolutePath + "/flags/"

        downloading.add(id)
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                downloading.remove(id)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.byteStream()?.let {
                    val bitmap = svgToBitmap(it) ?: return@let

                    Observable.just(bitmap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            if (view.tag == id) view.setImageBitmap(bitmap) // So we don't set it after we've already scrolled to a new row
                        }
                        .observeOn(Schedulers.io())
                        .doOnNext {
                            FileOutputStream(imageDir + id).use { out ->
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                            }
                        }.doOnComplete {
                            downloading.remove(id)
                        }
                        .subscribe()
                }

                downloading.remove(id)
            }
        })
    }

    private fun svgToBitmap(inputStream: InputStream): Bitmap? {
        try {
            val svg = SVG.getFromInputStream(inputStream)

            val bmp = if (svg.documentWidth != -1f) {
                Bitmap.createBitmap(
                    ceil(svg.documentWidth.toDouble()).toInt(),
                    ceil(svg.documentHeight.toDouble()).toInt(),
                    Bitmap.Config.ARGB_8888
                )
            } else {
                null
            }

            if (bmp == null) return null

            val canvas = Canvas(bmp)
            svg.renderToCanvas(canvas)

            return bmp
        } catch (e: Exception) {
            return null
        }
    }
}