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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.raju.tripplanner.R;
import com.raju.tripplanner.activities.CreateTripActivity;
import com.raju.tripplanner.activities.ViewTripActivity;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyTripsAdapter extends RecyclerView.Adapter<MyTripsAdapter.MyTripsViewHolder> {

    private Context context;
    private List<Trip> myTripsList;

    public MyTripsAdapter(Context context, List<Trip> myTripsList) {
        this.context = context;
        this.myTripsList = myTripsList;
    }

    @NonNull
    @Override
    public MyTripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myTrip = LayoutInflater.from(context).inflate(R.layout.layout_my_trip, parent, false);
        return new MyTripsViewHolder(myTrip);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTripsViewHolder holder, int position) {
        Trip trip = myTripsList.get(position);
        holder.bindTrip(trip);
    }

    @Override
    public int getItemCount() {
        return myTripsList.size();
    }

    public class MyTripsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private MaterialRippleLayout cardTrip;
        private ImageView myTripImage;
        private TextView myTripTitle, tripStartDate, tripEndDate;
        private ImageButton tripPopup;

        public MyTripsViewHolder(@NonNull View itemView) {
            super(itemView);
            initComponents(itemView);
            itemView.setOnClickListener(this);
        }

        private void initComponents(View view) {
            cardTrip = view.findViewById(R.id.card_trip);
            myTripImage = view.findViewById(R.id.my_trip_image);
            myTripTitle = view.findViewById(R.id.my_trip_title);
            tripStartDate = view.findViewById(R.id.trip_start_date);
            tripEndDate = view.findViewById(R.id.trip_end_date);
            tripPopup = view.findViewById(R.id.btn_trip_popup);
            tripPopup.setOnClickListener(this);
        }

        private void bindTrip(final Trip trip) {
            Picasso.get().load(trip.getDestination().getPhotoUrl()).into(myTripImage);
            myTripTitle.setText(trip.getName());
            tripStartDate.setText(Tools.formatDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMM dd", trip.getStartDate()) + " -");
            tripEndDate.setText(Tools.formatDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMM dd", trip.getEndDate()));

            cardTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewTrip(trip);
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_trip_popup) {
                PopupMenu tripPopupMenu = new PopupMenu(context, v, Gravity.RIGHT);
                tripPopupMenu.inflate(R.menu.menu_trip_popup);
                tripPopupMenu.setOnMenuItemClickListener(this);
                tripPopupMenu.show();
            } else {
                viewTrip(myTripsList.get(getAdapterPosition()));
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_view_trip:
                    viewTrip(myTripsList.get(getAdapterPosition()));
                    return true;

                case R.id.action_edit_trip:
                    Intent editTrip = new Intent(context, CreateTripActivity.class);
                    editTrip.putExtra("Trip", myTripsList.get(getAdapterPosition()));
                    context.startActivity(editTrip);
                    return true;

                default:
                    return false;
            }
        }
    }

    private void viewTrip(Trip trip) {
        Intent viewTrip = new Intent(context, ViewTripActivity.class);
        viewTrip.putExtra("TRIP", trip);
        context.startActivity(viewTrip);
    }

    public void updateData(List<Trip> lists) {
        this.myTripsList = lists;
        notifyDataSetChanged();
    }
}
