package techmarket.uno.mymovies.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import techmarket.uno.mymovies.R;

public class TrailerAdapter {

    public interface OnTrailerClickListener{
        void onTrailerClick(String url);
    }

    private OnTrailerClickListener onTrailerClickListener;

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewNameOfVideo;

        //переопределение конструктора
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameOfVideo = itemView.findViewById(R.id.textViewNameOfVideo);
            //установить слушатель события
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTrailerClickListener != null){
                        //onTrailerClickListener.onTrailerClick();
                    }
                }
            });
        }
    }

    //setter
    public void setOnTrailerClickListener(OnTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }
}
