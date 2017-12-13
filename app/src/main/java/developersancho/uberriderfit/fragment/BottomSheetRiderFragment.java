package developersancho.uberriderfit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import developersancho.uberriderfit.R;

/**
 * Created by developersancho on 10.12.2017.
 */

public class BottomSheetRiderFragment extends BottomSheetDialogFragment {

    String mLocation, mDestination;

    public static BottomSheetRiderFragment newInstance(String location, String destination) {
        BottomSheetRiderFragment f = new BottomSheetRiderFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        args.putString("destination", destination);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Toast.makeText(getContext(),"location: "+getArguments().getString("location") +
        " and destination: "+getArguments().getString("destination"),Toast.LENGTH_LONG).show();*/
        mLocation = getArguments().getString("location");
        mDestination = getArguments().getString("destination");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_rider, container, false);
        TextView txtLocation = (TextView) view.findViewById(R.id.txtLocation);
        TextView txtDestination = (TextView) view.findViewById(R.id.txtDestination);
        TextView txtCalculate = (TextView) view.findViewById(R.id.txtCalculate);

        // set data
        txtLocation.setText(mLocation);
        txtDestination.setText(mDestination);

        return view;
    }
}
