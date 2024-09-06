package kr.co.jsol.jem.extension.application

import kr.co.jsol.jem.extension.domain.Extension
import kr.co.jsol.jem.file.domain.File
import org.springframework.stereotype.Service

@Service
class ExtensionMapper {
    fun createExtension(
        id: String,
        version: String,
        folder: String,
        file: File,
    ): Extension {
        return Extension(id, version, folder, file)
    }
}
