package layout.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sai Krishna on 04-07-2015.
 */
public class AboutMe_Fragment extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    public AboutMe_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.aboutme, container, false);
        int option = this.getArguments().getInt(ARG_OPTION);

        return rootView;
    }

    public static AboutMe_Fragment newInstance(int option) {
        AboutMe_Fragment fragment = new AboutMe_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OPTION, option);
        fragment.setArguments(args);
        return fragment;

    }
}

