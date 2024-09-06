package kr.co.jsol.jem.common.domain.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class StringListConverter : AttributeConverter<List<String?>?, String?> {
    override fun convertToDatabaseColumn(stringList: List<String?>?): String? {
        return if (stringList != null) java.lang.String.join(SPLIT_CHAR, stringList) else null
    }

    override fun convertToEntityAttribute(string: String?): List<String?> {
        return string?.split(SPLIT_CHAR) ?: emptyList()
    }

    companion object {
        private const val SPLIT_CHAR = "%;%"

        fun getSplitChar(): String {
            return SPLIT_CHAR
        }
    }
}
