package com.example.herib.guasdomundo

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.ViewSwitcher
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perguntas.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.io.ByteArrayOutputStream


class PerguntasActivity : AppCompatActivity() {
    private val perguntas = mutableMapOf<Int, String>()
    private val perguntasButton = mutableMapOf<Int, Int>()
    private var pergunta: Int = 0
    private var respostas = mutableMapOf<Int, Int>()
    private var respostasString = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perguntas)

        val url = intent.getStringExtra("fotoUrl")
        val fotoUri = Uri.parse(url)

        Picasso.with(this)
                .load(fotoUri)
                .fit()
                .centerCrop()
                .into(fotoView)

        imgClose.setOnClickListener({
            finish()
        })


        preencherPerguntas()
        prepararPerguntasSwitcher()
        updatePergunta()
        updateButtons()
    }

    private fun prepararPerguntasSwitcher() {
        mPerguntaSwitcher.setFactory(mFactory)

        val inAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in)
        val outAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out)
        mPerguntaSwitcher.inAnimation = inAnimation
        mPerguntaSwitcher.outAnimation = outAnimation
    }

    private fun preencherPerguntas() {
        perguntas[0] = getString(R.string.pergunta_os_peixes_est_o_na_superf_cie_da_gua)
        perguntas[1] = getString(R.string.pergunta_qu_o_transparente_est_a_agua)
        perguntas[2] = getString(R.string.pergunta_a_gua_est_grossa)
        perguntas[3] = getString(R.string.pergunta_os_peixes_est_o_com_apar_ncia_ap_tica)

        perguntasButton[0] = 0
        perguntasButton[1] = 1
        perguntasButton[2] = 0
        perguntasButton[3] = 0
    }

    private fun updatePergunta() {
        numeroText.text = (pergunta + 1).toString()
        mPerguntaSwitcher.setText(perguntas[pergunta])
    }

    private fun updateButtons() {
        if (perguntasButton[pergunta] == 0) {
            simNaoLayout.visibility = VISIBLE
            poucoNormalMuitoLayout.visibility = GONE
        } else {
            simNaoLayout.visibility = GONE
            poucoNormalMuitoLayout.visibility = VISIBLE
        }
        if (pergunta > 0)
            anteriorButton.visibility = VISIBLE
        else
            anteriorButton.visibility = GONE
    }

    fun onClickBack(v: View) {
        pergunta--
        updatePergunta()
        updateButtons()
    }

    fun onClickButton(v: View) {
        var valor: Int = 0
        if (v.id == naoButton.id)
            valor = 0
        else if (v.id == simButton.id)
            valor = 10
        else if (v.id == poucoButton.id)
            valor = 0
        else if (v.id == normalButton.id)
            valor = 5
        else if (v.id == muitoButton.id)
            valor = 10

        respostas[pergunta] = valor
        respostasString[pergunta] = (v as Button).text.toString()
        pergunta++

        if(pergunta == 4) {
            alert(
                    perguntas[0] + " " + respostasString[0] + "\n" +
                    perguntas[1] + " " + respostasString[1] + "\n" +
                    perguntas[2] + " " + respostasString[2] + "\n" +
                    perguntas[3] + " " + respostasString[3],
                    getString(R.string.analisar_com_seguintes_dados)) {
                yesButton { analisarDados() }
                noButton { }
            }.show()
        } else {
            updatePergunta()
            updateButtons()
        }
    }

    private fun analisarDados() {
        setResult(RESULT_OK, null)
        finish()
    }

    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    private val mFactory = ViewSwitcher.ViewFactory {
        val t = TextView(this@PerguntasActivity)
        t.textSize = 18f
        t.textAlignment = TEXT_ALIGNMENT_CENTER
        t.gravity = Gravity.CENTER
        t
    }
}
