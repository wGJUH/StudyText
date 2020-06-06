package com.studypoem.byheart2.ui.home

import android.os.Bundle
import android.text.Spannable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.studypoem.byheart2.MainActivity
import com.studypoem.byheart2.R
import com.studypoem.byheart2.core.ObjectAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class StudyFragment : Fragment() {

    private val viewModel: StudyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    private val adapter = ObjectAdapter(arrayListOf<Spannable>(), SpannableItemPresenter())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.adapter = adapter
        viewModel.preparedText.observe(viewLifecycleOwner, Observer {
            when (it) {
                Result.Loading -> progressBar.visibility = View.VISIBLE
                is Result.Success -> onTextPrepareSuccess(it.result as List<*>)
                is Result.Error -> onTextPrepareError(it.thr)
            }

        })
        fab.setOnClickListener {
            viewModel.onHideClicked()
        }
        viewModel.updateObserver.observe(
            viewLifecycleOwner,
            Observer { adapter.notifyDataSetChanged() })
    }

    private fun onTextPrepareError(thr: Throwable) {
        progressBar.visibility = View.INVISIBLE
    }

    private fun onTextPrepareSuccess(strings: List<*>) {
        progressBar.visibility = View.INVISIBLE
        adapter.setItems(strings)
    }
}
