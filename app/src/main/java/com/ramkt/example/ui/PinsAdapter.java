/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.example.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ramkt.example.R;
import com.ramkt.example.response.Pins;
import com.ramkt.example.utils.CommonUtils;

import java.util.List;

/**
 * PinsAdapter class to hold pin detail
 */
public class PinsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = PinsAdapter.class.getSimpleName();
    private List<Pins> mSearchList;
    private Context mContext;
    private IPinAdapterListener mPinListener;

    /**
     * Constructor of the class
     *
     * @param context
     * @param searchList
     * @param recyclerView
     * @param listener
     */
    public PinsAdapter(Context context, List<Pins> searchList, RecyclerView recyclerView, IPinAdapterListener listener) {
        this.mContext = context;
        this.mSearchList = searchList;
        mPinListener = listener;
    }

    /**
     * Override onCreateViewHolder to decide the layout to be used in the view
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pins, parent, false);
        return new PinViewHolder(view);

    }

    /**
     * Override onBindViewHolder method binds the search data to the view holder
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Pins pin = mSearchList.get(position);
        PinViewHolder vh = (PinViewHolder) holder;
        CommonUtils.getInstance().changeImageLayout(vh.imagePin, pin.getHeight() / 4, pin.getColor());
        CommonUtils.getInstance().downloadImage(mContext, vh.imagePin, pin.getImageUrl(), pin.getWidth() / 2, pin.getHeight() / 2, pin.getWidth(), pin.getHeight());
        vh.textCategory.setText(pin.getCategory());
        vh.textUser.setText(pin.getUser().getName());
        vh.textLikes.setText(pin.getLikes());
        vh.linearLayout.setTag(pin);
    }

    /**
     * getItemCount method to get the number of item in the list
     *
     * @return int
     */
    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    /**
     * PinViewHolder class is the main ui view representation of the pin data
     */
    class PinViewHolder extends RecyclerView.ViewHolder {

        private TextView textCategory, textPins, textLikes, textUser;
        private LinearLayout linearLayout;
        private ImageView imagePin;

        public PinViewHolder(View itemView) {
            super(itemView);
            imagePin = (ImageView) itemView.findViewById(R.id.iv_pin_image);
            textCategory = (TextView) itemView.findViewById(R.id.tv_pin_category);
            textPins = (TextView) itemView.findViewById(R.id.tv_pins);
            textLikes = (TextView) itemView.findViewById(R.id.tv_pin_likes);
            textUser = (TextView) itemView.findViewById(R.id.tv_pin_user);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_pin);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPinListener.onItemClick((Pins) view.getTag());
                }
            });
        }
    }

    /**
     * Interface to implemented by framgents({@link PinsFragment})
     */
    public interface IPinAdapterListener {
        void onItemClick(Pins pin);
    }


}
