package kr.co.jsol.jem.common.infrastructure.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter
class BooleanToYNConverter : AttributeConverter<Boolean?, String?> {
    override fun convertToDatabaseColumn(attribute: Boolean?): String? {
        return attribute?.let { if (it) "Y" else "N" }
    }

    override fun convertToEntityAttribute(entityValue: String?): Boolean? {
        return entityValue?.let { it == "Y" }
    }
}
