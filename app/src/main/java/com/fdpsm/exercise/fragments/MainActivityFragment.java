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

import com.fdpsm.exercise.MovieRestClient;
import com.fdpsm.exercise.R;
import com.fdpsm.exercise.model.Search;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;

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
        System.out.println("clicked: " + item.getTitle());

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
                System.out.println("searching: " + text);

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

    @Background
    public void getMovieListTask(String searchText) {
        Search search = restClient.getMovieList(searchText);
        updateUIList(search.getResult());
    }

    @UiThread
    public void updateUIList(Search.Result[] list) {
        adapter.clear();

        if(list!= null && list.length>0) {
            adapter.addAll(list);
        }
        else
        {
            Toast.makeText(getActivity(), R.string.noResult,
                    Toast.LENGTH_SHORT).show();
        }
    }
}