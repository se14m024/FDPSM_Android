package com.fdpsm.exercise.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fdpsm.exercise.ExerciseApplication;
import com.fdpsm.exercise.R;
import com.fdpsm.exercise.dao.DaoSession;
import com.fdpsm.exercise.dao.MovieQuery;
import com.fdpsm.exercise.dao.MovieQueryDao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.fragment_movie_query_history)
public class MovieQueryHistoryFragment extends Fragment {

    @ViewById(R.id.queryList)
    ListView queryList;

    ArrayAdapter<MovieQuery> adapter;

    public MovieQueryHistoryFragment() {
    }

    @AfterViews
    public void init() {
        adapter = new ArrayAdapter<MovieQuery>(getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<MovieQuery>());
        queryList.setAdapter(adapter);

        this.getQueryListTask();
    }

    @Background
    public void getQueryListTask() {
        updateUIList(loadDBData());
    }

    @UiThread
    public void updateUIList(List<MovieQuery> list) {
        adapter.clear();

        if (list != null && list.size() > 0) {
            adapter.addAll(list);
        } else {
            Toast.makeText(getActivity(), R.string.noHistory,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private List<MovieQuery> loadDBData() {
        DaoSession daoSession = ((ExerciseApplication) getContext().getApplicationContext()).getDaoSession();
        MovieQueryDao movieQueryDao = daoSession.getMovieQueryDao();

        return movieQueryDao.queryBuilder().listLazy();
    }

    @ItemClick(R.id.queryList)
    public void historyItemClicked(MovieQuery item) {

        MovieQueryResultFragment_ fragment = (MovieQueryResultFragment_) getFragmentManager().findFragmentByTag(getString(R.string.fragment_tag_query_result));
        if (fragment == null || !fragment.isInLayout()) {

            MovieQueryResultFragment_ resultFragment = new MovieQueryResultFragment_();
            Bundle bundle = new Bundle();
            bundle.putString("id", String.valueOf(item.getId()));
            bundle.putString("text", item.getSearchText());
            resultFragment.setArguments(bundle);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container, resultFragment, getString(R.string.fragment_tag_query_result));
            ft.commit();
        }
    }
}