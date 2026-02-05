package com.example.animepedia.data.cache

object AnimeDetailCache {

    private val cache: HashMap<Int, Boolean> = HashMap()

    fun init(ids: List<Int>) {
        cache.clear()
        ids.forEach { id ->
            cache[id] = true
        }
    }

    fun isDetailCached(animeId: Int): Boolean {
        return cache[animeId] == true
    }

    fun markDetailCached(animeId: Int) {
        cache[animeId] = true
    }
}
