package layout.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Sai Krishna on 04-07-2015.
 */
public class Chat_Fragment extends Fragment {

    private static final String ARG_OPTION = "argument_option";


    public Chat_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Information.Block_Contact person=(Information.Block_Contact)this.getArguments().getSerializable(ARG_OPTION);
        final List<Information.Message> messages= person.getMessageList();

        View rootView  = inflater.inflate(R.layout.fragment_main, container, false);
        Button send=(Button)rootView.findViewById(R.id.button);
        final EditText one_message=(EditText)rootView.findViewById(R.id.editText);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        setRetainInstance(true);        recyclerView.setHasFixedSize(true);        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());        recyclerView.setLayoutManager(mLayoutManager);

         final MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), messages);
        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //select_movie_recycle(v, position);
                //HashMap<String,?> movie_new=(HashMap<String,?>)movieData.getItem(position);
            }
        });
        recyclerView.setAdapter(myRecyclerViewAdapter);
        recyclerView.scrollToPosition(messages.size() - 1);
        int option = this.getArguments().getInt(ARG_OPTION);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oneMsg_content = one_message.getText().toString();
                Information info=new Information();
                Information.Message msg=info.createMessage(oneMsg_content,true);
                messages.add(msg);
                myRecyclerViewAdapter.notifyDataSetChanged();
                one_message.setText(null);
            }
        });
        return rootView;
    }

    public static Chat_Fragment newInstance(Information.Block_Contact contact) {
        Chat_Fragment fragment = new Chat_Fragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_OPTION, contact);
        fragment.setArguments(args);
        return fragment;

    }
}
