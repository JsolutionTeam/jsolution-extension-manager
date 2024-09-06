package kr.co.jsol.jem.file.domain

import org.hibernate.annotations.Comment
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class File(
    @Column(name = "file_original_name")
    @Comment("원본 파일명")
    var originalName: String,

    @Column(name = "file_name")
    @Comment("저장된 파일명, 기본은 random UUID")
    var name: String,

    @Column(name = "file_extension")
    @Comment("확장자")
    var extension: String,

    @field:Column(name = "download_url")
    @field:Comment("파일 다운로드 경로(해당 경로로 파일 다운로드 가능)")
    var downloadUrl: String,

    @Column(name = "file_size")
    @Comment("파일 크기")
    var size: Long,
)
