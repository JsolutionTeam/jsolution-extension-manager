package kr.co.jsol.jem.extension.application

import kr.co.jsol.jem.extension.application.dto.GetExtensionsDto
import kr.co.jsol.jem.extension.application.dto.UpdateExtensionDto
import kr.co.jsol.jem.extension.domain.Extension
import kr.co.jsol.jem.extension.infrastructure.command.ExtensionCommandRepository
import kr.co.jsol.jem.extension.infrastructure.dto.ExtensionDto
import kr.co.jsol.jem.extension.infrastructure.query.ExtensionQueryRepository
import kr.co.jsol.jem.file.application.FileService
import kr.co.jsol.jem.file.application.dto.FileDto
import kr.co.jsol.jem.file.domain.File
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class ExtensionService(
    private val query: ExtensionQueryRepository,
    private val command: ExtensionCommandRepository,
    private val mapper: ExtensionMapper,
    private val fileService: FileService,
) {

    fun update(updateExtensionDto: UpdateExtensionDto, userDetails: UserDetails): ExtensionDto {
        val fileDto: FileDto = fileService.addFile(updateExtensionDto.file)
        val file = File(
            fileDto.originalName,
            fileDto.name,
            fileDto.extension,
            fileDto.downloadUrl,
            fileDto.size,
        )
        // 입력받은 정보로 강제저장
        val extension: Extension = mapper.createExtension(
            updateExtensionDto.id,
            updateExtensionDto.version,
            updateExtensionDto.folder,
            file,
        )

        // 저장
        command.save(extension)

        return ExtensionDto(extension)
    }

    fun delete(id: String) {
        val extension: Extension = query.getById(id)
        fileService.deleteFile(extension.file.name)
        command.softDelete(extension)
    }

    fun getById(id: String): ExtensionDto {
        val extension: Extension = query.getById(id)
        return ExtensionDto(extension)
    }

    fun getOffsetPage(getExtensionsDto: GetExtensionsDto, pageable: Pageable): Page<ExtensionDto> {
        val extensions: Page<Extension> = query.getOffsetPage(getExtensionsDto, pageable)
        return extensions.map { ExtensionDto(it) }
    }
}
