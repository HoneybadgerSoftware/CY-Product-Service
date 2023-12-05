package com.honeybadgersoftware.productservice.data

import com.google.gson.Gson

class SimplePage {
    List<Long> data

    SimplePage(List<Long> data) {
        this.data = data
    }

    static SimplePage simplePageRequest = new SimplePage([1L, 2L] as List)
    static String jsonRequest = new Gson().toJson(simplePageRequest)

    static SimplePage simplePageResponse = new SimplePage([1L, 6L, 7L, 9L, 10L] as List)
    static String jsonResponse = new Gson().toJson(simplePageResponse)

}
