package com.mobotechnology.bipinpandey.retrofit_handdirty.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobotechnology.bipinpandey.retrofit_handdirty.Listners.RecyclerClickListener;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.ExpertInfo;
import com.mobotechnology.bipinpandey.retrofit_handdirty.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Fills my topics list with response data
 */

public class ChatLIstingAdapter extends RecyclerView.Adapter<ChatLIstingAdapter.ViewHolder> {
    private static final String TAG = ChatLIstingAdapter.class.getSimpleName();
    private RecyclerClickListener recyclerItemClickListener;
    private List<ExpertInfo> experts;
    private Context context;
    private int KEY_PERMISSION = 0;
    private String permissionsAsk[];
    int globalPosition;
    boolean permissionGranted;

    public ChatLIstingAdapter(ArrayList<ExpertInfo> experts, RecyclerClickListener recyclerItemClickListener) {
        this.experts = experts;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public ChatLIstingAdapter(List<ExpertInfo> expertsList, Context context) {
        this.experts = expertsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        globalPosition = holder.getAdapterPosition();
        holder.tvExpertName.setText(experts.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return experts.size();
    }

    public List<ExpertInfo> returnAllergenList() {
        return experts;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvExpertName;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvExpertName = (TextView) itemView.findViewById(R.id.txtExperts);
        }

    }
}
