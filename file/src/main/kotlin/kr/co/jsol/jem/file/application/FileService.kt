package kr.co.jsol.jem.file.application

import kr.co.jsol.jem.file.application.dto.FileDto
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

abstract class FileService(val filePathPrefix: String) {

    fun getValidatedFile(multipartFile: MultipartFile): FileDto {
        if (multipartFile.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "파일이 없습니다.")
        }
        val fileOriginalName = multipartFile.originalFilename ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "파일이 없습니다.",
        )
        if (!fileOriginalName.contains(".")) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 확장자가 없습니다.")
        }
        if (multipartFile.size > 1024L * 1024 * 100) {
            // 파일 사이즈가 100mb 이상이면 업로드 불가
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 사이즈가 너무 큽니다. 최대 100MB 까지 업로드 가능합니다.")
        }
        val fileExtension = fileOriginalName.split(".").last()
        val filename = "${UUID.randomUUID()}.$fileExtension"

        return FileDto(
            originalName = fileOriginalName,
            name = filename,
            extension = fileExtension,
            downloadUrl = "$filePathPrefix/$filename",
            size = multipartFile.size,
        )
    }

    /**
     * 메서드 구현시 무조건 getValidatedFile(MultipartFile) 메서드로 받은 검증된 파일 사용
     * @param multipartFile
     */
    abstract fun addFile(multipartFile: MultipartFile): FileDto

    abstract fun getFile(filename: String): Resource
    abstract fun deleteFiles(filenames: List<String>): Boolean
    abstract fun deleteFile(filename: String): Boolean
    abstract fun getDirectorySize(companyId: String): Long
    abstract fun flushUnusedFiles(companyId: String, files: List<String>)
}
