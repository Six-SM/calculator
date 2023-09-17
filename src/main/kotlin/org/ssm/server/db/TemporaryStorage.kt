package org.ssm.server.db

import org.ssm.api.models.Calculation

object TemporaryStorage {
    private val temporaryStorage = mutableListOf<Calculation>()

    fun save(request: Calculation): Boolean = temporaryStorage.add(request)
    fun getHistory(): List<Calculation> = temporaryStorage.sortedBy { it.timestamp }
}