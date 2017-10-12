package com.example.herib.guasdomundo

import android.app.ProgressDialog
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Secure
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewSwitcher
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.example.herib.guasdomundo.Interface.PerguntasListener
import com.example.herib.guasdomundo.Models.Analise
import com.example.herib.guasdomundo.Presenters.PerguntasPresenter
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perguntas.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.io.File
import java.lang.Exception


class PerguntasActivity : AppCompatActivity(), PerguntasListener {
    private var progressDialog: ProgressDialog? = null
    private var perguntasPresenter: PerguntasPresenter? = null

    private val perguntas = mutableMapOf<Int, String>()
    private val perguntasButton = mutableMapOf<Int, Int>()
    private var pergunta: Int = 0
    private var respostas = mutableMapOf<Int, Int>()
    private var respostasString = mutableMapOf<Int, String>()

    private var fotoUri: Uri? = null

    private var longitude: Double? = null
    private var latitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perguntas)

        val url = intent.getStringExtra("fotoUrl")
        fotoUri = Uri.parse(url)

        Picasso.with(this)
                .load(fotoUri)
                .fit()
                .centerCrop()
                .into(fotoView)

        imgClose.setOnClickListener({
            finish()
        })

        perguntasPresenter = PerguntasPresenter(this, this)

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
        perguntas[1] = getString(R.string.pergunta_a_gua_est_grossa)
        perguntas[2] = getString(R.string.pergunta_os_peixes_est_o_com_apar_ncia_ap_tica)

        perguntasButton[0] = 0
        perguntasButton[1] = 0
        perguntasButton[2] = 1
    }

    private fun updatePergunta() {
        numeroText.text = (pergunta + 1).toString()
        mPerguntaSwitcher.setText(perguntas[pergunta])
    }

    private fun updateButtons() {
        if (perguntasButton[pergunta] == 0) {
            naoAplicaButton.visibility = GONE
        } else {
            naoAplicaButton.visibility = VISIBLE
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
        else if (v.id == naoAplicaButton.id)
            valor = -1

        respostas[pergunta] = valor
        respostasString[pergunta] = (v as Button).text.toString()
        pergunta++

        if (pergunta == 3) {
            alert(
                    perguntas[0] + " " + respostasString[0] + "\n" +
                            perguntas[1] + " " + respostasString[1] + "\n" +
                            perguntas[2] + " " + respostasString[2],
                    getString(R.string.analisar_com_seguintes_dados)) {
                yesButton { perguntasPresenter!!.getLastLocation() }
                noButton { pergunta-- }
            }.show()
        } else {
            updatePergunta()
            updateButtons()
        }
    }

    override fun onComplete(location: Task<Location>) {
        progressDialog = indeterminateProgressDialog(getString(R.string.processando))
        val file: File = File(fotoUri!!.path)
        perguntasPresenter!!.enviarFoto(file)

        longitude = location.result.longitude
        latitude = location.result.latitude
    }

    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}

    override fun onStateChanged(id: Int, state: TransferState?) {
        if (state == TransferState.COMPLETED) {
            val deviceId = Secure.getString(this.contentResolver,
                    Secure.ANDROID_ID)
            val analise = Analise(id = deviceId,
                    nome_foto = File(fotoUri!!.path).name,
                    superficie = respostas[0]!!,
                    dureza = respostas[1]!!,
                    peixe_apatico = respostas[2]!!,
                    longitude = longitude!!,
                    latitude = latitude!!)
            perguntasPresenter!!.enviarDados(analise)
        } else if (state == TransferState.IN_PROGRESS) {
        }
    }

    override fun onError(id: Int, ex: Exception?) {
        Toast.makeText(this, ex!!.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun finish(response: String) {
        progressDialog = null
        Toast.makeText(this, response, Toast.LENGTH_LONG).show()
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private val mFactory = ViewSwitcher.ViewFactory {
        val t = TextView(this@PerguntasActivity)
        t.textSize = 18f
        t.textAlignment = TEXT_ALIGNMENT_CENTER
        t.gravity = Gravity.CENTER
        t
    }
}
