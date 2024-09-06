package kr.co.jsol.jem.user.infrastructure.query

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.jem.common.domain.enums.UserAuthority
import kr.co.jsol.jem.common.infrastructure.exception.entities.UserException
import kr.co.jsol.jem.common.infrastructure.repository.BaseQueryRepository
import kr.co.jsol.jem.common.infrastructure.utils.strContains
import kr.co.jsol.jem.common.infrastructure.utils.toLocalDateTime
import kr.co.jsol.jem.user.application.dto.GetUsersDto
import kr.co.jsol.jem.user.domain.QUser.user
import kr.co.jsol.jem.user.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class UserQueryRepository(
    private val queryFactory: JPAQueryFactory,
) : BaseQueryRepository<User>(user, user._super, queryFactory) {

    override fun getById(id: String, withDeleted: Boolean): User {
        return findById(id, withDeleted)
            ?: throw UserException.NotFoundByIdException()
    }

    override fun getByIds(ids: List<String>, withDeleted: Boolean): List<User> {
        return findByIds(ids, withDeleted).apply {
            if (size != ids.size) {
                throw UserException.NotFoundByIdException()
            }
        }
    }

    fun existsAnyOne(): Boolean {
        return queryFactory
            .selectOne()
            .from(user)
            .fetchFirst() != null
    }

    fun findByEmail(email: String): User? {
        val query = queryFactory
            .selectFrom(user)
            .where(user.email.eq(email))

        return fetchFirst(query)
    }

    fun existsByEmail(email: String): Boolean {
        val query = queryFactory
            .selectFrom(user)
            .where(user.email.eq(email))

        return fetchFirst(query) != null
    }

    fun getWithPagination(pageable: Pageable, getUsersDto: GetUsersDto, isWithoutRoot: Boolean = false): Page<User> {
        val query = queryFactory
            .selectFrom(user)

        getUsersDto.name?.let { query.where(strContains(user.name, it)) }
        getUsersDto.startCreatedAt?.let { query.where(user.createdAt.gt(it.toLocalDateTime())) }
        getUsersDto.endCreatedAt?.let { query.where(user.createdAt.loe(it.toLocalDateTime())) }
        if (isWithoutRoot) query.where(user.authority.ne(UserAuthority.ROOT))

        return fetchPage(query, pageable)
    }
}
