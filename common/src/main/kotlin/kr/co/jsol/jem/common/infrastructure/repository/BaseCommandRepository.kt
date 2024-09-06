package kr.co.jsol.jem.common.infrastructure.repository

import kr.co.jsol.jem.common.domain.BaseEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

open class BaseCommandRepository<T : BaseEntity>(private val curdRepository: CrudRepository<T, String>) {
    fun save(entity: T): T {
        // 빈 문자열은 강제로 UUID 생성
        if (entity.id.isEmpty())
            entity.id = UUID.randomUUID().toString()
        return curdRepository.save(entity)
    }

    fun saveMultiple(entities: List<T>): List<T> {
        return curdRepository.saveAll(entities).toList()
    }

    fun softDelete(entity: T) {
        entity.softDelete()

        save(entity)
    }

    fun softDeleteMultiple(entities: List<T>) {
        entities.forEach { it.softDelete() }

        saveMultiple(entities)
    }

    fun hardDelete(entity: T) {
        curdRepository.delete(entity)
    }

    fun hardDeleteMultiple(entities: List<T>) {
        curdRepository.deleteAll(entities)
    }
}
