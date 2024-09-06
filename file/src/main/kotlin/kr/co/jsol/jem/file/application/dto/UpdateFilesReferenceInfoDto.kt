package kr.co.jsol.jem.file.application.dto

data class UpdateFilesReferenceInfoDto(
    val referenceType: String,
    val referenceTableInfo: String,
    val referenceTableName: String,
    val referenceColumnInfo: String,
    val referenceColumn: String,
    val referenceId: String
)
