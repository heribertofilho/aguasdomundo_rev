package br.heriberto.aguasdomundo

import android.app.ProgressDialog
import android.content.Intent
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
import android.widget.ViewSwitcher
import br.heriberto.aguasdomundo.Interface.PerguntasListener
import br.heriberto.aguasdomundo.Models.Analise
import br.heriberto.aguasdomundo.Presenters.PerguntasPresenter
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perguntas.*
import org.jetbrains.anko.*
import java.io.File
import java.lang.Exception


class PerguntasActivity : AppCompatActivity(), PerguntasListener {
    private var progressDialog: ProgressDialog? = null
    private var perguntasPresenter: PerguntasPresenter? = null

    private val perguntas = mutableMapOf<Int, String>()
    private val perguntasTipo = mutableMapOf<Int, String>()
    private val perguntasInfo = mutableMapOf<Int, String>()
    private val perguntasButton = mutableMapOf<Int, Int>()
    private var pergunta: Int = 0
    private var respostas = mutableMapOf<Int, Int>()
    private var respostasString = mutableMapOf<Int, String>()

    private var fotoUri: Uri? = null

    private var longitude: Double? = null
    private var latitude: Double? = null

    private lateinit var pais: String
    private lateinit var estado: String
    private lateinit var cidade: String

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

        perguntasPresenter = PerguntasPresenter(this, this, this)

        preencherPerguntas()
        prepararPerguntasSwitcher()
        updatePergunta()
        updateButtons()
    }

    //Atualiza o texto da pergunta usando uma animação de transição
    private val mFactory = ViewSwitcher.ViewFactory {
        val t = TextView(this@PerguntasActivity)
        t.textSize = 18f
        t.textAlignment = TEXT_ALIGNMENT_CENTER
        t.gravity = Gravity.CENTER
        t
    }

    //Escolhe aa animações utilizadas nas transições
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

        perguntasInfo[0] = getString(R.string.superficie_ajuda)
        perguntasInfo[1] = getString(R.string.dureza_ajuda)
        perguntasInfo[2] = getString(R.string.peixe_apatico_ajuda)

        perguntasTipo[0] = getString(R.string.oxigenio)
        perguntasTipo[1] = getString(R.string.dureza)
        perguntasTipo[2] = getString(R.string.ph_variante)

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

    fun onClickInfo(v: View) {
        if (pergunta != 1) {
            alert(perguntasInfo[pergunta]!!, perguntasTipo[pergunta]) { }.show()
            return
        }
        alert(perguntasInfo[pergunta]!! + "\n" + getString(R.string.dureza_medir), perguntasTipo[pergunta]) { }.show()
//        alert {
//            title = perguntasTipo[pergunta]!!
//            customView {
//                scrollView {
//                    linearLayout {
//                        textView {
//                            text = perguntasInfo[pergunta]
//                        }
//                        textView {
//                            text = getString(R.string.dureza_medir)
//                        }
//                    }
//                }
//            }
//        }.show()
    }

    fun onClickButton(v: View) {
        var valor = 0
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
                yesButton { perguntasPresenter!!.localizarUsuario() }
                noButton { pergunta-- }
            }.show()
        } else {
            updatePergunta()
            updateButtons()
        }
    }

    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}

    override fun onStateChanged(id: Int, state: TransferState?) {}

    override fun onError(id: Int, ex: Exception?) {
        alert(getString(R.string.erro_envio_imagem)) {
            yesButton {
                finishData()
            }
        }.show()
    }

    //Após todas os dados e fotografia serem enviados, este método encerra esta activity e as anterios,
    //para que a memória seja limpada e o mapa resetado.
    override fun finish(response: String) {
        progressDialog = null
        toast(response)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun placesError() {
        toast(getString(R.string.erro_localizacao))
    }

    override fun placesSuccess(pais: String, estado: String, cidade: String, latitude: Double, longitude: Double) {
        this.pais = pais
        this.estado = estado
        this.cidade = cidade
        this.latitude = latitude
        this.longitude = longitude
        enviarDados()
    }

    //Esse método chama o método no presenter que envia os dados para o Amazon Lambda.
    fun enviarDados() {
        progressDialog = indeterminateProgressDialog(getString(R.string.processando))
        val deviceId = Secure.getString(this.contentResolver,
                Secure.ANDROID_ID)
        val analise = Analise(id = deviceId,
                nome_foto = File(fotoUri!!.path).name,
                superficie = respostas[0]!!,
                dureza = respostas[1]!!,
                peixe_apatico = respostas[2]!!,
                pais = pais,
                estado = estado,
                cidade = cidade,
                longitude = longitude!!,
                latitude = latitude!!)
        perguntasPresenter!!.enviarDados(analise)
    }

    //Método chamado pelo presenter após os dados serem enviados e processados, por fim, este método
    //envia a fotografia.
    override fun finishData() {
        val file = File(fotoUri!!.path)
        perguntasPresenter!!.enviarFoto(file)
    }
}
