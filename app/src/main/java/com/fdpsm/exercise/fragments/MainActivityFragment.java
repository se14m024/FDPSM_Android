package com.fdpsm.exercise.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fdpsm.exercise.R;
import com.fdpsm.exercise.tasks.MovieListTask;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView movieList = (ListView) view.findViewById(R.id.listMovies);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        movieList.setAdapter(adapter);
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();
                System.out.println("clicked: " + item);

                MovieDetailFragment fragment = (MovieDetailFragment) getFragmentManager().findFragmentByTag(getString(R.string.fragment_tag_detail));
                if (fragment == null || !fragment.isInLayout()) {

                    MovieDetailFragment detailFragment = new MovieDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("item", item);
                    detailFragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.container, detailFragment, getString(R.string.fragment_tag_detail));
                    ft.commit();
                }
            }
        });

        final EditText searchText = (EditText) view.findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = String.valueOf(searchText.getText());

                    if (!text.isEmpty() && text.length() > 1) {
                        System.out.println("searching: " + text);

                        MovieListTask task = new MovieListTask(adapter);
                        task.execute(new String[]{text});

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
                    handled = true;
                }
                return handled;
            }
        });

        return view;
    }
}
