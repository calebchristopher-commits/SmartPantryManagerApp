package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantry.R;
import com.example.smartpantry.model.PantryItem;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {
    public interface Listener { void onEdit(PantryItem item); void onDelete(PantryItem item); }
    private List<PantryItem> items; private final Listener listener;
    public PantryAdapter(List<PantryItem> items, Listener listener) { this.items = items; this.listener = listener; }
    public void setItems(List<PantryItem> items) { this.items = items; notifyDataSetChanged(); }
    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false)); }
    @Override public void onBindViewHolder(@NonNull ViewHolder h, int position) { PantryItem item = items.get(position); h.name.setText(item.getName()); String expiry = item.getExpiryDate() == null || item.getExpiryDate().isEmpty() ? "No expiry date" : "Expiry: " + item.getExpiryDate(); h.details.setText(item.getQuantity() + " " + item.getUnit() + "  •  " + expiry); h.edit.setOnClickListener(v -> listener.onEdit(item)); h.delete.setOnClickListener(v -> listener.onDelete(item)); }
    @Override public int getItemCount() { return items.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder { TextView name, details; Button edit, delete; ViewHolder(View v) { super(v); name=v.findViewById(R.id.txtIngredientName); details=v.findViewById(R.id.txtIngredientDetails); edit=v.findViewById(R.id.btnEdit); delete=v.findViewById(R.id.btnDelete); } }
}
