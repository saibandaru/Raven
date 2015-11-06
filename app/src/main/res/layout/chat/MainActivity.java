package layout.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ChatList_Fragment.newInstance(R.id.chat_list))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.chat_list:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, ChatList_Fragment.newInstance(id))
                        .commit();
                break;
            case R.id.contacts:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, Contacts.newInstance(id))
                        .commit();
                break;
            case R.id.about_me:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, AboutMe_Fragment.newInstance(id))
                        .commit();
                break;
            case R.id.selection_sqlite:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, SqLite.newInstance(0))
                        .commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, ChatList_Fragment.newInstance(id))
                        .commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
