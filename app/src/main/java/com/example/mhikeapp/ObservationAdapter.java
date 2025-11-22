package com.example.mhikeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObsViewHolder> {

    private Context context;
    private List<Observation> obsList;
    private DatabaseHelper dbHelper;

    public ObservationAdapter(Context context, List<Observation> obsList, DatabaseHelper dbHelper) {
        this.context = context;
        this.obsList = obsList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ObsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.observation_item, parent, false);
        return new ObsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObsViewHolder holder, int position) {
        Observation obs = obsList.get(position);

        holder.tvName.setText(obs.getObservation());
        holder.tvTime.setText(obs.getTime());
        holder.tvComment.setText(obs.getComments());

        // Xử lý nút Xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Observation")
                    .setMessage("Delete this observation?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteObservation(obs.getId());
                        obsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, obsList.size());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddObservationActivity.class);
            intent.putExtra("HIKE_ID", obs.getHikeId());
            intent.putExtra("OBS_ID", obs.getId());
            intent.putExtra("OBS_NAME", obs.getObservation());
            intent.putExtra("OBS_TIME", obs.getTime());
            intent.putExtra("OBS_COMMENT", obs.getComments());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return obsList.size();
    }

    public static class ObsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvComment;
        ImageView btnDelete;

        public ObsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvObsName);
            tvTime = itemView.findViewById(R.id.tvObsTime);
            tvComment = itemView.findViewById(R.id.tvObsComment);
            btnDelete = itemView.findViewById(R.id.btnDeleteObs);
        }
    }
}