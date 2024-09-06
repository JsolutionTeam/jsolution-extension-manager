package kr.co.jsol.jem.extension.infrastructure.query

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.jem.common.infrastructure.exception.entities.ExtensionException
import kr.co.jsol.jem.common.infrastructure.repository.BaseQueryRepository
import kr.co.jsol.jem.common.infrastructure.utils.strContains
import kr.co.jsol.jem.extension.application.dto.GetExtensionsDto
import kr.co.jsol.jem.extension.domain.Extension
import kr.co.jsol.jem.extension.domain.QExtension.extension
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class ExtensionQueryRepository(queryFactory: JPAQueryFactory) :
    BaseQueryRepository<Extension>(extension, extension._super, queryFactory) {
    val queryFactory: JPAQueryFactory = queryFactory

    override fun getById(id: String, withDeleted: Boolean): Extension {
        val result: Extension? = queryFactory.selectFrom(extension)
            .where(
                extension.deletedAt.isNull(),
                extension.id.eq(id),
            )
            .fetchOne()
        return result ?: throw ExtensionException.NotFoundByIdException(id)
    }

    override fun getByIds(ids: List<String>, withDeleted: Boolean): List<Extension> {
        val query = queryFactory.selectFrom(extension)
            .where(
                extension.deletedAt.isNull(),
                extension.id.`in`(ids),
            )
        return fetch(query)
    }

    fun getOffsetPage(
        getExtensionsDto: GetExtensionsDto,
        pageable: Pageable,
    ): Page<Extension> {
        val query: JPAQuery<Extension> = queryFactory.selectFrom(extension)
            .where(
                extension.deletedAt.isNull(),
                idContains(getExtensionsDto.id),
                versionContains(getExtensionsDto.version),
                folderContains(getExtensionsDto.folder)
            )

        val file: GetExtensionsDto.File? = getExtensionsDto.file
        if (file != null) {
            query.where(
                fileOriginalNameContains(file.originalName),
                fileNameContains(file.name),
                fileExtensionContains(file.extension)
            )
        }

        return fetchPage(query, pageable)
    }

    //region where functions
    fun idContains(id: String?): BooleanExpression? {
        if (id == null) return null
        return strContains(extension.id, id)
    }

    fun versionContains(version: String?): BooleanExpression? {
        if (version == null) return null
        return strContains(extension.version, version)
    }

    fun folderContains(folder: String?): BooleanExpression? {
        if (folder == null) return null
        return strContains(extension.folder, folder)
    }

    fun fileOriginalNameContains(originalName: String?): BooleanExpression? {
        if (originalName == null) return null
        return strContains(extension.file.originalName, originalName)
    }

    fun fileNameContains(fileName: String?): BooleanExpression? {
        if (fileName == null) return null
        return strContains(extension.file.name, fileName)
    }

    fun fileExtensionContains(fileExtension: String?): BooleanExpression? {
        if (fileExtension == null) return null
        return strContains(extension.file.extension, fileExtension)
    } //endregion
}
