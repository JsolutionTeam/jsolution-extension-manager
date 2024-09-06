package kr.co.jsol.jem.common.infrastructure.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BeanPath
import com.querydsl.core.types.dsl.ComparableExpressionBase
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.jem.common.domain.BaseEntity
import kr.co.jsol.jem.common.domain.QBaseEntity
import kr.co.jsol.jem.common.infrastructure.utils.getDeclaredField
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

abstract class BaseQueryRepository<T : BaseEntity>// <T>의 이름을 entityName에 저장하는데,
// QEntity는 Q로 시작하니 제일 앞의 Q를 제거.
    (
    private var qEntity: EntityPathBase<T>,
    private var qBaseEntity: QBaseEntity,
    private var queryFactory: JPAQueryFactory
) {
    protected var entityName: String = ""

    open val orderColumnNameMap: Map<String, String> = emptyMap()

    init {
        var entityName = qEntity.type.simpleName
        if (entityName.startsWith("Q")) {
            entityName = entityName.substring(1)
        }
        this.entityName = entityName
    }

    open fun getAll(withDeleted: Boolean = false): List<T> {
        val query = queryFactory
            .select(qEntity)
            .from(qEntity)

        return fetch(query, withDeleted)
    }

    open fun findById(id: String, withDeleted: Boolean = false): T? {
        val query = queryFactory
            .select(qEntity)
            .from(qEntity)
            .where(qBaseEntity.id.eq(id))

        return fetchOne(query, withDeleted)
    }

    abstract fun getById(id: String, withDeleted: Boolean = false): T

    open fun findByIds(ids: List<String>, withDeleted: Boolean = false): List<T> {
        val query = queryFactory
            .select(qEntity)
            .from(qEntity)
            .where(qBaseEntity.id.`in`(ids))

        return fetch(query, withDeleted)
    }

    abstract fun getByIds(ids: List<String>, withDeleted: Boolean = false): List<T>

    protected fun fetchFirst(query: JPAQuery<T>, withDeleted: Boolean = false): T? {
        return query
            .withDeleted(withDeleted)
            .fetchFirst()
    }

    protected fun fetchOne(query: JPAQuery<T>, withDeleted: Boolean = false): T? {
        return query
            .withDeleted(withDeleted)
            .fetchOne()
    }

    protected fun fetch(query: JPAQuery<T>, withDeleted: Boolean = false): List<T> {
        return query
            .withDeleted(withDeleted)
            .fetch()
    }

    protected fun fetchPage(query: JPAQuery<T>, pageable: Pageable, withDeleted: Boolean = false): Page<T> {
        if (pageable.isUnpaged) {
            query.orderBy(*getSort(pageable.sort).toTypedArray())
            return PageImpl(fetch(query, withDeleted))
        }

        val result = query.clone()
            .withDeleted(withDeleted)
            .orderBy(*getSort(pageable.sort).toTypedArray())
            .pagination(pageable)
            .fetch()

        val count = query.clone()
            .select(qEntity.countDistinct())
            .withDeleted(withDeleted)
            .fetchOne() ?: 0L

        return PageImpl(result, pageable, count)
    }

    protected fun <T> JPAQuery<T>.withDeleted(withDeleted: Boolean): JPAQuery<T> {
        if (!withDeleted) {
            this.where(qBaseEntity.deletedAt.isNull)
        }

        return this
    }

    protected fun getColumnByString(
        columnName: String,
        entity: BeanPath<*> = qEntity,
        lazyMessage: () -> Any,
    ): ComparableExpressionBase<*> {
        val splitColumnName = columnName.split(".")
        return if (splitColumnName.size > 1) {
            val subEntityField = entity.javaClass.getDeclaredField(splitColumnName[0], lazyMessage)
            getColumnByString(
                columnName.substringAfter("."),
                subEntityField.get(entity) as BeanPath<*>,
                lazyMessage,
            )
        } else {
            val columnField = entity.javaClass.getDeclaredField(columnName, lazyMessage)
            columnField.get(entity) as ComparableExpressionBase<*>
        }
    }

    protected fun <T> JPAQuery<T>.pagination(pageable: Pageable): JPAQuery<T> {
        return this.offset(getOffset(pageable))
            .limit(getLimit(pageable))
    }

    protected fun getSort(sort: Sort): List<OrderSpecifier<*>> {
        return sort.map {
            val column: ComparableExpressionBase<*> =
                getColumnByString(orderColumnNameMap[it.property] ?: it.property) { "정렬할 수 없는 컬럼입니다." }
            if (it.isAscending) column.asc() else column.desc()
        }.toList()
    }

    private fun getOffset(pageable: Pageable): Long {
        return if (pageable.isPaged) {
            pageable.offset
        } else {
            0
        }
    }

    private fun getLimit(pageable: Pageable): Long {
        return if (pageable.isPaged) {
            pageable.pageSize.toLong()
        } else {
            // 너무 많은 데이터를 한 번에 가져오지 않도록 제한,
            // 서버가 다운될 수 있음
            1_000
        }
    }
}
