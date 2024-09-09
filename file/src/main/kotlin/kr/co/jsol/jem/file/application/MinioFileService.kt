package kr.co.jsol.jem.file.application

import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.RemoveObjectsArgs
import io.minio.messages.DeleteObject
import io.minio.messages.Item
import kr.co.jsol.jem.file.application.dto.FileDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

class MinioFileService(
    minioServiceEndpoint: String,
    private val defaultBucketName: String,
    private val minioClient: MinioClient,
) : FileService(
    filePathPrefix = "$minioServiceEndpoint/$defaultBucketName",
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun addFile(multipartFile: MultipartFile): FileDto {
        val file = super.getValidatedFile(multipartFile)
        log.info("파일 업로드 : $file")

        runCatching {
            log.info("파일 업로드 시작 : ${file.originalName}")
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(defaultBucketName)
                    .`object`(file.name)
                    .stream(multipartFile.inputStream, multipartFile.size, -1)
                    .build(),
            )
        }.onFailure {
            log.error("${file.originalName} 파일 업로드 실패 : ${it.message}")
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드를 실패했습니다.")
        }.getOrThrow()

        return file
    }

    override fun getFile(filename: String): Resource {
        throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "다른 방식으로 지원합니다.")
    }

    // 파일 삭제 , 삭제할 파일이 없으면 true 삭제를 완료 했으면 true
    override fun deleteFile(filename: String): Boolean {
        runCatching {
            minioClient.removeObject(
                RemoveObjectArgs
                    .builder()
                    .bucket(defaultBucketName)
                    .`object`(filename)
                    .build(),
            )
        }.onFailure {
            log.error("$filename 파일 삭제 실패")
            return false
        }
        log.info("$filename 파일 삭제 성공")
        return true
    }

    override fun deleteFiles(filenames: List<String>): Boolean {
        runCatching {
            val results = minioClient.removeObjects(
                RemoveObjectsArgs
                    .builder()
                    .bucket(defaultBucketName)
                    .objects(filenames.map { DeleteObject(it) })
                    .build(),
            )
            results.forEach {
                log.info(it.get().objectName() + it.get().message())
            }
        }.onFailure {
            log.error("$filenames 파일 삭제 실패")
            return false
        }
        log.info("$filenames 파일 삭제 성공")
        return true
    }

    // dir 사이즈 합산 바이트 크기 출력
    override fun getDirectorySize(companyId: String): Long {
        val minioObjects = getObjectLists(companyId)
        return minioObjects.sumOf { it.size() }
    }

    override fun flushUnusedFiles(
        companyId: String,
        files: List<String>,
    ) {
        val minioObjects = getObjectLists(companyId)
        val unusedFiles = minioObjects.filter { it.objectName() !in files }
        if (unusedFiles.isEmpty()) return
        deleteFiles(unusedFiles.map { it.objectName() })
    }

    private fun getObjectLists(companyId: String): List<Item> {
        return runCatching {
            minioClient.listObjects(
                ListObjectsArgs
                    .builder()
                    .bucket(defaultBucketName)
                    .prefix("$companyId/")
                    .build(),
            ).map { it.get() }
        }.onFailure {
            log.error("파일 리스트 조회 실패")
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 리스트 조회를 실패했습니다.")
        }.getOrThrow()
    }
}
