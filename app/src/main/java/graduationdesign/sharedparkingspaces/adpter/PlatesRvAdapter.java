package graduationdesign.sharedparkingspaces.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import graduationdesign.sharedparkingspaces.R;

/**
 * Created by lenovo on 2018/4/19.
 */

public class PlatesRvAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PlatesRvAdapter";
    private List<String> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public PlatesRvAdapter(Context context,List<String> data) {
        mContext = context;
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class myViewHolder extends RecyclerView.ViewHolder{
        private void setPlate(String plate) {
            plateTv.setText(plate);
        }

        public TextView plateTv;

        private myViewHolder(View itemView) {
            super(itemView);
            plateTv = (TextView) itemView.findViewById(R.id.plate);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.plate_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((myViewHolder) holder).setPlate(mData.get(position));
        ((myViewHolder) holder).plateTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "long click", Toast.LENGTH_SHORT).show();
                mData.remove(mData.get(position));
                notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }
}
