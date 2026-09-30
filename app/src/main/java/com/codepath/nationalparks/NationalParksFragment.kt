package com.codepath.nationalparks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
private const val API_KEY = "dPER6Vd9h7OdBaafpivGJhzJGTOLFRhaQBn4wZeO"

/*
 * The class for the only fragment in the app, which contains the progress bar,
 * recyclerView, and performs the network calls to the National Parks API.

 */
class NationalParksFragment : Fragment(), OnListFragmentInteractionListener {
        /*
     * Constructing the view
     */
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_national_parks_list, container, false)
            val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
            val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
            val context = view.context

            recyclerView.layoutManager = LinearLayoutManager(context)

            updateAdapter(progressBar, recyclerView)
            return view
        }

    /*
     * Updates the RecyclerView adapter with new data.  This is where the
     * networking magic happens!
     */
    private fun updateAdapter(
        progressBar: ContentLoadingProgressBar,
        recyclerView: RecyclerView
    ) {
        progressBar.show()

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()

        // Using the client, perform the HTTP request
        client["https://developer.nps.gov/api/v1/parks?api_key=$API_KEY",
            object : JsonHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    progressBar.hide()

                    // TODO - Parse JSON into Models

                    val models: List<NationalPark> = mutableListOf()

                    recyclerView.adapter =
                        NationalParksRecyclerViewAdapter(
                            models,
                            this@NationalParksFragment
                        )

                    Log.d(
                        "NationalParksFragment",
                        "response successful"
                    )
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    progressBar.hide()

                    t?.message?.let {
                        Log.e(
                            "NationalParksFragment",
                            errorResponse
                        )
                    }
                }
            }]
    }

    /*
     * What happens when a particular park is clicked.
     */
    override fun onItemClick(item: NationalPark) {
        Toast.makeText(context, "Park Name: " + item.name, Toast.LENGTH_LONG).show()
    }

}
