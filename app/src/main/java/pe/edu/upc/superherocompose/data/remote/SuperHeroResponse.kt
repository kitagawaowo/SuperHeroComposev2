package pe.edu.upc.superherocompose.data.remote

import pe.edu.upc.superherocompose.data.model.SuperHero

data class SuperHeroResponse(
    val results: List<SuperHero>?
)
