package kr.co.jsol.jem.extension.infrastructure.command

import kr.co.jsol.jem.common.infrastructure.repository.BaseCommandRepository
import kr.co.jsol.jem.extension.domain.Extension
import kr.co.jsol.jem.extension.infrastructure.repository.ExtensionRepository
import org.springframework.stereotype.Component

@Component
class ExtensionCommandRepository(repository: ExtensionRepository) : BaseCommandRepository<Extension>(repository)
