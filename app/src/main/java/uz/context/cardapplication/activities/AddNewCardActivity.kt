package uz.context.cardapplication.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.context.cardapplication.database.CardEntity
import uz.context.cardapplication.database.MyDatabase
import uz.context.cardapplication.databinding.ActivityAddNewCardBinding
import uz.context.cardapplication.networking.RetrofitHttp
import uz.context.cardapplication.util.Utils
import uz.context.cardapplication.util.Utils.checkInternet

class AddNewCardActivity : AppCompatActivity() {

    var s: String = ""
    var s2: String = ""
    private lateinit var binding: ActivityAddNewCardBinding

    private lateinit var myDatabase: MyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDatabase = MyDatabase.getInstance(this)

        initViews()

    }

    private fun initViews() {

        binding.apply {
            btnClose.setOnClickListener {
                finishBack()
            }
            textCardName.text = Utils.randomString()
            cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@AddNewCardActivity,
                    Utils.randomColor()
                )
            )
            btnAddNewCard.setOnClickListener {
                addNewCard()
            }
        }
        editTexts()
    }

    private fun editTexts() {
        binding.apply {
            editCardNumbers.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    s = text.toString()
                    textCardNumbers.text = s
                    when {
                        s.length > 12 -> {
                            val saveText = "${s.substring(0, 4)}  ${s.substring(4, 8)}  ${
                                s.substring(
                                    8,
                                    12
                                )
                            }  ${s.substring(12)}"
                            textCardNumbers.text = saveText
                        }
                        s.length > 8 -> {
                            val saveText =
                                "${s.substring(0, 4)}  ${s.substring(4, 8)}  ${s.substring(8)}"
                            textCardNumbers.text = saveText
                        }
                        s.length > 4 -> {
                            val saveText = "${s.substring(0, 4)}  ${s.substring(4)}"
                            textCardNumbers.text = saveText
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            editHolderName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    textCardHolderName.text = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            editDate1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    s2 = p0.toString()
                    if (s2.length == 2) {
                        s2 += "/"
                    }
                    textDate1.text = s2
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            editDate2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    textDate2.text = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
        }
    }

    private fun finishBack() {
        if (binding.editCardNumbers.text.toString().isNotEmpty() ||
            binding.editCvv.text.toString().isNotEmpty() || binding.editDate1.text.toString()
                .isNotEmpty()
            || binding.editHolderName.text.toString().isNotEmpty()
        ) {
            showDialog()
        } else {
            finish()
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Warning!")
            setMessage("Do you want exit?")
            setNeutralButton("No") { _, _ -> }
            setPositiveButton("Yes") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun addNewCard() {
        val cardNum = binding.editCardNumbers.text.toString().trim()
        val cardHolder = binding.editHolderName.text.toString().trim()
        val cardDate1 = binding.editDate1.text.toString().trim()
        val cardDate2 = binding.editDate2.text.toString().trim()
        val editCvv = binding.editCvv.text.toString()
        if (cardNum.isEmpty() ||
            cardDate1.isEmpty() || cardDate2.isEmpty()
            || editCvv.isEmpty() || cardHolder.isEmpty()
        ) {
            Toasty.error(this, "Please enter some data!", Toasty.LENGTH_LONG).show()
        } else if (cardNum.length < 16) {
            Toasty.error(this, "Card number size must be 16", Toasty.LENGTH_LONG).show()
        } else {
            if (checkInternet(this)) {
                postCard(cardNum, cardDate1, cardDate2, cardHolder)
            } else {
                Toasty.error(this,"Please check internet!").show()
            }
        }
    }

    private fun saveDatabase(
        cardNum: String,
        cardDate1: String,
        cardDate2: String,
        cardHolder: String
    ) {
        val entity = CardEntity(cardNum, cardDate1.toInt(), cardDate2.toInt(), cardHolder, 0)
        myDatabase.dao().insertCard(entity)
        Toasty.success(this, "Saved to Database", Toasty.LENGTH_LONG).show()
        finish()
    }

    private fun postCard(
        cardNum: String,
        cardDate1: String,
        cardDate2: String,
        cardHolder: String
    ) {
        val cardEntity = CardEntity(cardNum, cardDate1.toInt(), cardDate2.toInt(), cardHolder)
        RetrofitHttp.postService.postCards(cardEntity).enqueue(object : Callback<CardEntity> {
            override fun onResponse(call: Call<CardEntity>, response: Response<CardEntity>) {
                if (response.isSuccessful) {
                    Log.d("@@@@@@", response.body().toString())
                    Toasty.success(this@AddNewCardActivity, "Saved to Api", Toasty.LENGTH_LONG).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<CardEntity>, t: Throwable) {
                Log.d("@@@@@@", t.message.toString())
                Toasty.error(this@AddNewCardActivity, "Something error!", Toasty.LENGTH_LONG).show()
            }
        })
    }
}
