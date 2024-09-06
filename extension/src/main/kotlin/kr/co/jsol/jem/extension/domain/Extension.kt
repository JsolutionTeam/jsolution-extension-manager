package kr.co.jsol.jem.extension.domain


import kr.co.jsol.jem.common.domain.BaseEntity
import kr.co.jsol.jem.file.domain.File
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Comment
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Table
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@DynamicUpdate
@DynamicInsert //Extension
@Entity(name = "extension")
@Table(appliesTo = "extension", comment = "RFID 리더")
@BatchSize(size = 50)
class Extension(
    @field:Column(name = "id")
    @field:Comment("확장프로그램 아이디")
    override var id: String = UUID.randomUUID().toString(),

    @field:Column(name = "version")
    @field:Comment("버전")
    var version: String,

    @field:Column(name = "folder")
    @field:Comment("하위 폴더 (상대경로)")
    var folder: String,

    @Embedded
    var file: File,
) : BaseEntity(id)
