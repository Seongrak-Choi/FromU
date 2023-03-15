package com.fromu.fromu.model.listener

import com.fromu.fromu.data.dto.MailListResult

interface MailListListener {
    fun onSelect(letterInfo: MailListResult)
}