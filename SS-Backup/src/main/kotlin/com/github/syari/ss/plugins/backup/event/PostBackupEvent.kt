package com.github.syari.ss.plugins.backup.event

import com.github.syari.ss.plugins.backup.BackupGroup
import com.github.syari.ss.plugins.core.event.CustomEvent

class PostBackupEvent(val groups: List<BackupGroup>): CustomEvent()