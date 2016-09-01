package ua.asd.musicaround;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {

    static final String RES_CITATION = "RES_CITATION";
    int resCitation;

    /**
     * Create a new instance of PageFragment, providing "page"
     * as an argument.
     */

    static PageFragment newIntent(PresentationInfo info) {
        PageFragment pageFragment = new PageFragment();
        Bundle argument = new Bundle();
        argument.putInt(RES_CITATION, info.getCitation());
        pageFragment.setArguments(argument);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resCitation = getArguments().getInt(RES_CITATION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_page_fragment, null);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(resCitation);
        return view;
    }
}
