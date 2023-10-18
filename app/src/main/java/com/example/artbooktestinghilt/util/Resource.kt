package com.example.artbooktestinghilt.util

/**
 * Bu sınıf amaca uygun yazımı sebebiyle internet üzerinden copy paste edildi.
 * İçerisindeki 3 fonksiyon, veri akışındaki durumların sonuucuna göre belli değerler döndürür.
 * @constructor [status], [data], [message]
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion  object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

}

/**
 * Veri akışındaki sonucun takip edilmesini kolaylaştırmak için yazıldı.
 * @property [SUCCESS]
 * @property [ERROR]
 * @property [LOADING]
 * @author furkanharmanci
 * @since 1.0
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
