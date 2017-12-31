package br.heriberto.aguasdomundo

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment

class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(AppIntroFragment.newInstance("Bem-vindo ao Águas do Mundo", "Aplicação para análise inicial de aquíferos", R.mipmap.ic_launcher_foreground, resources.getColor(R.color.colorPrimaryDark)))
        addSlide(AppIntroFragment.newInstance("Como medir parâmetros da água: Oxigênio", "Verificar se há peixes na superfíce", R.drawable.superficie, resources.getColor(R.color.colorPrimaryDark)))
        addSlide(AppIntroFragment.newInstance("Como medir parâmetros da água: Dureza", "Ver a quantitade de espuma gerada ao utilizar sabão com a água do aquífero", R.drawable.dureza, resources.getColor(R.color.colorPrimaryDark)))
        addSlide(AppIntroFragment.newInstance("Como medir parâmetros da água: Variação do pH", "Ver se os peixes estão com aparência apática", R.drawable.apatico, resources.getColor(R.color.colorPrimaryDark)))
        addSlide(AppIntroFragment.newInstance("Como medir parâmetros da água: Algas", "Fotografar aquífero em um ângulo de 90º", R.drawable.algas, resources.getColor(R.color.colorPrimaryDark)))
        addSlide(AppIntroFragment.newInstance("Como medir parâmetros da água: Turbidez", "Fotografar amostra do aquífero em um copo transparente", R.drawable.turbidez, resources.getColor(R.color.colorPrimaryDark)))


        // OPTIONAL METHODS
        // Override bar/separator color.
//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false)
        isProgressButtonEnabled = true

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true)
        setVibrateIntensity(30)
    }

    override fun onSkipPressed(currentFragment: Fragment) {
        super.onSkipPressed(currentFragment)
    }

    override fun onDonePressed(currentFragment: Fragment) {
        super.onDonePressed(currentFragment)
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.
    }
}
