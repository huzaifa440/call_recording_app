// package com.example.call_recording_app


// import android.Manifest
// import android.content.pm.PackageManager
// import android.os.Bundle
// import android.provider.CallLog
// import androidx.core.app.ActivityCompat
// import androidx.core.content.ContextCompat
// import io.flutter.embedding.android.FlutterActivity
// import io.flutter.embedding.engine.FlutterEngine
// import io.flutter.plugin.common.MethodChannel
// import android.content.BroadcastReceiver
// import android.content.Intent
// import android.content.IntentFilter
// import android.os.Bundle
// import android.provider.CallLog
// import android.telephony.TelephonyManager
// import androidx.core.content.ContextCompat
// import io.flutter.embedding.engine.FlutterEngine
// import io.flutter.plugin.common.MethodChannel
// import java.io.File
// import java.text.SimpleDateFormat
// import java.util.Date
// import java.util.Locale
// import android.media.MediaRecorder
// import java.io.File
// import android.telephony.TelephonyManager
// import java.text.SimpleDateFormat




// class MainActivity : FlutterActivity() {

//     private val CHANNEL = "com.example.call_recording_app/recording"
//     private val CALL_LOG_CHANNEL = "com.example.call_recording_app/call_logs"
//     private var recorder: MediaRecorder? = null
//     private var recordingFile: File? = null

//     override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
//         super.configureFlutterEngine(flutterEngine)

//         MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
//             when (call.method) {
//                 "startRecording" -> {
//                     val fileName = call.argument<String>("fileName")
//                     if (fileName != null) {
//                         startRecording(fileName)
//                         result.success(null)
//                     } else {
//                         result.error("INVALID_ARGUMENT", "File name is null", null)
//                     }
//                 }
//                 "stopRecording" -> {
//                     stopRecording()
//                     result.success(null)
//                 }
//                 else -> {
//                     result.notImplemented()
//                 }
//             }
//         }

//         MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CALL_LOG_CHANNEL).setMethodCallHandler { call, result ->
//             when (call.method) {
//                 "getCallLogs" -> {
//                     val callLogs = getCallLogs()
//                     result.success(callLogs)
//                 }
//                 else -> {
//                     result.notImplemented()
//                 }
//             }
//         }

//         requestPermissions()
//         registerCallReceiver()
//     }

//     private fun startRecording(fileName: String) {
//         if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//             ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
//             return
//         }

//         recordingFile = File(getExternalFilesDir(null), fileName)
//         recorder = MediaRecorder().apply {
//             setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
//             setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//             setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//             setOutputFile(recordingFile?.absolutePath)
//             try {
//                 prepare()
//                 start()
//             } catch (e: Exception) {
//                 e.printStackTrace()
//             }
//         }
//     }

//     private fun stopRecording() {
//         recorder?.apply {
//             try {
//                 stop()
//                 release()
//             } catch (e: Exception) {
//                 e.printStackTrace()
//             }
//         }
//         recorder = null
//     }

//     private val callReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//         override fun onReceive(context: Context, intent: Intent) {
//             val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
//             val binaryMessenger = flutterEngine?.dartExecutor?.binaryMessenger
//             if (binaryMessenger != null) {
//                 when (state) {
//                     TelephonyManager.EXTRA_STATE_RINGING -> {
//                         val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
//                         val fileName = "IN_${incomingNumber}_${getCurrentDate()}.3gp"
//                         MethodChannel(binaryMessenger, CHANNEL).invokeMethod("startRecording", mapOf("fileName" to fileName))
//                     }
//                     TelephonyManager.EXTRA_STATE_OFFHOOK -> {
//                         val outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
//                         val fileName = "OUT_${outgoingNumber}_${getCurrentDate()}.3gp"
//                         MethodChannel(binaryMessenger, CHANNEL).invokeMethod("startRecording", mapOf("fileName" to fileName))
//                     }
//                     TelephonyManager.EXTRA_STATE_IDLE -> {
//                         MethodChannel(binaryMessenger, CHANNEL).invokeMethod("stopRecording", null)
//                     }
//                 }
//             }
//         }
//     }

//     private fun registerCallReceiver() {
//         val filter = IntentFilter()
//         filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
//         filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL)
//         registerReceiver(callReceiver, filter)
//     }

//     private fun getCurrentDate(): String {
//         val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
//         return sdf.format(Date())
//     }

//     private fun requestPermissions() {
//         val permissions = arrayOf(
//             Manifest.permission.RECORD_AUDIO,
//             Manifest.permission.READ_PHONE_STATE,
//             Manifest.permission.PROCESS_OUTGOING_CALLS,
//             Manifest.permission.WRITE_EXTERNAL_STORAGE,
//             Manifest.permission.READ_EXTERNAL_STORAGE,
//             Manifest.permission.READ_CALL_LOG,
//             Manifest.permission.WRITE_CALL_LOG
//         )
//         val allPermissionsGranted = permissions.all {
//             ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
//         }

