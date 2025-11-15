package com.example.mhikeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private Context context;
    private List<Hike> hikeList;
    private DatabaseHelper dbHelper;

    public HikeAdapter(Context context, List<Hike> hikeList, DatabaseHelper dbHelper) {
        this.context = context;
        this.hikeList = hikeList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hike_item, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikeList.get(position);

        holder.tvName.setText(hike.getName());
        holder.tvLocation.setText("Location: " + hike.getLocation());
        holder.tvDate.setText("Date: " + hike.getDate());

        // Xử lý sự kiện nút Xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Hike")
                    .setMessage("Are you sure you want to delete this hike?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteHike(hike.getId());
                        hikeList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, hikeList.size());
                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked: " + hike.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public static class HikeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvDate;
        ImageButton btnDelete;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvHikeName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDeleteHike);
        }
    }
}