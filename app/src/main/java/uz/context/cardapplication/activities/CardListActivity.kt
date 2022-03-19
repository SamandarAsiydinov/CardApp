package uz.context.cardapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.context.cardapplication.adapter.MyAdapter
import uz.context.cardapplication.adapter.MyAdapter2
import uz.context.cardapplication.database.CardEntity
import uz.context.cardapplication.database.MyDatabase
import uz.context.cardapplication.databinding.ActivityCardListBinding
import uz.context.cardapplication.networking.RetrofitHttp
import uz.context.cardapplication.util.Utils.checkInternet

class CardListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardListBinding
    private val cardList = ArrayList<CardEntity>()
    private val cardList2 = ArrayList<CardEntity>()
    private lateinit var myAdapter: MyAdapter
    private lateinit var myAdapter2: MyAdapter2
    private lateinit var myDatabase: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViews() {
        myDatabase = MyDatabase.getInstance(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CardListActivity)
        }
        myAdapter = MyAdapter(this, cardList)
        myAdapter2 = MyAdapter2(this, cardList2)

        if (checkInternet(this)) {
            Toasty.info(this,"From Api", Toasty.LENGTH_LONG).show()
            binding.apply {
                progressBar.isVisible = true
                apiRequest()
                recyclerView.adapter = myAdapter
            }
        } else {
            Toasty.info(this,"From Database", Toasty.LENGTH_LONG).show()
            getDatabase()
            binding.recyclerView.adapter = myAdapter2
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            if (checkInternet(this)) {
                cardList.clear()
                apiRequest()
                myAdapter.notifyDataSetChanged()
            } else {
                cardList2.clear()
                getDatabase()
                myAdapter2.notifyDataSetChanged()
            }
        }
        binding.btnAdd.setOnClickListener {
            intent()
        }
    }

    private fun apiRequest() {
        RetrofitHttp.postService.getAllCards().enqueue(object : Callback<ArrayList<CardEntity>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ArrayList<CardEntity>>,
                response: Response<ArrayList<CardEntity>>
            ) {
                if (response.isSuccessful) {
                    binding.progressBar.isVisible = false
                    cardList.addAll(response.body()!!)
                    myAdapter.notifyDataSetChanged()
                    myDatabase.dao().deleteAllData()
                    for (data in response.body()!!) {
                        myDatabase.dao().insertCard(data)
                    }
                } else if (response.body()!!.isEmpty()) {
                    binding.progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<ArrayList<CardEntity>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDatabase() {
        cardList2.addAll(myDatabase.dao().getAllCards())
        myAdapter2.notifyDataSetChanged()
    }

    private fun intent() {
        if (checkInternet(this)) {
            val intent = Intent(this, AddNewCardActivity::class.java)
            startActivity(intent)
        } else {
            Toasty.error(this,"Please check internet!").show()
        }
    }
}