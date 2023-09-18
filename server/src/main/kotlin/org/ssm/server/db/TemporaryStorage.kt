package org.ssm.server.db

import org.ssm.api.CalculationListResponse
import org.ssm.api.CalculationRequest
import org.ssm.api.CalculationResponse
import org.ssm.api.RequestsHistory
import org.ktorm.database.Database
import org.ktorm.dsl.*

class TemporaryStorage(url: String, username: String, password: String) {
    val database = Database.connect(url, user=username, password=password)

    init {
        val sql = "create table if not exists t_requests_history(id varchar primary key, expression varchar, result varchar, timestamp varchar);"
        database.useConnection { conn -> conn.prepareStatement(sql) }
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
                        )}.sortedBy { it.timestamp }.toList())
}