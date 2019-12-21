package com.example.architecturestudy.ui.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.architecturestudy.R
import com.example.architecturestudy.data.model.BlogData
import com.example.architecturestudy.data.repository.NaverSearchRepositoryImpl
import com.example.architecturestudy.network.Api
import com.example.architecturestudy.network.ApiClient
import kotlinx.android.synthetic.main.fragment_blog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogFragment : Fragment() {

    val restClient: Api = ApiClient.getRetrofitService(Api::class.java)

    private lateinit var blogAdapter: BlogAdapter

    private val naverSearchRepository by lazy { NaverSearchRepositoryImpl() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        blogAdapter = BlogAdapter()

        recycleview.apply {
            adapter = blogAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            )
        }

        btn_search.setOnClickListener {
            if(input_text != null) {
                val edit = edit_text.text.toString()
                searchBlogList(edit)
            }
        }
    }

    private fun searchBlogList(keyWord: String) {

        naverSearchRepository.getBlog(
            keyword= keyWord,
            success = { responseBlog -> blogAdapter.update(responseBlog.items) },
            fail = { e -> error(message = e.toString())}
        )

//        restClient.requestBlog(keyWord).enqueue(object : Callback<BlogData> {
//
//            override fun onFailure(call: Call<BlogData>, t: Throwable) {
//                error(message = t.toString())
//            }
//
//            override fun onResponse(call: Call<BlogData>, response: Response<BlogData>) {
//                if(response.isSuccessful) {
//                    response.body()?.items?.let {
//                        blogListAdapter()
//                        blogAdapter.update(it)
//                    }
//                }
//            }
//        })
    }



    private fun blogListAdapter() {
        recycleview.apply {
            adapter = blogAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            )
        }
    }
}