@file:Suppress("DEPRECATION") // for deprecated AsyncTask

package com.bmanchi.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}
private const val TAG = "GetRawData"

class GetRawData(private val listener: OnDownloadComplete): AsyncTask<String, Void, String>() {
    private var downloadStatus = DownloadStatus.IDLE

    interface  OnDownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }

//    private var listener: MainActivity? = null
//
//    fun setDownloadCompleteListener(callbackObject: MainActivity){
//        listener = callbackObject
//    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to [.execute]
     * by the caller of this task.
     *
     * This will normally run on a background thread. But to better
     * support testing frameworks, it is recommended that this also tolerates
     * direct execution on the foreground thread, as part of the [.execute] call.
     *
     * This method can call [.publishProgress] to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     *
     * @return A result, defined by the subclass of this task.
     *
     * @see .onPreExecute
     * @see .onPostExecute
     *
     * @see .publishProgress
     */
    override fun doInBackground(vararg params: String?): String {
        if (params[0] == null) {
          downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No URL specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return  URL(params[0]).readText()
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception reading data: ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Security Exception: Needs permission? ${e.message}"
                } else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "Unknown error: ${e.message}"
                }
            }
            Log.e(TAG, errorMessage)
            return errorMessage
        }
    }

    /**
     *
     * Runs on the UI thread after [.doInBackground]. The
     * specified result is the value returned by [.doInBackground].
     * To better support testing frameworks, it is recommended that this be
     * written to tolerate direct execution as part of the execute() call.
     * The default version does nothing.
     *
     *
     * This method won't be invoked if the task was cancelled.
     *
     * @param result The result of the operation computed by [.doInBackground].
     *
     * @see .onPreExecute
     *
     * @see .doInBackground
     *
     * @see .onCancelled
     */
    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute called")
        listener.onDownloadComplete(result, downloadStatus)
    }
}