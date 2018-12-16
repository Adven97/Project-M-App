package advenstudios.prostamapav2;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    Activity context;
    ArrayList<User> users;
    private static LayoutInflater inflater = null;

    public UserAdapter(Activity context, ArrayList<User> users){
        this.context=context;
        this.users=users;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return users.size();
    }

    @Override
    public User getItem(int nr){ return users.get(nr); }

    @Override
    public long getItemId(int nr){
        return nr;
    }

    @Override
    public View getView(int nr, View convertView, ViewGroup parent){
        View itemView = convertView;
        itemView = (itemView==null)? inflater.inflate(R.layout.adapter, null) : itemView;
        TextView tvImie = (TextView)itemView.findViewById(R.id.Imie);
        TextView tvNazwisko = (TextView)itemView.findViewById(R.id.Nazwisko);
        User selectedKontakt = users.get(nr);
        tvImie.setText(selectedKontakt.getFirstName());
        tvNazwisko.setText(selectedKontakt.getLastName());
        return itemView;
    }


}


