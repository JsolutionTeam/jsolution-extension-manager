package kr.co.jsol.jem.common.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

@ParameterObject
class PageRequest {
    @Schema(description = "페이지 번호(1부터 시작)", defaultValue = "1")
    var page: Int = 1

    @Schema(description = "페이지 크기", defaultValue = "10", maximum = "10_000")
    var size: Int = 10

    @Schema(description = "정렬 정보", defaultValue = "createdAt.desc")
    var sort: MutableList<String>? = null

    @Schema(
        description = "페이지네이션 사용 여부, 무조건 true이며 false는 지원하지 않음.",
        defaultValue = "true",
        implementation = Boolean::class
    )
    var paged = true

    private fun init() {
        this.page = if (page < FIRST_PAGE_NUMBER) FIRST_PAGE_NUMBER else page
        this.size = if (size > MAX_SIZE) DEFAULT_SIZE else size
    }

    fun of(): Pageable {
        if (!this.paged) {
            this.paged = true
            this.page = FIRST_PAGE_NUMBER
            this.size = MAX_SIZE
        }

        // 기본 값 세팅
        init()

        // 사용자 정의 sort가 오지 않았을때 createdAt.desc를 기본으로 사용하기 위해 생성.
        val defaultOrder = Sort.Order.desc("createdAt")

        val orders: List<Sort.Order> = sort?.filter {
            // name.asc 같은 형태로 와야 정렬처리 하므로 필터링한다.

            // 외래 테이블 조인해야하는 경우 table.column.asc 형태로 오기 때문에 2이상으로 필터링한다.
            it.split(".").size >= 2
        }?.map {
            // .으로 필터링된 정렬 값을 분리시킨다.
            val split = it.split(".")
            // properties의 정렬 방향을 설정한다.

            // split의 0부터 n-1까지
            val lastValueIndex = split.size - 1

            val properties = split.subList(0, lastValueIndex).joinToString(".")

            // split의 마지막 값은 항상 split.size - 1임.
            val directionValue = split[lastValueIndex].equals("asc", true)

            // direction은 asc로 왔을때만 asc로 설정한다.
            val direction = if (directionValue) Sort.Direction.ASC else Sort.Direction.DESC
            Sort.Order(direction, properties)
        } ?: listOf(defaultOrder)

        // 위에서 계산한 값으로 정렬값 생성
        val sortBy = Sort.by(orders)

        return org.springframework.data.domain.PageRequest.of(page - 1, size, sortBy)
    }

    override fun toString(): String {
        return "PageRequest(page=$page, size=$size, sort=$sort, paged=$paged)"
    }

    companion object {
        const val DEFAULT_SIZE = 10
        const val MAX_SIZE = 10_000
        const val FIRST_PAGE_NUMBER = 1

        // Pageable로 PageRequest 만들기
        fun from(pageable: Pageable): PageRequest {
            val pageRequest = PageRequest()
            pageRequest.page = pageable.pageNumber + 1
            pageRequest.size = pageable.pageSize
            pageRequest.sort = pageable.sort.map { it.property + "." + it.direction }.toMutableList()
            return pageRequest
        }
    }
}
