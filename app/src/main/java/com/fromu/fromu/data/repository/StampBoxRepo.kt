package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.CoupleDataSource
import com.fromu.fromu.data.remote.datasource.ViewDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.FromCountRes
import com.fromu.fromu.data.remote.network.response.PurchaseStampRes
import com.fromu.fromu.data.remote.network.response.StampCountRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StampBoxRepo @Inject constructor(
    private val viewDataSource: ViewDataSource,
    private val coupleDataSource: CoupleDataSource
) {

    fun getStampList(): Flow<Resource<StampCountRes>> {
        return viewDataSource.getStampList()
    }

    fun getFromCount(): Flow<Resource<FromCountRes>> {
        return viewDataSource.getFromCount()
    }

    fun purchaseStamp(stampId: Int): Flow<Resource<PurchaseStampRes>> {
        return coupleDataSource.getCouplesStamp(stampId)
    }

}