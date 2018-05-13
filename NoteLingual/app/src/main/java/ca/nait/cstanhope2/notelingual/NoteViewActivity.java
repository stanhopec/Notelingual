package ca.nait.cstanhope2.notelingual;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;

public class NoteViewActivity extends BaseActivity {

    DBManager dbManager;
    SQLiteDatabase db;

    static int selectedNoteID;
    static Note selectedNote;

    EditText titleInput;
    EditText contentInput;
    Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        setTitle("");

        //region set objects, basic activity needs
        dbManager = new DBManager(this);
        db = dbManager.getReadableDatabase();

        titleInput = (EditText) findViewById(R.id.editTextNoteName);
        contentInput = (EditText) findViewById(R.id.editTextNoteBody);

        today = new Date();
        today.getDate();
        //endregion

        //region get selected ID from bundle if bundle not empty
        Intent intent = getIntent();
        if(intent.hasExtra(NoteListActivity.BUNDLE_ID_KEY)){
            Bundle bundle = intent.getExtras();
            selectedNoteID = bundle.getInt(NoteListActivity.BUNDLE_ID_KEY);
        } else {
            selectedNoteID = -3; // create a new note
        }
        //endregion

        if(selectedNoteID > 0){ // if a bundle was passed
            //region get note info from database
            Cursor cursor = db.query(DBManager.TABLE_NAME, null, DBManager.COL_ID + " = " + selectedNoteID, null, null, null, null, "1");

            startManagingCursor(cursor);

            //region getting info from cursor, creating the selected Note object
            int id;
            String title, editDate, createdDate, content;

            cursor.moveToFirst(); // only one returned

            id = cursor.getInt(cursor.getColumnIndex(DBManager.COL_ID));
            title = cursor.getString(cursor.getColumnIndex(DBManager.COL_TITLE));
            editDate = cursor.getString(cursor.getColumnIndex(DBManager.COL_DATE_LAST_EDIT));
            createdDate = cursor.getString(cursor.getColumnIndex(DBManager.COL_DATE_CREATED));
            content = cursor.getString(cursor.getColumnIndex(DBManager.COL_CONTENT));

            selectedNote = new Note(id, title, createdDate, editDate, content);
            //endregion

            //endregion

            //region fill edit texts with information
            titleInput.setText(selectedNote.getTitle());
            contentInput.setText(selectedNote.getContent());
            //endregion
        }
    }

    @Override
    protected void onPause() {
        //region get from textboxes
        String newContent = contentInput.getText().toString();
        String enteredTitle = titleInput.getText().toString();
        //endregion

        //region "validation" type stuff
        boolean contactDatabase = true;

        // if no text entered in either field
        if(enteredTitle.length() == 0 && newContent.length() == 0){
            contactDatabase = false;
        }

        // if note has not changed at all (ignores new notes)
        if(selectedNoteID != -3){
            if(enteredTitle.equals(selectedNote.getTitle()) && newContent.equals(selectedNote.getContent())){
                contactDatabase = false;
            }
        }
        //endregion

        if(contactDatabase){
            String newTitle = enteredTitle.length() > 0 ? enteredTitle : newContent.substring(0, newContent.length() < 30 ? newContent.length() : 30).trim();

            //region put into ContentValue
            ContentValues values = new ContentValues();
            values.put(DBManager.COL_TITLE, newTitle);
            values.put(DBManager.COL_CONTENT, newContent);
            values.put(DBManager.COL_DATE_LAST_EDIT, today.toString());
            //endregion

            //region if new note
            if(selectedNoteID == -3){ // new note
                values.put(DBManager.COL_DATE_CREATED, today.toString());

                // add to database
                db.insert(DBManager.TABLE_NAME, null, values);
                Toast.makeText(this, this.getString(R.string.userSuccessCreated), Toast.LENGTH_LONG).show();
            }
            //endregion

            //region if existing note
            else { // updating existing note
                values.put(DBManager.COL_DATE_CREATED, selectedNote.getDateCreated());

                // update in database
                db.update(DBManager.TABLE_NAME, values, DBManager.COL_ID + " = " + selectedNote.getNoteID(), null);
                Toast.makeText(this, this.getString(R.string.userSuccessSaved), Toast.LENGTH_LONG).show();
            }
            //endregion
        }

        super.onPause();
    }

    //region Menu Stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuItemDeleteNote:{
                DeleteNote();
                break;
            }
        }

        return true;
    }
    //endregion

    protected void DeleteNote(){
        db.delete(DBManager.TABLE_NAME, DBManager.COL_ID + " = " + selectedNoteID, null);
        Toast.makeText(this, selectedNote.getTitle() + " was successfully deleted.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, NoteListActivity.class);
        this.startActivity(intent);
    }
}
