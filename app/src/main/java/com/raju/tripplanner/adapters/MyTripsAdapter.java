package com.raju.tripplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.raju.tripplanner.R;
import com.raju.tripplanner.activities.ViewTripActivity;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyTripsAdapter extends RecyclerView.Adapter<MyTripsAdapter.MyTripsViewHolder> implements android.widget.PopupMenu.OnMenuItemClickListener {

    private Context context;
    private List<Trip> myTripsList;
    private Trip trip;

    public MyTripsAdapter(Context context, List<Trip> myTripsList) {
        this.context = context;
        this.myTripsList = myTripsList;
    }

    @NonNull
    @Override
    public MyTripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myTrips = LayoutInflater.from(context).inflate(R.layout.layout_my_trip, parent, false);
        return new MyTripsViewHolder(myTrips);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTripsViewHolder holder, int position) {
        trip = myTripsList.get(position);
        holder.bindTrip(trip);
    }

    @Override
    public int getItemCount() {
        return myTripsList.size();
    }

    public class MyTripsViewHolder extends RecyclerView.ViewHolder {
        private MaterialRippleLayout cardTrip;
        private ImageView myTripImage;
        private TextView myTripTitle, tripStartDate, tripEndDate;
        private ImageButton tripPopup;

        public MyTripsViewHolder(@NonNull View itemView) {
            super(itemView);
            initComponents(itemView);
        }

        private void initComponents(View view) {
            cardTrip = view.findViewById(R.id.card_trip);
            myTripImage = view.findViewById(R.id.my_trip_image);
            myTripTitle = view.findViewById(R.id.my_trip_title);
            tripStartDate = view.findViewById(R.id.trip_start_date);
            tripEndDate = view.findViewById(R.id.trip_end_date);
            tripPopup = view.findViewById(R.id.btn_trip_popup);
            tripPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTripPopup(v);
                }
            });
        }

        private void bindTrip(Trip trip) {
            Picasso.get().load(trip.getDestination().getPhotoUrl()).into(myTripImage);
            myTripTitle.setText(trip.getName());
            tripStartDate.setText(Tools.formatDate(trip.getStartDate()) + " -");
            tripEndDate.setText(Tools.formatDate(trip.getEndDate()));

            cardTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewTrip(trip);
                }
            });
        }
    }

    private void showTripPopup(View anchorView) {
        PopupMenu tripPopupMenu = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            tripPopupMenu = new PopupMenu(context, anchorView, Gravity.END);
        } else {
            tripPopupMenu = new PopupMenu(context, anchorView);
        }
        tripPopupMenu.inflate(R.menu.menu_trip_popup);
        tripPopupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        tripPopupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_trip:
//                viewTrip();
                return true;

            case R.id.action_delete_trip:
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }

    private void viewTrip(Trip trip) {
        Intent viewTrip = new Intent(context, ViewTripActivity.class);
        viewTrip.putExtra("TRIP", trip);
        context.startActivity(viewTrip);
    }
}
