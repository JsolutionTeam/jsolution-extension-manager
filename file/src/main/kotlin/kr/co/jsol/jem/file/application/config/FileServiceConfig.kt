package kr.co.jsol.jem.file.application.config

import io.minio.MinioClient
import kr.co.jsol.jem.common.infrastructure.utils.requireNotEmpty
import kr.co.jsol.jem.file.application.FileService
import kr.co.jsol.jem.file.application.MinioFileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FileServiceConfig(
    @Value("\${spring.file.minio-access-key:}")
    private val minioAccessKey: String,
    @Value("\${spring.file.minio-secret-key:}")
    private val minioSecretKey: String,
    @Value("\${spring.file.minio-url:}")
    private val minioServiceEndpoint: String,
    @Value("\${spring.file.minio-bucket:}")
    private val defaultBucketName: String,
) {
    @Bean
    fun fileService(): FileService {
        // minio
        requireNotEmpty(minioAccessKey) {
            "Could not resolve placeholder 'spring.minio.access-key' in value \"\${spring.minio.access-key}\""
        }
        requireNotEmpty(minioSecretKey) {
            "Could not resolve placeholder 'spring.minio.secret-key' in value \"\${spring.minio.secret-key}\""
        }
        requireNotEmpty(minioServiceEndpoint) {
            "Could not resolve placeholder 'spring.minio.url' in value \"\${spring.minio.url}\""
        }
        requireNotEmpty(defaultBucketName) {
            "Could not resolve placeholder 'spring.minio.bucket' in value \"\${spring.minio.bucket}\""
        }
        val minioClient = MinioClient.builder()
            .endpoint(minioServiceEndpoint)
            .credentials(minioAccessKey, minioSecretKey)
            .build()
        return MinioFileService(
            minioServiceEndpoint = minioServiceEndpoint,
            defaultBucketName = defaultBucketName,
            minioClient = minioClient,
        )
    }
}
