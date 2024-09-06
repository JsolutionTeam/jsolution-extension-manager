package kr.co.jsol.jem.common.infrastructure.dto

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class Unpaged(private val sort: Sort = Sort.unsorted()) : Pageable {
    override fun isPaged(): Boolean = false

    override fun previousOrFirst(): Pageable = this

    override fun next(): Pageable = this

    override fun hasPrevious(): Boolean = false

    override fun getSort(): Sort = sort

    override fun getPageSize(): Int = throw UnsupportedOperationException()

    override fun getPageNumber(): Int = throw UnsupportedOperationException()

    override fun getOffset(): Long = throw UnsupportedOperationException()

    override fun first(): Pageable = this

    override fun withPage(pageNumber: Int): Pageable =
        if (pageNumber == 0) {
            this
        } else {
            throw UnsupportedOperationException()
        }
}
