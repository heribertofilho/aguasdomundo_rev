package br.heriberto.aguasdomundo

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.LinearLayout
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class RecomendacoesActivity : AppCompatActivity() {

    var oxigenio: Boolean = false
    var dureza: Boolean = false
    var ph: Boolean = false
    var turbidez: Boolean = false
    var algas: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_recomendacoes)
        oxigenio = intent.getBooleanExtra("oxigenio", false)
        dureza = intent.getBooleanExtra("dureza", false)
        ph = intent.getBooleanExtra("ph", false)
        turbidez = intent.getBooleanExtra("turbidez", false)
        algas = intent.getBooleanExtra("algas", false)

        verticalLayout {
            gravity = Gravity.CENTER
            if(oxigenio) {
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    textView { text = getString(R.string.oxigenio) + ": "
                        typeface = Typeface.DEFAULT_BOLD
                    }
                    verticalLayout {
                        bottomPadding = 8
                        textView { text = getString(R.string.oxigenio_1) }
                        textView { text = getString(R.string.oxigenio_2) }
                        textView { text = getString(R.string.oxigenio_3) }
                        textView { text = getString(R.string.oxigenio_4) }
                    }
                }
            }
            if(ph) {
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    textView { text = getString(R.string.ph_variante) + ": "
                        typeface = Typeface.DEFAULT_BOLD
                    }
                    verticalLayout {
                        bottomPadding = 8
                        textView { text = getString(R.string.ph_1) }
                    }
                }
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                textView { text = getString(R.string.dureza) + ": "
                    typeface = Typeface.DEFAULT_BOLD
                }
                verticalLayout {
                    bottomPadding = 8
                    if (dureza)
                        textView { text = "Se a água está dura, então o aquífero é indicado para criação de camarão." }
                    else
                        textView { text = "Água mole, indicada para criação de peixes." }
                }
            }
            if(algas) {
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    textView { text = "Algas: "
                        typeface = Typeface.DEFAULT_BOLD
                    }
                    verticalLayout {
                        bottomPadding = 8
                        textView { text = "Água eutrofizada, reduza a quantidade de alimento." }
                    }
                }
            }
        }
    }
}
