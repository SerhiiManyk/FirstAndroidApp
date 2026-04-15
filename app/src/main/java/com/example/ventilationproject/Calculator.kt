package com.example.ventilationproject

object Calculator {

    fun calculatePipe(diameter: Double, insulation: Double): Double {
        return Math.PI * (diameter + 2 * insulation)
    }

    fun calculateRect(width: Double, height: Double, insulation: Double): Double {
        return 2 * (width + height + 2 * insulation)
    }
}