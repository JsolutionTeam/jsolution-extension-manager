package kr.co.jsol.jem.user.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.jsol.jem.common.domain.enums.UserAuthority
import kr.co.jsol.jem.common.infrastructure.dto.PageRequest
import kr.co.jsol.jem.common.infrastructure.exception.GeneralClientException
import kr.co.jsol.jem.user.application.UserService
import kr.co.jsol.jem.user.application.dto.GetUsersDto
import kr.co.jsol.jem.user.application.dto.UpdateUserDto
import kr.co.jsol.jem.user.infrastructure.dto.UserDetailsImpl
import kr.co.jsol.jem.user.infrastructure.dto.UserDto
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "001. 사용자", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
class UserController(
    private val service: UserService,
) {
//    @Operation(summary = "회원 자신 수정 (ROOT, MANAGER, USER)")
//    @ApiResponses(
//        ApiResponse(responseCode = "200", description = "성공"),
//        ApiResponse(responseCode = "403", description = "권한이 없습니다.", content = [Content()]),
//    )
//    @PreAuthorize(EUserAuthority.RoleCheckMethod.HasAnyRole)
//    @SecurityRequirement(name = "Bearer Authentication")
//    @PatchMapping("")
//    @ResponseStatus(value = HttpStatus.OK)
//    fun updateUserSelf(
//        @RequestBody @Valid
//        updateUserDto: UpdateUserDto,
//        @AuthenticationPrincipal
//        userDetails: UserDetailsImpl,
//    ): UserDto {
//        return service.updateUser(userDetails.username, updateUserDto)
//    }

    @Operation(summary = "회원 수정 (ROOT, MANAGER)")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "권한이 없습니다.", content = [Content()]),
    )
    @PreAuthorize(UserAuthority.RoleCheckMethod.HasRootAndManagerRole)
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateUser(
        @PathVariable
        id: String,
        @RequestBody @Valid
        updateUserDto: UpdateUserDto,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): UserDto {
        if (userDetails.isUser) {
            if (userDetails.username != id) {
                throw GeneralClientException.ForbiddenException()
            }
        }

        updateUserDto.id = id

        return service.update(updateUserDto)
    }

    @Operation(summary = "회원 리스트 조회 (ROOT, MANAGER)")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "권한이 없습니다.", content = [Content()]),
    )
    @PreAuthorize(UserAuthority.RoleCheckMethod.HasRootAndManagerRole)
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUsers(
        pageRequest: PageRequest,
        @Valid
        getUsersDto: GetUsersDto,
        @AuthenticationPrincipal
        userDetails: UserDetailsImpl,
    ): Page<UserDto> {
        return service.getUsers(userDetails.username, pageRequest.of(), getUsersDto)
    }

    @Operation(summary = "회원 상세 조회 (ROOT, MANAGER)")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "권한이 없습니다.", content = [Content()]),
    )
    @PreAuthorize(UserAuthority.RoleCheckMethod.HasAnyRole)
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUser(
        @AuthenticationPrincipal
        userDetails: UserDetailsImpl,
        @PathVariable
        id: String,
    ): UserDto {
        return service.getUser(userDetails.username, id)
    }

    @Operation(summary = "내 정보 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @PreAuthorize(UserAuthority.RoleCheckMethod.HasAnyRole)
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("my-info")
    @ResponseStatus(value = HttpStatus.OK)
    fun getMyInfo(
        @AuthenticationPrincipal
        userDetails: UserDetailsImpl,
    ): UserDto {
        return service.getUser(userDetails.username, userDetails.username)
    }
}
