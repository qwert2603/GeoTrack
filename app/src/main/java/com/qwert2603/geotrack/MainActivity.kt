package com.qwert2603.geotrack

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val vm: MainActivityViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1
        )

        vm.geoInfoDatabase.getInfoDao().observeLastGeoInfo().observe(this, Observer {
            if (it != null) {
                lastInfo_TextView.text = "${it.millis}\n${it.lat}\n${it.lon}"
            } else {
                lastInfo_TextView.text = "null"
            }
        })

        vm.geoInfoDatabase.getInfoDao().observeGeoInfosCount().observe(this, Observer {
            pointsCount_TextView.text = "Total: $it"
        })
        vm.accuracy.observe(this, Observer {
            accuracyCount_TextView.text = "Accuracy: $it"
        })

        vm.started.observe(this, Observer {
            start_Button.isVisible = !it
            stop_Button.isVisible = it
        })

        start_Button.setOnClickListener { vm.start() }
        stop_Button.setOnClickListener { vm.stop() }
        send_Button.setOnClickListener {
            lifecycleScope.launch {
                val text = vm.geoInfoDatabase.getInfoDao().getAllGeoInfos()
                    .joinToString(separator = "\n") {
                        "%d;%.8f;%.8f".format(
                            it.millis,
                            it.lat,
                            it.lon
                        )
                    }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, text)
                startActivity(Intent.createChooser(intent, "share title"))
            }
        }
        clear_Button.setOnClickListener {
            lifecycleScope.launch { vm.geoInfoDatabase.getInfoDao().deleteAllGeoInfos() }
        }
    }
}