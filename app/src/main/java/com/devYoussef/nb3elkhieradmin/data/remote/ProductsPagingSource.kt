package com.devYoussef.nb3elkhieradmin.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devYoussef.nb3elkhieradmin.data.local.DataStoreRepository
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.utils.WebServices
import javax.inject.Inject

class ProductsPagingSource @Inject constructor(
    private val webServices: WebServices,
    private val dataStoreRepository: DataStoreRepository
):
PagingSource<Int, ProductResponse.Data>() {

    override fun getRefreshKey(state: PagingState<Int, ProductResponse.Data>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductResponse.Data> {
        return try {

            val currentPage = params.key ?: 1
//            dataStoreRepository.savePageNumber("page", currentPage)

            val response = webServices.getProducts(page = currentPage )

            val nextPageNumber = if ( response.data!!.isNotEmpty()) currentPage + 1 else null

            LoadResult.Page(
                response.data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = nextPageNumber
            )

        } catch (e: Throwable) {
            Log.e("load: ", e.message.toString())
            LoadResult.Error(e)
        }
    }
}