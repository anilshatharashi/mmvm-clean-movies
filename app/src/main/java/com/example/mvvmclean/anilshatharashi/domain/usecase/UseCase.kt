package com.example.mvvmclean.anilshatharashi.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

abstract class UseCase<PARAMS, RESULT> {

    suspend fun execute(params: PARAMS): Flow<RESULT> = withContext(Dispatchers.IO) {
        buildUseCase(params)
    }

    abstract suspend fun buildUseCase(params: PARAMS): Flow<RESULT>

}

