package br.heriberto.aguasdomundo.Models

import java.util.*

/**
 * Created by herib on 16/11/2017.
 */
class Media(val date: Date,
            val analises: Array<Map<String, Int>>,
            val latitude: Double,
            val longitude: Double,
            val month: Int,
            var superficie: Double,
            var dureza: Double,
            var peixe_apatico: Double,
            var rain: Double,
            var transparency: Double,
            var algae: Double,
            var algaeColor: Double)