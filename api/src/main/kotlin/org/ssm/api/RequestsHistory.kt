package org.ssm.api

import org.ktorm.schema.*

object RequestsHistory : Table<Nothing>("t_requests_history") {
    val id = varchar("id").primaryKey()
    val expression = varchar("expression")
    val result = varchar("result")
    val timestamp = varchar("timestamp")
}