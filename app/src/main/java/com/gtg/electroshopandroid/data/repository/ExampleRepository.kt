package com.gtg.electroshopandroid.data.repository

import com.gtg.electroshopandroid.data.model.ExampleDto
import com.gtg.electroshopandroid.data.network.ExampleApiService

interface ExampleRepository {
    suspend fun getExample(): ExampleDto
}

class ExampleRepositoryImpl(
    private val exampleApiService: ExampleApiService
) : ExampleRepository {

    override suspend fun getExample(): ExampleDto = exampleApiService.getExample()
}