package com.example.jogodereflexo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class HistoricoAdapter extends ListAdapter<Jogada, HistoricoAdapter.JogadaViewHolder> {

    protected HistoricoAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public JogadaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jogada, parent, false);
        return new JogadaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JogadaViewHolder holder, int position) {
        Jogada jogada = getItem(position);
        holder.bind(jogada);
    }

    public static class JogadaViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewPontuacao;
        private final TextView textViewData;
        private final ImageView imageViewRecordeIcon;

        public JogadaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPontuacao = itemView.findViewById(R.id.textViewPontuacaoJogada);
            textViewData = itemView.findViewById(R.id.textViewDataJogada);
            imageViewRecordeIcon = itemView.findViewById(R.id.imageViewRecordeIcon);
        }

        public void bind(Jogada jogada) {
            Context context = itemView.getContext();
            String textoPontuacao = context.getString(R.string.adapter_pontuacao, jogada.getPontuacao());
            String textoData = context.getString(R.string.adapter_data, jogada.getData());

            textViewPontuacao.setText(textoPontuacao);
            textViewData.setText(textoData);

            if (jogada.foiRecorde()) {
                imageViewRecordeIcon.setVisibility(View.VISIBLE);
                if ("Hardcore".equals(jogada.getModoDeJogo())) {
                    imageViewRecordeIcon.setImageResource(R.drawable.ic_trofeu);
                } else {
                    imageViewRecordeIcon.setImageResource(R.drawable.ic_record);
                }
            } else {
                imageViewRecordeIcon.setVisibility(View.GONE);
            }

            Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.slide_from_bottom);
            itemView.startAnimation(animation);
        }
    }

    private static final DiffUtil.ItemCallback<Jogada> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Jogada oldItem, @NonNull Jogada newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Jogada oldItem, @NonNull Jogada newItem) {
            return oldItem.getPontuacao() == newItem.getPontuacao() && oldItem.getData().equals(newItem.getData()) && oldItem.getModoDeJogo().equals(newItem.getModoDeJogo());
        }
    };
}