//         if (!allPermissionsGranted) {
//             ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
//         }
//     }

//     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//         if (requestCode == PERMISSION_REQUEST_CODE) {
//             if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                 // All permissions are granted
//             } else {
//                 // Handle the case where permissions are not granted
//             }
//         }
//     }

//     private fun getCallLogs(): List<Map<String, String>> {
//         val callLogs = mutableListOf<Map<String, String>>()
//         if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
//             val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC")
//             cursor?.use {
//                 while (it.moveToNext()) {
//                     val phoneNumber = it.getString(it.getColumnIndex(CallLog.Calls.NUMBER))
//                     val contactName = it.getString(it.getColumnIndex(CallLog.Calls.CACHED_NAME)) ?: "Unknown"
//                     val date = it.getString(it.getColumnIndex(CallLog.Calls.DATE))
//                     val type = when (it.getInt(it.getColumnIndex(CallLog.Calls.TYPE))) {
//                         CallLog.Calls.INCOMING_TYPE -> "incoming"
//                         CallLog.Calls.OUTGOING_TYPE -> "outgoing"
//                         CallLog.Calls.MISSED_TYPE -> "missed"
//                         else -> "unknown"
//                     }
//                     callLogs.add(mapOf("phoneNumber" to phoneNumber, "contactName" to contactName, "date" to date, "type" to type))
//                 }
//             }
//         }
//         return callLogs
//     }

//     companion object {
//         private const val RECORD_AUDIO_REQUEST_CODE = 123
//         private const val PERMISSION_REQUEST_CODE = 456
//     }
// }















package com.example.call_recording_app

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : FlutterActivity() {

    private val CHANNEL = "com.example.call_recording_app/recording"
    private var recorder: MediaRecorder? = null
    private var recordingFile: File? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "startRecording" -> {
                    val fileName = call.argument<String>("fileName")
                    if (fileName != null) {
                        startRecording(fileName)
                        result.success(null)
                    } else {
                        result.error("INVALID_ARGUMENT", "File name is null", null)
                    }
                }
                "stopRecording" -> {
                    stopRecording()
                    result.success(null)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }

        requestPermissions()
        registerCallReceiver()
    }

    private fun startRecording(fileName: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
            return
        }

        recordingFile = File(getExternalFilesDir(null), fileName)
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFile?.absolutePath)
            try {
                prepare()
                start()
            } catch (e: Exception) {
                // Handle recording start error
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            try {
                stop()
                release()
            } catch (e: Exception) {
                // Handle recording stop error
                e.printStackTrace()
            }
        }
        recorder = null
    }

    private val callReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val binaryMessenger = flutterEngine?.dartExecutor?.binaryMessenger
            if (binaryMessenger != null) {
                when (state) {
                    TelephonyManager.EXTRA_STATE_RINGING -> {
                        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                        val fileName = "IN_${incomingNumber}_${getCurrentDate()}.3gp"
                        MethodChannel(binaryMessenger, CHANNEL)
                            .invokeMethod("startRecording", mapOf("fileName" to fileName))
                    }
                    TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                        val outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
                        val fileName = "OUT_${outgoingNumber}_${getCurrentDate()}.3gp"
                        MethodChannel(binaryMessenger, CHANNEL)
                            .invokeMethod("startRecording", mapOf("fileName" to fileName))
                    }
                    TelephonyManager.EXTRA_STATE_IDLE -> {
                        MethodChannel(binaryMessenger, CHANNEL)
                            .invokeMethod("stopRecording", null)
                    }
                }
            }
        }
    }

    private fun registerCallReceiver() {
        val filter = IntentFilter()
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL)
        registerReceiver(callReceiver, filter)
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG
        )
        val allPermissionsGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, 
        permissions: Array<out String>, 
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions are granted
            } else {
                // Handle the case where permissions are not granted
            }
        }
    }

    companion object {
        private const val RECORD_AUDIO_REQUEST_CODE = 123
        private const val PERMISSION_REQUEST_CODE = 456
    }
}













// package com.example.call_recording_app

// import android.Manifest
// import android.content.BroadcastReceiver
// import android.content.Context
// import android.content.Intent
// import android.content.IntentFilter
// import android.content.pm.PackageManager
// import android.media.MediaRecorder
// import android.os.Bundle
// import android.telephony.TelephonyManager
// import androidx.core.app.ActivityCompat
// import androidx.core.content.ContextCompat
// import io.flutter.embedding.android.FlutterActivity
// import io.flutter.embedding.engine.FlutterEngine
// import io.flutter.plugin.common.MethodChannel
// import java.io.File
// import java.text.SimpleDateFormat
// import java.util.Date
// import java.util.Locale

