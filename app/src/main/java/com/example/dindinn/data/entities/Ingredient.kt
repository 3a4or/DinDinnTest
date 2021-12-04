package com.example.dindinn.data.entities

data class Ingredient(
    val idIngredient: String?,
    val strABV: String?,
    val strAlcohol: String?,
    val strDescription: String?,
    val strIngredient: String,
    val strType: String?
) {
    override fun toString(): String {
        return strIngredient
    }
}