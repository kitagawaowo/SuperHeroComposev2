package pe.edu.upc.superherocompose.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import pe.edu.upc.superherocompose.data.local.SuperHeroDao
import pe.edu.upc.superherocompose.data.local.SuperHeroEntity
import pe.edu.upc.superherocompose.data.model.SuperHero
import pe.edu.upc.superherocompose.data.remote.SuperHeroResponse
import pe.edu.upc.superherocompose.data.remote.SuperHeroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuperHeroRepository(
    private val superHeroService: SuperHeroService,
    private val superHeroDao: SuperHeroDao

) {
    private val _superHeroes = MutableLiveData<List<SuperHero>>(emptyList())
    val superHeroes get() = _superHeroes

    fun fetchByName(name: String) {
        val fetchByName = superHeroService.fetchByName(name)

        fetchByName.enqueue(object : Callback<SuperHeroResponse> {
            override fun onResponse(
                call: Call<SuperHeroResponse>,
                response: Response<SuperHeroResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.results == null) {
                        _superHeroes.value = emptyList()
                    }
                    else {
                        _superHeroes.value = response.body()!!.results!!
                        for (superHero in _superHeroes.value!!) {
                            superHero.favorite = superHeroDao.fetchById(superHero.id).isNotEmpty()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SuperHeroResponse>, t: Throwable) {
                Log.d("SuperHeroRepository", t.message.toString())
            }

        })
    }

    fun insert(superHero: SuperHero) {
        val superHeroEntity = SuperHeroEntity(superHero.id)
        superHeroDao.insert(superHeroEntity)
        superHero.favorite = true
    }

    fun delete(superHero: SuperHero) {
        val superHeroEntity = SuperHeroEntity(superHero.id)
        superHeroDao.delete(superHeroEntity)
        superHero.favorite = true
    }
}