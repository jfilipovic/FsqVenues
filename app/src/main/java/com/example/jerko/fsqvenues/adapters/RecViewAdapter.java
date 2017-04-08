package com.example.jerko.fsqvenues.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerko.fsqvenues.R;
import com.example.jerko.fsqvenues.models.Venue;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jerko on 6.4.2017..
 */

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.VenueViewHolder>{

    private Activity activity;
    private List<Venue> venues;


    public RecViewAdapter(Activity activity, List<Venue> venues){
        this.activity = activity;
        this.venues = venues;
    }


    @Override
    public int getItemCount() {
        return venues.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public VenueViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        VenueViewHolder venueViewHolder = new VenueViewHolder(v);
        return venueViewHolder;
    }

    @Override
    public void onBindViewHolder(VenueViewHolder venueViewHolder, int i) {
        // todo check nulls
        if (venues.get(i).getName() != null)venueViewHolder.venueName.setText(venues.get(i).getName());
        if (venues.get(i).getCategories().get(0) != null)venueViewHolder.venueType.setText(venues.get(i).getCategories().get(0).getName());
        if (venues.get(i).getRating() != null )venueViewHolder.venueRating.setText(venues.get(i).getRating());
        if (venues.get(i).getLocation() != null && venues.get(i).getLocation().getAddress() != null)venueViewHolder.venueAddress.setText("Address: " + venues.get(i).getLocation().getAddress());
        if (venues.get(i).getPrice() != null)venueViewHolder.venuePrice.setText("Price: " + venues.get(i).getPrice().getMessage());

        if (venues.get(i).getHours() != null && venues.get(i).getHours().getIsOpen() != null){
            if (venues.get(i).getHours().getIsOpen().equalsIgnoreCase("false")){
                venueViewHolder.venueIsOpen.setText("CLOSED");
                venueViewHolder.venueIsOpen.setTextColor(activity.getResources().getColor(R.color.red));
            }
            else {
                venueViewHolder.venueIsOpen.setText("OPEN");
                venueViewHolder.venueIsOpen.setTextColor(activity.getResources().getColor(R.color.green));
            }
        }


        String iconSuffix, iconPrefix;
        iconPrefix = venues.get(i).getCategories().get(0).getIcon().getPrefix();
        iconSuffix = venues.get(i).getCategories().get(0).getIcon().getSuffix();
        if (venues.get(i).getCategories().get(0).getIcon() != null)
            Picasso.with(activity).load(iconPrefix + "88" + iconSuffix ).into(venueViewHolder.venueIcon);
    }

    //refreshing adapter data without instantiating a new adapter
    public void swap(List<Venue> newVenues){
        if (venues != null) {
            venues.clear();
            venues.addAll(newVenues);
        }
        else {
            venues = newVenues;
        }
        notifyDataSetChanged();
    }

    public static class VenueViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView venueName;
        TextView venueRating;
        TextView venuePrice;
        TextView venueType;
        TextView venueIsOpen;
        TextView venueAddress;
        ImageView venueIcon;

        VenueViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            venueName = (TextView)itemView.findViewById(R.id.venue_name);
            venueRating = (TextView)itemView.findViewById(R.id.venue_rating);
            venuePrice = (TextView)itemView.findViewById(R.id.venue_price);
            venueType = (TextView)itemView.findViewById(R.id.venue_type);
            venueIsOpen = (TextView)itemView.findViewById(R.id.venue_isopen);
            venueAddress = (TextView)itemView.findViewById(R.id.venue_address);
            venueIcon= (ImageView)itemView.findViewById(R.id.venue_icon);
        }
    }


}
