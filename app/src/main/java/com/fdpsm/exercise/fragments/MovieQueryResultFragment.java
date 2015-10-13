package com.fdpsm.exercise.fragments;

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fdpsm.exercise.ExerciseApplication;
import com.fdpsm.exercise.R;
import com.fdpsm.exercise.dao.DaoSession;
import com.fdpsm.exercise.dao.MovieResult;
import com.fdpsm.exercise.dao.MovieResultDao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.fragment_movie_query_result)
public class MovieQueryResultFragment extends Fragment {

    @ViewById(R.id.queryResultList)
    ListView queryResultList;

    @ViewById(R.id.headlineQueryResult)
    TextView headlineResultQuery;


    ArrayAdapter<MovieResult> adapter;

    public MovieQueryResultFragment() {
    }

    @AfterViews
    public void init() {
        String id = getArguments().getString("id");
        String text = getArguments().getString("text");

        headlineResultQuery.setText("Result for Query: " + text);

        adapter = new ArrayAdapter<MovieResult>(getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<MovieResult>());
        queryResultList.setAdapter(adapter);

        this.getQueryListTask(id);
    }

    @Background
    public void getQueryListTask(String id) {
        updateUIList(loadDBData(id));
    }

    @UiThread
    public void updateUIList(List<MovieResult> list) {
        adapter.clear();

        if (list != null && list.size() > 0) {
            adapter.addAll(list);
        } else {
            Toast.makeText(getActivity(), R.string.noHistoryResult,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private List<MovieResult> loadDBData(String id) {
        DaoSession daoSession = ((ExerciseApplication) getContext().getApplicationContext()).getDaoSession();
        MovieResultDao movieResultDao = daoSession.getMovieResultDao();

        return movieResultDao.queryBuilder()
                .where(MovieResultDao.Properties.QueryId.eq(id)).listLazy();
    }
}
