package com.app.rxjavaretrofitcurrency

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), MyAdapter.Listener {

    private var myAdapter: MyAdapter? = null
    private var myCompositeDisposable: CompositeDisposable? = null
    private var myRetroCryptoArrayList: ArrayList<RetroCrypto>? = null
    private val BASE_URL = "https://api.nomics.com/v1/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myCompositeDisposable = CompositeDisposable()
        initRecyclerView()
        loadData()

    }

//Initialise the RecyclerView//

    private fun initRecyclerView() {

//Use a layout manager to position your items to look like a standard ListView//

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        cryptocurrency_list.layoutManager = layoutManager

    }

//Implement loadData//

    private fun loadData() {

//Define the Retrofit request//

        val requestInterface = Retrofit.Builder()

//Set the API’s base URL//

            .baseUrl(BASE_URL)

//Specify the converter factory to use for serialization and deserialization//

            .addConverterFactory(GsonConverterFactory.create())

//Add a call adapter factory to support RxJava return types//

            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

//Build the Retrofit instance//

            .build().create(GetData::class.java)

//Add all RxJava disposables to a CompositeDisposable//

        myCompositeDisposable?.add(
            requestInterface.getData()

//Send the Observable’s notifications to the main UI thread//

                .observeOn(AndroidSchedulers.mainThread())

//Subscribe to the Observer away from the main UI thread//

                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse)
        )

    }

    private fun handleResponse(cryptoList: List<RetroCrypto>) {

        myRetroCryptoArrayList = ArrayList(cryptoList)
        myAdapter = MyAdapter(myRetroCryptoArrayList!!, this)

//Set the adapter//

        cryptocurrency_list.adapter = myAdapter

    }

    override fun onItemClick(retroCrypto: RetroCrypto) {

//If the user clicks on an item, then display a Toast//

        Toast.makeText(this, "You clicked: ${retroCrypto.currency}", Toast.LENGTH_LONG).show()

    }

    override fun onDestroy() {
        super.onDestroy()

//Clear all your disposables//

        myCompositeDisposable?.clear()

    }

}