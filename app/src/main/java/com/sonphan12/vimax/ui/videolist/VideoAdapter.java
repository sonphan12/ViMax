package com.sonphan12.vimax.ui.videolist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sonphan12.vimax.R;
import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.ui.videoedit.VideoEditActivity;
import com.sonphan12.vimax.utils.AppConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    List<Video> listVideo;
    Context ctx;

    public VideoAdapter() {
        super();
    }
    public VideoAdapter(Context ctx) {
        this.ctx = ctx;
        this.listVideo = new ArrayList<>();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.video_card, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = listVideo.get(position);
        Glide.with(ctx).load(video.getFileSrc()).apply(new RequestOptions().centerCrop()).into(holder.imgVideoImage);
        holder.txtVideoName.setText(video.getName());
        holder.txtDuration.setText(video.getDuration());

        holder.cardViewVideo.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, VideoEditActivity.class);
            intent.putExtra(AppConstants.EXTRA_VIDEO_PATH, listVideo.get(position).getFileSrc());
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listVideo.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgVideoImage) ImageView imgVideoImage;
        @BindView(R.id.txtVideoName) TextView txtVideoName;
        @BindView(R.id.txtDuration) TextView txtDuration;
        @BindView(R.id.cardViewVideo) CardView cardViewVideo;

        VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setListVideo(List<Video> listVideo) {
        this.listVideo = new ArrayList<>(listVideo);
    }
}
