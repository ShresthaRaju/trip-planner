package com.raju.tripplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.tripplanner.R;
import com.raju.tripplanner.fragments.HomeFragment;
import com.raju.tripplanner.models.MapResult.Place;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.LodgeViewHolder> {

    private Context context;
    private List<Place> lodgeList;

    public PlacesAdapter(Context context, List<Place> lodgeList) {
        this.context = context;
        this.lodgeList = lodgeList;
    }

    @NonNull
    @Override
    public LodgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View restaurantView = LayoutInflater.from(context).inflate(R.layout.layout_place, parent, false);
        return new LodgeViewHolder(restaurantView);
    }

    @Override
    public void onBindViewHolder(@NonNull LodgeViewHolder holder, int position) {
        Place lodge = lodgeList.get(position);
        holder.bindLodgeData(lodge);
    }

    @Override
    public int getItemCount() {
        return lodgeList.size();
    }

    public class LodgeViewHolder extends RecyclerView.ViewHolder {
        private ImageView lodgeImage;
        private TextView lodgeName;
        private AppCompatRatingBar lodgeRating;

        public LodgeViewHolder(@NonNull View itemView) {
            super(itemView);

            lodgeImage = itemView.findViewById(R.id.place_image);
            lodgeName = itemView.findViewById(R.id.place_name);
            lodgeRating = itemView.findViewById(R.id.place_rating);
        }

        public void bindLodgeData(Place lodge) {
            String imageUri = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=" + lodge.getPhotos().get(0).getPhoto_reference() + "&key=" + HomeFragment.MAP_API;
            Picasso.get().load(imageUri).into(lodgeImage);
            lodgeName.setText(lodge.getName());
            lodgeRating.setRating(lodge.getRating());
        }
    }
}
