package com.raju.tripplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raju.tripplanner.R;
import com.raju.tripplanner.models.InvitationItem;
import com.raju.tripplanner.utils.Tools;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InvitationAutocompleteAdapter extends ArrayAdapter<InvitationItem> {

    private List<InvitationItem> invitationItemsAll;

    public InvitationAutocompleteAdapter(@NonNull Context context, @NonNull List<InvitationItem> invitationItems) {
        super(context, 0, invitationItems);
        invitationItemsAll = new ArrayList<>(invitationItems);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return invitationFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_invitation_dropdown, parent, false);
        }

        ImageView userDp = convertView.findViewById(R.id.invitation_user_dp);
        TextView username = convertView.findViewById(R.id.invitation_username);

        InvitationItem invitationItem = getItem(position);
        if (invitationItem != null) {
            Picasso.get().load(Tools.IMAGE_URI + invitationItem.getUserDp()).into(userDp);
            username.setText(invitationItem.getUsername());
        }
        return convertView;
    }

    private Filter invitationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<InvitationItem> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(invitationItemsAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InvitationItem invitationItem : invitationItemsAll) {
                    if (invitationItem.getUsername().toLowerCase().contains(filterPattern)) {
                        suggestions.add(invitationItem);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((InvitationItem) resultValue).getUsername();
        }
    };
}
