package kr.co.jsol.jem.common.domain

import kr.co.jsol.jem.common.domain.enums.DeleteType
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.PostLoad
import javax.persistence.PostPersist
import javax.persistence.PrePersist
import javax.persistence.PreUpdate


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id
    @Comment("id") open var id: String = UUID.randomUUID().toString(),
) {
    // update 쿼리 시 안날라감, 최초 생성이후 변하지 않음
    @CreatedDate
    @Column(name = "CRT_DT", updatable = false)
    @Comment("생성일시")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(name = "MDFCN_DT")
    @Comment("수정일시")
    var updatedAt: LocalDateTime? = null

    @Column(name = "DEL_DT")
    @Comment("삭제일시")
    var deletedAt: LocalDateTime? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL_TYPE")
    @Comment("삭제유형")
    var deleteType: DeleteType? = null

    @PostPersist
    @PostLoad
    @PrePersist
    @PreUpdate
    private fun runValidate() {
        validate()
    }

    open fun validate() {
    }

    fun softDelete() {
        deletedAt = LocalDateTime.now()
        deleteType = DeleteType.SOFT
    }

    fun hardDelete() {
        deletedAt = LocalDateTime.now()
        deleteType = DeleteType.HARD
    }
}

