package com.isanechek.gdz.utils

import java.security.SecureRandom

/**
 * Created by isanechek on 12/2/17.
 */
object Utils {

    private const val AB: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    private val rnd = SecureRandom()

    fun generationId(len: Int) : String {
        val builder = StringBuilder(len)
        for (i in 0 until len) {
            builder.append(AB.get(rnd.nextInt(AB.length)))
        }
        return builder.toString()
    }
}