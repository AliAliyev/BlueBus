package media.apis.android.example.packagecom.blue_bus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ali on 21/04/2016.
 */
public class RideAdapter extends ArrayAdapter<RideItem> {

    private Activity activity;
    private ArrayList<RideItem> itemList;
    private static LayoutInflater inflater = null;

    public RideAdapter (Activity activity, int textViewResourceId, ArrayList<RideItem> _itemList) {
        super(activity, textViewResourceId, _itemList);
        try {
            this.activity = activity;
            this.itemList = _itemList;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return itemList.size();
    }

    public RideItem getItem(RideItem position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_number;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.ride_item_view, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.route);
                holder.display_number = (TextView) vi.findViewById(R.id.time);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            holder.display_name.setText(itemList.get(position).getRoute());
            holder.display_number.setText(itemList.get(position).getDate());


        } catch (Exception e) {

        }
        return vi;
    }
}
