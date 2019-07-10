package com.raju.tripplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TripInviteesAdapter extends RecyclerView.Adapter<TripInviteesAdapter.TripInviteeViewHolder> {

    private Context context;
    private List<User> invitees;

    public TripInviteesAdapter(Context context, List<User> invitees) {
        this.context = context;
        this.invitees = invitees;
    }

    @NonNull
    @Override
    public TripInviteeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trip_invitee, parent, false);
        return new TripInviteeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripInviteeViewHolder holder, int position) {
        User invitee = invitees.get(position);
        holder.bindInviteeData(invitee);
    }

    @Override
    public int getItemCount() {
        return invitees.size();
    }

    public class TripInviteeViewHolder extends RecyclerView.ViewHolder {
        private ImageView inviteeDp;
        private TextView inviteeName;

        public TripInviteeViewHolder(@NonNull View itemView) {
            super(itemView);

            inviteeDp = itemView.findViewById(R.id.invitee_dp);
            inviteeName = itemView.findViewById(R.id.invitee_name);

        }


        public void bindInviteeData(User invitee) {
            Picasso.get().load(Tools.IMAGE_URI + invitee.getDisplayPicture()).into(inviteeDp);
            inviteeName.setText(invitee.getFirstName() + " " + invitee.getFamilyName());
        }
    }

    public void updateInviteeList(List<User> invitees) {
        this.invitees = invitees;
        notifyDataSetChanged();
    }

}
