package kr.co.jsol.jem.user.domain

import kr.co.jsol.jem.common.domain.BaseEntity
import kr.co.jsol.jem.common.domain.enums.UserAuthority
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Table
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity(name = "user")
@Table(appliesTo = "user", comment = "사용자")
@BatchSize(size = 50)
class User(
    @Column
    @Comment("아이디")
    override var id: String,

    @Column(name = "NM")
    @Comment("이름")
    var name: String,

    @Column(name = "EML")
    @Comment("이메일")
    var email: String,

    @Column(name = "PSWD")
    @Comment("비밀번호")
    var password: String,

    @Column(name = "AUTHRT")
    @Comment("권한")
    @Enumerated(EnumType.STRING)
    var authority: UserAuthority = UserAuthority.USER,
) : BaseEntity(id) {

    @Column(name = "ENBL_YN")
    @ColumnDefault("true")
    @Comment("사용 가능 여부")
    var enabled: Boolean = true


    fun isRoot(): Boolean {
        return authority == UserAuthority.ROOT
    }

    fun isManager(): Boolean {
        return authority == UserAuthority.MANAGER
    }

    fun isUser(): Boolean {
        return authority == UserAuthority.USER
    }
}
