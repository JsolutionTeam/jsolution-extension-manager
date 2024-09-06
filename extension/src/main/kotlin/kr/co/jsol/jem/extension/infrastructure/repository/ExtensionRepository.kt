package kr.co.jsol.jem.extension.infrastructure.repository

import kr.co.jsol.jem.extension.domain.Extension
import org.springframework.data.repository.CrudRepository

@org.springframework.stereotype.Repository
interface ExtensionRepository : CrudRepository<Extension, String>
