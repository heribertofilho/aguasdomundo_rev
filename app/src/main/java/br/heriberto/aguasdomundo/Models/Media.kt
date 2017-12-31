package br.heriberto.aguasdomundo.Models

/**
 * Created by herib on 16/11/2017.
 */
class Media(val date: Long,
            val analises: Map<String, Long>,
            val latitude: Double,
            val longitude: Double,
            val month: Int,
            var superficie: Double,
            var dureza: Double,
            var peixe_apatico: Double,
            var rain: Double,
            var turbidez: Double,
            var algae: Double,
            var algaeColor: Double)