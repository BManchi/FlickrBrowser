package com.bmanchi.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

private const val  TAG = "GetFlickrJsonData"

@Suppress("DEPRECATION")
class GetFlickrJsonData(private val listener: OnDataAvailable): AsyncTask<String, Void , ArrayList<Photo>>() {

    interface OnDataAvailable {
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception: Exception)
    }

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
    override fun doInBackground(vararg params: String?): ArrayList<Photo> {
        Log.d(TAG, "doInBackground starts")

        val photoList = ArrayList<Photo>()
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")

            for (i in 0 until itemsArray.length()){
                val jsonPhoto = itemsArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg", "_b.jpg")

                val photoObject = Photo(title, author, authorId, link, tags, photoUrl)

                photoList.add(photoObject)
                Log.d(TAG, "doInBackground $photoObject")
            }
        } catch (e:JSONException) {
            e.printStackTrace()
            Log.e(TAG, "doInBackground: Error processing Json data. ${e.message}")
              cancel(true)
            listener.onError(e)
        }
        Log.d(TAG, "doInBackground ends")
        return photoList
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
    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG, "onPostExecute starts")
        super.onPostExecute(result)
        listener.onDataAvailable(result)
        Log.d(TAG, "onPostExecute ends")
    }
}