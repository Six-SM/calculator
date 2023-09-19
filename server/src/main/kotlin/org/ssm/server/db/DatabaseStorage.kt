package org.ssm.server.db

import org.ssm.api.CalculationListResponse
import org.ssm.api.CalculationRequest
import org.ssm.api.CalculationResponse
import org.ssm.api.RequestsHistory
import org.ssm.api.requestsHistoryCreateTable
import org.ktorm.database.Database
import org.ktorm.dsl.*

class DatabaseStorage(url: String, username: String, password: String) {
    val database = Database.connect(url, user=username, password=password)

    init {
        database.useConnection { conn -> conn.prepareStatement(requestsHistoryCreateTable).use { it.execute() } }
    }

    fun save(request: CalculationRequest, response: CalculationResponse): Int = 
        database.insert(RequestsHistory) {
            set(it.expression, request.expression)
            set(it.result, response.result)
            set(it.id, response.id)
            set(it.timestamp, response.timestamp)
        }

    fun getHistory(): CalculationListResponse = CalculationListResponse(database.from(RequestsHistory).select().map { row -> CalculationListResponse.CalculationHistoryRequest(
                            row[RequestsHistory.expression] ?: "", 
                            row[RequestsHistory.result] ?: "", 
                            row[RequestsHistory.id] ?: "", 
                            row[RequestsHistory.timestamp] ?: ""
                        )}.sortedByDescending { it.timestamp }.toList())
}