package org.ssm.db

import org.ssm.models.Calculation

val temporaryStorage = mutableListOf<Calculation>()

fun save(request: Calculation) = temporaryStorage.add(request)
fun getHistory() = temporaryStorage.sortedBy { it.timestamp }