package org.ssm.api

import org.ktorm.schema.*

private const val requestsHistoryTableName = "t_requests_history";
val requestsHistoryCreateTable = "create table if not exists " + requestsHistoryTableName + " (id varchar primary key, expression varchar, result varchar, timestamp varchar);"

object RequestsHistory : Table<Nothing>(requestsHistoryTableName) {
    val id = varchar("id").primaryKey()
    val expression = varchar("expression")
    val result = varchar("result")
    val timestamp = varchar("timestamp")
}