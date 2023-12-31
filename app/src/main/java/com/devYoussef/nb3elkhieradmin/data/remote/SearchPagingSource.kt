package com.devYoussef.nb3elkhieradmin.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.utils.WebServices

class SearchPagingSource(
    private val webServices: WebServices,
    private val query:String
) :
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

            val response = webServices.getSearch(page = currentPage , query = query)
            Log.e( "loadPaging: ",query.toString())

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