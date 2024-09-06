package kr.co.jsol.jem.extension.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.jsol.jem.common.domain.enums.UserAuthority
import kr.co.jsol.jem.extension.application.ExtensionService
import kr.co.jsol.jem.extension.application.dto.UpdateExtensionDto
import kr.co.jsol.jem.extension.infrastructure.dto.ExtensionDto
import kr.co.jsol.jem.file.application.FileService
import kr.co.jsol.jem.user.infrastructure.dto.UserDetailsImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "9999. 확장 프로그램", description = "확장 프로그램 관리 API")
@RestController
@RequestMapping("/api/open/extensions")
class ExtensionController(
    extensionService: ExtensionService,
    fileService: FileService,
) {
    val log: Logger = LoggerFactory.getLogger(ExtensionController::class.java)

    val extensionService: ExtensionService = extensionService
    val fileService: FileService = fileService

    @Operation(summary = "확장 프로그램 업데이트")
    @PostMapping(value = [""], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize(UserAuthority.RoleCheckMethod.HasRootAndManagerRole)
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateExtension(
        // springdoc 1.8.0 에선 ModelAttribute로 받으면 제대로 동작하지 않음.. 1.7.0으로 내리거나 현재처럼 분할해서 받고 합쳐야 함
        //            @Valid @RequestParam UpdateExtensionDto updateExtensionDto,
        @RequestPart @Parameter(description = "키 값") id: String,
        @RequestPart @Parameter(description = "버전") version: String,
        @RequestPart @Parameter(description = "폴더") folder: String,
        @RequestPart @Parameter(description = "파일") file: MultipartFile,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ExtensionDto {
        val updateExtensionDto = UpdateExtensionDto(id, version, folder, file)
        println("updateExtensionDto = $updateExtensionDto")
        return extensionService.update(updateExtensionDto, userDetails)
    }

    @Operation(summary = "확장 프로그램 조회")
    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getById(
        @PathVariable id: String,
    ): ExtensionDto {
        return extensionService.getById(id)
    }

    @Operation(summary = "확장 프로그램 버전 조회")
    @GetMapping("version/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getExtensionVersionById(
        @PathVariable id: String,
    ): String {
        return extensionService.getById(id).version
    }


//    @Operation(summary = "확장프로그램 파일 다운로드")
//    @ApiResponses(
//        ApiResponse(responseCode = "200", description = "성공"),
//        ApiResponse(responseCode = "400", description = "파일이 존재하지 않습니다.")
//    )
//    @GetMapping("download/{extensionId}")
//    @ResponseStatus(value = HttpStatus.OK)
//    fun getFile(
//        @PathVariable extensionId: String
//    ): ResponseEntity<Resource> {
//        val extension: ExtensionDto = extensionService.getById(extensionId)
//        log.info("extension = {}", extension)
//        val resource = fileService.getFile(extension.file.name) as FileSystemResource
//        log.info("resource.getFilename: {}", resource.filename)
//
//        val originalFileName: String = extension.file.originalName
//        val encodedOriginalFileName: String = UriUtils.encode(originalFileName, StandardCharsets.UTF_8)
//        val contentDisposition = "attachment; filename=\"$encodedOriginalFileName\""
//        log.info("contentDisposition: {}", contentDisposition)
//
//        return ResponseEntity
//            .status(HttpStatus.OK) // filename을 그대로 반환
//            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//            .contentType(MediaType.APPLICATION_OCTET_STREAM)
//            .header("Accept-Ranges", "bytes")
//            .body(resource)
//    }

//    @get:ResponseStatus(value = HttpStatus.OK)
//    @get:GetMapping("default-files")
//    @get:ApiResponses(
//        ApiResponse(
//            responseCode = "200",
//            description = "성공"
//        ), ApiResponse(responseCode = "400", description = "파일이 존재하지 않습니다.")
//    )
//    @get:Operation(summary = "기본파일 다운로드")
//    val defaultFiles: ResponseEntity<Resource>
//        // resources의 파일을 가져오는 메서드
//        get() {
//            // resources에서 파일을 가져오기
//            val filePath = Paths.get(
//                File.separatorChar.toString(),
//                File.separatorChar.toString() + "default-files.zip"
//            )
//            val resource: Resource = InputStreamResource(
//                Objects.requireNonNull(javaClass.getResourceAsStream(filePath.toString()))
//            )
//            val contentDisposition = "attachment; filename=default-files.zip"
//
//            return ResponseEntity
//                .status(HttpStatus.OK) // filename을 그대로 반환
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header("Accept-Ranges", "bytes")
//                .body<Resource>(resource)
//        }
}
