package layout.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sai Krishna on 04-07-2015.
 */

public class ChatList_Fragment extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    public ChatList_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Information info = new Information();
        View rootView  = inflater.inflate(R.layout.chat_list, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_chatlist);
        setRetainInstance(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        MyRecyclerViewAdapter_ChatList myRecyclerViewAdapter = new MyRecyclerViewAdapter_ChatList(getActivity(), info);
        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter_ChatList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Information.Block_Contact person=info.getItem(position);
                Chat_Fragment fragment=Chat_Fragment.newInstance(person);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                        .addToBackStack(null).commit();
            }
        });
        recyclerView.setAdapter(myRecyclerViewAdapter);
        int option = this.getArguments().getInt(ARG_OPTION);
        return rootView;
    }

    public static ChatList_Fragment newInstance(int option) {
        ChatList_Fragment fragment = new ChatList_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }
}

