package com.devYoussef.nb3elkhieradmin.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.utils.WebServices

class ProductsPagingSource(
    private val webServices: WebServices,

) :
    PagingSource<Int, ProductResponse.Data>() {
    override fun getRefreshKey(state: PagingState<Int, ProductResponse.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductResponse.Data> {
        return try {
            val currentPage = params.key ?: 1

            val response = webServices.getProducts(page = currentPage )

            val nextPageNumber = if ( response.data!!.isNotEmpty()) currentPage + 1 else null

            LoadResult.Page(
                response.data,
                prevKey = null,
                nextKey = nextPageNumber
            )

        } catch (e: Throwable) {
            Log.e("load: ", e.message.toString())
            LoadResult.Error(e)
        }
    }
}