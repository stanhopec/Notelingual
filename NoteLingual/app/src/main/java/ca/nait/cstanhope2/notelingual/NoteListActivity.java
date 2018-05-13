package ca.nait.cstanhope2.notelingual;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class NoteListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    DBManager dbManager;
    SQLiteDatabase db;
    String orderBy;

    ArrayList<Note> noteArray;
    ListView noteListView;

    // for bundles
    static final String BUNDLE_ID_KEY = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // need this to show the app title with the correct translation
        setTitle(R.string.app_name);

        //region instantiate objects, set on click listeners...
        dbManager = new DBManager(this);
        noteListView = (ListView) findViewById(R.id.listViewNoteList);
        noteListView.setOnItemClickListener(this); // make clickable

        Button floatingButton = (Button) findViewById(R.id.buttonFloating);
        floatingButton.setOnClickListener(this);
        //endregion

        //region get sort order from preferences
        String sortType = prefs.getString(this.getString(R.string.prefsSortKey), this.getString(R.string.prefsSortDefaultValue));
        switch(sortType){
            case "alpha":{
                orderBy = DBManager.COL_TITLE + " asc";
                break;
            }
            case "editDate":{
                orderBy = DBManager.COL_DATE_LAST_EDIT + " asc";
                break;
            }
            case "createdDate":{
                orderBy = DBManager.COL_DATE_CREATED + " asc";
                break;
            }
            default:{
                orderBy = DBManager.COL_TITLE + " asc";
                break;
            }
        }
        //endregion

        populateList();
    }

    @Override
    protected void onResume() {
        populateList();
        super.onResume();
    }

    public void populateList(){
        noteArray = new ArrayList<Note>();

        db = dbManager.getReadableDatabase();
        Cursor cursor = db.query(DBManager.TABLE_NAME, null, null, null, null, null, orderBy);

        startManagingCursor(cursor);

        //region populate the array version
        int id;
        String title, editDate, createdDate, content;

        while(cursor.moveToNext()){
            //region get all the stuff from the current cursor position
            id = cursor.getInt(cursor.getColumnIndex(DBManager.COL_ID));
            title = cursor.getString(cursor.getColumnIndex(DBManager.COL_TITLE));
            editDate = cursor.getString(cursor.getColumnIndex(DBManager.COL_DATE_LAST_EDIT));
            createdDate = cursor.getString(cursor.getColumnIndex(DBManager.COL_DATE_CREATED));
            content = cursor.getString(cursor.getColumnIndex(DBManager.COL_TITLE));
            //endregion

            Note temp = new Note(id, title, createdDate, editDate, content);

            noteArray.add(temp);
        }
        //endregion

        String[] from ={DBManager.COL_TITLE};
        int[] to = {R.id.textViewRowNoteTitle};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_note_list, cursor, from, to, 0);
        noteListView.setAdapter(adapter);
    }

    @Override
    // used for when a note is clicked on in the list
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Note selectedNote = noteArray.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID_KEY, selectedNote.getNoteID());

        Intent intent = new Intent(this, NoteViewActivity.class);
        intent.putExtras(bundle);

        this.startActivity(intent);
    }

    @Override
    // for button
    public void onClick(View v) {
        Intent intent = new Intent(this, NoteViewActivity.class);
        this.startActivity(intent);
    }

    //region Menu Stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuItemPreferences:{
                startActivity(new Intent(this, UserPrefsActivity.class));
                break;
            }
        }

        return true;
    }
    //endregion
}
