package id.ac.umn.leleair.kelompok.smallchange;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public MyViewHolder(View itemView){
        super(itemView);
        mView = itemView;
    }

    public void setType(String type){
        TextView mType = mView.findViewById(R.id.tvType);
        mType.setText(type);
    }

    public  void setNote(String note){
        TextView mNote = mView.findViewById(R.id.tvNote);
        mNote.setText(note);
    }

    public void setDate(String date){
        TextView mDate = mView.findViewById(R.id.tvDate);
        mDate.setText(date);
    }

    public void setAmount(int amount){
        TextView mAmount = mView.findViewById(R.id.tvAmount);
        String stamount = String.valueOf(amount);
        mAmount.setText(stamount);
    }
}
