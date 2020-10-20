package org.unitec.apptextoavoz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    // Este objeto es el intermediario entre nuestra app y TextToSpeech
    private var tts:TextToSpeech?=null
    // El siguiente codigo de peticion es un entero, que nos va ayudar a garantizar el objeto TextToSpeech
    // Se inicion completamente
    private val CODIGO_PETICION=100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Iniciamos ahora i la variable tts para que ya no este en null
        tts= TextToSpeech(this, this)

        // Invocamos la clase Log
        Log.e("XYZ", "Se acaba de iniciar el metodo OnCreate")
        Log.i("XYZ", "tu edad en dias es: " + tuEdadEnDias(21))
        // El signo de pesos en kotlin se conoce como interpolacion de String junto con llaves
        Log.i("XYZ", "Tu edad en dias es: ${tuEdadEnDias(21)} ya sale bien")
        // En kotlin las funciones TAMBIEN SON VARIABLES y su ambito se puede definir solo con llaves
        Log.i("XYZ", "La siguiente es otro ejemplo ${4+5} te dara una suma de 9")
        // En Kotlin, ademas de ser orientado a objetos: tambien es FUNCIONAL
        // Es decir las funciones son tratadas como VARIABLES
        var x=2
        // En kotlin una funcion puede ser declarada DENTRO DE OTRA PORQUE SON TRATADAS COMO VARIABLES
        fun funcioncita()={
            print("Una funcioncita ya con notacion funcional!!")
        }
        // Otro ejemplo con argumentos
        fun otraFuncion(x:Int, y:Int)={
            print("Esta funcion hace la suma de los argumentos que le pases ${x+y}")
        }
        Log.i("XYZ", "Mi primer funcion con notacion funcional ${funcioncita()} listoooo")
        // Se invoca directamente abajo
        otraFuncion(5, 4)

        // Funciones de orden superior y operador lambda

        // Para este ejercicio necesitamos una nueva clase
        class Ejemplito:(Int)->Int{
            override fun invoke(p1: Int): Int {
                TODO("Not yet implemented")
            }

        }


        hablar.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            try {
                startActivityForResult(intent, CODIGO_PETICION)
            }catch (e:Exception){

            }
        }

        // Programamos el clickeo del Boton para que interprete lo escrito
        interpretar.setOnClickListener {
            if(fraseEscrita.text.isEmpty()){
                Toast.makeText(this, "Debes escribir algo para que lo hable", Toast.LENGTH_LONG).show()
            }
            else{
                // Este metodo ahorita lo vamos a implementar
                hablarTexto(fraseEscrita.text.toString())
            }
        }


        // KEMOSION!!!! Vamos a escuchar esa vocecita de Android, de Bienvenida
        Timer("Bienvenida", false).schedule(1000){
            tts!!.speak(
                    "Hola, Bienvenido a mi aplicacion, espero les encante!!",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    ""
            )
        }
    }

    override fun onInit(estado: Int) {
        // Este metodo o funcion sirve para que se inicialize la configuracion al arrancar la app. (IDIOMA)
        if(estado == TextToSpeech.SUCCESS){
            // Si el if se cumplio la ejecucion seguira aqui dentro
            var local=Locale("spa", "MEX")
            // La siguiente variable es para internamente nosotros sepamos que la aplicacion va bien
            val resultado=tts!!.setLanguage(local)
            if(resultado == TextToSpeech.LANG_MISSING_DATA){
                Log.i("MALO","NOOOOOOOO, VALIO VERGA KRNAL, NO JALO EL LENGUAJE, F")
            }

        }

    }

    // Esta funcion es la que nos ayuda a interpretar lo que se escriba
    fun hablarTexto(textoHablar:String){
        tts!!.speak(textoHablar, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        if(tts!=null){
            // En el caso de las apps de espionaje estos dos renglones NUNCA SE APAGAN
            tts!!.stop()
            tts!!.shutdown()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODIGO_PETICION->{
                if(resultCode == RESULT_OK && null != data){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    // Finalmente le vamos a decir a nuestro texto estatico que aqui nos muestre lo
                    // Lo que dijimos pero en texto
                    TextoInterpretado.setText(result!![0])
                }
            }
        }
    }

    // Implementamos un metodo o funcion que es lo mismo
    fun saludar(mensaje:String){
        Log.i("HOLA", "Un mensaje dentro de kotlin")
    }
    fun saludar2(mensaje: String):String{

        return "mi mensaje de bienvenida"
    }
    fun tuEdadEnDias(edad:Int):Int{
        val diasAnios=365
        return diasAnios*edad
    }


}