// class MainActivity : FlutterActivity() {

//     private val CHANNEL = "com.example.call_recording_app/recording"
//     private var recorder: MediaRecorder? = null
//     private var recordingFile: File? = null

//     override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
//         super.configureFlutterEngine(flutterEngine)

//         MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
//             when (call.method) {
//                 "startRecording" -> {
//                     val fileName = call.argument<String>("fileName")
//                     if (fileName != null) {
//                         startRecording(fileName)
//                         result.success(null)
//                     } else {
//                         result.error("INVALID_ARGUMENT", "File name is null", null)
//                     }
//                 }
//                 "stopRecording" -> {
//                     stopRecording()
//                     result.success(null)
//                 }
//                 else -> {
//                     result.notImplemented()
//                 }
//             }
//         }

//         requestPermissions()
//         registerCallReceiver(flutterEngine)
//     }

//     private fun startRecording(fileName: String) {
//         if (ContextCompat.checkSelfPermission(
//                 this,
//                 Manifest.permission.RECORD_AUDIO
//             ) != PackageManager.PERMISSION_GRANTED
//         ) {
//             ActivityCompat.requestPermissions(
//                 this,
//                 arrayOf(Manifest.permission.RECORD_AUDIO),
//                 RECORD_AUDIO_REQUEST_CODE
//             )
//             return
//         }

//         recordingFile = File(getExternalFilesDir(null), fileName)
//         recorder = MediaRecorder().apply {
//             setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
//             setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//             setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//             setOutputFile(recordingFile?.absolutePath)
//             try {
//                 prepare()
//                 start()
//             } catch (e: Exception) {
//                 // Handle recording start error
//                 e.printStackTrace()
//             }
//         }
//     }

//     private fun stopRecording() {
//         recorder?.apply {
//             try {
//                 stop()
//                 release()
//             } catch (e: Exception) {
//                 // Handle recording stop error
//                 e.printStackTrace()
//             }
//         }
//         recorder = null
//     }

//     private val callReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//         override fun onReceive(context: Context, intent: Intent) {
//             val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
//             val binaryMessenger = flutterEngine?.dartExecutor?.binaryMessenger
//             if (binaryMessenger != null) {
//                 when (state) {
//                     TelephonyManager.EXTRA_STATE_RINGING -> {
//                         val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
//                         val fileName = "IN_${incomingNumber}_${getCurrentDate()}.3gp"
//                         MethodChannel(binaryMessenger, CHANNEL)
//                             .invokeMethod("startRecording", mapOf("fileName" to fileName))
//                     }
//                     TelephonyManager.EXTRA_STATE_OFFHOOK -> {
//                         val outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
//                         val fileName = "OUT_${outgoingNumber}_${getCurrentDate()}.3gp"
//                         MethodChannel(binaryMessenger, CHANNEL)
//                             .invokeMethod("startRecording", mapOf("fileName" to fileName))
//                     }
//                     TelephonyManager.EXTRA_STATE_IDLE -> {
//                         MethodChannel(binaryMessenger, CHANNEL)
//                             .invokeMethod("stopRecording", null)
//                     }
//                 }
//             }
//         }
//     }

//     private fun registerCallReceiver(flutterEngine: FlutterEngine) {
//         val filter = IntentFilter()
//         filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
//         filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL)
//         registerReceiver(callReceiver, filter)
//     }

//     private fun getCurrentDate(): String {
//         val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
//         return sdf.format(Date())
//     }

//     private fun requestPermissions() {
//         val permissions = arrayOf(
//             Manifest.permission.RECORD_AUDIO,
//             Manifest.permission.READ_PHONE_STATE,
//             Manifest.permission.PROCESS_OUTGOING_CALLS,
//             Manifest.permission.WRITE_EXTERNAL_STORAGE,
//             Manifest.permission.READ_EXTERNAL_STORAGE,
//             Manifest.permission.READ_CALL_LOG,
//             Manifest.permission.WRITE_CALL_LOG
//         )
//         val allPermissionsGranted = permissions.all {
//             ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
//         }

//         if (!allPermissionsGranted) {
//             ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
//         }
//     }

//     override fun onRequestPermissionsResult(
//         requestCode: Int, 
//         permissions: Array<out String>, 
//         grantResults: IntArray
//     ) {
//         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//         if (requestCode == PERMISSION_REQUEST_CODE) {
//             if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                 // All permissions are granted
//             } else {
//                 // Handle the case where permissions are not granted
//             }
//         }
//     }

//     companion object {
//         private const val RECORD_AUDIO_REQUEST_CODE = 123
//         private const val PERMISSION_REQUEST_CODE = 456
//     }
// }
