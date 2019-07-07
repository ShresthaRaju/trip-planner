package com.raju.tripplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.tripplanner.DaoImpl.InvitationDaoImpl;
import com.raju.tripplanner.R;
import com.raju.tripplanner.activities.ViewTripActivity;
import com.raju.tripplanner.models.Invitation;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyInvitationsAdapter extends RecyclerView.Adapter<MyInvitationsAdapter.InvitationViewHolder> {

    private Context context;
    private List<Invitation> myInvitationList;
    private InvitationDaoImpl invitationDaoImpl;

    public MyInvitationsAdapter(Context context, List<Invitation> myInvitationList) {
        this.context = context;
        this.myInvitationList = myInvitationList;
        invitationDaoImpl = new InvitationDaoImpl(context);
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_invitation_item, parent, false);
        return new InvitationViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {
        Invitation invitation = myInvitationList.get(position);
        holder.bindInvitationData(invitation);
    }

    @Override
    public int getItemCount() {
        return myInvitationList.size();
    }

    public class InvitationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView inviterDp;
        private TextView tvInvitation, tvInvitationTime, tvAccepted;
        private Button btnDecline, btnAccept;

        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);

            inviterDp = itemView.findViewById(R.id.inviter_dp);
            tvInvitation = itemView.findViewById(R.id.txt_invitation);
            tvInvitationTime = itemView.findViewById(R.id.txt_invitation_time);
            tvAccepted = itemView.findViewById(R.id.tv_accepted);
            btnDecline = itemView.findViewById(R.id.btn_decline);
            btnDecline.setOnClickListener(this);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnAccept.setOnClickListener(this);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_decline:
                    Invitation declineInvitation = myInvitationList.get(getAdapterPosition());
                    invitationDaoImpl.declineInvitation(declineInvitation.getId());
                    break;

                case R.id.btn_accept:
                    Invitation acceptInvitation = myInvitationList.get(getAdapterPosition());
                    invitationDaoImpl.acceptInvitation(acceptInvitation.getId());
                    break;

                default:
                    viewTrip(myInvitationList.get(getAdapterPosition()).getInvitedTo());
                    break;
            }
        }

        public void bindInvitationData(Invitation invitation) {
            String inviter = invitation.getInviter().getFirstName() + " " + invitation.getInviter().getFamilyName();
            String text = " has invited you to ";
            String tripTitle = invitation.getInvitedTo().getName();
            String invitationText = inviter + text + tripTitle;

            // for making part of a text in a textview bold
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(invitationText);
            stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), invitationText.indexOf(inviter), inviter.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), invitationText.indexOf(tripTitle), invitationText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Picasso.get().load(Tools.IMAGE_URI + invitation.getInviter().getDisplayPicture()).into(inviterDp);
            tvInvitation.setText(stringBuilder);

            if (invitation.isSeen()) {
                tvAccepted.setVisibility(View.VISIBLE);
                btnDecline.setVisibility(View.GONE);
                btnAccept.setVisibility(View.GONE);
            } else {
                tvAccepted.setVisibility(View.GONE);
                btnDecline.setVisibility(View.VISIBLE);
                btnAccept.setVisibility(View.VISIBLE);
            }
            tvInvitationTime.setText(Tools.formatDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMM d, h:mm a", invitation.getInvitedOn()));
        }
    }

    public void updateInvitationList(List<Invitation> newInvitationList) {
        this.myInvitationList = newInvitationList;
        notifyDataSetChanged();
    }

    private void viewTrip(Trip trip) {
        Intent viewTrip = new Intent(context, ViewTripActivity.class);
        viewTrip.putExtra("TRIP", trip);
        context.startActivity(viewTrip);
    }
}
