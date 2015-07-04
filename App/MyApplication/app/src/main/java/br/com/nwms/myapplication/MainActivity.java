package br.com.nwms.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    UserLocalStore userLocalStore;
    EditText etUsername;
    Button bLogout;

    MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        bLogout = (Button) findViewById(R.id.bLogout);

        bLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            //Generate list View from ArrayList
            displayListView();
            checkButtonClick();
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayListView() {
        User user = userLocalStore.getLoggedInUser();
        etUsername.setText(user.username);

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(user.listaDisciplinas);
            JSONArray jArray = jsonObj.getJSONArray("disciplinas");

            ArrayList<Disciplina> listaDisciplina = new ArrayList<>();

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObj = jArray.getJSONObject(i);
                Disciplina disciplina = new Disciplina(jObj.getInt("disciplina_pk"), jObj.getString("disciplina_nome"), jObj.getBoolean("selected"));
                listaDisciplina.add(disciplina);
            }

            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(this, R.layout.disciplinas_info, listaDisciplina);
            ListView listView = (ListView) findViewById(R.id.listView1);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Disciplina disciplina = (Disciplina) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                            "Clicked on Row: " + disciplina.getDisciplina_nome(),
                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Disciplina> {

        private ArrayList<Disciplina> disciplinaList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Disciplina> disciplinaList) {
            super(context, textViewResourceId, disciplinaList);
            this.disciplinaList = new ArrayList<Disciplina>();
            this.disciplinaList.addAll(disciplinaList);
        }

        private class ViewHolder {
            //TextView disciplina_pk;
            CheckBox disciplina_nome;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.disciplinas_info, null);

                holder = new ViewHolder();
                //holder.disciplina_pk = (TextView) convertView.findViewById(R.id.code);
                holder.disciplina_nome = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.disciplina_nome.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Disciplina disciplina = (Disciplina) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Click no Checkbox: " + cb.getText() +
                                        " e " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        disciplina.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Disciplina disciplina = disciplinaList.get(position);
            //holder.disciplina_pk.setText(" (" +  disciplina.getDisciplina_pk() + ")");
            holder.disciplina_nome.setText(disciplina.getDisciplina_nome());
            holder.disciplina_nome.setChecked(disciplina.isSelected());
            holder.disciplina_nome.setTag(disciplina);

            return convertView;
        }

    }

    private void checkButtonClick() {

        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("Foram selecionadas as seguintes...\n");

                ArrayList<Disciplina> disciplinaList = dataAdapter.disciplinaList;
                for (int i = 0; i < disciplinaList.size(); i++) {
                    Disciplina disciplina = disciplinaList.get(i);
                    if (disciplina.isSelected()) {
                        responseText.append("\n" + disciplina.getDisciplina_nome());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });

    }
}