package com.fdpsm.exercise.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fdpsm.exercise.ExerciseApplication;
import com.fdpsm.exercise.MovieRestClient;
import com.fdpsm.exercise.R;
import com.fdpsm.exercise.dao.DaoSession;
import com.fdpsm.exercise.dao.MovieQuery;
import com.fdpsm.exercise.dao.MovieQueryDao;
import com.fdpsm.exercise.dao.MovieResult;
import com.fdpsm.exercise.dao.MovieResultDao;
import com.fdpsm.exercise.model.Search;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.Calendar;

@EFragment(R.layout.fragment_main)
public class MainActivityFragment extends Fragment {

    @ViewById(R.id.listMovies)
    ListView movieList;

    @ViewById(R.id.searchText)
    EditText searchText;

    @RestService
    MovieRestClient restClient;

    ArrayAdapter<Search.Result> adapter;

    public MainActivityFragment() {
    }

    @AfterViews
    public void init() {
        adapter = new ArrayAdapter<Search.Result>(getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<Search.Result>());
        movieList.setAdapter(adapter);
    }

    @ItemClick(R.id.listMovies)
    public void movieItemClicked(Search.Result item) {

        MovieDetailFragment_ fragment = (MovieDetailFragment_) getFragmentManager().findFragmentByTag(getString(R.string.fragment_tag_detail));
        if (fragment == null || !fragment.isInLayout()) {

            MovieDetailFragment_ detailFragment = new MovieDetailFragment_();
            Bundle bundle = new Bundle();
            bundle.putString("item", item.getTitle());
            detailFragment.setArguments(bundle);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container, detailFragment, getString(R.string.fragment_tag_detail));
            ft.commit();
        }
    }

    @EditorAction(R.id.searchText)
    public void movieSearchTextEntered(TextView v, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String text = String.valueOf(searchText.getText());

            if (!text.isEmpty() && text.length() > 1) {

                getMovieListTask(text);

            } else {

                if (text.length() == 1) {
                    Toast.makeText(getActivity(), R.string.shortSearch,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.emptySearch,
                            Toast.LENGTH_SHORT).show();
                }
                adapter.clear();
            }
        }
    }

    @Click(R.id.historyButton)
    void historyButtonClicked() {

        MovieQueryHistoryFragment_ fragment = (MovieQueryHistoryFragment_) getFragmentManager().findFragmentByTag(getString(R.string.fragment_tag_query_history));
        if (fragment == null || !fragment.isInLayout()) {

            MovieQueryHistoryFragment_ queryFragment = new MovieQueryHistoryFragment_();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container, queryFragment, getString(R.string.fragment_tag_query_history));
            ft.commit();
        }
    }

    @Background
    public void getMovieListTask(String searchText) {
        Search search = restClient.getMovieList(searchText);
        search.setSearchText(searchText);

        createDBEntry(search);

        updateUIList(search.getResult());
    }

    @UiThread
    public void updateUIList(Search.Result[] list) {
        adapter.clear();

        if (list != null && list.length > 0) {
            adapter.addAll(list);
        } else {
            Toast.makeText(getActivity(), R.string.noResult,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void createDBEntry(Search s) {
        DaoSession daoSession = ((ExerciseApplication) getContext().getApplicationContext()).getDaoSession();
        MovieQueryDao movieQueryDao = daoSession.getMovieQueryDao();
        MovieResultDao movieResultDao = daoSession.getMovieResultDao();

        Calendar cal = Calendar.getInstance();

        MovieQuery mq = new MovieQuery();
        mq.setSearchText(s.getSearchText());
        mq.setSearchDate(cal.getTime());

        movieQueryDao.insert(mq);

        // query with no result
        if (s.getResult() != null && s.getResult().length > 0) {
            for (Search.Result i : s.getResult()) {
                MovieResult mr = new MovieResult();
                mr.setTitle(i.getTitle());
                mr.setMovieQuery(mq);
                movieResultDao.insert(mr);
                mq.getMovieResultList().add(mr);
                mq.update();
            }
        }
    }
}