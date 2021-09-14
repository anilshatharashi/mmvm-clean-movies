package com.example.mvvmclean.anilshatharashi.domain.mapper

interface Mapper<T, U> {
    fun mapFrom(from: T): U
}
