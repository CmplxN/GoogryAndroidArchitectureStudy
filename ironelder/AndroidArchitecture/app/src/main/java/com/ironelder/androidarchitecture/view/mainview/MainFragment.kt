package com.ironelder.androidarchitecture.view.mainview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ironelder.androidarchitecture.R
import com.ironelder.androidarchitecture.common.BLOG
import com.ironelder.androidarchitecture.common.TYPE_KEY
import com.ironelder.androidarchitecture.component.CustomListViewAdapter
import com.ironelder.androidarchitecture.data.ResultItem
import com.ironelder.androidarchitecture.data.TotalModel
import com.ironelder.androidarchitecture.data.source.SearchDataSourceImpl
import com.ironelder.androidarchitecture.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_search_listview.*


class MainFragment : BaseFragment(R.layout.fragment_main), MainContract.View {
    override fun showNoSearchData() {
        noSearchLayout.visibility = View.VISIBLE
    }

    override fun onDataChanged(result: ArrayList<ResultItem>) {
        (rv_resultListView?.adapter as? CustomListViewAdapter)?.setItemList(result)
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            .show()
    }

    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

    private val mType: String? by lazy {
        arguments?.getString(TYPE_KEY)
    }

    private val mPresenter: MainPresenter? by lazy {
        MainPresenter(this)
    }

    override fun doViewCreated(view: View, savedInstanceState: Bundle?) {
        with(rv_resultListView) {
            adapter =
                CustomListViewAdapter(mType ?: BLOG)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager(context).orientation
                )
            )
        }
    }

    override fun doCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchView =
            SearchView((context as? MainActivity)?.supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.action_search)?.apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    Toast.makeText(
                        context,
                        getString(R.string.msg_empty_search_string),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    SearchDataSourceImpl.getDataForSearch(
                        mType ?: BLOG,
                        query,
                        ::onSuccess,
                        ::onFail
                    )
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun onSuccess(result: TotalModel) {
        (rv_resultListView?.adapter as? CustomListViewAdapter)?.setItemList(result.items)
    }

    private fun onFail(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        fun newInstance(type: String?): MainFragment {
            return MainFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE_KEY, type)
                }
            }
        }
    }
}