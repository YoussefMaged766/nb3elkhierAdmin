package com.devYoussef.nb3elkhieradmin.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.model.dummyProduct
import com.devYoussef.nb3elkhieradmin.model.toDomain
import com.devYoussef.nb3elkhieradmin.utils.WebServices

class SearchPagingSource(
    private val webServices: WebServices,
    private val query:String
) :
    PagingSource<Int, dummyProduct>() {
    override fun getRefreshKey(state: PagingState<Int, dummyProduct>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, dummyProduct> {
        return try {
            val currentPage = params.key ?: 1

            val response = webServices.getSearch(page = currentPage , query = query).data?.map {
                it.toDomain(currentPage)
            }
            Log.e( "loadPaging: ",query.toString())

            val nextPageNumber =  if (response.isNullOrEmpty()) null  else currentPage + 1

            LoadResult.Page(
                response?: listOf(),
                prevKey = null,
                nextKey = nextPageNumber
            )

        } catch (e: Throwable) {
            Log.e("load: ", e.message.toString())
            LoadResult.Error(e)
        }
    }
}