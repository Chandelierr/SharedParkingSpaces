package graduationdesign.sharedparkingspaces.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationdesign.sharedparkingspaces.R;
import graduationdesign.sharedparkingspaces.adpter.PlatesRvAdapter;

/**
 * Created by wangmengjie on 2018/4/19.
 */

public class PlateManageView extends RelativeLayout {
    private static final String TAG = "PlateManageView";

    private PlatesRvAdapter mAdapter;
    private List<String> mData;

    private Button mAddPlate;
    private View mFillInView;
    private EditText mEditPlate;
    private RecyclerView mPlatesRv;

    public PlateManageView(Context context) {
        this(context, null);
    }

    public PlateManageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlateManageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        inflate(context, R.layout.plate_manage, this);
        mAddPlate = (Button) findViewById(R.id.add_plate);
        mAddPlate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFillInView.setVisibility(View.VISIBLE);
            }
        });
        mFillInView = findViewById(R.id.fill_in_plate);
        mEditPlate = (EditText) findViewById(R.id.edit_plate);
        findViewById(R.id.sure_to_add_plate).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String plateName = mEditPlate.getText().toString();
                if (isPlateValid(plateName)) {
                    mData.add(plateName);
                    Log.d(TAG, "add plate: " + plateName);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.plate_invalid), Toast.LENGTH_SHORT).show();
                }
                mEditPlate.setText("");
                mFillInView.setVisibility(View.GONE);
            }
        });
        mPlatesRv = (RecyclerView) findViewById(R.id.plates_recycler);
        mPlatesRv.setLayoutManager(new LinearLayoutManager(context));
    }

    private boolean isPlateValid(String plateName) {
        if (mData.contains(plateName)) {
            return false;
        }
        Pattern p;
        Matcher m;
        String reg = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
        p = Pattern.compile(reg);
        m = p.matcher(plateName);
        Log.d(TAG, "match: " + m.matches());
        return m.matches();
    }

    public void setPlatesData(List<String> plates) {
        mData = plates;
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new PlatesRvAdapter(getContext(), mData);
        }
        mPlatesRv.setAdapter(mAdapter);
    }

    public List<String> getData() {
        return mData;
    }
}
